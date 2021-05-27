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

import fr.smato.gameproject.R;

public class GameNotePlayerFragment  extends ConstraintLayout {


    public final TextView name;

    public GameNotePlayerFragment(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_game_note_fragment_player, this);

        name = findViewById(R.id.username);
    }


}
