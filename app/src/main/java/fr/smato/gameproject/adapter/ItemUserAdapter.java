package fr.smato.gameproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.smato.gameproject.R;
import fr.smato.gameproject.model.User;
import fr.smato.gameproject.popup.UserProfilePopup;
import fr.smato.gameproject.utils.Updater;

public class ItemUserAdapter extends RecyclerView.Adapter<ItemUserAdapter.ViewHolder> implements Updater {


    private Context mContext;
    public List<User> mUsers;

    private boolean isChat;
    private static UserProfilePopup currentPopup;

    public ItemUserAdapter(Context context, List<User> users, boolean isChat) {
        this.mContext = context;
        this.mUsers = users;
        this.isChat = isChat;
    }



    @NonNull
    @Override
    public ItemUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_users, parent, false);

        return new ItemUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemUserAdapter.ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profileImage);
        }

        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
                holder.img_playing.setVisibility(View.GONE);
            } else if (user.getStatus().equals("offline")) {
                holder.img_off.setVisibility(View.VISIBLE);
                holder.img_on.setVisibility(View.GONE);
                holder.img_playing.setVisibility(View.GONE);
            } else if (user.getStatus().equals("playing")) {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);
                holder.img_playing.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        if (user.getId() != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup(new UserProfilePopup(mContext, user));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public void update() {

        if (currentPopup != null) {
            if (currentPopup.isShowing()) {
                currentPopup.update();
            }
        }
    }

    private void  popup(UserProfilePopup popup) {
        currentPopup = popup;
        currentPopup.show();
        update();
    }

    //boite pour ranger tous les composants à controler
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profileImage;
        public ImageView img_on;
        public ImageView img_off;
        public ImageView img_playing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.status_on);
            img_off = itemView.findViewById(R.id.status_off);
            img_playing = itemView.findViewById(R.id.status_playing);
        }
    }

}
