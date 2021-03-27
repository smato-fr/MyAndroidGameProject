package fr.smato.gameproject.game.map;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.model.Chat;
import fr.smato.gameproject.model.User;

public class MapManager {

    private Level level;

    private List<Chat> chats = new ArrayList<>();


    private final GameView gameView;
    private DatabaseReference reference;

    public MapManager(GameView gameView, Level level) {
        this.gameView = gameView;
        changeLevel(level);
    }

    public void resize(int screenWidth, int screenHeight) {
        level.resize(screenWidth, screenHeight);
    }

    public void draw(Canvas canvas) {
        level.draw(canvas);
    }


    public void changeLevel(Level level) {
        this.level = level;
        this.level.init();
        this.reference = gameView.getReference().child("Rooms").child(level.getRoomName()).child("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                chats.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    keys.add(ds.getKey());

                    Chat chat = ds.getValue(Chat.class);

                    if (chats.size() >= 10) {
                        reference.child(keys.get(0)).removeValue();
                        keys.remove(0);
                    }

                    chats.add(chat);

                }

                if (gameView.getChatPopup().isShowing()) {
                    gameView.getChatPopup().updateRecycleView();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void sendMessage(String sender, String message) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("message",message);

        reference.push().setValue(hashMap);
    }


    public Level getLevel() {
        return level;
    }



    public List<Chat> getChats() {
        return chats;
    }
}
