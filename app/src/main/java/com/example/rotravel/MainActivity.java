package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotravel.HelperClasses.AllCitiesRecViewAdapter;
import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.Model.Place;
import com.example.rotravel.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    MaterialButton btnLogin;
    TextView btnRegister;
    EditText txtEmail, txtPassword;
    private DatabaseReference mDatabse;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        mAuth = FirebaseAuth.getInstance();
        mDatabse = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("User");

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> loginUser());
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            getUser(currentUser.getUid());
//        }
//    }

    private void loginUser(){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
        }else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    getUser(task.getResult().getUser().getUid());
                } else {
                    Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(task.getException().getMessage());
                }
            });
        }
    }

    private void getUser(String uid){
        ValueEventListener postListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(uid).getValue(User.class);
                ApplicationManager.getInstance().setUser(user);
                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        mDatabse.addValueEventListener(postListener);
    }
}