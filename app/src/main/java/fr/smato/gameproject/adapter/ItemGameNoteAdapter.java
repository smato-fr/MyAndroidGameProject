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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.smato.gameproject.R;
import fr.smato.gameproject.fragments.notepop.GameNotePlayerFragment;
import fr.smato.gameproject.popup.GameNotePopupLayout;

public class ItemGameNoteAdapter extends RecyclerView.Adapter<ItemGameNoteAdapter.ViewHolder>{

    private final List<String> list;
    private Context context;

    private GameNotePopupLayout parent;

    public ItemGameNoteAdapter(Context context, List<String> list, GameNotePopupLayout parent) {
        this.context = context;
        this.list = list;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ItemGameNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_chat, parent, false);

        return new ItemGameNoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemGameNoteAdapter.ViewHolder holder, int position) {

        String image = list.get(position);

        if (image.equals("default")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(image).into(holder.profileImage);
        }

        holder.profileImage.setOnClickListener(v -> parent.loadFragment(new GameNotePlayerFragment(context)));
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
