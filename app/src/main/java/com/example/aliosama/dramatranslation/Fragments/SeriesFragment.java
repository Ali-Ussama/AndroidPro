package com.example.aliosama.dramatranslation.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.aliosama.dramatranslation.Activities.SeriesSearchResultActivity;
import com.example.aliosama.dramatranslation.Adapters.SeriesRecAdapter;
import com.example.aliosama.dramatranslation.Fetch.FetchSeries;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.RetroClient;
import com.example.aliosama.dramatranslation.model.SerieModel;
import com.wang.avi.AVLoadingIndicatorView;

import org.unbescape.html.HtmlEscape;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesFragment extends Fragment {
    public static LinearLayout SeriesIndicatorViewLL,SeriesindicatorViewLLBottom;
    public static RecyclerView SeriesRecyclerView;
    public static ArrayList<SerieModel> SeriesData;
    public static SeriesRecAdapter SeriesRecAdapter;
    public static AVLoadingIndicatorView SeriesIndicatorView, SeriesindicatorViewBtm;
    static ArrayList<SerieModel.ImageModel> Images;
    static ArrayList<SerieModel> data ;
    static ApiService api;
    static Call<ArrayList<SerieModel.ImageModel>> mCall1 ;
    static Call<ArrayList<SerieModel>> call;
    static int TotalMoviesNumber = 0;

    public SeriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_series, container, false);
        try {
            setHasOptionsMenu(true);
            Init(v);
        }catch (Exception e){
            e.printStackTrace();
        }
        return v;
    }

    private void Init(View v) {

        try {
            SeriesData = new ArrayList<>();
            SeriesIndicatorViewLL = (LinearLayout) v.findViewById(R.id.Fragment_series_indicatorViewLLcenter);
            SeriesindicatorViewLLBottom = (LinearLayout) v.findViewById(R.id.Fragment_series_indicatorViewLLbottom);
            SeriesIndicatorView = (AVLoadingIndicatorView) v.findViewById(R.id.Fragment_series_Indicator);
            SeriesindicatorViewBtm = (AVLoadingIndicatorView) v.findViewById(R.id.Fragment_series_IndicatorBottom);
            SeriesRecyclerView = (RecyclerView) v.findViewById(R.id.Fragment_series_RecyclerView);
            SeriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            SeriesRecAdapter = new SeriesRecAdapter(SeriesData,getActivity());
            SeriesRecyclerView.setAdapter(SeriesRecAdapter);

            FetchSeries(1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showCenterLoading(){
        SeriesIndicatorViewLL.setVisibility(View.VISIBLE);
        SeriesIndicatorView.smoothToShow();
    }

    public static void hideCenterLoading(){
        SeriesIndicatorViewLL.setVisibility(View.GONE);
        SeriesIndicatorView.hide();
    }

    public static void showBottomLoading(){

        SeriesindicatorViewLLBottom.setVisibility(View.VISIBLE);
        SeriesindicatorViewBtm.show();
    }

    public static void hideBottonLoading(){
        SeriesindicatorViewBtm.hide();
        SeriesindicatorViewLLBottom.setVisibility(View.GONE);
    }

    public static void FetchSeries(final int PageNumber){
        try {
            data = new ArrayList<>();
            api = RetroClient.getApiService();
            String Url = "tvshows?page=" + String.valueOf(PageNumber);
            call = api.getSerieJson(Url);

        }catch (Exception e){
            e.printStackTrace();
        }

        call.enqueue(new Callback<ArrayList<SerieModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<SerieModel>> call, @NonNull Response<ArrayList<SerieModel>> response) {
                if (response.isSuccessful()){
                    data = response.body();
                    if (PageNumber == 1) {
                        Log.i("MoviesResponseHeaders", "" + response.headers().get("X-WP-Total"));
                        Log.i("MoviesResponseHeaders", "" + response.headers().get("X-WP-TotalPages"));
                        TotalMoviesNumber = Integer.parseInt(response.headers().get("X-WP-Total"));
                    }
                    Images = new ArrayList<>();
                    if (data != null && data.size() > 0) {
                        for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {

                            String title =  HtmlEscape.unescapeHtml(data.get(dataIndex).getTitle().getRendered());
                            String story = Html.fromHtml(data.get(dataIndex).getStory().getRendered()).toString();

                            data.get(dataIndex).getTitle().setRendered(title);
                            data.get(dataIndex).getStory().setRendered(story);
                            try {
                                FetchSeriesImages(PageNumber,data.get(dataIndex),TotalMoviesNumber);
                            }catch (Exception ee){
                                ee.printStackTrace();
                            }
                        }
                    }else{
                        SeriesRecAdapter.UpdateSeries(PageNumber,null,TotalMoviesNumber);
                    }
                }else{
                    FetchSeries(PageNumber);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SerieModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static void FetchSeriesImages(final int PageNumber, final SerieModel serieModel,final int TotalMoviesNumber){
        mCall1 = api.getSerieImages("media?parent=" + serieModel.getID());
        mCall1.enqueue(new Callback<ArrayList<SerieModel.ImageModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<SerieModel.ImageModel>> call, @NonNull Response<ArrayList<SerieModel.ImageModel>> response) {
                if (response.isSuccessful()){
                    Images = response.body();
                    serieModel.setImagesUrls(Images);
                    SeriesRecAdapter.UpdateSeries(PageNumber,serieModel,TotalMoviesNumber);
                }else{
                    FetchSeriesImages(PageNumber,serieModel,TotalMoviesNumber);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SerieModel.ImageModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_series_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem SearchItem = menu.findItem(R.id.Fragment_series_menu_search);
        final SearchView searchView = (SearchView)  MenuItemCompat.getActionView(SearchItem);
        searchView.setQueryHint(getContext().getResources().getString(R.string.search));
        searchView.setActivated(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {
                    query = query.toLowerCase();
                    Intent intent = new Intent(getActivity(), SeriesSearchResultActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("series",SeriesData);
                    intent.putExtra("Search String",query);
                    getActivity().startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (!call.isCanceled() || call.isExecuted()){
                call.cancel();
            }
            if (!mCall1.isCanceled() || mCall1.isExecuted()){
                mCall1.cancel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
