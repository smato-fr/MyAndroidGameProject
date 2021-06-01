package fr.smato.gameproject.game;

import android.widget.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.model.objects.Player;
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
        game.getReference().child("infos").child("state").setValue("starting");

        new Thread(new Runnable() {

            private int timer = 10;

            @Override
            public void run() {

                while (timer > 0) {
                    game.getReference().child("infos").child("timer").setValue(timer);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timer--;
                }

                play();
            }
        }, "game-start").start();
    }

    private void play() {

        game.getReference().child("infos").child("state").setValue("playing");

        Random r = new Random();

        Player mendax = game.getPlayers().get(r.nextInt(game.getPlayers().size()));
        game.getReference().child("info").child("mendax").setValue(mendax.getId());
    }


    public void stop() {
        DataBaseManager.rootDatabaseRef.child("Game").child("waiting").child(game.getGameId()).removeValue();
        game.getReference().removeValue();
    }

}
