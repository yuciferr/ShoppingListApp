package com.example.shoppinglist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppinglist.Models.User;
import com.example.shoppinglist.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    private static final int RC_SIGN_IN = 101;

    private GoogleSignInClient gsc;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Google Sign In ...");

        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        binding.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                signIn();

            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        if (user != null) {
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }
    }

    public void login() {
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.setError("Please provide valid email");
            binding.email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.password.setError("Password is required");
            binding.password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.password.setError("Password length should be more than 6 characters");
            binding.password.requestFocus();
            return;
        }


        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if(user.isEmailVerified()){
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                            binding.progressBar.setVisibility(View.GONE);
                            finish();
                        }else{
                            user.sendEmailVerification();
                            Toast.makeText(this, "Check your mail", Toast.LENGTH_SHORT).show();
                        }*/
                        startActivity(new Intent(SignIn.this, MainActivity.class));
                        binding.progressBar.setVisibility(View.GONE);
                        finish();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Failed try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                dialog.dismiss();
                Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            dialog.dismiss();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            dialog.dismiss();
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                        }
                    }


                });
    }

    private void updateUI(FirebaseUser user) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            String name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            Uri photoUrl=FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

            User userD=new User(name,email,photoUrl.toString());

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(userD).addOnCompleteListener(task1->{
                        if(task1.isSuccessful()){
                            Toast.makeText(SignIn.this,"User created",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignIn.this,"User not created",Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        Intent intent = new Intent(SignIn.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}