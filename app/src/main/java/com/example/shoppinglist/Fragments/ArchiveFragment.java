package com.example.shoppinglist.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.shoppinglist.Adapeters.ArchiveCardAdapter;
import com.example.shoppinglist.Models.Cards;
import com.example.shoppinglist.Models.Item;
import com.example.shoppinglist.Models.List;
import com.example.shoppinglist.databinding.FragmentArchiveBinding;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment {

    FragmentArchiveBinding binding;
    ArrayList<Cards> cards = new ArrayList<>();
    ArrayList<List> lists = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();
    ArchiveCardAdapter adapter;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentArchiveBinding.inflate(inflater, container, false);
        adapter = new ArchiveCardAdapter(cards, getContext());
        binding.recyclerView.setAdapter(adapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(staggeredGridLayoutManager);

        Cards card = new Cards();
        card.setTitle("Archive");
        card.setPrice("50TL");
        card.setWho("You");
        cards.add(card);

        return binding.getRoot();
    }

    public void onStart() {
        super.onStart();

    }
}