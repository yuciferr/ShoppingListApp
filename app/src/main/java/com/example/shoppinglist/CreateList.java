package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppinglist.Models.List;
import com.example.shoppinglist.databinding.ActivityCreateListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateList extends AppCompatActivity {

    ActivityCreateListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.nameList.getText().toString().equals("")) {
                    binding.nameList.setError("Field is required");
                    binding.nameList.requestFocus();
                } else {

                    String name = binding.nameList.getText().toString().trim();
                    name = upperLetter(name);
                    List list = new List(name);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                            .getInstance().getCurrentUser().getUid()).child("Lists").push().setValue(list);
                    startActivity(new Intent(CreateList.this, ListActivity.class));
                    finish();
                }
            }
        });
    }

    public String upperLetter(String name) {
        String result = "";
        String[] n1 = name.split(" ");
        for (String n : n1) {
            result += n.substring(0, 1).toUpperCase() + n.substring(1) + " ";
        }
        return result;
    }
}