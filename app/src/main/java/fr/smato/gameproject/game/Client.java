package fr.smato.gameproject.game;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import fr.smato.gameproject.game.model.enums.GameState;
import fr.smato.gameproject.game.model.utils.GameViewI;

public class Client {

    private GameViewI game;

    private DatabaseReference ref;


    public Client(GameViewI gameView) {
        this.game = gameView;
    }


    public void init() {

        ref = game.getReference();
        ref.child("infos").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GameState state = GameState.getByName(snapshot.getValue(String.class));

                if (state == GameState.waiting) {
                    game.onWait();
                }

                else if (state == GameState.starting) {
                    game.onStart();
                }

                else if (state == GameState.playing) {
                    game.onPlay();
                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void start() {

    }
}
