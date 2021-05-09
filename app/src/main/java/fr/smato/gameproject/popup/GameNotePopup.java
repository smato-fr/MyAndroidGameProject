package fr.smato.gameproject.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import fr.smato.gameproject.R;
import fr.smato.gameproject.adapter.ItemGameNoteAdapter;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.utils.Updater;

public class GameNotePopup extends Dialog {

    private final GameViewI gameView;


    private RecyclerView recyclerView;
    private ItemGameNoteAdapter adapter;
    private ImageButton home;

    private Fragment currentFragment;

    public GameNotePopup(@NonNull Context context, GameViewI gameView) {
        super(context);
        this.gameView = gameView;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_game_note);
    }

    private void loadFragment(Fragment fragment) {
        currentFragment = fragment;

        final FragmentTransaction transaction = gameView.getGameActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_note_frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
