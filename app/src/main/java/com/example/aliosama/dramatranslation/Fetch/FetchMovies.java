package com.example.aliosama.dramatranslation.Fetch;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;

import com.example.aliosama.dramatranslation.Activities.MovieActivity;
import com.example.aliosama.dramatranslation.Adapters.MoviesRecAdapter;
import com.example.aliosama.dramatranslation.Fragments.MoviesFragment;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.RetroClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.unbescape.html.HtmlEscape;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

import static com.example.aliosama.dramatranslation.Fragments.MoviesFragment.indicatorView;
import static com.example.aliosama.dramatranslation.Fragments.MoviesFragment.indicatorViewBtm;
import static com.example.aliosama.dramatranslation.Fragments.MoviesFragment.indicatorViewLLBottom;
import static com.example.aliosama.dramatranslation.Fragments.MoviesFragment.indicatorViewLLCenter;

/**
 * Created by aliosama on 8/14/2017.
 */

public class FetchMovies extends AsyncTask<Object, ArrayList<MovieModel>, Void> {

    FragmentActivity context;
    ArrayList<MovieModel.MovieLink> DownloadLink;
    ArrayList<MovieModel.ImageModel> Images;
    ArrayList<MovieModel> data ;
    ApiService api;
    Call<ArrayList<MovieModel.ImageModel>> mCall1 ;
    Call<ArrayList<MovieModel>> call;
    Call<ArrayList<MovieModel.MovieLink>> DownloadLinkCall;

    int PageNumber;

    public FetchMovies(FragmentActivity context,int i){
        this.context = context;
        PageNumber = i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Object... params) {

        try {
            data = new ArrayList<>();
            api = RetroClient.getApiService();
            String Url = "movies?page=" + String.valueOf(PageNumber);

            call = api.getMovieJson(Url);
            try {
                data = call.execute().body();
            }catch (JsonSyntaxException x) {
                x.printStackTrace();
            }

            Images = new ArrayList<>();
            if (data != null && data.size() > 0) {
                for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {
                    String title =  HtmlEscape.unescapeHtml(data.get(dataIndex).getTitle().getRendered());
                    String story = Html.fromHtml(data.get(dataIndex).getStory().getRendered()).toString();

                    data.get(dataIndex).getTitle().setRendered(title);
                    data.get(dataIndex).getStory().setRendered(story);
                    mCall1 = api.getMovieImages("media?parent=" + data.get(dataIndex).getID());
                    Images = mCall1.execute().body();
                    data.get(dataIndex).setImagesUrls(Images);

                    try {
                        DownloadLinkCall = api.getDownloadMovieLink("dt_links?slug=\""+data.get(dataIndex).getDt_String()+"\"");
                        DownloadLink = DownloadLinkCall.execute().body();
                        data.get(dataIndex).setMoviesDownloadLink(DownloadLink.get(0));
                    }catch (Exception e){
                        data.get(dataIndex).setMoviesDownloadLink(null);
                    }


                    MoviesFragment.MoviesData.add(data.get(dataIndex));
                }
            } else {
                MoviesRecAdapter.validMovies = false;
                return null;
            }

        }catch (Exception e){
            MoviesRecAdapter.validMovies = false;
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        try {
            MoviesFragment.moviesRecAdapter.notifyDataSetChanged();
            indicatorViewLLBottom.setVisibility(View.GONE);
            indicatorViewLLCenter.setVisibility(View.GONE);
            indicatorView.hide();
            indicatorViewBtm.hide();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
