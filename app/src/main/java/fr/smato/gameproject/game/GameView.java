package fr.smato.gameproject.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.game.GameActivity;
import fr.smato.gameproject.game.map.GameLevel;
import fr.smato.gameproject.game.map.MapManager;
import fr.smato.gameproject.game.map.levels.FirstGameLevel;
import fr.smato.gameproject.game.model.drawable.ActionButton;
import fr.smato.gameproject.game.model.drawable.Entity;
import fr.smato.gameproject.game.model.drawable.JoyStick;
import fr.smato.gameproject.game.model.drawable.PlayerEntity;
import fr.smato.gameproject.game.model.enums.GameState;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.game.model.objects.Player;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.game.model.utils.PlayerList;
import fr.smato.gameproject.popup.GameMessagePopup;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, GameViewI {



    private final Player player;
    private final JoyStick joyStick;
    private final ActionButton actionButton;
    private final List<Entity> entities = new ArrayList<>();

    private int screenWidth, screenHeight;

    private GameLoopThread gameLoopThread;

    private MapManager mapManager;
    private boolean inited = false;

    public static GameView INSTANCE;

    private DatabaseReference reference;
    private final String gameId;

    private GameMessagePopup chatPopup;


    private PlayerList players = new PlayerList();


    private Hoster hoster;
    private Client client;

    //gameManagement
    private GameState state = GameState.playing;


    // création de la surface de dessin
    public GameView(final GameActivity context, String gameId, boolean host) {
        super(context);
        INSTANCE = this;

        getHolder().addCallback(this);
        gameLoopThread = new GameLoopThread(this);

        this.gameId = gameId;
        reference = DataBaseManager.rootDatabaseRef.child("Games").child(gameId);

        //init popups
        this.chatPopup = new GameMessagePopup(getContext(), this, "Salle 1");


        this.mapManager = new MapManager(this, new FirstGameLevel(this));

        player = new Player(this, DataBaseManager.currentUser.getId(), getMapManager().getLevel().getRoomName(), true);

        //init drawables
        joyStick = new JoyStick(getContext(), this);
        actionButton = new ActionButton(getContext(), this);
        actionButton.setActionButtons(Arrays.asList(
                actionButton.newButtonAction(R.drawable.send, () -> chatPopup.show()),
                actionButton.newButtonAction(R.drawable.btn_game_interact, () -> ((GameLevel) mapManager.getLevel()).onInteract()),
                actionButton.newButtonAction(R.drawable.ic_test, () -> {
                    getGameActivity().showGameNote();
                    Log.d("TAG", "GameView: ");
                }),
                actionButton.newButtonAction(R.drawable.ic_test, () -> {})

        ));
        joyStick.setOnMove(() -> {
            double vx = joyStick.getActuatorX()*PlayerEntity.SPEED_MAX;
            double vy = joyStick.getActuatorY()*PlayerEntity.SPEED_MAX;

            player.getEntity().setVx(vx);
            player.getEntity().setVy(vy);
        });



        reference.child("players").child("inGame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> IDs = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(DataBaseManager.currentUser.getId())) continue;

                    IDs.add(ds.getKey());
                }

                PlayerList newPlayersList = new PlayerList();

                for (String userId : IDs) {

                    if (players.containsKey(userId)) {
                        newPlayersList.add(players.get(userId));
                        players.remove(userId);
                    } else {
                        Player p = new Player(GameView.this, userId, getMapManager().getLevel().getRoomName(), false);
                        p.resize((int) resizerH(40));
                        newPlayersList.add(p);
                    }

                }

                for (Player p : players) {
                    p.destroy();
                }

                players = newPlayersList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DataBaseManager.onError();
            }
        });

        //client/server

        //load hoster if is
        if (host) {
            hoster = new Hoster(this);
            hoster.play();
        }

        //load client
        client = new Client(this);
        client.play();

        setFocusable(true);
    }


    //creation or resize of screen
    //get the new screen size
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int w, int h) {
        this.screenWidth = w;
        this.screenHeight = h;

        mapManager.resize(screenWidth, screenHeight);
        joyStick.resize(new Location(resizerW(120), resizerH(800)), (int) resizerW(80), (int) resizerW(40));
        player.resize((int) resizerH(50));
        actionButton.resize(new Location(resizerW(800), resizerH(670)), (int) resizerW(160), (int) resizerW(60), (int) resizerH(300));
        for (Player p : players) {
            p.resize((int) resizerH(40));
        }


        inited = true;
    }






    //update components and game
    public void update() {

        //update terrain
        getMapManager().getLevel().update();

        //update components
        player.update();
        joyStick.update();
        for (Entity e : entities) {
            e.update();
        }

    }



    // render game
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //map
        mapManager.draw(canvas);

        //entity
        player.draw(canvas);
        for (Player p : players) {
             if (getPlayer().getRoom().equals(p.getRoom())) p.getEntity().draw(canvas);
        }
        for (Entity e : entities) {
            e.draw(canvas);
        }

        //gui
        joyStick.draw(canvas);
        actionButton.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);

    }


    private void drawFPS(Canvas canvas) {
        String fps = Double.toString(gameLoopThread.getFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("-FPS: "+ fps, 100, 200, paint);
    }

    private void drawUPS(Canvas canvas) {
        String ups = Double.toString(gameLoopThread.getUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("-UPS: "+ ups, 100, 100, paint);
    }


    // Fonction appelée immédiatement après la création de l'objet SurfaceView
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // création du processus GameLoopThread si cela n'est pas fait
        if(gameLoopThread.getState()==Thread.State.TERMINATED) {
            gameLoopThread=new GameLoopThread(this);
        }
        gameLoopThread.setRunning(true);
        gameLoopThread.start();

        //join game
        reference.child("players").child("inGame").child(DataBaseManager.currentUser.getId()).setValue(1);
    }


    // Fonction appelée juste avant que l'objet ne soit détruit.
    // on tente ici de stopper le processus de gameLoopThread
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        gameLoopThread.setRunning(false);
        while (retry) {
            try {
                gameLoopThread.join();
                retry = false;
            }
            catch (InterruptedException e) {}
        }

        //leave game
        reference.child("players").child("inGame").child(DataBaseManager.currentUser.getId()).removeValue();
    }

    // Gère les touchés sur l'écran
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        double currentX = (double) event.getX();
        double currentY = (double) event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (joyStick.isTouched(currentX, currentY)) {
                    joyStick.onDrag();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (joyStick.isDragging()) {
                    joyStick.setActuator(currentX, currentY);
                }
                break;

            case MotionEvent.ACTION_UP:
                joyStick.onDrop();
                actionButton.touch(currentX, currentY);
                break;

        }

        return true;  // On retourne "true" pour indiquer qu'on a géré l'évènement
    }



    @Override
    public double resizerW(double i) {

        return i/1000*screenWidth;

    }

    @Override
    public double resizerH(double i) {

        return i/1000*screenHeight;

    }

    @Override
    public boolean isInited() {
        return inited;
    }

    @Override
    public SurfaceHolder getSurfaceHolder() {
        return getHolder();
    }


    public static Bitmap loadImage(int res) {
        return BitmapFactory.decodeResource(getCurrentContext().getResources(), res);
    }

    public static Bitmap resizeImage(Bitmap image, int weight, int height) {
        return Bitmap.createScaledBitmap(image, weight, height, false);
    }



    public void onPlayerLoaded() {

        getGameActivity().update();

    }


    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void setState(GameState state) {
        this.state = state;
    }



    @Override
    public MapManager getMapManager() {
        return mapManager;
    }



    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public PlayerEntity getPlayerEntity() {
        return (PlayerEntity) player.getEntity();
    }

    @Override
    public void movePlayer() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x", player.getEntity().getX()/screenWidth);
        map.put("y", player.getEntity().getY()/screenHeight);
        map.put("room", player.getRoom());
        reference.child("players").child("list").child(DataBaseManager.currentUser.getId()).child("location").updateChildren(map);

        float vx = (float) (player.getEntity().getX() - screenWidth/2);
        float vy = (float) (player.getEntity().getY() - screenHeight/2);

        getMapManager().getLevel().setTranslation(vx, vy);

    }

    @Override
    public void sendMessage(String sender, String msg) {
        getMapManager().sendMessage(sender, msg);
    }

    @Override
    public PlayerList getPlayers() {
        return players;
    }

    public static Context getCurrentContext() {
        return WaitingGameView.INSTANCE.getContext();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public String getGameId() {
        return gameId;
    }

    public DatabaseReference getReference() {
        return reference;
    }



    public GameMessagePopup getChatPopup() {
        return chatPopup;
    }

    @Override
    public GameActivity getGameActivity() {
        return (GameActivity) getCurrentContext();
    }

    public void changeLevel(GameLevel level, float x, float y) {
        player.getEntity().setLocation(x*screenWidth, y*screenHeight);
        player.setRoom(level.getRoomName());

        chatPopup.changeLevel(level.getRoomName());

        getMapManager().changeLevel(level);
        getMapManager().resize(screenWidth, screenHeight);
        movePlayer();
    }



}
