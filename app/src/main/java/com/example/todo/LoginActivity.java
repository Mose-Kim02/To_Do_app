package com.example.todo;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {


    private EditText email, password;
    private Button loginButton;
    private TextView registerButton, forgotPasswordButton;
    private FirebaseAuth auth; // Firebase Auth instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);


        // Optional: Add Forgot Password functionality




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();


                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }


                loginUser(emailText, passwordText);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });




    }


    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();


                            // Navigate to MainActivity
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish(); // Prevent going back to Login screen
                        } else {
                            Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Login failed!";
                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
