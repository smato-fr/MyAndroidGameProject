package fr.smato.gameproject.activities.start;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.menu.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameField, emailField, passwordField, confirmPasswordField;
    private Button registerBtn;

    private FirebaseAuth auth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = findViewById(R.id.username);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        confirmPasswordField  = findViewById(R.id.confirm_password);
        registerBtn = findViewById(R.id.registerButton);

        auth = FirebaseAuth.getInstance();


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBtn.setClickable(false);
                String username = usernameField.getText().toString();
                String email = emailField.getText().toString();
                String password= passwordField.getText().toString();
                String confirm_password = confirmPasswordField.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_password)) {
                    Toast.makeText(RegisterActivity.this, R.string.error_allFields, Toast.LENGTH_LONG).show();
                    registerBtn.setClickable(true);
                }

                else if (username.length() > 15) {
                    Toast.makeText(RegisterActivity.this, R.string.error_entry_long_username, Toast.LENGTH_LONG).show();
                    registerBtn.setClickable(true);
                }

                else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, R.string.error_entry_short_password, Toast.LENGTH_LONG).show();
                    registerBtn.setClickable(true);
                }

                else if (!password.equals(confirm_password)) {
                    Toast.makeText(RegisterActivity.this, R.string.error_entry_different_password, Toast.LENGTH_LONG).show();
                    registerBtn.setClickable(true);
                }

                else {
                    register(username, email, password);
                }

            }
        });
    }

    private void register(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.error_register, Toast.LENGTH_LONG).show();
                            registerBtn.setClickable(true);
                        }
                    }
                });
    }
}
