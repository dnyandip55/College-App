package com.example.collegeapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.collegeapp.ui.about.AboutFragment;
import com.example.collegeapp.ui.faculty.FacultyFragment;
import com.example.collegeapp.ui.gallery.GalleryFragment;
import com.example.collegeapp.ui.home.HomeFragment;
import com.example.collegeapp.ui.notice.NoticeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true; // Return true if the toggle handles the item selection
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.frame_layout);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(this);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_video) {
            // Handle video lectures navigation
            Toast.makeText(this,"Video",Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.navigation_ebook) {
            Toast.makeText(this,"ebook",Toast.LENGTH_SHORT).show();
            // Handle ebooks navigation
        } else if (itemId == R.id.navigation_website) {
            Toast.makeText(this,"website",Toast.LENGTH_SHORT).show();
            // Handle website navigation
        } else if (itemId == R.id.navigation_share) {
            Toast.makeText(this,"share",Toast.LENGTH_SHORT).show();
            // Handle share action
        } else if (itemId == R.id.navigation_rate) {
            Toast.makeText(this,"rate us",Toast.LENGTH_SHORT).show();
            // Handle rate action
        } else if (itemId == R.id.navigation_developer) {
            Toast.makeText(this,"developer",Toast.LENGTH_SHORT).show();
            // Handle developer info
        }

        return true;
    }
}
