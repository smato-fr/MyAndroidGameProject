package fr.smato.gameproject.fragments.notepop;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import fr.smato.gameproject.R;
import fr.smato.gameproject.adapter.ItemSelectableNoteAdapter;
import fr.smato.gameproject.game.model.enums.PlayerColor;
import fr.smato.gameproject.game.model.enums.PlayerRole;
import fr.smato.gameproject.model.User;

public class GameNotePlayerFragment  extends ConstraintLayout {


    public final TextView name;
    public final RecyclerView colors;
    public final RecyclerView roles;



    public GameNotePlayerFragment(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_game_note_fragment_player, this);

        name = findViewById(R.id.username);
        colors = findViewById(R.id.recycler_view_colors);
        roles = findViewById(R.id.recycler_view_roles);
    }


    public void init(User user) {
        name.setText(user.getUsername());

        colors.setAdapter(new ItemSelectableNoteAdapter(getContext(), PlayerColor.valuesID()));
        roles.setAdapter(new ItemSelectableNoteAdapter(getContext(), PlayerRole.valuesID()));
    }


}
