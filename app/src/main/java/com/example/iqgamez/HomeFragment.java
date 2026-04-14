package com.example.iqgamez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    List<GameModel> list;
    GameAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerGames);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        list = new ArrayList<>();

        list.add(new GameModel(R.drawable.riddle_icon, "Riddle Solver"));
        list.add(new GameModel(R.drawable.cards_icon, "Card Game"));

        adapter = new GameAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        return view;
    }
}