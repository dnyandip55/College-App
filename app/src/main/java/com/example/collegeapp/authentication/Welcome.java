package com.example.collegeapp.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeapp.MainActivity;
import com.example.collegeapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Check if the user is already logged in
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is already logged in, start the MainActivity
            startActivity(new Intent(Welcome.this, MainActivity.class));
            finish(); // Finish the Welcome activity
            return; // Return to avoid executing the rest of the code
        }

        Button buttonLogin=findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(view -> {
            Intent intent=new Intent(Welcome.this,LoginActivity.class);
            startActivity(intent);
        });

        TextView buttonRegister=findViewById(R.id.register);
        buttonRegister.setOnClickListener(view -> {
            Intent intent=new Intent(Welcome.this,RegisterActivity.class);
            startActivity(intent);
        });
    }
}