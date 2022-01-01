package com.example.rotravel;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rotravel.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText txtFirstName, txtLastName, txtPhone, txtEmail, txtPassword, txtRePassword;
    MaterialButton btnRegister;
    private DatabaseReference mDatabse;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtRePassword = findViewById(R.id.txtRePassword);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        mDatabse = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("User");

        btnRegister.setOnClickListener(view -> {
            createUser();
        });
    }

    private void createUser(){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
        }else{
            User user = new User();
            user.setFirstName(txtFirstName.getText().toString());
            user.setLastName(txtLastName.getText().toString());
            user.setPhone(txtPhone.getText().toString());
            user.setEmail(txtEmail.getText().toString());

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User Created",Toast.LENGTH_SHORT).show();
                        user.setId(task.getResult().getUser().getUid());
                        mDatabse.child(user.getId()).setValue(user);
                        startActivity(new Intent(RegisterActivity.this, WelcomeActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, " " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(task.getException().getMessage());
                    }
                }
            });
        }
    }

}