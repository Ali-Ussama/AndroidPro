package com.example.aliosama.dramatranslation.Fetch;

import android.os.AsyncTask;
import android.text.Html;
import android.view.View;

import com.example.aliosama.dramatranslation.Fragments.FavortieMoviesFragment;
import com.example.aliosama.dramatranslation.Fragments.MoviesFragment;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.RetroClient;

import java.util.ArrayList;

import retrofit2.Call;

import static com.example.aliosama.dramatranslation.Fragments.FavortieMoviesFragment.FavoriteindicatorView;
import static com.example.aliosama.dramatranslation.Fragments.FavortieMoviesFragment.FavoriteindicatorViewLLCenter;
import static com.example.aliosama.dramatranslation.Fragments.FavortieMoviesFragment.FavortiemoviesRecAdapter;

/**
 * Created by aliosama on 8/25/2017.
 */

public class FetchFavoriteMovies extends AsyncTask<ArrayList<String>,Void,Void> {
    ArrayList<String> IDs;
    ArrayList<MovieModel.ImageModel> Images;
    ApiService api;
    Call<ArrayList<MovieModel.ImageModel>> mCall1 ;
    Call<MovieModel> call;
    MovieModel movieModel;
    @Override
    protected Void doInBackground(ArrayList<String>... params) {
        try {

            /**
             * Calling JSON
             */
            IDs = params[0];
            api = RetroClient.getApiService();
            for (int i = 0; i < IDs.size();i++) {
                String Url = "movies/" + IDs.get(i);
                System.out.println(Url);
                call = api.getFavoriteMovieJson(Url);
                movieModel = call.execute().body();
                try {
                    if (movieModel != null && movieModel.getStory() != null) {
                        String story = Html.fromHtml(movieModel.getStory().getRendered()).toString();
                        movieModel.getStory().setRendered(story);
                    }else{
                        movieModel.getStory().setRendered("لا يوجد");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                Images = new ArrayList<>();
                mCall1 = api.getMovieImages("media?parent=" +movieModel.getID());
                Images = mCall1.execute().body();

                movieModel.setImagesUrls(Images);
                FavortieMoviesFragment.FavortieMoviesData.add(movieModel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            FavortiemoviesRecAdapter.notifyDataSetChanged();
            FavoriteindicatorViewLLCenter.setVisibility(View.GONE);
            FavoriteindicatorView.smoothToHide();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
