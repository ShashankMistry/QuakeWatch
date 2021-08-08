package com.shashank.quakewatch.ActivitiesAndFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.shashank.quakewatch.DialogsAndAnimation.FilterDialog;
import com.shashank.quakewatch.DialogsAndAnimation.settingsDialog;
import com.shashank.quakewatch.R;
import com.shashank.quakewatch.customAdapterAndLoader.pageAdapter;


public class MainActivity extends AppCompatActivity implements settingsDialog.onClickListenerFragment, FilterDialog.onClickListenerFragment,
        settingsDialog.onSetTheme{
    ViewPager viewPager;
    com.shashank.quakewatch.customAdapterAndLoader.pageAdapter pageAdapter;
    EarthquakeData earthquakeData;
    AllEarthquakes allEarthquakes;
    int backPressed = 0;
    String mTheme;
    newsData NewsData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        ActionBar actionBar = getSupportActionBar();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (actionBar!=null) {
                    switch (position) {
                        case 1:
                            actionBar.setTitle("MAP");
//                            getSupportFragmentManager().beginTransaction().add(R.id.viewPager,allEarthquakes).commit();
                            break;
                        case 2:
                            actionBar.setTitle("NEWS");
                            break;
                        default:
                            actionBar.setTitle("QuakeWatch");
                            break;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTheme = LoadTheme();
        if (Build.VERSION.SDK_INT >= 28) {
            if (Build.VERSION.SDK_INT == 28) { // it causes app hanging, user can not interact with app
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (mTheme.equals("Dark")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (mTheme.equals("Light")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (mTheme.equals("System Default")){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        }
        earthquakeData = new EarthquakeData();
        NewsData = new newsData();
        allEarthquakes = new AllEarthquakes();
        pageAdapter = new pageAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(pageAdapter);
//        getSupportFragmentManager().beginTransaction().add(R.id.viewPager,earthquakeData).add(R.id.viewPager,NewsData).commit();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (backPressed == 0){
            backPressed++;
        }else{
            getSupportFragmentManager().beginTransaction().detach(earthquakeData).detach(NewsData).attach(new EarthquakeData()).attach(new newsData()).commit();
        }
    }

    @Override
    public void onBackPressed() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure you want to exit??");
            builder.setCancelable(true);
            builder.setNegativeButton("Yes", (dialog, which) -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
            builder.setPositiveButton("cancel", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }
    @Override
    public void detachFragment() {
      //  Toast.makeText(getApplicationContext(),"attach called",Toast.LENGTH_LONG).show();
        getSupportFragmentManager().beginTransaction().detach(earthquakeData).detach(NewsData).detach(new AllEarthquakes()).attach(new EarthquakeData()).attach(new newsData()).commit();
    }

    @Override
    public void sendThemeToActivity(String theme, boolean themeReset) {
        mTheme = theme;

        SharedPreferences sharedPreferences = getSharedPreferences("MainActivity",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("theme",theme);
        editor.apply();

        if (theme.equals("Dark")) {
            if (themeReset) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                Intent intent = new Intent(MainActivity.this,splashScreen.class);
                finish();
                startActivity(intent);
            }
        } else if (theme.equals("Light")) {
            if (themeReset) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Intent intent = new Intent(MainActivity.this,splashScreen.class);
                finish();
                startActivity(intent);
            }
        } else {
            if (themeReset) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                Intent intent = new Intent(MainActivity.this,splashScreen.class);
                finish();
                startActivity(intent);
            }
        }
    }
    public String LoadTheme(){
        SharedPreferences sharedPreferences = getSharedPreferences("MainActivity",MODE_PRIVATE);
        return sharedPreferences.getString("theme","");
    }


}