package fr.smato.gameproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.activities.menu.MainActivity;
import fr.smato.gameproject.R;
import fr.smato.gameproject.adapter.ItemWaitingGamesAdapter;
import fr.smato.gameproject.model.WaitingGame;
import fr.smato.gameproject.utils.Updater;

public class PlayMainFragment extends Fragment implements Updater {

    private MainActivity context;

    private List<WaitingGame> waitingGames = new ArrayList<>();
    private ValueEventListener listener;

    public PlayMainFragment(MainActivity context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_play, container, false);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                waitingGames.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    WaitingGame wg = ds.getValue(WaitingGame.class);
                    waitingGames.add(wg);
                }

                RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new ItemWaitingGamesAdapter(context, waitingGames));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DataBaseManager.onError();
            }
        };

        DataBaseManager.rootDatabaseRef.child("Games").child("waiting").addValueEventListener(listener);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        DataBaseManager.rootDatabaseRef.child("Games").child("waiting").removeEventListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        DataBaseManager.rootDatabaseRef.child("Games").child("waiting").addValueEventListener(listener);
    }

    @Override
    public void update() {

    }
}
