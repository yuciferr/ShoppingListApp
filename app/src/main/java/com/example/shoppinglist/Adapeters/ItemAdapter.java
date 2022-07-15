package com.example.shoppinglist.Adapeters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.Models.Item;
import com.example.shoppinglist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    ArrayList<Item> items;
    Context context;
    Dialog editItem;

    public ItemAdapter(ArrayList<Item> items, Context context) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        editItem = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int p = position;
        holder.itemName.setText(items.get(position).getName());
        holder.personName.setText(items.get(position).getPersonName());
        holder.amount.setText(items.get(position).getAmount());
        holder.brand.setText(items.get(position).getBrand());

        if(items.get(position).isBought()){
            holder.checkBox.setChecked(true);
            holder.itemName.setTextColor(Color.parseColor("#BCB4B4"));
            holder.personName.setTextColor(Color.parseColor("#BCB4B4"));
            holder.brand.setTextColor(Color.parseColor("#BCB4B4"));
            holder.amount.setTextColor(Color.parseColor("#BCB4B4"));
        }else{
            holder.checkBox.setChecked(false);
            holder.itemName.setTextColor(Color.parseColor("#302E2E"));
            holder.personName.setTextColor(Color.parseColor("#302E2E"));
            holder.brand.setTextColor(Color.parseColor("#302E2E"));
            holder.amount.setTextColor(Color.parseColor("#302E2E"));
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    holder.itemName.setTextColor(Color.parseColor("#BCB4B4"));
                    holder.personName.setTextColor(Color.parseColor("#BCB4B4"));
                    holder.brand.setTextColor(Color.parseColor("#BCB4B4"));
                    holder.amount.setTextColor(Color.parseColor("#BCB4B4"));

                    items.get(p).setBought(true);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                            .getInstance().getCurrentUser().getUid()).child("Lists").child(items.get(p).getListId())
                            .child("Items").child(items.get(p).getItemId()).child("bought").setValue(true);

                } else {

                    holder.itemName.setTextColor(Color.parseColor("#302E2E"));
                    holder.personName.setTextColor(Color.parseColor("#302E2E"));
                    holder.brand.setTextColor(Color.parseColor("#302E2E"));
                    holder.amount.setTextColor(Color.parseColor("#302E2E"));

                    items.get(p).setBought(false);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                                    .getInstance().getCurrentUser().getUid()).child("Lists").child(items.get(p).getListId())
                            .child("Items").child(items.get(p).getItemId()).child("bought").setValue(false);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem.setContentView(R.layout.edit_item);
                editItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editItem.getWindow().setGravity(Gravity.BOTTOM);


                ImageView done = editItem.findViewById(R.id.done2);
                TextView nAmount = editItem.findViewById(R.id.enter_amount);
                TextView nBrand = editItem.findViewById(R.id.enter_brand);

                nAmount.setText(holder.amount.getText().toString());
                nBrand.setText(holder.brand.getText().toString());


                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.brand.setText(nBrand.getText().toString().trim());
                        holder.amount.setText(nAmount.getText().toString().trim());

                        items.get(p).setAmount(nAmount.getText().toString().trim());
                        items.get(p).setBrand(nBrand.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                                        .getInstance().getCurrentUser().getUid()).child("Lists").child(items.get(p).getListId())
                                .child("Items").child(items.get(p).getItemId()).child("amount").setValue(nAmount.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                                        .getInstance().getCurrentUser().getUid()).child("Lists").child(items.get(p).getListId())
                                .child("Items").child(items.get(p).getItemId()).child("brand").setValue(nBrand.getText().toString().trim());

                        editItem.dismiss();
                    }
                });
                editItem.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, personName, brand, amount, strike;
        // add checkbox and priority
        CheckBox checkBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            personName = itemView.findViewById(R.id.item_personName);
            brand = itemView.findViewById(R.id.item_brand);
            amount = itemView.findViewById(R.id.item_amount);
            checkBox = itemView.findViewById(R.id.checkBox);
            strike = itemView.findViewById(R.id.strike);
        }
    }

}
