package fr.smato.gameproject.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import fr.smato.gameproject.game.map.MapManager;
import fr.smato.gameproject.game.map.levels.WaitingRoomLevel;
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

// SurfaceView
public class WaitingGameView extends SurfaceView implements SurfaceHolder.Callback, GameViewI {

    private final PlayerEntity player;
    private final JoyStick joyStick;
    private final ActionButton actionButton;
    private final List<Entity> entities = new ArrayList<>();

    private int screenWidth, screenHeight;

    private GameLoopThread gameLoopThread;

    private MapManager mapManager;
    private boolean inited = false;

    public static WaitingGameView INSTANCE;

    private DatabaseReference reference;
    private final String gameId;
    private GameMessagePopup chatPopup;

    private Map<String, Player> players = new HashMap<>();

    private Hoster hoster;

    private Client client;

    //GUI above
    private Button playButton;
    private TextView waitingText;

    //gameManagement
    private GameState state = GameState.waiting;


    // création de la surface de dessin
    public WaitingGameView(final GameActivity context, String gameId, boolean host) {
        super(context);
        INSTANCE = this;

        getHolder().addCallback(this);
        gameLoopThread = new GameLoopThread(this);

        this.gameId = gameId;
        reference = DataBaseManager.rootDatabaseRef.child("Games").child(gameId);


        //init popups
        this.chatPopup = new GameMessagePopup(getContext(), WaitingGameView.this, "Salle d'attente");

        //map management
        this.mapManager = new MapManager(this, new WaitingRoomLevel());


        //init drawables
        joyStick = new JoyStick(getContext(), this);
        player = new PlayerEntity(getContext(), this);
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

                }
            }),actionButton.newButtonAction(R.drawable.ic_test, new Event() {
                @Override
                public void onEvent() {

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

                player.setVx(vx);
                player.setVy(vy);
            }
        });



       reference.child("players").child("inGame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (Player p : players.values()) {
                    p.destroy();
                }
                players.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(DataBaseManager.currentUser.getId())) continue;
                    String userId = ds.getKey();

                    Player p = new Player(WaitingGameView.this, userId, getMapManager().getLevel().getRoomName(), false);
                    p.resize((int) resizerH(40));
                    players.put(userId, p);
                }

                waitingText.setText((players.size()+1) + " joueur(s) connecté(s)");
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
            hoster.init();
            playButton = new Button(getContext());
            playButton.setText("Jouer");
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playButton.setOnClickListener(null);
                    playButton.setClickable(false);
                    setState(GameState.starting);
                    playButton.setText("Démarrage...");
                    hoster.start();
                }
            });
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            getGameActivity().getGameWidgets().addView(playButton, params);

        }

        //load client
       client = new Client(this);
       client.init();

        //GUI on FrameLayout
        waitingText = new TextView(getContext());
        waitingText.setText(players.size() + " joueur(s) connecté(s)");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.bottomMargin = 150;
        getGameActivity().getGameWidgets().addView(waitingText, params);


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
        player.resize(new Location(screenWidth/2, screenHeight/2), (int) resizerH(50));
        actionButton.resize(new Location(resizerW(800), resizerH(670)), (int) resizerW(160), (int) resizerW(70), (int) resizerH(260));
        for (Player p : players.values()) {
            p.resize((int) resizerH(40));
        }


        inited = true;
    }


    //update components and game
    public void update() {

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
        for (Player p : players.values()) {
            p.getEntity().draw(canvas);
        }
        for (Entity e : entities) {
            e.draw(canvas);
        }

        //gui on surfaceView
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
        canvas.drawText("FPS: "+ fps, 100, 200, paint);
    }

    private void drawUPS(Canvas canvas) {
        String ups = Double.toString(gameLoopThread.getUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: "+ ups, 100, 100, paint);
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
    public PlayerEntity getPlayerEntity() {
        return player;
    }

    @Override
    public void movePlayer() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x", player.getX()/screenWidth);
        map.put("y", player.getY()/screenHeight);
        map.put("room", getMapManager().getLevel().getRoomName());
        reference.child("players").child("list").child(DataBaseManager.currentUser.getId()).child("location").updateChildren(map);
    }

    @Override
    public void sendMessage(String sender, String msg) {

        getMapManager().sendMessage(sender, msg);

    }

    private double resizerW(double i) {

        return i/1000*screenWidth;

    }
    private double resizerH(double i) {

        return i/1000*screenHeight;

    }

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



    public void onWait() {
        state = GameState.waiting;
    }

    public void onStart() {
        state = GameState.starting;
    }

    public void onPlay() {
        state = GameState.playing;
        getGameActivity().onPlay();
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void setState(GameState state) {
        this.state = state;
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

    public MapManager getMapManager() {
        return mapManager;
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

    public TextView getWaitingText() {
        return waitingText;
    }
} // class GameView
