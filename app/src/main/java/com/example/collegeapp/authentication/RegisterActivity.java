package com.example.collegeapp.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.collegeapp.R;
import com.example.collegeapp.profile.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterMobile,
            editTextRegisterPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        Toast.makeText(RegisterActivity.this, "You can register now " , Toast.LENGTH_SHORT).show();


        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        // Setting up DatePicker on EditText for date of birth
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextRegisterDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = editTextRegisterFullName.getText().toString();
                String email = editTextRegisterEmail.getText().toString();
                String dob = editTextRegisterDoB.getText().toString();
                String mobile = editTextRegisterMobile.getText().toString();
                String password = editTextRegisterPwd.getText().toString();
                String gender = ((RadioButton) findViewById(radioGroupRegisterGender.getCheckedRadioButtonId())).getText().toString();

                
                
                if (validateInputs(fullName, email, dob, mobile, password, gender)) {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(fullName, email, dob, mobile, password, gender);
                }
            }
        });
    }

    private boolean validateInputs(String fullName, String email, String dob, String mobile, String password, String gender) {
        // Check if any field is empty
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
            editTextRegisterFullName.setError("Full name is required");
            editTextRegisterFullName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
            editTextRegisterEmail.setError("Email is required");
            editTextRegisterEmail.requestFocus();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegisterActivity.this, "Please re- enter your email", Toast.LENGTH_LONG).show();
            editTextRegisterEmail.setError("valid email is required");
            editTextRegisterEmail.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(dob)) {
            Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
            editTextRegisterDoB.setError("Date of birth is required");
            editTextRegisterDoB.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(RegisterActivity.this, "Please enter your mobile number", Toast.LENGTH_LONG).show();
            editTextRegisterMobile.setError("Mobile number is required");
            editTextRegisterMobile.requestFocus();
            return false;
        } else if (mobile.length()!=10) {
            Toast.makeText(RegisterActivity.this, "Please re-enter your mobile number", Toast.LENGTH_LONG).show();
            editTextRegisterMobile.setError("Mobile number should be 10 digits");
            editTextRegisterMobile.requestFocus();
            return false;
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
            editTextRegisterPwd.setError("Password is required");
            editTextRegisterPwd.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(gender)) {
            // This should never happen if radio buttons are properly configured
            Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_LONG).show();
            editTextRegisterPwd.setError("Password is too short");
            editTextRegisterPwd.requestFocus();
            return false;
        } else {
            // Additional validation if needed
            return true;
        }
    }

    private void registerUser(String fullName, String email, String dob, String mobile, String password, String gender) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            // Store user details in Firebase Realtime Database
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                            User user = new User(fullName, email, dob, mobile, gender);
                            userRef.setValue(user);

                            // Proceed to send verification email and navigate to user profile
                            firebaseUser.sendEmailVerification();
                            Toast.makeText(RegisterActivity.this, "User registration Succesfull " , Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // User registration failed
                        Toast.makeText(RegisterActivity.this, "User registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

}
