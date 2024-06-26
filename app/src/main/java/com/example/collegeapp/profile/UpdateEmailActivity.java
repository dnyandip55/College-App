package com.example.collegeapp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.collegeapp.R;
import com.example.collegeapp.authentication.LoginActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UpdateEmailActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    ProgressBar progressBar;
    private TextView textViewAuthenticated;
    private String userOldEmail,userNewEmail,userPwd;
    private Button buttonUpdateEmail;
    private EditText editTextNewEmail ,editTextPwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Email");

        progressBar=findViewById(R.id.progressBar);
        editTextPwd=findViewById(R.id.editText_update_email_verify_password);
        editTextNewEmail=findViewById(R.id.editText_update_email_new);
        textViewAuthenticated=findViewById(R.id.textView_update_email_authenticated);
        buttonUpdateEmail=findViewById(R.id.button_update_email);

        buttonUpdateEmail.setEnabled(false);//make button is disabled in the beginning until the user is authenticated
        editTextNewEmail.setEnabled(false);

        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //Set old email id on textView
        assert firebaseUser != null;
        userOldEmail= firebaseUser.getEmail();
        TextView textViewOldEmail=findViewById(R.id.textView_update_email_old);
        textViewOldEmail.setText(userOldEmail);

        if(firebaseUser.equals("")){
            Toast.makeText(UpdateEmailActivity.this,"Something went wrong ! User's details not available",Toast.LENGTH_LONG).show();

        }else {
            reAuthenticate(firebaseUser);
        }
    }

    //Verify user before updating email
    private void reAuthenticate(FirebaseUser firebaseUser) {
        Button buttonVerifyUser=findViewById(R.id.button_authenticate_user);
        buttonVerifyUser.setOnClickListener(view -> {

            //Obtain password for authentication
            userPwd=editTextPwd.getText().toString();

            if(TextUtils.isEmpty(userPwd)){
                Toast.makeText(UpdateEmailActivity.this,"Password is needed to continue",Toast.LENGTH_LONG).show();
                editTextPwd.setError("Please enter your password for authentication");
                editTextPwd.requestFocus();
            }else {
                progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential= EmailAuthProvider.getCredential(userOldEmail,userPwd);

                firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(UpdateEmailActivity.this,"Password has been verified"+"You can update email now",Toast.LENGTH_LONG).show();

                        //set text view to show that user is authenticated
                        textViewAuthenticated.setText(R.string.authenticated);

                        editTextNewEmail.setEnabled(true);
                        editTextPwd.setEnabled(false);
                        buttonVerifyUser.setEnabled(false);
                        buttonUpdateEmail.setEnabled(true);

                        //
                        buttonUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this,
                                R.color.dark_green));
                        buttonUpdateEmail.setOnClickListener(view1 -> {
                            userNewEmail=editTextNewEmail.getText().toString();
                            if(TextUtils.isEmpty(userNewEmail)){
                                Toast.makeText(UpdateEmailActivity.this,"New Email is required",Toast.LENGTH_LONG).show();
                                editTextNewEmail.setError("Please enter new email");
                                editTextNewEmail.requestFocus();

                            } else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()) {
                                Toast.makeText(UpdateEmailActivity.this,"Please enter valid email",Toast.LENGTH_LONG).show();
                                editTextNewEmail.setError("Please provide valid email");
                                editTextNewEmail.requestFocus();


                            } else if (userOldEmail.matches(userNewEmail)) {
                                Toast.makeText(UpdateEmailActivity.this,"New Email cannot be same as old Email",Toast.LENGTH_LONG).show();
                                editTextNewEmail.setError("Please enter new email");
                                editTextNewEmail.requestFocus();


                            }else{
                                progressBar.setVisibility(View.VISIBLE);
                                updateEmail(firebaseUser);
                            }
                        });
                    }else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch (Exception e){
                            Toast.makeText(UpdateEmailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(task -> {
            if(task.isComplete()){
                //verify email
                firebaseUser.sendEmailVerification();
                Toast.makeText(UpdateEmailActivity.this,"Email has been updated .Please verify your new Email",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UpdateEmailActivity.this,UserProfileActivity.class);
                startActivity(intent);
                finish();
            }else{
                try{
                    throw Objects.requireNonNull(task.getException());
                }catch (Exception e){
                    Toast.makeText(UpdateEmailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id==R.id.menu_update_profile) {
            Intent intent=new Intent(UpdateEmailActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_update_email) {
            Intent intent=new Intent(UpdateEmailActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_change_password) {
            Intent intent=new Intent(UpdateEmailActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_delete_profile) {
            Intent intent=new Intent(UpdateEmailActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_log_out) {
            authProfile.signOut();
            Toast.makeText(UpdateEmailActivity.this,"Logged Out",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(UpdateEmailActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(UpdateEmailActivity.this,"Something went wrong !",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}