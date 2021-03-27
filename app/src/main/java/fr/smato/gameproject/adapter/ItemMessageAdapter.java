package fr.smato.gameproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.model.Chat;

public class ItemMessageAdapter  extends RecyclerView.Adapter<ItemMessageAdapter.ViewHolder>{

    public final static int MSG_TYPE_LEFT = 1;
    public final static int MSG_TYPE_LEFT_UNDER = -1;
    public final static int MSG_TYPE_RIGHT = 2;
    public final static int MSG_TYPE_RIGHT_UNDER = -2;

    protected Context mContext;
    public List<Chat> mChat;
    private String imageURL;



    public ItemMessageAdapter(Context context, List<Chat> mChat, String imageURL) {
        this.mContext = context;
        this.mChat = mChat;
        this.imageURL = imageURL;
    }



    @NonNull
    @Override
    public ItemMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;

        switch (viewType) {
            case MSG_TYPE_RIGHT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_mp_right, parent, false);
                break;

            case MSG_TYPE_RIGHT_UNDER:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_under_right, parent, false);
                break;

            case MSG_TYPE_LEFT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_mp_left, parent, false);
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
    public void onBindViewHolder(@NonNull ItemMessageAdapter.ViewHolder holder, int position) {
        final Chat chat = mChat.get(position);


        holder.message.setText(chat.getMessage());

        switch (holder.getItemViewType()) {

            case MSG_TYPE_LEFT:
                holder.date.setText(chat.getDate());

                if (imageURL.equals("default")) {
                    holder.profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(mContext).load(imageURL).into(holder.profileImage);
                }
                break;

            case MSG_TYPE_RIGHT:
                holder.date.setText(chat.getDate());

                if (DataBaseManager.currentUser.getImageURL().equals("default")) {
                    holder.profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(mContext).load(DataBaseManager.currentUser.getImageURL()).into(holder.profileImage);
                }
                break;

        }


    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = mChat.get(position);
        if (chat.getSender().equals(DataBaseManager.currentUser.getId())) {

            if (position > 0 && mChat.get(position-1).getSender().equals(DataBaseManager.currentUser.getId()) && mChat.get(position-1).getDate().equals(chat.getDate())) {
                return MSG_TYPE_RIGHT_UNDER;
            }

            return MSG_TYPE_RIGHT;
        } else {
            if (position > 0 &&  !mChat.get(position-1).getSender().equals(DataBaseManager.currentUser.getId()) && mChat.get(position-1).getDate().equals(chat.getDate())) {
                return MSG_TYPE_LEFT_UNDER;
            }
            return  MSG_TYPE_LEFT;
        }
    }

    //boite pour ranger tous les composants à controler
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView message;
        public ImageView profileImage;
        public TextView date;
        public TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            profileImage = itemView.findViewById(R.id.profile_image);
            date = itemView.findViewById(R.id.time);
            username = itemView.findViewById(R.id.username);
        }
    }


}
