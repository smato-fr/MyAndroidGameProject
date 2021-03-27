package fr.smato.gameproject.activities.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

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
        gameView=new GameView(this, getIntent().getStringExtra("id"), getIntent().getBooleanExtra("host", false));
        setContentView(gameView);
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
}
