package fr.smato.gameproject.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.R;
import fr.smato.gameproject.activities.game.GameActivity;
import fr.smato.gameproject.activities.menu.MainActivity;
import fr.smato.gameproject.model.WaitingGame;

public class ItemWaitingGamesAdapter extends RecyclerView.Adapter<ItemWaitingGamesAdapter.ViewHolder> {

    private MainActivity context;
    private List<WaitingGame> waitingGames;

    public ItemWaitingGamesAdapter(MainActivity context, List<WaitingGame> waitingGames) {
        this.context = context;
        this.waitingGames = waitingGames;
    }

    @NonNull
    @Override
    public ItemWaitingGamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_waiting_game, parent, false);


        return new ItemWaitingGamesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemWaitingGamesAdapter.ViewHolder holder, int position) {
        final WaitingGame game = waitingGames.get(position);

        holder.gameName.setText(game.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("id", game.getId());
                intent.putExtra("host", (DataBaseManager.currentUser.getUsername().equalsIgnoreCase("smato") ? true : false));
                context.startActivity(intent);
                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return waitingGames.size();
    }


    //boite pour ranger tous les composants à controler
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView gameName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.gameName);
        }
    }
}
