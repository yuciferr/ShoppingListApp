package com.example.shoppinglist.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.shoppinglist.Adapeters.CardAdapter;
import com.example.shoppinglist.Models.Cards;
import com.example.shoppinglist.Models.Item;
import com.example.shoppinglist.Models.List;
import com.example.shoppinglist.databinding.FragmentListsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListsFragment extends Fragment {

    FragmentListsBinding binding;
    ArrayList<Cards> cards = new ArrayList<>();
    ArrayList<List> lists = new ArrayList<>();
    ArrayList<Item> items = new ArrayList<>();
    CardAdapter adapter;
    String content = "";

    public ListsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListsBinding.inflate(inflater, container, false);

        adapter = new CardAdapter(cards, getContext());
        binding.recyclerView.setAdapter(adapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(staggeredGridLayoutManager);

        onStart();


        return binding.getRoot();
    }

    public void onStart() {
        super.onStart();

        if(cards.size() == 0) {
            binding.progressBar3.setVisibility(View.VISIBLE);
            Cards message = new Cards("Welcome Shopping List", "1-Create your shopping list and start adding items\n\n" +
                    "2-Add items to your list and share it with your friends\n\n" +"3-Delete items from your list by swiping left");


            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                    .getInstance().getCurrentUser().getUid()).child("Lists").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lists.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        List list = dataSnapshot.getValue(List.class);
                        assert list != null;
                        list.setListId(dataSnapshot.getKey());
                        lists.add(list);
                    }
                    adapter.notifyDataSetChanged();
                    if (lists != null) {
                        cards.clear();
                        cards.add(message);
                        for (List list : lists) {
                            Cards card = new Cards();
                            card.setTitle(list.getName());
                            card.setId(list.getListId());

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                                            .getInstance().getCurrentUser().getUid()).child("Lists").
                                    child(list.getListId()).child("Items").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            items.clear();
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Item item = dataSnapshot.getValue(Item.class);
                                                assert item != null;
                                                item.setItemId(list.getListId());
                                                items.add(item);
                                            }
                                            adapter.notifyDataSetChanged();
                                            if (items != null) {
                                                for(Item item : items){
                                                    if(item.getItemId().equals(list.getListId())){
                                                        content+= item.getName() + "\n";
                                                    }
                                                }
                                                card.setContent(content);
                                                content = "";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                            cards.add(card);

                        }
                        binding.progressBar3.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}