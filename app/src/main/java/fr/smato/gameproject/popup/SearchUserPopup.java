package fr.smato.gameproject.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.menu.MainActivity;
import fr.smato.gameproject.adapter.ItemUserAdapter;
import fr.smato.gameproject.model.User;

public class SearchUserPopup extends Dialog {

    private EditText searcText;
    private ImageButton searchButton;

    private ImageButton closeButton;

    private RecyclerView recyclerView;
    private ItemUserAdapter adapter;
    private List<User> users;
    private List<User> waitingFriends;

    public SearchUserPopup(@NonNull Context context, List<User> waitingFriends) {
        super(context);
        this.waitingFriends = waitingFriends;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_search_user);

        users = new ArrayList<>();
        users.addAll(waitingFriends);

        searcText = findViewById(R.id.search_text);
        searchButton = findViewById(R.id.search_btn);
        closeButton = findViewById(R.id.close_btn);
        recyclerView = findViewById(R.id.recycler_view);


        final User noResult = new User(null, "recherche...", "https://img.icons8.com/color/2x/reading.png", "offline");

        if (users.isEmpty()) {
            users.add(noResult);
        }

        adapter = new ItemUserAdapter(getContext(), users, false);
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = searcText.getText().toString();

                final Query query = DataBaseManager.rootDatabaseRef.child("Users").orderByChild("username")
                        .startAt(text)
                        .endAt(text + "\uf8ff")
                        .limitToLast(10);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            User user = ds.getValue(User.class);
                            if (!user.getId().equals(DataBaseManager.currentUser.getId())) users.add(user);
                        }
                        users.addAll(waitingFriends);

                        if (users.isEmpty()) {
                            users.add(noResult);
                        }

                        adapter = new ItemUserAdapter(getContext(), users, false);
                        recyclerView.setAdapter(adapter);

                        query.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        DataBaseManager.onError();
                    }
                });

                /*for (User user : DataBaseManager.users.values()) {

                    if (user.getUsername().toLowerCase().contains(text)) {
                        users.add(user);
                    }

                }
                users.addAll(waitingFriends);*/

            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
