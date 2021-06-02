package fr.smato.gameproject.game;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.model.enums.PlayerRole;
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

                game.getReference().child("infos").child("state").setValue("playing");
            }
        }, "game-start").start();
    }

    public void play() {

        List<Player> players = new ArrayList<>();
        players.addAll(game.getPlayers());
        players.add(game.getPlayer());


        Random r = new Random();

        Player mendax = players.get(r.nextInt(players.size()));
        game.getReference().child("info").child("mendax").setValue(mendax.getId());


        List<PlayerRole> rolesGiven = new ArrayList<>();

        for (Player pls: players) {
            PlayerRole role;

            do {
                role = PlayerRole.values()[r.nextInt(PlayerRole.values().length)];
            } while (rolesGiven.contains(role));

            rolesGiven.add(role);
            pls.setRole(role.getRole());
        }



    }


    public void stop() {
        DataBaseManager.rootDatabaseRef.child("Game").child("waiting").child(game.getGameId()).removeValue();
        game.getReference().removeValue();
    }

}
