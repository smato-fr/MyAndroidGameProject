package fr.smato.gameproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.adapter.ItemShopAdapter;
import fr.smato.gameproject.model.User;
import fr.smato.gameproject.utils.Updater;

public class HomeMainFragment extends Fragment implements Updater {

    private TextView username;
    private ImageView profileImage;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_home, container, false);

        //récupérer le recyclerview
        final RecyclerView recyclerView = view.findViewById(R.id.item_shop_recyclerview);
        recyclerView.setAdapter(new ItemShopAdapter());


        username= view.findViewById(R.id.username);
        profileImage = view.findViewById(R.id.profile_image);



        return view;
    }


    @Override
    public void update() {
        User user = DataBaseManager.currentUser;
        username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(view.getContext()).load(user.getImageURL()).into(profileImage);
        }
    }
}
