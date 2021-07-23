package fr.smato.gameproject.activities.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.fragments.HomeMainFragment;
import fr.smato.gameproject.fragments.PlayMainFragment;
import fr.smato.gameproject.fragments.ProfileMainFragment;
import fr.smato.gameproject.fragments.SocialMainFragment;
import fr.smato.gameproject.popup.ErrorPopup;
import fr.smato.gameproject.utils.Updater;

public class MainActivity extends AppCompatActivity implements Updater {

    private ImageView homeButton;

    private HomeMainFragment homeMainFragment;
    private SocialMainFragment socialMainFragment;

    private Updater enabledFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseManager.updateData(this);

        homeMainFragment = new HomeMainFragment();
        socialMainFragment = new SocialMainFragment(MainActivity.this);


        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(homeMainFragment);
            }
        });


        BottomNavigationView nav = findViewById(R.id.nav_main);
        nav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.profile_page:
                    loadFragment(new ProfileMainFragment(MainActivity.this));
                    return true;
                case R.id.play_page:
                    loadFragment(new PlayMainFragment(MainActivity.this));
                    return true;
                case R.id.social_page:
                    loadFragment(socialMainFragment);
                    return true;

            }

            return false;
        });


        loadFragment(homeMainFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataBaseManager.setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataBaseManager.setStatus("offline");
    }

    @Override
    public void update() {
        enabledFragment.update();
    }

    private void loadFragment(Fragment fragment) {
        enabledFragment = (Updater) fragment;

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
