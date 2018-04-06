package com.example.aliosama.dramatranslation.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.aliosama.dramatranslation.Adapters.FavortieMoviesRecAdapter;
import com.example.aliosama.dramatranslation.Adapters.MoviesRecAdapter;
import com.example.aliosama.dramatranslation.Fetch.FetchFavoriteMovies;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FavortieMoviesFragment extends Fragment {

    RecyclerView FavortiemoviesRecyclerView;
    public static ArrayList<MovieModel> FavortieMoviesData;
    public static LinearLayout FavoriteindicatorViewLLCenter,FavoriteindicatorViewLLBottom;
    public static FavortieMoviesRecAdapter FavortiemoviesRecAdapter;
    public static AVLoadingIndicatorView FavoriteindicatorView , FavoriteindicatorViewBtm;
    FetchFavoriteMovies mFetchFavoriteMovies;
    ArrayList<String> IDs;

    public FavortieMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favortie_movies, container, false);
        try {
            FavortiemoviesRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_favorite_movies_RecyclerView);
            FavoriteindicatorViewLLCenter = (LinearLayout) v.findViewById(R.id.fragment_favorite_movies_indicatorViewLLcenter);
            FavoriteindicatorViewLLBottom = (LinearLayout) v.findViewById(R.id.fragment_favorite_movies_indicatorViewLLbottom);
            FavoriteindicatorView = (AVLoadingIndicatorView) v.findViewById(R.id.fragment_favorite_movies_Indicator) ;
            FavoriteindicatorViewBtm = (AVLoadingIndicatorView) v.findViewById(R.id.fragment_favorite_movies_IndicatorBottom);

            FavortieMoviesData = new ArrayList<>();
            FavortiemoviesRecAdapter = new FavortieMoviesRecAdapter(FavortieMoviesData,getContext());
            FavortiemoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            FavortiemoviesRecyclerView.setAdapter(FavortiemoviesRecAdapter);

            IDs = new ArrayList<>();

            SharedPreferences sp = getContext().getSharedPreferences("FavoriteMovies", Context.MODE_PRIVATE);
            Map<String, ?> mMap = sp.getAll();
            for(Map.Entry<String,?> Key:mMap.entrySet()){
                IDs.add(Key.getValue().toString());
                System.out.println(Key.getValue().toString());
            }
            FavoriteindicatorViewLLCenter.setVisibility(View.VISIBLE);
            FavoriteindicatorView.smoothToShow();

            mFetchFavoriteMovies = new FetchFavoriteMovies();
            mFetchFavoriteMovies.execute(IDs);

        }catch (Exception e){
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        FavortiemoviesRecAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(!mFetchFavoriteMovies.isCancelled()){
                Log.d("TAG","Favorite Movies Fetch Not Canceled");
                mFetchFavoriteMovies.cancel(true);
            }else{
                Log.d("TAG","Favorite Movies Fetch Canceled");
                mFetchFavoriteMovies.cancel(true);
            }
        }catch (Exception e){
//            e.printStackTrace();
        }
    }
}
