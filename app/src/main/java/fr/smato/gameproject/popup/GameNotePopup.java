package fr.smato.gameproject.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import androidx.annotation.NonNull;
import fr.smato.gameproject.R;
import fr.smato.gameproject.game.model.utils.GameViewI;

public class GameNotePopup extends Dialog {

    private final GameViewI gameView;


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

}
