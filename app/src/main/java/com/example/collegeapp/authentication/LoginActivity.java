package com.example.collegeapp.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeapp.MainActivity;
import com.example.collegeapp.R;
import com.example.collegeapp.profile.ForgotPasswordActivity;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextLoginEmail,editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Find the TextView for the register link
        TextView registerLinkTextView = findViewById(R.id.textView_register_link);

        // Set an OnClickListener to navigate to the registration activity when the link is clicked
        registerLinkTextView.setOnClickListener(v -> {
            // Start the registration activity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


        //set up forgot password link

        TextView forgotPasswordLinkTextView=findViewById(R.id.textView_forgot_password_link);
        forgotPasswordLinkTextView.setOnClickListener(view -> {
            Toast.makeText(LoginActivity.this, "You can reset your password now", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
        // Initialize Firebase Auth
        authProfile = FirebaseAuth.getInstance();
        ImageView imageViewShowHidePwd=findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(view -> {
            if(editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
            }else{
                editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
            }
        });

        // Initialize EditText and ProgressBar
        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressBar);



        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(v -> {
            String textEmail=editTextLoginEmail.getText().toString();
            String textPwd=editTextLoginPwd.getText().toString();

            if(TextUtils.isEmpty(textEmail)){
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                editTextLoginEmail.setError("Email is required");
                editTextLoginEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                editTextLoginEmail.setError("Valid email is required");
                editTextLoginEmail.requestFocus();
            } else if (TextUtils.isEmpty(textPwd)) {
                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                editTextLoginPwd.setError("Password is required");
                editTextLoginPwd.requestFocus();
            }else {
                progressBar.setVisibility(View.VISIBLE);
                loginUser(textEmail, textPwd);
            }

        });

    }

    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE); // Hide progress bar when login attempt completes

            if (task.isSuccessful()) {
                // Login successful

                // Get instance of the current user
                FirebaseUser firebaseUser = authProfile.getCurrentUser();

                // Check if email is verified before user can access user profile
                assert firebaseUser != null;
                if (firebaseUser.isEmailVerified()) {
                    Toast.makeText(LoginActivity.this, "You are logged in now", Toast.LENGTH_SHORT).show();

                    // Open app
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    firebaseUser.sendEmailVerification(); // Send verification email
                    showAlertDialog(); // Show alert dialog
                }
            } else {
                // Login failed
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthInvalidUserException e) {
                    // User not found
                    Toast.makeText(LoginActivity.this, "No user found with this email", Toast.LENGTH_SHORT).show();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    // Invalid password
                    Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                } catch (FirebaseNetworkException e) {
                    // Network error
                    Toast.makeText(LoginActivity.this, "Network error, please try again later", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Other exceptions
                    Toast.makeText(LoginActivity.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void showAlertDialog() {
        //set up alert builder
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now.You can not login without email verification.");

        //open email app is user clicks/taps continue button

        builder.setPositiveButton("Continue", (dialogInterface, i) -> {
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //TO email app in new window
            startActivity(intent);

        });

        //Create the AlertDialog

        AlertDialog alertDialog= builder.create();

        //SHow Alert Dialog
        alertDialog.show();


    }
  //  check if user is already logged in , in such case straightway take the user to the MainActivity
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() !=null){
            Toast.makeText(LoginActivity.this,"Already Logged In !",Toast.LENGTH_SHORT).show();
            //Start user profile activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(LoginActivity.this,"You can login now",Toast.LENGTH_SHORT).show();
        }
    }

}