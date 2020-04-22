package com.coursehub.app;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
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
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;



public class MainActivity extends AppCompatActivity {

    String link = "https://play.google.com/store/apps/details?id=com.coursehub.app";
    private AppBarConfiguration mAppBarConfiguration;
    private AdView adView;
    private boolean backstatus = false;
    static int MY_REQUESTCODE = 9;
    private AppUpdateManager appUpdateManager;
    private String extraOnShare = "Download Course Hub App and get Daily new 100% free courses\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("course");
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        // Checks that the platform will allow the specified type of update.
                        if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                            // Request the update.
                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        AppUpdateType.IMMEDIATE,
                                        MainActivity.this,
                                        MY_REQUESTCODE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


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
        AdvanceDrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        drawer.setViewScale(GravityCompat.START, 0.9f);
        drawer.setViewElevation(GravityCompat.START, 20);
        drawer.useCustomBehavior(GravityCompat.END);
        drawer.setRadius(GravityCompat.START, 45);


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

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (resultCode == MY_REQUESTCODE) {
            if (resultCode != RESULT_OK) {
                Log.d("Update", "onActivityReenter: Faild Update" + resultCode);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        if (appUpdateInfo.updateAvailability()
                                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                            // If an in-app update is already running, resume the update.
                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        IMMEDIATE,
                                        MainActivity.this,
                                        MY_REQUESTCODE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

}

