package fr.smato.gameproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.model.Chat;
import fr.smato.gameproject.model.User;
import fr.smato.gameproject.utils.callback.UserLoadCallback;

public class ItemGameMessageAdapter extends ItemMessageAdapter {


    public ItemGameMessageAdapter(Context context, List<Chat> mChat) {
        super(context, mChat, null);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;

        switch (viewType) {
            case MSG_TYPE_RIGHT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_game_right, parent, false);
                break;

            case MSG_TYPE_RIGHT_UNDER:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_under_right, parent, false);
                break;

            case MSG_TYPE_LEFT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_game_left, parent, false);
                break;

            case MSG_TYPE_LEFT_UNDER:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_under_left, parent, false);
                break;
            default:
                view = null;
                break;
        }

        return new ItemMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Chat chat = mChat.get(position);



        final UserLoadCallback callback = new UserLoadCallback() {
            @Override
            public void onEvent(User user) {
                if (holder.getItemViewType() == MSG_TYPE_LEFT) {

                    holder.username.setText(user.getUsername());
                    if (user.getImageURL().equals("default")) {
                        holder.profileImage.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(mContext).load(user.getImageURL()).into(holder.profileImage);
                    }

                }  else  if (holder.getItemViewType() == MSG_TYPE_RIGHT){

                    holder.username.setText(user.getUsername());
                    if (DataBaseManager.currentUser.getImageURL().equals("default")) {
                        holder.profileImage.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(mContext).load(DataBaseManager.currentUser.getImageURL()).into(holder.profileImage);
                    }
                }
            }
        };

        User user = DataBaseManager.getUserById(chat.getSender());

        if (user != null) {
            callback.onEvent(user);
        } else {
            DataBaseManager.loadUser(chat.getSender(), callback);
        }

        holder.message.setText(chat.getMessage());
    }


    @Override
    public int getItemViewType(int position) {
        Chat chat = mChat.get(position);
        if (chat.getSender().equals(DataBaseManager.currentUser.getId())) {

            if (position > 0 && mChat.get(position-1).getSender().equals(chat.getSender())) {
                return MSG_TYPE_RIGHT_UNDER;
            }

            return MSG_TYPE_RIGHT;
        } else {
            if (position > 0 &&  mChat.get(position-1).getSender().equals(chat.getSender())) {
                return MSG_TYPE_LEFT_UNDER;
            }
            return  MSG_TYPE_LEFT;
        }
    }


}
