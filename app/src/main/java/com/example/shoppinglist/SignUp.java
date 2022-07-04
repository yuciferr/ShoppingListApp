package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppinglist.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();


        binding.singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        binding.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });
    }

    public void register(){
        String name = binding.name.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        if(name.isEmpty()){
            binding.name.setError("Name is required");
            binding.name.requestFocus();
            return;
        }

        if(email.isEmpty()){
            binding.email.setError("Email is required");
            binding.email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.setError("Please provide valid email");
            binding.email.requestFocus();
            return;
        }

        if(password.isEmpty()){
            binding.password.setError("Password is required");
            binding.password.requestFocus();
            return;
        }

        if(password.length()<6){
            binding.password.setError("Password length should be more than 6 characters");
            binding.password.requestFocus();
            return;
        }


        binding.progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Users user = new Users(name, email);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                binding.progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                finish();
                            }
                        });

                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Failed try again", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}