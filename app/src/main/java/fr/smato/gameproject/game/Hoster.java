package fr.smato.gameproject.game;

import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.model.enums.GameState;
import fr.smato.gameproject.game.model.utils.GameViewI;

public class Hoster {

    private GameViewI game;


    //SPECIAL GUI
    private Button playButton;

    public Hoster(GameViewI gameView) {
        this.game = gameView;
    }

    public void init() {

        Map<String, Object> map = new HashMap<>();
        map.put("id", game.getId());
        map.put("host", DataBaseManager.currentUser.getId());
        map.put("state", "waiting");

        game.getReference().child("infos").setValue(map);


    }


    public void start() {
        Map<String, Object> map = new HashMap<>();
        map.put("state", "starting");
        game.getReference().child("infos").updateChildren(map);
    }


    public void stop() {
        DataBaseManager.rootDatabaseRef.child("Game").child("waiting").child(game.getGameId()).removeValue();
        game.getReference().removeValue();
    }
}
