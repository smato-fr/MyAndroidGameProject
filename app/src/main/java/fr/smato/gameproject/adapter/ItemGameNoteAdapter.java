package fr.smato.gameproject.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemGameNoteAdapter extends RecyclerView.Adapter<ItemMessageAdapter.ViewHolder>{

    private Context context;

    public ItemGameNoteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMessageAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
