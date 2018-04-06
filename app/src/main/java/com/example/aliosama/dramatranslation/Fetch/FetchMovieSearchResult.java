package com.example.aliosama.dramatranslation.Fetch;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.example.aliosama.dramatranslation.Activities.MoviesSearchResultActivity;
import com.example.aliosama.dramatranslation.Adapters.MoviesSearchResultRecAdapter;
import com.example.aliosama.dramatranslation.Fragments.MoviesFragment;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.RetroClient;

import java.util.ArrayList;

import retrofit2.Call;

import static com.example.aliosama.dramatranslation.Activities.MoviesSearchResultActivity.ResultNotFoundTV;
import static com.example.aliosama.dramatranslation.Activities.MoviesSearchResultActivity.moviesSearchResultRecAdapter;
import static com.example.aliosama.dramatranslation.Activities.MoviesSearchResultActivity.moviesSearchResultRecyclerView;
import static com.example.aliosama.dramatranslation.Activities.MoviesSearchResultActivity.moviesSearchResultRelativeLayout;
import static com.example.aliosama.dramatranslation.Activities.MoviesSearchResultActivity.moviesSearchResultindicatorView;
import static com.example.aliosama.dramatranslation.Activities.MoviesSearchResultActivity.moviesSearchResultindicatorViewLLCenter;

/**
 * Created by aliosama on 8/17/2017.
 */

public class FetchMovieSearchResult extends AsyncTask<String,Void,ArrayList<MovieModel>> {

    ArrayList<MovieModel.ImageModel> Images;
    ArrayList<MovieModel> data ;
    ApiService api;
    Call<ArrayList<MovieModel.ImageModel>> mCall1 ;
    Call<ArrayList<MovieModel>> call;
    private String Searchkey;

    @Override
    protected void onPreExecute() {
        ResultNotFoundTV.setVisibility(View.GONE);
        moviesSearchResultRelativeLayout.setVisibility(View.VISIBLE);
//        moviesSearchResultRecyclerView.setVisibility(View.GONE);
//        moviesSearchResultindicatorView.smoothToShow();
//        moviesSearchResultindicatorViewLLCenter.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<MovieModel> doInBackground(String... params) {

        try {

            data = new ArrayList<>();
            api = RetroClient.getApiService();

            Searchkey = params[0];
            String Key =Searchkey.replace(" ","-");
            call = api.getMovieJson("movies?slug=\""+Key+"\"");

            data = call.execute().body();
            Images = new ArrayList<>();
            if(data != null) {
                for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {

                    String story = Html.fromHtml(data.get(dataIndex).getStory().getRendered()).toString();
                    data.get(dataIndex).getStory().setRendered(story);

                    mCall1 = api.getMovieImages("media?parent=" + data.get(dataIndex).getID());
                    Images = mCall1.execute().body();
                    data.get(dataIndex).setImagesUrls(Images);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieModel> result) {
        super.onPostExecute(result);
        try {
            MoviesSearchResultRecAdapter.MoviesSearchResultAdapterData.clear();
            if(data != null && data.size() > 0){
                for (int i = 0; i < data.size(); i++) {
                    MoviesSearchResultRecAdapter.MoviesSearchResultAdapterData.add(data.get(i));
                }
                ResultNotFoundTV.setVisibility(View.GONE);
                moviesSearchResultRelativeLayout.setVisibility(View.VISIBLE);
            }else{
                moviesSearchResultRelativeLayout.setVisibility(View.GONE);
                String s = "لم يتم العثور على ";
                Searchkey = s+" \" "+Searchkey+" \"";
                ResultNotFoundTV.setText(Searchkey);
                System.out.println(ResultNotFoundTV.getText().toString());
                ResultNotFoundTV.setVisibility(View.VISIBLE);
            }

//            moviesSearchResultindicatorView.smoothToHide();
//            moviesSearchResultindicatorViewLLCenter.setVisibility(View.GONE);
//            moviesSearchResultRecyclerView.setVisibility(View.VISIBLE);
            moviesSearchResultRecAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
