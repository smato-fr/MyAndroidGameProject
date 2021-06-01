package fr.smato.gameproject.game;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.model.enums.GameState;
import fr.smato.gameproject.game.model.utils.GameViewI;

public class Client {

    private GameViewI game;

    private DatabaseReference ref;


    public Client(GameViewI gameView) {
        this.game = gameView;
        ref = game.getReference();
    }


    public void init() {

        ref.child("infos").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GameState state = GameState.getByName(snapshot.getValue(String.class));

                if (state == GameState.waiting) {
                    game.onWait();
                }

                else if (state == GameState.starting) {
                    game.onStart();
                    start();
                }

                else if (state == GameState.playing) {
                    game.onPlay();
                    ref.child("infos").child("state").removeEventListener(this);
                    play();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void play() {

        ref.child("infos").child("mendax").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String mendaxId = snapshot.getValue(String.class);
                if (game.getPlayer().getId().equals(mendaxId))
                    game.getPlayer().setMendax(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void start() {

            ref.child("infos").child("timer").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (game.getState() != GameState.starting) {
                        ref.child("infos").child("timer").removeEventListener(this);
                    }

                    int timer = snapshot.getValue(int.class);
                    ((WaitingGameView) game).getWaitingText().setText("Démarrage dans "+ timer + "s...");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    DataBaseManager.onError();
                }
            });

    }

}
