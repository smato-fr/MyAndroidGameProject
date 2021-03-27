package fr.smato.gameproject.activities.menu;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.adapter.ItemMessageAdapter;
import fr.smato.gameproject.model.Chat;
import fr.smato.gameproject.model.User;
import fr.smato.gameproject.utils.callback.UserLoadCallback;

public class MessageActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView username;

    private ImageButton sendBtn;
    private EditText sendText;

    private Intent intent;

    private ItemMessageAdapter adapter;
    private List<Chat> chats;
    private RecyclerView recyclerView;

    private DatabaseReference reference;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'à' HH:mm");
    private ValueEventListener messageLoadValueListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        sendBtn = findViewById(R.id.btn_send);
        sendText = findViewById(R.id.text_send);
        recyclerView = findViewById(R.id.recycler_view);

        intent = getIntent();



        //loading user and messages
        final String userId = intent.getStringExtra("userid");

        User user = DataBaseManager.getUserById(userId);

        UserLoadCallback callback = new UserLoadCallback() {
            @Override
            public void onEvent(final User user) {
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profileImage);
                }

                messageLoadValueListner = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String myId = DataBaseManager.currentUser.getId();


                        List<String> keys = new ArrayList<>();
                        chats.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            keys.add(ds.getKey());

                            Chat chat = ds.getValue(Chat.class);
                            if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)) {
                                if (chats.size() >= 10) {
                                    reference.child(keys.get(0)).removeValue();
                                    keys.remove(0);
                                }

                                chats.add(chat);

                            }
                        }



                        adapter = new ItemMessageAdapter(MessageActivity.this, chats, user.getImageURL());
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };

                readMessages();
            }
        };

        if (user == null) DataBaseManager.loadUser(userId, callback);
        else callback.onEvent(user);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendText.getText().toString();
                if (!msg.isEmpty()) {
                    if (msg.length() > 150) {
                        Toast.makeText(MessageActivity.this, "150 caractères maximun !", Toast.LENGTH_SHORT).show();
                    } else {

                        sendMessage(DataBaseManager.currentUser.getId(), userId, msg);
                    }
                } else {
                    Toast.makeText(MessageActivity.this, "Message vide !", Toast.LENGTH_SHORT).show();
                }
                sendText.setText("");
            }
        });



        //recycle View Set
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }


    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference ref = DataBaseManager.rootDatabaseRef;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("date", formatter.format(System.currentTimeMillis()));

        ref.child("Chats").push().setValue(hashMap);
    }

    private void readMessages() {
        chats = new ArrayList<>();
        reference = DataBaseManager.rootDatabaseRef.child("Chats");
        reference.addValueEventListener(messageLoadValueListner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (reference != null && messageLoadValueListner != null) {
            reference.removeEventListener(messageLoadValueListner);
        }
        DataBaseManager.setStatus("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reference != null && messageLoadValueListner != null) {
            reference.addValueEventListener(messageLoadValueListner);
        }
        DataBaseManager.setStatus("online");
    }
}
