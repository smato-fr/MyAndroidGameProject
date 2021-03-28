package fr.smato.gameproject.game.model.objects;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.model.drawable.Entity;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.model.LocationModel;
import fr.smato.gameproject.model.User;


public class Player {


    private Location location = new Location(500, 500);

    private User user;

    private final String id;
    private final GameViewI game;

    private Entity entity;

    private Role role;


    public Player(final GameViewI game, String id) {
        this.game = game;
        this.id = id;

        this.entity = new Entity(game.getContext(), game);


        DataBaseManager.userDatabaseRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Player.this.user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        game.getReference().child("players").child("list").child(id).child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LocationModel model = snapshot.getValue(LocationModel.class);
                location.x = model.getX()*game.getScreenWidth();
                location.y = model.getY()*game.getScreenHeight();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void resize(Object...objects) {
        entity.resize(location, objects);
    }


    public Location getLocation() {
        return location;
    }

    public GameViewI getGame() {
        return game;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Entity getEntity() {
        return entity;
    }
}
