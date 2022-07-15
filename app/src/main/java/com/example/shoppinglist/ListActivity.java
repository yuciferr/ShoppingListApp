package com.example.shoppinglist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.Adapeters.ItemAdapter;
import com.example.shoppinglist.Models.Item;
import com.example.shoppinglist.Models.List;
import com.example.shoppinglist.databinding.ActivityListBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<List> lists = new ArrayList<>();
    ItemAdapter adapter;
    Item deletedItem;
    String idList;
    String idItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar2.setVisibility(View.VISIBLE);

        adapter = new ItemAdapter(items, this);
        binding.recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(items.size() - 1);
        binding.recyclerView.setLayoutManager(layoutManager);


        idList = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

        if (!(idList == null)) {
            binding.progressBar2.setVisibility(View.VISIBLE);

            binding.listName.setText(name);

            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                            .getInstance().getCurrentUser().getUid()).child("Lists").child(idList)
                    .child("Items").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            items.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Item item = snapshot.getValue(Item.class);
                                item.setItemId(snapshot.getKey());
                                item.setListId(idList);
                                items.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            binding.progressBar2.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                    .getInstance().getCurrentUser().getUid()).child("Lists").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lists.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        List list = dataSnapshot.getValue(List.class);
                        list.setListId(dataSnapshot.getKey());
                        idList = dataSnapshot.getKey();
                        lists.add(list);
                    }
                    if (lists != null) {
                        binding.listName.setText(lists.get((lists.size()) - 1).getName());
                        binding.progressBar2.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerView);
    }

    public void save(View view) {

        String data = binding.enterItems.getText().toString().trim();
        data = upperLetter(data);
        items.add(new Item(false, data, Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getDisplayName(), false));
        adapter.notifyDataSetChanged();
        binding.enterItems.setText("");

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                        .getInstance().getCurrentUser().getUid()).child("Lists").child(idList)
                .child("Items").push().setValue(items.get(items.size() - 1));
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            deletedItem = items.get(position);
            idItem = items.get(position).getItemId();

            //Remove from firebase
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                            .getInstance().getCurrentUser().getUid()).child("Lists").child(idList)
                    .child("Items").child(idItem).removeValue();


            //Remove from arraylist
            items.remove(position);


            adapter.notifyDataSetChanged();
            Snackbar snackbar = Snackbar.make(binding.recyclerView, "Item deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                                    .getInstance().getCurrentUser().getUid()).child("Lists").child(idList)
                            .child("Items").child(idItem).setValue(deletedItem);
                    items.add(position, deletedItem);
                    adapter.notifyDataSetChanged();
                }
            });
            snackbar.setAnchorView(binding.enterItems);
            snackbar.show();
        }
    };

    public String upperLetter(String name) {
        String result = "";
        String[] n1 = name.split(" ");
        for (String n : n1) {
            result += n.substring(0, 1).toUpperCase() + n.substring(1) + " ";
        }
        return result;
    }

}