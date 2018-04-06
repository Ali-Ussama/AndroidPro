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
 * Created by aliosama on 8/25/2017.
 */

public class FavortieMoviesRecAdapter extends RecyclerView.Adapter<FavortieMoviesRecAdapter.ViewHolder>{

    ArrayList<MovieModel> FavRecData;
    Context context;
    SharedPreferences sp;

    public FavortieMoviesRecAdapter(ArrayList<MovieModel> favRecData,Context mcontext) {
        FavRecData = favRecData;
        context = mcontext;
        sp = context.getSharedPreferences("FavoriteMovies", Context.MODE_PRIVATE);

    }

    @Override
    public FavortieMoviesRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_favorite_movies_recycler_row_item,parent,false);
        return new ViewHolder(v,FavRecData);
    }

    @Override
    public void onBindViewHolder(FavortieMoviesRecAdapter.ViewHolder holder, int position) {
        Picasso.with(context).load(FavRecData.get(position).getImagesUrls().get(FavRecData.get(position).getImagesUrls().size() - 1).getImageUrl()).into(holder.MovieImage);
        holder.Title.setText(FavRecData.get(position).getTitle().getRendered());
        holder.Story.setText(FavRecData.get(position).getStory().getRendered());
        holder.Date.setText(FavRecData.get(position).getDate());

        if(sp.getInt(String.valueOf(FavRecData.get(position).getID()),0) != 0) {
            holder.FavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
            holder.FavoriteIcon.setTag("1");
        }else{
            holder.FavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
            holder.FavoriteIcon.setTag("0");
        }
    }
    @Override
    public int getItemCount() {
        return FavRecData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView MovieImage , FavoriteIcon;
        TextView Title,Story,ReadMore,Date;
        ArrayList<MovieModel> data;
        public ViewHolder(View itemView, final ArrayList<MovieModel> data) {
            super(itemView);
            this.data = data;

            MovieImage  = (ImageView) itemView.findViewById(R.id.FFMRI_image);
            FavoriteIcon = (ImageView) itemView.findViewById(R.id.FFMRI_favorite);
            Title = (TextView) itemView.findViewById(R.id.FFMRI_title);
            Story = (TextView) itemView.findViewById(R.id.FFMRI_story);
            ReadMore = (TextView) itemView.findViewById(R.id.FFMRI_read_more);
            Date = (TextView) itemView.findViewById(R.id.FFMRI_Date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MovieActivity.class);
                    intent.putExtra("data",data.get(getAdapterPosition()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
