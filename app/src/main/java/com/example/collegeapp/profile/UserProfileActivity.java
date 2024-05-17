package com.example.collegeapp.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeapp.R;
import com.example.collegeapp.authentication.LoginActivity;
import com.example.collegeapp.authentication.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private ImageView imageView;

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Profile");

        // Initialize FirebaseAuth
        authProfile = FirebaseAuth.getInstance();

        // Find TextViews
        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);

        // Find ProgressBar
        progressBar = findViewById(R.id.progress_bar);

        // Set onclick listener on imageView to open UploadProfilePicActivity
        imageView=findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserProfileActivity.this,UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

        // Check if user is authenticated
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null) {
            // User not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        } else {
            // User authenticated, show profile
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        // Populate TextViews with user details
                        textViewWelcome.setText("Welcome ");
                        textViewFullName.setText(user.getFullName());
                        textViewEmail.setText(user.getEmail());
                        textViewDoB.setText(user.getDob());
                        textViewGender.setText(user.getGender());
                        textViewMobile.setText(user.getMobile());

                        // Set user dp
                        Uri uri=firebaseUser.getPhotoUrl();

                        // Load the user's profile picture into ImageView using Glide
                        Glide.with(UserProfileActivity.this)
                                .load(uri)
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.profile1) // Placeholder for when image is loading
                                .error(R.drawable.profile1) // Placeholder for if image fails to load
                                .into(imageView);

                    } else {
                        // User details not found
                        Toast.makeText(UserProfileActivity.this, "User details not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Data snapshot does not exist
                    Toast.makeText(UserProfileActivity.this, "Data snapshot does not exist", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Database error occurred
                Toast.makeText(UserProfileActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Creating Action bar Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menu_refresh){
            // Refresh Activity
            finish();
            startActivity(getIntent());
            overridePendingTransition(0,0);
        } else if (id==R.id.menu_update_profile) {
            Intent intent=new Intent(UserProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);

        } else if (id==R.id.menu_update_email) {
            Intent intent=new Intent(UserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);

        } else if (id==R.id.menu_change_password) {
            Intent intent=new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);

        } else if (id==R.id.menu_delete_profile) {
            Intent intent=new Intent(UserProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);

        }else if (id==R.id.menu_log_out) {
            authProfile.signOut();
            Toast.makeText(UserProfileActivity.this,"Logged Out",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(UserProfileActivity.this, LoginActivity.class);

            // Clear stack to prevent user coming back to UserProfileActivity on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(UserProfileActivity.this,"Something went wrong !",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
