package com.example.shoppinglist.Adapeters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.ListActivity;
import com.example.shoppinglist.Models.Cards;
import com.example.shoppinglist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    Context context;
    ArrayList<Cards> list;
    AlertDialog.Builder alert;

    public CardAdapter(ArrayList<Cards> list, Context context) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        alert = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.list_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.content.setText(list.get(position).getContent());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                alert.setTitle("Deleting List");
                alert.setMessage("Are you sure you want to delete this list?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(list.get(holder.getAdapterPosition()).getId()!=null){
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                                        .getInstance().getCurrentUser().getUid()).child("Lists").
                                child(list.get(holder.getAdapterPosition()).getId()).removeValue();
                        }

                        list.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("name", list.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("id", list.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);

        }
    }
}
