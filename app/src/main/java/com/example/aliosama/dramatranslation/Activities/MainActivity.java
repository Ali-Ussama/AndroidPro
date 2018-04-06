package com.example.aliosama.dramatranslation.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.aliosama.dramatranslation.Fragments.FavoriteSeriesFragment;
import com.example.aliosama.dramatranslation.Fragments.FavortieMoviesFragment;
import com.example.aliosama.dramatranslation.Fragments.MoviesFragment;
import com.example.aliosama.dramatranslation.Fragments.SeriesFragment;
import com.example.aliosama.dramatranslation.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String MOVIES_TAG = "movies";
    private static final String SERIES_TAG = "series";
    private static final String FAV_MOVIES_TAG = "favorite_movies";
    private static final String FAV_SERIES_TAG = "favorite_series";

    static  String CURRENT_TAG = SERIES_TAG;
    public static int navItemIndex = 0;
    int BackEventCount = 0 ;
    String [] activityTitles;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    FragmentManager mFragmentManager;
//    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Locale.setDefault(new Locale("ar"));
            MobileAds.initialize(this, getResources().getString(R.string.app_id));

            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
            setSupportActionBar(toolbar);

            activityTitles = getResources().getStringArray(R.array.nav_drawer_fragments_title);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navItemIndex = 0;
            if (!isNetworkAvailable()) {
                // Create an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Set the Alert Dialog Message
                builder.setMessage(getString(R.string.NoInternetConnection))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.Retry),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // Restart the Activity
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                loadFragment();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        if (id == R.id.nav_series) {
            if(navItemIndex != 0) {
                CURRENT_TAG = SERIES_TAG;
                navItemIndex = 0;
                loadFragment();
            }
        } else if (id == R.id.nav_movies) {
            if(navItemIndex != 1) {
                CURRENT_TAG = MOVIES_TAG;
                navItemIndex = 1;
                loadFragment();
            }
        } else if (id == R.id.fav_interests_series) {
            if(navItemIndex != 2) {
                CURRENT_TAG = FAV_SERIES_TAG;
                navItemIndex = 2;
                loadFragment();
            }
        } else if (id == R.id.fav_interests_movies) {
            if(navItemIndex != 3) {
                CURRENT_TAG = FAV_MOVIES_TAG;
                navItemIndex = 3;
                loadFragment();
            }
        }else if (id == R.id.nav_setting) {
            if(navItemIndex != 4){
                navItemIndex = 4;
                loadActivity();
            }
        }else if(id == R.id.nav_contact_us){
            if(navItemIndex != 5){
                navItemIndex = 5;
                loadActivity();
            }
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void loadActivity(){
        try {
            Intent intent = getActivity();
            this.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadFragment() {
        try {
            if (!isNetworkAvailable()) {
                // Create an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Set the Alert Dialog Message
                builder.setMessage(getString(R.string.NoInternetConnection))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.Retry),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // Restart the Activity
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }else{
                BackEventCount = 0 ;
                selectNavMenu();
                setToolbarTitle();
                mFragmentManager = getSupportFragmentManager();
                getFragmentManager().popBackStack();
                mFragmentManager.beginTransaction().replace(R.id.content_main, getFragment()).commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setToolbarTitle() throws Exception {
        try {
            getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void selectNavMenu() throws Exception{
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    private Intent getActivity() {
        switch (navItemIndex){
            case 4:
                Intent intent = new Intent(this,SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                return intent;
            case 5:
                Intent intent1 = new Intent(this,ContactUsActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                return intent1;
            default:
                return null;
        }
    }

    private Fragment getFragment() throws Exception {
        switch (navItemIndex){
            case 0:
                return new SeriesFragment();
            case 1:
                return new MoviesFragment();
            case 2:
                return new FavoriteSeriesFragment();
            case 3:
                return new FavortieMoviesFragment();
            default:
                return new SeriesFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            navItemIndex = 0;
            selectNavMenu();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK: {
                    try {
                        BackEventCount++;
                        if (BackEventCount == 2) {
                            finish();
                        } else {
                            if (navItemIndex != 0) {
                                navItemIndex = 0;
                                CURRENT_TAG = SERIES_TAG;
                                loadFragment();
                            } else {
                                Toast.makeText(this, "اضغط مره اخرى للخروج", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // Private class isNetworkAvailable
    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
