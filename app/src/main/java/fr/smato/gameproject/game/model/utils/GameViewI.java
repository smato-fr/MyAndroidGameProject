package fr.smato.gameproject.game.model.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;

import fr.smato.gameproject.activities.game.GameActivity;
import fr.smato.gameproject.game.map.MapManager;
import fr.smato.gameproject.game.model.drawable.PlayerEntity;
import fr.smato.gameproject.game.model.enums.GameState;
import fr.smato.gameproject.game.model.objects.Player;
import fr.smato.gameproject.popup.GameMessagePopup;

public interface GameViewI {

    //surfaceView
    void update();
    void draw(Canvas canvas);
    boolean isInited();
    SurfaceHolder getSurfaceHolder();
    int getScreenWidth();
    int getScreenHeight();
    double resizerH(double i);
    double resizerW(double i);

    //hoster
    int getId();
    String getGameId();
    DatabaseReference getReference();

    //client
    GameState getState();
    void setState(GameState state);
    void onWait();
    void onStart();
    void onPlay();

    //other
    MapManager getMapManager();
    GameMessagePopup getChatPopup();
    Context getContext();
    GameActivity getGameActivity();

    PlayerEntity getPlayerEntity();

    void movePlayer();

    void sendMessage(String id, String msg);

    PlayerList getPlayers();
}
