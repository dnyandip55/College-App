package com.example.collegeapp.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeapp.R;
import com.example.collegeapp.authentication.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private EditText editTextPwdCurr,editTextPwdNew,getEditTextPwdConfirmNew;
    private TextView textViewAuthenticated;
    private Button buttonChangePwd,buttonReAuthenticate;
    private ProgressBar progressBar;
    private String userPwdCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle("Change Password");

        editTextPwdNew=findViewById(R.id.editText_change_pwd_new);
        editTextPwdCurr=findViewById(R.id.editText_change_pwd_current);
       textViewAuthenticated=findViewById(R.id.textView_change_pwd_authenticated);
       progressBar=findViewById(R.id.progressBar);
       buttonReAuthenticate=findViewById(R.id.button_change_pwd_authenticate);
       buttonChangePwd=findViewById(R.id.button_change_pwd);

       //Disable editText for new Password ,and make change Pwd Button unclickable till user is authenticated
        editTextPwdNew.setEnabled(false);
        buttonChangePwd.setEnabled(false);

        authProfile= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();

        if(firebaseUser.equals("")){
            Toast.makeText(ChangePasswordActivity.this,"Something went wrong! User's details not available",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
            startActivity(intent);
            finish();

        }else{
            reAuthenticateUser(firebaseUser);
        }

    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwdCurr=editTextPwdCurr.getText().toString();

                if(TextUtils.isEmpty(userPwdCurr)){
                    Toast.makeText(ChangePasswordActivity.this,"Password is needed",Toast.LENGTH_SHORT).show();
                    editTextPwdCurr.setError("Please enter your current password to authenticate");
                    editTextPwdCurr.requestFocus();


                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    //Authenticate User now
                    AuthCredential credential= EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwdCurr);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                
                                //Disable editText for current passwword .Enable EditText for new password 
                                editTextPwdCurr.setEnabled(false);
                                editTextPwdNew.setEnabled(true);
                                
                                //Enable change Pwd Button . Disable Authentication Button
                                buttonReAuthenticate.setEnabled(false);
                                buttonChangePwd.setEnabled(true);
                                
                                //set TextView to show user is authenticated 
                                textViewAuthenticated.setText("You are authenticated/verified"+"You can change password now");
                                Toast.makeText(ChangePasswordActivity.this,"Password has been verified"+"Change password now",
                                        Toast.LENGTH_SHORT).show();
                                
                                buttonChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(
                                        ChangePasswordActivity.this,R.color.dark_green));
                                
                                buttonChangePwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            }else{
                                try{
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(ChangePasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE );
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userPwdNew =editTextPwdNew.getText().toString();
        if(TextUtils.isEmpty(userPwdNew)){
            Toast.makeText(ChangePasswordActivity.this,"New password is needed",Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please enter your new password");
            editTextPwdNew.requestFocus();
        } else if (
                userPwdCurr.matches(userPwdNew)) {

            Toast.makeText(ChangePasswordActivity.this,"New password cannot be same as old password",Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please enter your new password");
            editTextPwdNew.requestFocus();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChangePasswordActivity.this,"Password has been changed",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        try{
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(ChangePasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

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
            Intent intent=new Intent(ChangePasswordActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_update_email) {
            Intent intent=new Intent(ChangePasswordActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_change_password) {
            Intent intent=new Intent(ChangePasswordActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_delete_profile) {
            Intent intent=new Intent(ChangePasswordActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_log_out) {
            authProfile.signOut();
            Toast.makeText(ChangePasswordActivity.this,"Logged Out",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(ChangePasswordActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(ChangePasswordActivity.this,"Something went wrong !",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}