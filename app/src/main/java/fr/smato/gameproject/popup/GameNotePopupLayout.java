package fr.smato.gameproject.popup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
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
import fr.smato.gameproject.fragments.notepop.GameNoteHomeFragment;
import fr.smato.gameproject.game.model.objects.Player;
import fr.smato.gameproject.utils.Updater;

public class GameNotePopupLayout extends RelativeLayout implements Updater {

    private final GameActivity gameActivity;


    private ImageButton home_btn;
    private ImageButton close_btn;

    private RecyclerView playersView;
    private ItemGameNoteAdapter adapter;

    private GameNoteHomeFragment homeFragment;


    private FrameLayout layout;

    public GameNotePopupLayout(GameActivity gameActivity) {
        super(gameActivity.getApplicationContext());
        this.gameActivity = gameActivity;

        LayoutInflater inflater = (LayoutInflater)gameActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_game_note, this);

        homeFragment = new GameNoteHomeFragment(gameActivity);

        layout = findViewById(R.id.game_note_frame_layout);
        close_btn = findViewById(R.id.close_btn);
        home_btn = findViewById(R.id.btn_home);

        close_btn.setOnClickListener(v -> {
            gameActivity.hideGameNote();
        });

        home_btn.setOnClickListener(v ->{
            loadFragment(homeFragment);
        });

        playersView = findViewById(R.id.recycler_view);


        loadFragment(homeFragment);


    }


    public void loadFragment(View view) {
        layout.removeAllViews();
        layout.addView(view);
    }

    @Override
    public void update() {
        adapter = new ItemGameNoteAdapter(this);
        playersView.setLayoutManager(new LinearLayoutManager(getContext()));
        playersView.setAdapter(adapter);
    }


    public GameActivity getGameActivity() {
        return gameActivity;
    }
}
