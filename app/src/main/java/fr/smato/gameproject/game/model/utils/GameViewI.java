package fr.smato.gameproject.game.model.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.google.firebase.database.DatabaseReference;

import fr.smato.gameproject.game.map.MapManager;
import fr.smato.gameproject.popup.GameMessagePopup;

public interface GameViewI {

    //surfaceView
    void update();
    void draw(Canvas canvas);
    boolean isInited();
    SurfaceHolder getSurfaceHolder();
    int getScreenWidth();
    int getScreenHeight();

    //hoster
    int getId();
    String getGameId();
    DatabaseReference getReference();

    //client
    void onWait();
    void onStart();
    void onPlay();

    //other
    MapManager getMapManager();
    GameMessagePopup getChatPopup();
    Context getContext();
}
