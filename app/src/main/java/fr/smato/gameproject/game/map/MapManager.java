package fr.smato.gameproject.game.map;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.model.Chat;

public class MapManager {

    private Level level;

    private List<Chat> chats = new ArrayList<>();
    private List<Chat> visibleChats = new ArrayList<>();
    private String lastRoomName;


    private final GameViewI gameView;
    private DatabaseReference reference;

    private final ValueEventListener eventListener;


    public MapManager(GameViewI gameView, final Level level) {
        this.gameView = gameView;
        this.eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();

                boolean changedRoom = !lastRoomName.equals(MapManager.this.level.getRoomName());
                if (changedRoom) {
                    chats.clear();
                    visibleChats.clear();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = ds.getKey();
                    keys.add(id);

                    Chat chat = ds.getValue(Chat.class);
                    chat.id = id;

                    if (chats.size() >= 10) {
                        reference.child(keys.get(0)).removeValue();
                        keys.remove(0);
                    }


                    boolean in = false;
                    for (Chat c : chats) {
                        if (c.id == chat.id) in = true;
                    }
                    if (!in) {
                        chats.add(chat);
                        if (!changedRoom) {
                            visibleChats.add(chat);
                        }
                    }

                }

                MapManager.this.gameView.getChatPopup().updateRecycleView();

                lastRoomName = MapManager.this.level.getRoomName();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        this.lastRoomName = "void";
        changeLevel(level);
    }

    public void resize(int screenWidth, int screenHeight) {
        level.resize(screenWidth, screenHeight);
    }

    public void draw(Canvas canvas) {
        level.draw(canvas);
    }


    public void changeLevel(Level level) {
        if (reference != null) reference.removeEventListener(eventListener);
        if (this.level != null) lastRoomName = this.level.getRoomName();

        this.level = level;
        this.level.init();

        chats.clear();
        visibleChats.clear();
        gameView.getChatPopup().updateRecycleView();

        this.reference = gameView.getReference().child("Rooms").child(level.getRoomName()).child("Chat");
        reference.addValueEventListener(eventListener);
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
        return visibleChats;
    }
}
