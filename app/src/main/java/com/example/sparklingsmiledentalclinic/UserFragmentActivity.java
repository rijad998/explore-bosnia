package com.example.sparklingsmiledentalclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserFragmentActivity extends AppCompatActivity {

    Button logOutBtn;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    private AboutFragment aboutFragment;
    private AppointmentFragment appointmentFragment;
    private GalleryFragment galleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fragment);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);

        aboutFragment = new AboutFragment();
        appointmentFragment = new AppointmentFragment();
        galleryFragment = new GalleryFragment();

        logOutBtn = findViewById(R.id.userLogoutBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserFragmentActivity.this, LoginActivity.class));
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.navigation_about :
                        InitializeFragment(aboutFragment);
                        return true;
                    case R.id.navigation_appointment :
                        InitializeFragment(appointmentFragment);
                        return true;
                    case R.id.navigation_gallery :
                        InitializeFragment(galleryFragment);
                        return true;
                }

                return false;
            }

        });
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_about);
        }
    }

    private void InitializeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
