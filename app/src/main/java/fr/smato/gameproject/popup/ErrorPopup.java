package fr.smato.gameproject.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.start.StartActivity;


public class ErrorPopup extends Dialog {


    private final String errorName;
    private final AppCompatActivity activity;

    public ErrorPopup(AppCompatActivity activity, String errorName) {
        super(activity, false, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.errorName = errorName;
        this.activity = activity;

        show();
    }



    private TextView error;
    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_crash);

        error = findViewById(R.id.error_description);
        error.setText(errorName);

        button = findViewById(R.id.error_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                Intent intent = new Intent(activity, StartActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
}
