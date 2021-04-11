package fr.smato.gameproject.game.model.objects;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.model.drawable.Entity;
import fr.smato.gameproject.game.model.drawable.PlayerEntity;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.model.LocationModel;
import fr.smato.gameproject.model.User;


public class Player {


    private Location location = new Location(500, 500);
    private String room;

    private User user;

    private final String id;
    private final GameViewI game;

    private Entity entity;

    private boolean valid;

    private Role role;

    public Player(GameViewI game, String id, String room, boolean selfUser) {
        this.game = game;
        this.id = id;
        this.room = room;

        this.valid = true;

        if (!selfUser) {

            this.entity = new Entity(game.getContext(), game);


            DataBaseManager.userDatabaseRef.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!valid) {
                        DataBaseManager.userDatabaseRef.child(Player.this.id).removeEventListener(this);
                        return;
                    }
                    Player.this.user = snapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            game.getReference().child("players").child("list").child(id).child("location").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!valid) {
                        DataBaseManager.userDatabaseRef.child(Player.this.id).removeEventListener(this);
                        return;
                    }
                    LocationModel model = snapshot.getValue(LocationModel.class);
                    if (model != null) {
                        location.x = model.getX() * Player.this.game.getScreenWidth();
                        location.y = model.getY() * Player.this.game.getScreenHeight();
                        Player.this.room = model.getRoom();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {


            //selfUser
            this.entity = new PlayerEntity(game.getContext(), game);

        }

    }

    public void resize(Object...objects) {
        entity.resize(location, objects);
    }

    public void update() {
        entity.update();
    }

    public void draw(Canvas canvas) {
        entity.draw(canvas);
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }


    public void destroy() {
        this.valid = false;
    }

    public boolean isValid() {
        return valid;
    }
}
