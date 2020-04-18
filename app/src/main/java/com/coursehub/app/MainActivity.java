package com.coursehub.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    String link = "https://play.google.com/store/apps/details?id=com.coursehub.app";
    private AppBarConfiguration mAppBarConfiguration;
    private AdView adView;
    private boolean backstatus = false;
    private String extraOnShare = "Download Course Hub App and get Daily new 100% free courses\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adView = findViewById(R.id.adView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_latest, R.id.nav_development, R.id.nav_cyber,
                R.id.nav_design,
                R.id.nav_software,
                R.id.nav_photography,
                R.id.nav_business,
                R.id.nav_health,
                R.id.nav_life,
                R.id.nav_persona,
                R.id.nav_teaching)
                .setDrawerLayout(drawer)
                .build();
//        drawer.setViewScale(GravityCompat.START, 0.9f);
//        drawer.setViewElevation(GravityCompat.START, 20);
//        drawer.useCustomBehavior(GravityCompat.END);
//        drawer.setRadius(GravityCompat.START, 45);


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareButton: {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, extraOnShare + link);
                startActivity(shareIntent);
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if (backstatus) {
            super.onBackPressed();
            finish();
        }
        Toast.makeText(this, "Pressed Again For Exit", Toast.LENGTH_SHORT).show();
        this.backstatus = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backstatus = false;
            }
        }, 2000);
    }
}
