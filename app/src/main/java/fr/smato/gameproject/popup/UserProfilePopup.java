package fr.smato.gameproject.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.menu.MessageActivity;
import fr.smato.gameproject.model.Friend;
import fr.smato.gameproject.model.FriendI;
import fr.smato.gameproject.model.User;
import fr.smato.gameproject.utils.Updater;

public class UserProfilePopup extends Dialog implements Updater {

    private User user;

    private TextView username;
    private ImageView profileImage;
    private Button button;

    private ImageButton closeButton;


    public UserProfilePopup(@NonNull Context context, User user) {
        super(context);
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_user_info);

        username = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        closeButton = findViewById(R.id.close_btn);
        button = findViewById(R.id.button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(getContext()).load(user.getImageURL()).into(profileImage);
        }

        update();
    }

    @Override
    public void update() {
        final FriendI friendI = DataBaseManager.getFriendIyId(user.getId());
        int state = (friendI == null ? 0 : friendI.getFriendState());


        Log.d("update", "update: "+state+friendI);
        if (state == 0) {
            button.setText("Envoyer une demande d'ami");
            button.setClickable(true);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    button.setClickable(false);
                    button.setOnClickListener(null);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("friend1", DataBaseManager.currentUser.getId());
                    map.put("friend2", user.getId());
                    map.put("state", 1);

                    DataBaseManager.rootDatabaseRef.child("Friends").push().setValue(map);
                }
            });
        } else {

            if (state == 1) {

                if (friendI.fromCurrentUser()) {

                    button.setText("Invitation envoyée");
                    button.setClickable(false);
                    button.setOnClickListener(null);

                } else {

                    button.setText("Ajouter un ami");
                    button.setClickable(true);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            button.setClickable(false);
                            button.setOnClickListener(null);
                            Toast.makeText(getContext(), "Ami ajouté !", Toast.LENGTH_LONG).show();
                            DataBaseManager.rootDatabaseRef.child("Friends").child(friendI.getId()).child("state").setValue(2);
                        }
                    });

                }

            } else if (state == 2) {

                button.setText("Envoyer un message");
                button.setClickable(true);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), MessageActivity.class);
                        intent.putExtra("userid", user.getId());
                        getContext().startActivity(intent);
                    }
                });

            }
        }

    }
}