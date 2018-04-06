package com.example.aliosama.dramatranslation.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.aliosama.dramatranslation.Adapters.FavoriteSeriesRecAdapter;
import com.example.aliosama.dramatranslation.Adapters.FavortieMoviesRecAdapter;
import com.example.aliosama.dramatranslation.Fetch.FetchFavoriteMovies;
import com.example.aliosama.dramatranslation.Fetch.FetchFavoriteSeries;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.SerieModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Map;

public class FavoriteSeriesFragment extends Fragment {

    RecyclerView FavoriteSerieRecyclerView;
    public static ArrayList<SerieModel> FavoriteSeriesData;
    public static LinearLayout FavoriteindicatorViewLLCenter,FavoriteindicatorViewLLBottom;
    public static FavoriteSeriesRecAdapter FavoriteSeriesRecAdapter;
    public static AVLoadingIndicatorView FavoriteindicatorView , FavoriteindicatorViewBtm;
    FetchFavoriteSeries mFetchFavoriteSeries;
    ArrayList<String> IDs;
    public FavoriteSeriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite_series, container, false);
        try {

            FavoriteSerieRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_favorite_series_RecyclerView);
            FavoriteindicatorViewLLCenter = (LinearLayout) v.findViewById(R.id.fragment_favorite_series_indicatorViewLLcenter);
            FavoriteindicatorViewLLBottom = (LinearLayout) v.findViewById(R.id.fragment_favorite_series_indicatorViewLLbottom);
            FavoriteindicatorView = (AVLoadingIndicatorView) v.findViewById(R.id.fragment_favorite_series_Indicator) ;
            FavoriteindicatorViewBtm = (AVLoadingIndicatorView) v.findViewById(R.id.fragment_favorite_series_IndicatorBottom);

            FavoriteSeriesData = new ArrayList<>();
            FavoriteSeriesRecAdapter = new FavoriteSeriesRecAdapter(FavoriteSeriesData,getContext());
            FavoriteSerieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            FavoriteSerieRecyclerView.setAdapter(FavoriteSeriesRecAdapter);

            IDs = new ArrayList<>();

            SharedPreferences sp = getContext().getSharedPreferences("FavoriteSeries", Context.MODE_PRIVATE);
            Map<String, ?> mMap = sp.getAll();
            for(Map.Entry<String,?> Key:mMap.entrySet()){
                IDs.add(Key.getValue().toString());
            }
            FavoriteindicatorViewLLCenter.setVisibility(View.VISIBLE);
            FavoriteindicatorView.smoothToShow();
            mFetchFavoriteSeries = new FetchFavoriteSeries();
            mFetchFavoriteSeries.execute(IDs);

        }catch (Exception e){
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        FavoriteSeriesRecAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(!mFetchFavoriteSeries.isCancelled()){
                Log.d("TAG","Favorite Series Fetch Not Canceled");
                mFetchFavoriteSeries.cancel(true);
            }else{
                Log.d("TAG","Favorite Series Fetch Canceled");
                mFetchFavoriteSeries.cancel(true);
            }
        }catch (Exception e){
//            e.printStackTrace();
        }
    }

}
