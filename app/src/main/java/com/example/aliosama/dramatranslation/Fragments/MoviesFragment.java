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
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.aliosama.dramatranslation.Activities.MainActivity;
import com.example.aliosama.dramatranslation.Activities.MovieActivity;
import com.example.aliosama.dramatranslation.Activities.MoviesSearchResultActivity;
import com.example.aliosama.dramatranslation.Adapters.MoviesRecAdapter;
import com.example.aliosama.dramatranslation.Fetch.FetchMovieSearchResult;
import com.example.aliosama.dramatranslation.Fetch.FetchMovies;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.RetroClient;
import com.wang.avi.AVLoadingIndicatorView;

import org.unbescape.html.HtmlEscape;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {

    RecyclerView moviesRecyclerView;
    public static ArrayList<MovieModel> MoviesData;
    public static LinearLayout indicatorViewLLCenter,indicatorViewLLBottom;
    public static MoviesRecAdapter moviesRecAdapter;
    public static AVLoadingIndicatorView indicatorView , indicatorViewBtm;
    FetchMovies mFetchMovies;

    static ArrayList<MovieModel.ImageModel> Images;
    static ArrayList<MovieModel> data;
    static ApiService api;
    static Call<ArrayList<MovieModel.ImageModel>> mCall1;
    static Call<ArrayList<MovieModel>> call;
    static int TotalMoviesNumber = 0;
    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        try {
            setHasOptionsMenu(true);
            Init(v);
            FetchMovies(1);
        }catch (Exception e){
            e.printStackTrace();
        }

        return v;
    }
    private void Init(View v) {
        indicatorView = (AVLoadingIndicatorView) v.findViewById(R.id.Indicator);
        indicatorViewBtm = (AVLoadingIndicatorView) v.findViewById(R.id.IndicatorBottom);
        indicatorViewLLCenter = (LinearLayout) v.findViewById(R.id.indicatorViewLLcenter);
        indicatorViewLLBottom = (LinearLayout) v.findViewById(R.id.indicatorViewLLbottom);
        MoviesData = new ArrayList<>();
        moviesRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_movies_RecyclerView);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moviesRecAdapter = new MoviesRecAdapter(MoviesData,getActivity());
        moviesRecyclerView.setAdapter(moviesRecAdapter);
//        mSwipeRefreshLayout = findViewById(R.id.fragment_movies_swipeRefreshLayout);
//        MoviesData = new ArrayList<>();

    }

    private static void showCenterLoading(){
        indicatorViewLLCenter.setVisibility(View.VISIBLE);
        indicatorView.smoothToShow();
    }

    public static void hideCenterLoading(){
        indicatorView.smoothToHide();
        indicatorViewLLCenter.setVisibility(View.GONE);
    }

    public static void showBottomLoading(){
        indicatorViewLLBottom.setVisibility(View.VISIBLE);
        indicatorViewBtm.smoothToShow();
    }

    public static void hideBottomLoading(){
        indicatorViewBtm.smoothToHide();
        indicatorViewLLBottom.setVisibility(View.GONE);
    }

    public static void FetchMovies(final int PageNumber) {
        try {
            api = RetroClient.getApiService();
            String Url = "movies?page="+String.valueOf(PageNumber);
            call = api.getMovieJson(Url);
            data = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }

        call.enqueue(new Callback<ArrayList<MovieModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<MovieModel>> call, @NonNull Response<ArrayList<MovieModel>> response) {
                if(response.isSuccessful()) {
                    if (PageNumber == 1) {
                        TotalMoviesNumber = Integer.parseInt(response.headers().get("X-WP-Total"));
                    }

                    data = response.body();
                    if (data != null && !data.isEmpty()) {
                        for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {
                            String title = HtmlEscape.unescapeHtml(data.get(dataIndex).getTitle().getRendered());
                            String story = Html.fromHtml(data.get(dataIndex).getStory().getRendered()).toString();
                            data.get(dataIndex).getTitle().setRendered(title);
                            data.get(dataIndex).getStory().setRendered(story);
                            FetchMoviesImages(PageNumber,dataIndex,TotalMoviesNumber);
                        }
                    }else{
                        moviesRecAdapter.updateMovies(null,PageNumber,TotalMoviesNumber);
                    }
                }else{
                    FetchMovies(PageNumber);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<MovieModel>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static void FetchMoviesImages(final int PageNumber,final  int index,final int TotalMoviesNumber){
        try {
            Images = new ArrayList<>();
            mCall1 = api.getMovieImages("media?parent=" + data.get(index).getID());
            mCall1.enqueue(new Callback<ArrayList<MovieModel.ImageModel>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<MovieModel.ImageModel>> call, @NonNull Response<ArrayList<MovieModel.ImageModel>> response) {
                    if(response.isSuccessful()) {
                        Images = response.body();
                        data.get(index).setImagesUrls(Images);
                        moviesRecAdapter.updateMovies(data.get(index),PageNumber,TotalMoviesNumber);
                    }else if (response.code() == 500){
                        FetchMoviesImages(PageNumber,index,TotalMoviesNumber);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ArrayList<MovieModel.ImageModel>> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_movies_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);


        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem SearchItem = menu.findItem(R.id.Fragment_movies_menu_search);
        final SearchView searchView = (SearchView)  MenuItemCompat.getActionView(SearchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getContext().getResources().getString(R.string.search));
        searchView.setActivated(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {
                            query = query.toLowerCase();
                    Intent intent = new Intent(getActivity(), MoviesSearchResultActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("movies",MoviesData);
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
        try {
            moviesRecAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
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
//            e.printStackTrace();
        }
    }
}
