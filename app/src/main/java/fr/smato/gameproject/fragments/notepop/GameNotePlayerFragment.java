package fr.smato.gameproject.fragments.notepop;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import fr.smato.gameproject.R;
import fr.smato.gameproject.adapter.ItemColorNoteAdapter;
import fr.smato.gameproject.game.model.enums.PlayerColor;
import fr.smato.gameproject.model.User;

public class GameNotePlayerFragment  extends ConstraintLayout {


    public final TextView name;
    public final RecyclerView colors;


    private PlayerColor color;


    public GameNotePlayerFragment(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_game_note_fragment_player, this);

        name = findViewById(R.id.username);
        colors = findViewById(R.id.recycler_view_colors);
    }


    public void init(User user) {
        name.setText(user.getUsername());

        ItemColorNoteAdapter adapter = new ItemColorNoteAdapter(getContext());
        colors.setAdapter(adapter);
    }


}
