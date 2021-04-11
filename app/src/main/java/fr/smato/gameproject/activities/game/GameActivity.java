package fr.smato.gameproject.activities.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.WaitingGameView;

public class GameActivity extends AppCompatActivity {

    private FrameLayout game;
    private SurfaceView gameView;
    private RelativeLayout gameWidgets;

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

        game.addView(gameView);
        game.addView(gameWidgets);

        setContentView(game);
    }


    public void onPlay() {

        game.removeAllViews();
        gameView = new GameView(this, getIntent().getStringExtra("id"), getIntent().getBooleanExtra("host", false));
        game.addView(gameView);

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
}
