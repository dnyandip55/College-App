package com.example.collegeapp.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.collegeapp.R;
import com.example.collegeapp.authentication.LoginActivity;
import com.example.collegeapp.authentication.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText editTextUpdateName,editTextUpdateDob,editTextUpdateMobile;
    private RadioGroup radioGroupUpdateGender;
    private String textFullName ,textDob,textGender,textMobile;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private RadioButton radioButtonUpdateGenderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setTitle("Update Profile ");

        progressBar=findViewById(R.id.progressBar);
        editTextUpdateName=findViewById(R.id.editText_update_profile_name);
        editTextUpdateDob=findViewById(R.id.editText_update_profile_dob);
        editTextUpdateMobile=findViewById(R.id.editText_update_profile_mobile);
        radioGroupUpdateGender=findViewById(R.id.radio_group_update_profile_gender);

        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();

        //show profile
        showProfile(firebaseUser);

        //Upload Profile Pic
        Button buttonUploadProfilePic=findViewById(R.id.button_profile_upload_pic);
        buttonUploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Update Email
     //   Button buttonUpdateEmail=findViewById(R.id.button_profile_update_email);
//        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(UpdateProfileActivity.this,
//                        UpdateEmailActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });


        // Setting up DatePicker on EditText for date of birth
        editTextUpdateDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Extracting  saved dd,m,yyyy into diffrent variables  by creating array delimited by "/"
                String textSADoB[]=textDob.split("/");


                int day =Integer.parseInt(textSADoB[0]);
                int month = Integer.parseInt(textSADoB[1])-1;
                int year =Integer.parseInt(textSADoB[2]);

                DatePickerDialog picker;

                picker = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextUpdateDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //Update Profile
        Button buttonUpdateProfile=findViewById(R.id.button_update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);
            }
        });
    }

    //Update profile
    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderId=radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGenderSelected=findViewById(selectedGenderId);

        //validate mobile number using matcher and pattern
        String mobileRegex="[6-9][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern=Pattern.compile(mobileRegex);
        mobileMatcher=mobilePattern.matcher(textMobile);
        if(TextUtils.isEmpty(textFullName)){
            Toast.makeText(UpdateProfileActivity.this,"Please enter your full name",Toast.LENGTH_SHORT).show();
            editTextUpdateName.setError("Full name is required");
            editTextUpdateName.requestFocus();
        } else if (TextUtils.isEmpty(textDob)) {
            Toast.makeText(UpdateProfileActivity.this,"Please enter your date of birth",Toast.LENGTH_SHORT).show();
            editTextUpdateDob.setError("Date of birth is required");
            editTextUpdateDob.requestFocus();
        }else if (TextUtils.isEmpty(radioButtonUpdateGenderSelected.getText())) {
            Toast.makeText(UpdateProfileActivity.this,"Please select your gender",Toast.LENGTH_SHORT).show();
            radioButtonUpdateGenderSelected.setError("Gender is required");
            radioButtonUpdateGenderSelected.requestFocus();
        }else if (TextUtils.isEmpty(textMobile)) {
            Toast.makeText(UpdateProfileActivity.this,"Please enter your mobile number",Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile Number is required");
            editTextUpdateMobile.requestFocus();

        } else if (textMobile.length() !=10) {
            Toast.makeText(UpdateProfileActivity.this,"Please enter your mobile number",Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile Number is required");
            editTextUpdateMobile.requestFocus();
        } else if (!mobileMatcher.find()) {
            Toast.makeText(UpdateProfileActivity.this,"Please re-enter your mobile number",Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile Number is not valid");
            editTextUpdateMobile.requestFocus();
        }else{
            //obtain the data enter by user
            textGender=radioButtonUpdateGenderSelected.getText().toString();
            textFullName=editTextUpdateName.getText().toString();
            textDob=editTextUpdateDob.getText().toString();
            textMobile=editTextUpdateMobile.getText().toString();

            //Enter user data into firebase realtime database.set up dependency

            ReadWriteUserDetails writeUserDetails=new ReadWriteUserDetails(textDob,textGender,textMobile);

            //extract user reference from database  for "Registered user"
            DatabaseReference referenceProfile=FirebaseDatabase.getInstance().getReference("Registered Users");

            String userID=firebaseUser.getUid();


            progressBar.setVisibility(View.VISIBLE);

            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //setting new display name

                        UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(UpdateProfileActivity.this,"Update Successful !",Toast.LENGTH_SHORT).show();

                        //Stop user for returning  to UpdateProfileActivity on pressing back button and close activity
                        Intent intent=new Intent(UpdateProfileActivity.this,UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        try{
                            throw task.getException();

                        }catch (Exception e){
                            Toast.makeText(UpdateProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
            
        }

        //fetch data from firebase and display
    private void showProfile(FirebaseUser firebaseUser) {
        String userIdOfRegistered=firebaseUser.getUid();

        //Extracting User Refernce from Database for "Registered Users"
        DatabaseReference  referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userIdOfRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    textFullName=firebaseUser.getDisplayName();
                    textDob=readUserDetails.dob;
                    textGender=readUserDetails.gender;
                    textMobile=readUserDetails.mobile;

                    editTextUpdateName.setText(textFullName);
                    editTextUpdateDob.setText(textDob);
                    editTextUpdateMobile.setText(textMobile);

                    if(textGender.equals("Male")){
                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_male);

                    }else{
                        radioButtonUpdateGenderSelected=findViewById(R.id.radio_female);
                    }
                    radioButtonUpdateGenderSelected.setChecked(true);
                }else{
                    Toast.makeText(UpdateProfileActivity.this,"Something went wrong" ,Toast.LENGTH_SHORT).show();

                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this,"Something went wrong" ,Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
            }
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
        } else if (id==R.id.menu_log_out) {
            authProfile.signOut();
            Toast.makeText(UpdateProfileActivity.this,"Logged Out",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(UpdateProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id==R.id.menu_update_profile) {
            Intent intent=new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
            
        } else{
            Toast.makeText(UpdateProfileActivity.this,"Something went wrong !",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}