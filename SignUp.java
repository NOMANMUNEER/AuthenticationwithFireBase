package com.example.gidanbeachresorts;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore; // <-- Import Firestore

import java.util.HashMap; // <-- Import HashMap
import java.util.Map;     // <-- Import Map

public class SignUpActivity extends AppCompatActivity {

    // --- ADD THE NEW EDITTEXTS ---
    private EditText editTextFullName, editTextPhone, editTextEmail, editTextPassword;
    private Button buttonSignUp;
    private TextView textViewLoginLink;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // <-- Declare Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // <-- Initialize Firestore

        // Initialize views
        editTextFullName = findViewById(R.id.editTextFullName); // <-- Init Full Name
        editTextPhone = findViewById(R.id.editTextPhone);       // <-- Init Phone
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLoginLink = findViewById(R.id.textViewLoginLink);

        buttonSignUp.setOnClickListener(v -> registerNewUser());

        textViewLoginLink.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void registerNewUser() {
        // --- GET TEXT FROM NEW FIELDS ---
        final String fullName = editTextFullName.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // --- VALIDATION FOR NEW FIELDS ---
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(getApplicationContext(), "Please enter your full name...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }


        // Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            // --- SAVE USER DATA TO FIRESTORE ---
                            saveUserData(userId, fullName, email, phone);
                        }
                    } else {
                        // Registration failed
                        Toast.makeText(getApplicationContext(), "Registration failed! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // --- NEW METHOD TO SAVE USER DATA ---
    private void saveUserData(String userId, String fullName, String email, String phone) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("phone", phone);

        // Add a new document with a generated ID
        db.collection("Users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "DocumentSnapshot successfully written!");
                    // Navigate to Login activity after successful data save
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error writing document", e);
                    Toast.makeText(getApplicationContext(), "Failed to save user data.", Toast.LENGTH_SHORT).show();
                });
    }
}
