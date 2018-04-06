package com.example.aliosama.dramatranslation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aliosama.dramatranslation.Activities.MovieActivity;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aliosama on 8/18/2017.
 */

public class MoviesSearchResultRecAdapter extends RecyclerView.Adapter<MoviesSearchResultRecAdapter.ViewHolder> {

    public static ArrayList<MovieModel> MoviesSearchResultAdapterData;
    Context context;
    SharedPreferences sp;

    public MoviesSearchResultRecAdapter(ArrayList<MovieModel> moviesSearchResultAdapterData, Context context) {
        MoviesSearchResultAdapterData = moviesSearchResultAdapterData;
        this.context = context;
        sp = context.getSharedPreferences("FavoriteMovies", Context.MODE_PRIVATE);

    }

    @Override
    public MoviesSearchResultRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movies_search_result_recyclerview_row_item,parent,false);
        return new ViewHolder(v,MoviesSearchResultAdapterData,context);
    }

    @Override
    public void onBindViewHolder(MoviesSearchResultRecAdapter.ViewHolder holder, int position) {
        if(sp.getInt(String.valueOf(MoviesSearchResultAdapterData.get(position).getID()),0) != 0) {
            System.out.println("Founded in Adapter");
            sp.edit().putInt(String.valueOf(MoviesSearchResultAdapterData.get(position).getID()), MoviesSearchResultAdapterData.get(position).getID()).apply();
            holder.FavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
            holder.FavoriteIcon.setTag("1");
        }else{
            System.out.println("Not Founded in Adapter");
            holder.FavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
            holder.FavoriteIcon.setTag("0");
        }
        Picasso.with(context).load(MoviesSearchResultAdapterData.get(position).getImagesUrls().get(MoviesSearchResultAdapterData.get(position).getImagesUrls().size() - 1).getImageUrl()).into(holder.MovieImage);
        holder.Title.setText(MoviesSearchResultAdapterData.get(position).getTitle().getRendered());
        holder.Date.setText(MoviesSearchResultAdapterData.get(position).getDate());
        holder.Story.setText(MoviesSearchResultAdapterData.get(position).getStory().getRendered());
    }

    @Override
    public int getItemCount() {
        return MoviesSearchResultAdapterData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView MovieImage , FavoriteIcon;
        TextView Title,CountryType,MovieType,Story,ReadMore,Date;
        ArrayList<MovieModel> data;
        Context context;
        public ViewHolder(View itemView, final ArrayList<MovieModel> data, final Context context) {
            super(itemView);
            this.data = data;
            this.context = context;
            MovieImage  = (ImageView) itemView.findViewById(R.id.AMSRRI_image);
            FavoriteIcon = (ImageView) itemView.findViewById(R.id.AMSRRI_favorite);
            Title = (TextView) itemView.findViewById(R.id.AMSRRI_title);
//            CountryType = (TextView) itemView.findViewById(R.id.AMSRRI_county_type);
//            MovieType = (TextView) itemView.findViewById(R.id.AMSRRI_movie_type);
            Story = (TextView) itemView.findViewById(R.id.AMSRRI_description);
            ReadMore = (TextView) itemView.findViewById(R.id.AMSRRI_read_more);
            Date = (TextView) itemView.findViewById(R.id.AMSRRI_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   try {
                       Intent intent = new Intent(context, MovieActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       intent.putExtra("data",data.get(getAdapterPosition()));
                       context.startActivity(intent);
                   }catch (Exception e){
                       e.printStackTrace();
                   }

                }
            });

            FavoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = context.getSharedPreferences("FavoriteMovies", Context.MODE_PRIVATE);
                    if(FavoriteIcon.getTag().equals("1")){
                        sp.edit().remove(String.valueOf(data.get(getAdapterPosition()).getID())).apply();
                        FavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
                        FavoriteIcon.setTag("0");
                    }else{
                        sp.edit().putInt(String.valueOf(data.get(getAdapterPosition()).getID()),data.get(getAdapterPosition()).getID()).apply();
                        FavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
                        FavoriteIcon.setTag("1");
                    }
                }
            });
        }
    }
}
