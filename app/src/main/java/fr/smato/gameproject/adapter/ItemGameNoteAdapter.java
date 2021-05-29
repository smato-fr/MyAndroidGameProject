package fr.smato.gameproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.smato.gameproject.R;
import fr.smato.gameproject.fragments.notepop.GameNotePlayerFragment;
import fr.smato.gameproject.game.model.objects.Player;
import fr.smato.gameproject.game.model.utils.PlayerList;
import fr.smato.gameproject.model.User;
import fr.smato.gameproject.popup.GameNotePopupLayout;

public class ItemGameNoteAdapter extends RecyclerView.Adapter<ItemGameNoteAdapter.ViewHolder>{

    private final PlayerList list;
    private static GameNotePopupLayout parent;

    private static Context context;

    public ItemGameNoteAdapter(GameNotePopupLayout parent) {
        this.context = parent.getContext();
        this.parent = parent;

        list = parent.getGameActivity().getGameView().getPlayers();
    }

    @NonNull
    @Override
    public ItemGameNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_chat, parent, false);

        return new ItemGameNoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemGameNoteAdapter.ViewHolder holder, int position) {

        Player p = list.get(position);

        String image = p.getUser().getImageURL();

        if (image.equals("default")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(image).into(holder.profileImage);
        }

        holder.profileImage.setOnClickListener(v -> loadFragment(p));
    }

    private void loadFragment(Player p) {

        GameNotePlayerFragment fragment;


        //looking for player notes
        if (p.getDatas().get("NF") != null)
            fragment = (GameNotePlayerFragment) p.getDatas().get("NF");

        else {
            fragment = new GameNotePlayerFragment(context);
            p.getDatas().put("NF", fragment);
            fragment.init(p.getUser());
        }

        parent.loadFragment(fragment);

    }



    @Override
    public int getItemCount() {
        return list.size();
    }






    class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}
