package fr.smato.gameproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.smato.gameproject.R;

public class ItemSelectableNoteAdapter extends RecyclerView.Adapter<ItemSelectableNoteAdapter.ViewHolder>{

    private final Context context;


    private int[] ID;

    public ItemSelectableNoteAdapter(Context context, int[] ID) {
        this.context = context;
        this.ID = ID;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_color, parent, false);


        return new ItemSelectableNoteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSelectableNoteAdapter.ViewHolder holder, int position) {

        holder.innerCircle.setImageResource(ID[position]);

        holder.itemView.setOnClickListener(e ->{
            holder.select();
        });

    }

    @Override
    public int getItemCount() {
        return ID.length;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView outCircleYes;
        public CircleImageView outCircleNeutral;
        public CircleImageView outCircleNo;
        public CircleImageView innerCircle;

        private short mode = -1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            outCircleYes = itemView.findViewById(R.id.out_circle_yes);
            outCircleNeutral = itemView.findViewById(R.id.out_circle_neutral);
            outCircleNo = itemView.findViewById(R.id.out_circle_no);
            innerCircle = itemView.findViewById(R.id.inner_circle);
        }

        private void select() {


            if (mode == 2) {
                mode=1;
                outCircleNeutral.setVisibility(View.VISIBLE);
                outCircleYes.setVisibility(View.GONE);
                outCircleNo.setVisibility(View.GONE);
            }

            else if (mode == -2) {
                mode=-1;
                outCircleNeutral.setVisibility(View.VISIBLE);
                outCircleYes.setVisibility(View.GONE);
                outCircleNo.setVisibility(View.GONE);
            }
            else if (mode == 1) {
                mode=-2;
                outCircleNeutral.setVisibility(View.GONE);
                outCircleYes.setVisibility(View.GONE);
                outCircleNo.setVisibility(View.VISIBLE);
            }
            else if (mode == -1) {
                mode=2;
                outCircleNeutral.setVisibility(View.GONE);
                outCircleYes.setVisibility(View.VISIBLE);
                outCircleNo.setVisibility(View.GONE);
            }

        }


    }

}
