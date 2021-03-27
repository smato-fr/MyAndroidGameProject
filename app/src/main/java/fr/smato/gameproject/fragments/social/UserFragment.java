package fr.smato.gameproject.fragments.social;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.adapter.ItemUserAdapter;
import fr.smato.gameproject.model.Friend;
import fr.smato.gameproject.model.FriendI;
import fr.smato.gameproject.model.User;
import fr.smato.gameproject.popup.SearchUserPopup;


public class UserFragment extends Fragment {


    private RecyclerView recyclerView;

    private ItemUserAdapter userAdapter;

    private  View view;

    private Button searchButton;
    private ImageView notifImage;

    private List<User> friends = new ArrayList<>();
    private List<User> waitingFriends = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social_friends, container, false);

        recyclerView = view.findViewById(R.id.users_recycler_view);
        searchButton = view.findViewById(R.id.search_user_btn);
        notifImage = view.findViewById(R.id.notif);

        notifImage.setVisibility(View.INVISIBLE);



        DataBaseManager.rootDatabaseRef.child("Friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                waitingFriends.clear();
                for (FriendI fr : DataBaseManager.friends.values()) {
                    if (fr.getFriendState() == 1 && !fr.fromCurrentUser()) {
                        waitingFriends.add(fr.getUser());
                    }
                }

                if (waitingFriends.isEmpty()) {
                    notifImage.setVisibility(View.INVISIBLE);
                } else {
                    notifImage.setVisibility(View.VISIBLE);
                }
                refresh();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchUserPopup popup = new SearchUserPopup(getContext(), waitingFriends);
                popup.show();
            }
        });


        refresh();

        return view;
    }

    public void refresh() {
        if (recyclerView != null) {
            recyclerView = view.findViewById(R.id.users_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            friends.clear();
            for (FriendI fr : DataBaseManager.friends.values()) {

                if (fr.getFriendState() == 2) {
                    friends.add(fr.getUser());
                }

            }

            userAdapter = new ItemUserAdapter(getContext(), friends, true);
            userAdapter.update();
            recyclerView.setAdapter(userAdapter);



        }
    }


}
