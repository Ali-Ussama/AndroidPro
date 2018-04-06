package com.example.aliosama.dramatranslation.model;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by aliosama on 8/15/2017.
 */

public interface ApiService {

    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of ContactList
    */
    @GET
    Call<ArrayList<MovieModel>> getMovieJson(@Url String url);

    @GET
    Call<ArrayList<MovieModel.ImageModel>> getMovieImages(@Url String url);

    @GET
    Call<ArrayList<MovieModel.MovieLink>> getDownloadMovieLink(@Url String url);
    @GET
    Call<ArrayList<SerieModel>> getSerieJson(@Url String url);

    @GET
    Call<ArrayList<SerieModel.ImageModel>> getSerieImages(@Url String url);

    @GET
    Call<ArrayList<EpisodeModel>> getEpisodes(@Url String url);

    @GET
    Call<ArrayList<DownloadModel>> getDownloads(@Url String url);

    @GET
    Call<MovieModel> getFavoriteMovieJson(@Url String url);

    @GET
    Call<SerieModel> getFavoriteSerieJson(@Url String url);

}
