package com.example.iqgamez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    RecyclerView recyclerView;
    List<GameModel> list;
    GameAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerLeaderboard);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        list = new ArrayList<>();

        list.add(new GameModel(R.drawable.riddle_icon, "Riddle Solver"));
        list.add(new GameModel(R.drawable.cards_icon, "Card Game"));
        list.add(new GameModel(R.drawable.multi_icon, "Multiplication Puzzle"));

        adapter = new GameAdapter(getContext(), list, true);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
