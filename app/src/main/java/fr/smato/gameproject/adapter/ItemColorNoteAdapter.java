package fr.smato.gameproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.smato.gameproject.R;
import fr.smato.gameproject.game.model.enums.PlayerColor;

public class ItemColorNoteAdapter extends RecyclerView.Adapter<ItemColorNoteAdapter.ViewHolder>{

    private final Context context;

    private ItemColorNoteAdapter.ViewHolder current;


    public ItemColorNoteAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_color, parent, false);



        return new ItemColorNoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemColorNoteAdapter.ViewHolder holder, int position) {

        holder.innerCircle.setImageResource(PlayerColor.values()[position].COLOR_ID);

        holder.itemView.setOnClickListener(e ->{
            if (current != null) current.unselect();
            current = holder;
            current.select();
        });

    }

    @Override
    public int getItemCount() {
        return PlayerColor.values().length;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView outCircleTrue;
        public CircleImageView outCircleFalse;
        public CircleImageView innerCircle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            outCircleTrue = itemView.findViewById(R.id.out_circle_true);
            outCircleFalse= itemView.findViewById(R.id.out_circle_false);
            innerCircle = itemView.findViewById(R.id.inner_circle);
        }

        private void select() {
            outCircleFalse.setVisibility(View.GONE);
            outCircleTrue.setVisibility(View.VISIBLE);
        }

        private void unselect() {
            outCircleFalse.setVisibility(View.VISIBLE);
            outCircleTrue.setVisibility(View.GONE);
        }
    }

}
