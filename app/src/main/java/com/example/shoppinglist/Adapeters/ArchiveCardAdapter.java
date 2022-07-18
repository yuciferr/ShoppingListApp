package com.example.shoppinglist.Adapeters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.Models.Cards;
import com.example.shoppinglist.R;

import java.util.ArrayList;

public class ArchiveCardAdapter extends RecyclerView.Adapter<ArchiveCardAdapter.ViewHolder> {

    Context context;
    ArrayList<Cards> cards;

    public ArchiveCardAdapter(ArrayList<Cards> cards, Context context) {
        this.context = context;
        this.cards = cards;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.archive_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(cards.get(position).getTitle());
        holder.who.setText(cards.get(position).getWho());
        holder.price.setText(cards.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, price,who;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            who = itemView.findViewById(R.id.who);
        }
    }
}
