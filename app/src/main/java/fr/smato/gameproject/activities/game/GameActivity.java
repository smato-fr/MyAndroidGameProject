package fr.smato.gameproject.activities.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.model.utils.GameViewI;
import fr.smato.gameproject.popup.GameNotePopupLayout;
import fr.smato.gameproject.utils.Updater;

public class GameActivity extends AppCompatActivity implements Updater {

    private FrameLayout game;
    private SurfaceView gameView;
    private RelativeLayout gameWidgets;

    private GameNotePopupLayout gameNoteLayout;
    private boolean isGameNoteShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set Window to fullScreen
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                );



        //set content of activity
        game = new FrameLayout(this);
        gameWidgets = new RelativeLayout(this);
        gameView=new WaitingGameView(this, getIntent().getStringExtra("id"), getIntent().getBooleanExtra("host", false));


        gameNoteLayout = new GameNotePopupLayout(this);


        game.addView(gameView);
        game.addView(gameWidgets);

        setContentView(game);
    }


    public void onPlay() {


        game.removeAllViews();
        gameView = new GameView(this, getIntent().getStringExtra("id"), getIntent().getBooleanExtra("host", false));
        game.addView(gameView);
        gameWidgets.removeAllViews();
        game.addView(gameWidgets);
    }


    @Override
    protected void onResume() {
        super.onResume();
        DataBaseManager.setStatus("playing");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataBaseManager.setStatus("offline");
    }


    public FrameLayout getMainLayout() {
        return game;
    }


    public RelativeLayout getGameWidgets() {
        return gameWidgets;
    }

    public void showGameNote() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        gameWidgets.addView(gameNoteLayout, params);
        isGameNoteShowing = true;
    }

    public void hideGameNote() {
        gameWidgets.removeAllViews();
        isGameNoteShowing = false;
    }

    public boolean isGameNoteShowing() {
        return isGameNoteShowing;
    }

    public GameViewI getGameView() {
        return (GameViewI) gameView;
    }

    @Override
    public void update() {
        gameNoteLayout.update();
    }
}
