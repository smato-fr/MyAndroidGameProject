package fr.smato.gameproject.activities.start;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.menu.MainActivity;

public class StartActivity extends AppCompatActivity {

    private Button loginBtn, registenBtn;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        //check if connected
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        loginBtn = findViewById(R.id.loginButton);
        registenBtn = findViewById(R.id.registerButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
