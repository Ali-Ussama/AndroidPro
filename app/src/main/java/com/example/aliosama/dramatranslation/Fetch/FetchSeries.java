package com.example.aliosama.dramatranslation.Fetch;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;

import com.example.aliosama.dramatranslation.Adapters.SeriesRecAdapter;
import com.example.aliosama.dramatranslation.Fragments.MoviesFragment;
import com.example.aliosama.dramatranslation.Fragments.SeriesFragment;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.RetroClient;
import com.example.aliosama.dramatranslation.model.SerieModel;

import org.unbescape.html.HtmlEscape;

import java.util.ArrayList;

import retrofit2.Call;


/**
 * Created by aliosama on 8/20/2017.
 */

public class FetchSeries extends AsyncTask<Object, Object, Void> {

    FragmentActivity context;
    ArrayList<SerieModel.ImageModel> Images;
    ArrayList<SerieModel> data ;
    ApiService api;
    Call<ArrayList<SerieModel.ImageModel>> mCall1 ;
    Call<ArrayList<SerieModel>> call;

    int PageNumber;

    public FetchSeries(FragmentActivity context,int i){
        this.context = context;
        PageNumber = i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Override
    protected Void doInBackground(Object... params) {

        try {
            /**
             * Calling JSON
             */
            data = new ArrayList<>();
            api = RetroClient.getApiService();
                String Url = "tvshows?page=" + String.valueOf(PageNumber);

                call = api.getSerieJson(Url);
                data = call.execute().body();

                Images = new ArrayList<>();
                if (data != null && data.size() > 0) {
                    for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {

                        String title =  HtmlEscape.unescapeHtml(data.get(dataIndex).getTitle().getRendered());
                        String story = Html.fromHtml(data.get(dataIndex).getStory().getRendered()).toString();

                        data.get(dataIndex).getTitle().setRendered(title);
                        data.get(dataIndex).getStory().setRendered(story);
                        try {
                            mCall1 = api.getSerieImages("media?parent=" + data.get(dataIndex).getID());
                            Images = mCall1.execute().body();
                            data.get(dataIndex).setImagesUrls(Images);
                        }catch (Exception ee){
                            ee.printStackTrace();
                        }

                        SeriesFragment.SeriesData.add(data.get(dataIndex));
                    }
                }else{
                    SeriesRecAdapter.validSeries = false;
                    return null;
                }

        }catch (Exception e){
            SeriesRecAdapter.validSeries = false;
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        try {

            SeriesFragment.SeriesRecAdapter.notifyDataSetChanged();
//            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            SeriesFragment.SeriesIndicatorViewLL.setVisibility(View.GONE);
            SeriesFragment.SeriesindicatorViewLLBottom.setVisibility(View.GONE);
            SeriesFragment.SeriesIndicatorView.hide();
            SeriesFragment.SeriesindicatorViewBtm.hide();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
