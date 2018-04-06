package com.example.aliosama.dramatranslation.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aliosama.dramatranslation.Configurations.YoutubeConfig;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.RetroClient;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBar actionBar;
    MovieModel movieModel;
    TextView title,story,date;
    ImageView image;
    Button watchMovieBtn,DonwloadBtn;
    String FavIconTag = "0";
    SharedPreferences sp;
    String TrailerID = "";
    LinearLayout YoutubeLayout;
    private InterstitialAd mInterstitialAd;
    AdView mAdView;
    ProgressDialog progressDialog;
    ArrayList<MovieModel.MovieLink> DownloadLink;
    Call<ArrayList<MovieModel.MovieLink>> DownloadLinkCall;
    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        try {
            toolbar = (Toolbar) findViewById(R.id.Movies_activity_toolbar);
            setSupportActionBar(toolbar);
            Intent intent = getIntent();
            movieModel = (MovieModel) intent.getSerializableExtra("data");
            actionBar = getSupportActionBar();
            actionBar.setTitle(movieModel.getTitle().getRendered());

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            //            Ads Implementation
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.movies_interstitial_unit_id));
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
            mAdView = (AdView) findViewById(R.id.movie_adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

//            Youtube
            YoutubeLayout = (LinearLayout) findViewById(R.id.YoutubeLayout);
            if(movieModel.getYoutubeVideoID() != null && !movieModel.getYoutubeVideoID().isEmpty() && movieModel.getYoutubeVideoID().length() > 1) {
                try {
                    YoutubeLayout.setVisibility(View.VISIBLE);
                    YouTubePlayerSupportFragment mYouTubePlayerSupportFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
                    TrailerID = movieModel.getYoutubeVideoID().substring(1, movieModel.getYoutubeVideoID().length() - 1);
                    mYouTubePlayerSupportFragment.initialize(YoutubeConfig.API_KEY, new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            youTubePlayer.loadVideo(TrailerID);

                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                YoutubeLayout.setVisibility(View.GONE);
            }
            image = (ImageView) findViewById(R.id.Movies_activity_image);
            title = (TextView) findViewById(R.id.Movies_activity_title);
            story = (TextView) findViewById(R.id.Movies_activity_story);
            date = (TextView) findViewById(R.id.Movies_activity_Date);
            sp = getSharedPreferences("FavoriteMovies", Context.MODE_PRIVATE);



            Picasso.with(getBaseContext()).load(movieModel.getImagesUrls().get(movieModel.getImagesUrls().size()-1).getImageUrl()).into(image);
            title.setText(movieModel.getTitle().getRendered());
            if(movieModel.getStory().getRendered().length() > 1) {
                story.setText(movieModel.getStory().getRendered());
            }else {
                story.setText("لا يوجد");
            }
            date.setText(movieModel.getDate());
            watchMovieBtn = (Button) findViewById(R.id.Movies_activity_WatchBtn);
            watchMovieBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CharSequence watchTypes[] = new CharSequence[movieModel.getVideos().size()];
                        for (int i = 0 ; i < movieModel.getVideos().size() ; i++){
                            watchTypes[i] = movieModel.getVideos().get(i).getHost();
                        }
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MovieActivity.this);
                        builder.setTitle("اختر نوع المشاهدة");
                        builder.setItems(watchTypes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               try {
                                   Intent intent = new Intent(MovieActivity.this,WebActivity.class);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                   intent.putExtra("url",movieModel.getVideos().get(which).getUrl());
                                   startActivity(intent);
//                                 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieModel.getVideos().get(which).getUrl())));
                                   dialog.dismiss();
                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                            }
                        });
                        builder.show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            DonwloadBtn = (Button) findViewById(R.id.Movies_activity_DownloadBtn);
            DonwloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        progressDialog = new ProgressDialog(MovieActivity.this);
                        progressDialog.setMessage("جار التحميل..."); // Setting Message
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(true);

                        loadDownloadLinks(movieModel);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void loadDownloadLinks(final MovieModel movieModel){
        api = RetroClient.getApiService();
        DownloadLinkCall = api.getDownloadMovieLink("dt_links?slug=\""+movieModel.getDt_String()+"\"");
        DownloadLinkCall.enqueue(new Callback<ArrayList<MovieModel.MovieLink>>() {
            @Override
            public void onResponse(Call<ArrayList<MovieModel.MovieLink>> call, Response<ArrayList<MovieModel.MovieLink>> response) {
                if (response.isSuccessful()) {
                    DownloadLink = response.body();
                    progressDialog.dismiss();
                    movieModel.setMoviesDownloadLink(DownloadLink.get(0));
                    String Url = movieModel.getMoviesDownloadLink().getLinks_url();
                    Log.i("MovieDownloadLink","Movie Name : "+movieModel.getTitle().getRendered());
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Url)));
                }else{
                    loadDownloadLinks(movieModel);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MovieModel.MovieLink>> call, Throwable t) {
                movieModel.setMoviesDownloadLink(null);
                AlertDialog.Builder alert = new AlertDialog.Builder(MovieActivity.this);
                alert.setTitle("تنبيه");
                alert.setMessage("عذرا لا يوجد رابط تحميل");
                alert.setPositiveButton("OK",null);
                alert.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_movie_menu, menu);
        try {
            MenuItem FavIcon = menu.findItem(R.id.movie_menu_favorite);

            if(sp.getInt(String.valueOf(movieModel.getID()),0) != 0) {
                FavIcon.setIcon(R.drawable.ic_star_white_24dp);
                FavIconTag = "1";
            }else{
                FavIcon.setIcon(R.drawable.ic_star_border_white_24dp);
                FavIconTag = "0";
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.movie_menu_download_movie) {

            CharSequence downloadtypes[] = new CharSequence[] {"جودة عالية", "جودة متوسطة", "جودة عادية"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("اختر جودة التحميل");
            builder.setItems(downloadtypes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                }
            });
            final AlertDialog alert = builder.create();
            alert.show();

            return true;


        } else if(id == R.id.movie_menu_watch_movie) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        CharSequence watchTypes[] = new CharSequence[movieModel.getVideos().size()];
                        for (int i = 0 ; i < movieModel.getVideos().size() ; i++){
                            watchTypes[i] = movieModel.getVideos().get(i).getHost();
                        }
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MovieActivity.this);
                        builder.setTitle("اختر نوع المشاهدة");
                        builder.setItems(watchTypes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieModel.getVideos().get(which).getUrl())));
                                    dialog.dismiss();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        builder.show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            return true;


        } else if(id == R.id.movie_menu_favorite){
            try {
                if(FavIconTag.equals("1")){
                    sp.edit().remove(String.valueOf(movieModel.getID())).apply();
                    item.setIcon(R.drawable.ic_star_border_white_24dp);
                    FavIconTag =  "0";

                }else{
                    sp.edit().putInt(String.valueOf(movieModel.getID()),movieModel.getID()).apply();
                    item.setIcon(R.drawable.ic_star_white_24dp);
                    FavIconTag = "1";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }else if(id == android.R.id.home){
             finish();
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
        super.onDestroy();
    }
}
