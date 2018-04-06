package com.example.aliosama.dramatranslation.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aliosama.dramatranslation.Adapters.EpisodeRecAdapter;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.EpisodeModel;
import com.example.aliosama.dramatranslation.model.RetroClient;
import com.example.aliosama.dramatranslation.model.SerieModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SerieActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    SerieModel serieModel;
    TextView story,OrignalName,NumOfSeasons,NumOfEpisodes,AirDate;
    ImageView image;
    public static RecyclerView EpisodesRecyclerView;
    public static ArrayList<EpisodeModel> EpisodesData;
    public static EpisodeRecAdapter mEpisodeRecAdapter;
    SharedPreferences sp;
    String FavIconTag = "0";
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    public static LinearLayout EpisodeindicatorViewLLBottom;
    public static AVLoadingIndicatorView EpisodeindicatorViewBtm;
    static ArrayList<EpisodeModel> data ;
    static ApiService api;
    static Call<ArrayList<EpisodeModel>> call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);

        try {
            toolbar = (Toolbar) findViewById(R.id.activity_serie_toolbar);
            setSupportActionBar(toolbar);
            Intent intent = getIntent();
            serieModel = (SerieModel) intent.getSerializableExtra("Serie data");
            actionBar = getSupportActionBar();
            actionBar.setTitle(serieModel.getTitle().getRendered());
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

//            Ads Implementation
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.series_interstitial_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Call displayInterstitial() function
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        Log.d("TAG", "The interstitial loaded now.");
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }
            });

            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            image = (ImageView) findViewById(R.id.activity_serie_Image);
            story = (TextView) findViewById(R.id.activity_serie_story);
            OrignalName = (TextView) findViewById(R.id.activity_serie_Original_Name);
            NumOfSeasons = (TextView) findViewById(R.id.activity_serie_Number_of_Seasons);
            NumOfEpisodes = (TextView) findViewById(R.id.activity_serie_Number_of_Episodes);
            AirDate = (TextView) findViewById(R.id.activity_serie_Air_Date);
            EpisodeindicatorViewLLBottom = (LinearLayout) findViewById(R.id.activity_serie_Episode_IndicatorView_LLbottom);
            EpisodeindicatorViewBtm = (AVLoadingIndicatorView) findViewById(R.id.activity_serie_Episode_IndicatorBottom);
            EpisodesRecyclerView = (RecyclerView) findViewById(R.id.activity_serie_RecyclerView);

            try{
                boolean validImage = false;
                for(int i = 0 ; i < serieModel.getImagesUrls().size(); i++){
                    if (serieModel.getImagesUrls().get(i).getMediaDetails().getSizes() != null) {
                        Picasso.with(getBaseContext()).load(serieModel.getImagesUrls().get(i).getImageUrl()).into(image);
                        validImage = true;
                        break;
                    }
                }
                if(!validImage)
                    image.setImageResource(R.drawable.blackimag);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(!serieModel.getStory().getRendered().isEmpty()) {
                story.setText(serieModel.getStory().getRendered());
            }else {
                story.setText("لا يوجد");
            }
            if(!serieModel.getOrignalName().isEmpty()){
                OrignalName.setText(serieModel.getOrignalName());
            }else{
                OrignalName.setText("لا يوجد");
            }
            if(!serieModel.getNumberOfSeasons().isEmpty()){
                NumOfSeasons.setText(serieModel.getNumberOfSeasons());
            }else{
                NumOfSeasons.setText("لا يوجد");
            }
            if(!serieModel.getNumberOfEpisodes().isEmpty()){
                NumOfEpisodes.setText(serieModel.getNumberOfEpisodes());
            }else{
                NumOfEpisodes.setText("لا يوجد");
            }
            if(!serieModel.getDate().isEmpty()){
                AirDate.setText(serieModel.getDate());
            }else{
                AirDate.setText("لا يوجد");
            }

            sp = getSharedPreferences("FavoriteSeries", Context.MODE_PRIVATE);

            EpisodesData = new ArrayList<>();
            EpisodesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mEpisodeRecAdapter = new EpisodeRecAdapter(getBaseContext(),EpisodesData);
            EpisodesRecyclerView.setNestedScrollingEnabled(false);
            EpisodesRecyclerView.setAdapter(mEpisodeRecAdapter);
            ShowLoading();
            FetchEpisodes(serieModel.getSlug(),1);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void ShowLoading(){
        try {
            EpisodeindicatorViewLLBottom.setVisibility(View.VISIBLE);
            EpisodeindicatorViewBtm.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void HideLoading(){
        try {
            EpisodeindicatorViewLLBottom.setVisibility(View.GONE);
            EpisodeindicatorViewBtm.hide();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void FetchEpisodes(final String serieName,final int EpisodeNumber) {
        try {
            data = new ArrayList<>();
            api = RetroClient.getApiService();

//          Read Episode
            String Url = "episodes?slug=\""+serieName + "-1x"+String.valueOf(EpisodeNumber)+"\"";
            Log.i("EpisodeSlug",Url);
            call = api.getEpisodes(Url);
            call.enqueue(new Callback<ArrayList<EpisodeModel>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<EpisodeModel>> call, @NonNull retrofit2.Response<ArrayList<EpisodeModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null && !response.body().isEmpty()){
                                EpisodeModel mEpisodeModel = response.body().get(0);
                                mEpisodeRecAdapter.UpdateEpisodes(mEpisodeModel,serieName,EpisodeNumber);
                            }else{
                                mEpisodeRecAdapter.UpdateEpisodes(null,serieName,EpisodeNumber);
                            }
                        } else if (response.code() == 500){
                            FetchEpisodes(serieName,EpisodeNumber);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ArrayList<EpisodeModel>> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    mEpisodeRecAdapter.UpdateEpisodes(null,serieName,EpisodeNumber);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_serie_menu, menu);

        MenuItem FavIcon = menu.findItem(R.id.serie_menu_favorite);

        if(sp.getInt(String.valueOf(serieModel.getID()),0) != 0) {
            FavIcon.setIcon(R.drawable.ic_star_white_24dp);
            FavIconTag = "1";
        }else{
            FavIcon.setIcon(R.drawable.ic_star_border_white_24dp);
            FavIconTag = "0";
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                if(!mFetchEpisodes.isCancelled()){
//                    mFetchEpisodes.cancel(true);
//                }
                finish();
                return true;
            case R.id.serie_menu_favorite:
                try {
                    if(FavIconTag.equals("1")){
                        sp.edit().remove(String.valueOf(serieModel.getID())).apply();
                        item.setIcon(R.drawable.ic_star_border_white_24dp);
                        FavIconTag =  "0";
                    }else{
                        sp.edit().putInt(String.valueOf(serieModel.getID()),serieModel.getID()).apply();
                        item.setIcon(R.drawable.ic_star_white_24dp);
                        FavIconTag = "1";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
//        try {
//            if(!mFetchEpisodes.isCancelled()){
//                Log.d("TAG","Episode Fetch Not Canceled");
//                mFetchEpisodes.cancel(true);
//            }else{
//                Log.d("TAG","Episode Fetch Canceled");
//                mFetchEpisodes.cancel(true);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        super.onDestroy();
    }
}
