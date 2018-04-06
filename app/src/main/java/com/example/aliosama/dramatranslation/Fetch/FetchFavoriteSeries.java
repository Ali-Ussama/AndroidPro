package com.example.aliosama.dramatranslation.Fetch;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;

import com.example.aliosama.dramatranslation.Fragments.FavoriteSeriesFragment;
import com.example.aliosama.dramatranslation.Fragments.SeriesFragment;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.RetroClient;
import com.example.aliosama.dramatranslation.model.SerieModel;

import java.util.ArrayList;

import retrofit2.Call;

import static com.example.aliosama.dramatranslation.Fragments.FavoriteSeriesFragment.FavoriteindicatorView;
import static com.example.aliosama.dramatranslation.Fragments.FavoriteSeriesFragment.FavoriteindicatorViewLLCenter;

/**
 * Created by aliosama on 8/28/2017.
 */

public class FetchFavoriteSeries extends AsyncTask<ArrayList<String>,Void,Void>{

    ArrayList<SerieModel.ImageModel> Images;
    SerieModel data ;
    ApiService api;
    Call<ArrayList<SerieModel.ImageModel>> mCall1 ;
    Call<SerieModel> call;
    ArrayList<String> IDs;

    @Override
    protected Void doInBackground(ArrayList<String>... params) {
        try {

            /**
             * Calling JSON
             */

            IDs = params[0];

            api = RetroClient.getApiService();
            for (int i = 0 ; i < IDs.size() ;i++) {
                String Url = "tvshows/" + IDs.get(i);

                call = api.getFavoriteSerieJson(Url);
                data = call.execute().body();

                Images = new ArrayList<>();
                if (data != null ) {
                        String story = Html.fromHtml(data.getStory().getRendered()).toString();
                        data.getStory().setRendered(story);

                        mCall1 = api.getSerieImages("media?parent=" + data.getID());
                        Images = mCall1.execute().body();
                        data.setImagesUrls(Images);
                        FavoriteSeriesFragment.FavoriteSeriesData.add(data);
                }
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
            FavoriteindicatorViewLLCenter.setVisibility(View.GONE);
            FavoriteindicatorView.smoothToHide();
            FavoriteSeriesFragment.FavoriteSeriesRecAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
