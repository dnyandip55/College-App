package com.example.collegeapp.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.collegeapp.R;
import com.example.collegeapp.authentication.LoginActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DeleteProfileActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
   private EditText editTextUserPwd;
    private TextView textViewAuthenticated;
   private ProgressBar progressBar;
   private String userPwd;
   private Button buttonReAuthenticate,buttonDeleteUser;
   private static final  String TAG="DeleteProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Delete Your Profile");
        progressBar=findViewById(R.id.progressBar);
        editTextUserPwd=findViewById(R.id.editText_delete_user_pwd);
        textViewAuthenticated=findViewById(R.id.textView_delete_user_authenticated);
        buttonDeleteUser=findViewById(R.id.button_delete_user);
        buttonReAuthenticate=findViewById(R.id.button_delete_user_authenticate);

        buttonDeleteUser.setEnabled(false);

        authProfile=FirebaseAuth.getInstance();
        firebaseUser=authProfile.getCurrentUser();

        assert firebaseUser != null;
        if(firebaseUser.equals("")){
            Toast.makeText(DeleteProfileActivity.this,"Something went wrong!"+ "User Details are not available at the moment",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(DeleteProfileActivity.this,UserProfileActivity.class);
            startActivity(i);
            finish();
        }else{
            reAuthenticateUser(firebaseUser);
        }
    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(view -> {
            userPwd=editTextUserPwd.getText().toString();

            if(TextUtils.isEmpty(userPwd)){
                Toast.makeText(DeleteProfileActivity.this,"Password is needed",Toast.LENGTH_SHORT).show();
                editTextUserPwd.setError("Please enter your current password to authenticate");
               editTextUserPwd.requestFocus();


            }else{
                progressBar.setVisibility(View.VISIBLE);
                //Authenticate User now
                AuthCredential credential= EmailAuthProvider.getCredential(Objects.requireNonNull(firebaseUser.getEmail()),userPwd);
                firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);

                        //Disable editText for current password
                        editTextUserPwd.setEnabled(false);


                        //Enable Delete user Button .
                        buttonReAuthenticate.setEnabled(false);
                        buttonDeleteUser.setEnabled(true);

                        //set TextView to show user is authenticated
                        textViewAuthenticated.setText(getString(R.string.verified));
                        Toast.makeText(DeleteProfileActivity.this,"Password has been verified"+"Delete profile now",
                                Toast.LENGTH_SHORT).show();

                        buttonDeleteUser.setBackgroundTintList(ContextCompat.getColorStateList(
                                DeleteProfileActivity.this,R.color.dark_green));

                        buttonDeleteUser.setOnClickListener(view1 -> showAlertDialog());
                    }else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch (Exception e){
                            Toast.makeText(DeleteProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE );
                });
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(DeleteProfileActivity.this);
        builder.setTitle("Delete User and Related Data?");
        builder.setMessage("Do you really want to delete your profile and related data?This action is irreversible");

        builder.setPositiveButton("CONTINUE", (dialogInterface, i) -> deleteUserData(firebaseUser));
        //RETURN TO USER PROFILE ACTIVITY IF USER PRECESS cancel BUTTON
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            Intent intent=new Intent(DeleteProfileActivity.this,UserProfileActivity.class);
            startActivity(intent);
            finish();
        });
        AlertDialog alertDialog=builder.create();

        //CHANGE THE BUTTON COLOR OF CONTINUE
        alertDialog.setOnShowListener(dialogInterface -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red)));

        //SHOW THIS ALERT DIALOG
        alertDialog.show();
    }

    private void deleteUser() {
        firebaseUser.delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                deleteUserData(firebaseUser);
                authProfile.signOut();
                Toast.makeText(DeleteProfileActivity.this,"User has been deleted",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DeleteProfileActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                try{
                    throw Objects.requireNonNull(task.getException());
                }catch (Exception e){
                    Toast.makeText(DeleteProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });

    }

    //DELETE ALL THE DATA OF THE USER
    private void deleteUserData(FirebaseUser firebaseUser) {

        //DELETE DISPLAY PIC ALSO CHECK IF THE USER HAS UPLOADED ANY PIC BEFORE DELETING
        if(firebaseUser.getPhotoUrl() != null){
            FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
            StorageReference storageReference=firebaseStorage.getReferenceFromUrl(firebaseUser.getPhotoUrl().toString());
            storageReference.delete().addOnSuccessListener(unused -> Log.d(TAG,"OnSuccess:Photo Deleted")).addOnFailureListener(e -> {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                Toast.makeText(DeleteProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            });
        }


        //Delete Data from realtime Database
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseUser.getUid()).removeValue().addOnSuccessListener(unused -> {
            Log.d(TAG,"OnSuccess:User Data Deleted");
            //FINALLY DELETE THE USER
            deleteUser();
        }).addOnFailureListener(e -> {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            Toast.makeText(DeleteProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

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
            Intent intent=new Intent(DeleteProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_update_email) {
            Intent intent=new Intent(DeleteProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_change_password) {
            Intent intent=new Intent(DeleteProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_delete_profile) {
            Intent intent=new Intent(DeleteProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id==R.id.menu_log_out) {
            authProfile.signOut();
            Toast.makeText(DeleteProfileActivity.this,"Logged Out",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(DeleteProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(DeleteProfileActivity.this,"Something went wrong !",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}