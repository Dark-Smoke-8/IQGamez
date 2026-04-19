package com.example.iqgamez;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    Context context;
    List<GameModel> list;
    boolean isLeaderboard = false;

    public GameAdapter(Context context, List<GameModel> list) {
        this.context = context;
        this.list = list;
    }

    public GameAdapter(Context context, List<GameModel> list, boolean isLeaderboard) {
        this.context = context;
        this.list = list;
        this.isLeaderboard = isLeaderboard;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView gameImage;
        TextView gameName;

        public ViewHolder(View itemView) {
            super(itemView);
            gameImage = itemView.findViewById(R.id.gameImage);
            gameName = itemView.findViewById(R.id.gameName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GameModel game = list.get(position);

        holder.gameImage.setImageResource(game.image);
        holder.gameName.setText(game.name);

        holder.itemView.setOnClickListener(v -> {
            Intent intent;

            if (game.name.equals("Riddle Solver")) {
                intent = new Intent(context, DifficultyActivity.class);
                intent.putExtra("gameType", "riddle");

                if (isLeaderboard) {
                    intent.putExtra("isLeaderboard", true);
                }

                context.startActivity(intent);

            } else if (game.name.equals("Multiplication Puzzle")) {
                intent = new Intent(context, DifficultyActivity.class);
                intent.putExtra("gameType", "multiplication");
                context.startActivity(intent);
            }else if(game.name.equals("Card Game")){
                intent = new Intent(context, DifficultyActivity.class);
                intent.putExtra("gameType", "card");
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}