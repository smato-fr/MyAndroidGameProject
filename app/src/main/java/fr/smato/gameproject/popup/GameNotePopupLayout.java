package fr.smato.gameproject.popup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.game.GameActivity;
import fr.smato.gameproject.adapter.ItemGameNoteAdapter;
import fr.smato.gameproject.game.model.objects.Player;
import fr.smato.gameproject.utils.Updater;

public class GameNotePopupLayout extends RelativeLayout implements Updater {

    private final GameActivity gameActivity;

    private ImageButton close_btn;

    private RecyclerView playersView;
    private ItemGameNoteAdapter adapter;

    public GameNotePopupLayout(GameActivity gameActivity) {
        super(gameActivity.getApplicationContext());
        this.gameActivity = gameActivity;

        LayoutInflater inflater = (LayoutInflater)gameActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_game_note, this);

        close_btn = findViewById(R.id.close_btn);
        close_btn.setOnClickListener(v -> {
            gameActivity.hideGameNote();
        });

        playersView = findViewById(R.id.recycler_view);


    }


    @Override
    public void update() {
        Map<String, Player> players = gameActivity.getGameView().getPlayers();
        List<String> list = new ArrayList<>();
        for (Player pls : players.values()) {
            if (pls.getUser() != null)
                list.add(pls.getUser().getImageURL());
        }


        adapter = new ItemGameNoteAdapter(getContext(), list);
        playersView.setLayoutManager(new LinearLayoutManager(getContext()));
        playersView.setAdapter(adapter);
    }
}
