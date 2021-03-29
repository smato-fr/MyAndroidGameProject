package fr.smato.gameproject.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import fr.smato.gameproject.game.map.levels.SecondGameLevel;
import fr.smato.gameproject.game.model.drawable.ActionButton;
import fr.smato.gameproject.game.model.drawable.Entity;
import fr.smato.gameproject.game.model.drawable.JoyStick;
import fr.smato.gameproject.game.model.drawable.PlayerEntity;
import fr.smato.gameproject.game.model.enums.GameState;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.game.model.objects.Player;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.popup.GameMessagePopup;
import fr.smato.gameproject.utils.callback.Event;

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

    private Map<String, Player> players = new HashMap<>();


    private Hoster hoster;
    private Client client;

    //gameManagement
    private GameState state = GameState.playing;


    // création de la surface de dessin
    public GameView(final GameActivity context, String gameId, boolean host) {
        super(context);
        INSTANCE = this;

        this.gameId = gameId;
        reference = DataBaseManager.rootDatabaseRef.child("Games").child(gameId);


        this.mapManager = new MapManager(this, new FirstGameLevel());
        getHolder().addCallback(this);
        gameLoopThread = new GameLoopThread(this);


        //init popups
        this.chatPopup = new GameMessagePopup(getContext(), GameView.this);


        player = new Player(this, gameId, getMapManager().getLevel().getRoomName(), true);

        //init drawables
        joyStick = new JoyStick(getContext(), this);
        actionButton = new ActionButton(getContext(), this);
        actionButton.setActionButtons(Arrays.asList(
                actionButton.newButtonAction(R.drawable.send, new Event() {
                    @Override
                    public void onEvent() {
                        chatPopup.show();
                    }
                }),actionButton.newButtonAction(R.drawable.ic_test, new Event() {
                    @Override
                    public void onEvent() {
                        mapManager.changeLevel(new FirstGameLevel());
                        mapManager.resize(screenWidth, screenHeight);
                    }
                }),actionButton.newButtonAction(R.drawable.ic_test, new Event() {
                    @Override
                    public void onEvent() {
                        mapManager.changeLevel(new SecondGameLevel());
                        mapManager.resize(screenWidth, screenHeight);
                    }
                }),actionButton.newButtonAction(R.drawable.ic_test, new Event() {
                    @Override
                    public void onEvent() {

                    }
                })

        ));
        joyStick.setOnMove(new Event() {
            @Override
            public void onEvent() {
                double vx = joyStick.getActuatorX()*PlayerEntity.SPEED_MAX;
                double vy = joyStick.getActuatorY()*PlayerEntity.SPEED_MAX;

                player.getEntity().setVx(vx);
                player.getEntity().setVy(vy);
            }
        });



        reference.child("players").child("inGame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                players.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(DataBaseManager.currentUser.getId())) continue;
                    String userId = ds.getKey();

                    Player p = new Player(GameView.this, userId, getMapManager().getLevel().getRoomName(), false);
                    p.resize((int) resizerH(40));
                    players.put(userId, p);
                }
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
        }

        //load client
        client = new Client(this);

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
        actionButton.resize(new Location(resizerW(800), resizerH(670)), (int) resizerW(160), (int) resizerW(70), (int) resizerH(260));
        for (Player p : players.values()) {
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

        //update game
        updateGame();
    }



    // render game
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //map
        mapManager.draw(canvas);

        //entity
        player.draw(canvas);
        for (Player p : players.values()) {
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

        //draw game
        drawGame(canvas);
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




    public double resizerW(double i) {

        return i/1000*screenWidth;

    }

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



    //TODO GAME MANAGER
    @Override
    public void onWait() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPlay() {

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


    private void updateGame() {



    }

    private void drawGame(Canvas canvas) {



    }


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

        getMapManager().changeLevel(level);
        getMapManager().resize(screenWidth, screenHeight);
        movePlayer();
    }
}
