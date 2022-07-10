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

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    ArrayList<Item> list;
    Context context;
    Dialog editItem;

    public ItemAdapter(ArrayList<Item> list, Context context) {
        this.context = context;
        this.list = list;
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
        holder.itemName.setText(list.get(position).getName());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    int len = holder.itemName.getText().toString().trim().length();
                    String str = "";
                    for (int i = 0; i < len; i++) {
                        str += "--";
                    }
                    holder.strike.setText(str);
                    holder.strike.setVisibility(View.VISIBLE);

                } else {
                    holder.strike.setVisibility(View.INVISIBLE);
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
                        holder.brand.setText(nBrand.getText().toString());
                        holder.amount.setText(nAmount.getText().toString());
                        editItem.dismiss();
                    }
                });
                editItem.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
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
