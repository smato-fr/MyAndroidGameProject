package fr.smato.gameproject.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.Toolbar.LayoutParams;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.menu.MessageActivity;
import fr.smato.gameproject.adapter.ItemGameMessageAdapter;
import fr.smato.gameproject.adapter.ItemMessageAdapter;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.model.Chat;
import fr.smato.gameproject.model.User;

public class GameMessagePopup extends Dialog {

    private final GameView gameView;





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


    public GameMessagePopup(@NonNull Context context, GameView gameView) {
        super(context);
        this.gameView = gameView;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_game_chat);


        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        sendBtn = findViewById(R.id.btn_send);
        sendText = findViewById(R.id.text_send);
        recyclerView = findViewById(R.id.recycler_view);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendText.getText().toString();
                if (!msg.isEmpty()) {
                    if (msg.length() > 150) {
                        Toast.makeText(getContext(), "150 caractères maximun !", Toast.LENGTH_SHORT).show();
                    } else {

                        gameView.getMapManager().sendMessage(DataBaseManager.currentUser.getId(), msg);
                    }
                } else {
                    Toast.makeText(getContext(), "Message vide !", Toast.LENGTH_SHORT).show();
                }
                sendText.setText("");
            }
        });



        //recycle View Set
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        chats = gameView.getMapManager().getChats();
        updateRecycleView();
    }

    public void updateRecycleView() {
        adapter = new ItemGameMessageAdapter(getContext(), chats);
        recyclerView.setAdapter(adapter);
    }


}
