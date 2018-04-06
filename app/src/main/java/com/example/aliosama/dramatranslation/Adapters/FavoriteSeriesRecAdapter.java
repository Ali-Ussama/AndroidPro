package com.example.aliosama.dramatranslation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aliosama.dramatranslation.Activities.MovieActivity;
import com.example.aliosama.dramatranslation.Activities.SerieActivity;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.SerieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aliosama on 8/28/2017.
 */

public class FavoriteSeriesRecAdapter  extends RecyclerView.Adapter<FavoriteSeriesRecAdapter.ViewHolder>{

    ArrayList<SerieModel> FavSerieRecData;
    Context context;
    SharedPreferences sp;

    public FavoriteSeriesRecAdapter(ArrayList<SerieModel> favRecData,Context mcontext) {
        FavSerieRecData = favRecData;
        context = mcontext;
        sp = context.getSharedPreferences("FavoriteSeries", Context.MODE_PRIVATE);
    }

    @Override
    public FavoriteSeriesRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_favorite_series_recycler_row_item,parent,false);
        return new FavoriteSeriesRecAdapter.ViewHolder(v,FavSerieRecData);
    }

    @Override
    public void onBindViewHolder(FavoriteSeriesRecAdapter.ViewHolder holder, int position) {
        Picasso.with(context).load(FavSerieRecData.get(position).getImagesUrls().get(FavSerieRecData.get(position).getImagesUrls().size() - 1).getImageUrl()).into(holder.MovieImage);
        holder.Title.setText(FavSerieRecData.get(position).getTitle().getRendered());
        holder.Story.setText(FavSerieRecData.get(position).getStory().getRendered());
//        holder.Date.setText(FavSerieRecData.get(position).getDate());

        if(sp.getInt(String.valueOf(FavSerieRecData.get(position).getID()),0) != 0) {
            holder.FavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
            holder.FavoriteIcon.setTag("1");
        }else{
            holder.FavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
            holder.FavoriteIcon.setTag("0");
        }
    }
    @Override
    public int getItemCount() {
        return FavSerieRecData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView MovieImage , FavoriteIcon;
        TextView Title,Story,ReadMore,Date;
        ArrayList<SerieModel> data;
        public ViewHolder(View itemView, final ArrayList<SerieModel> data) {
            super(itemView);
            this.data = data;

            MovieImage  = (ImageView) itemView.findViewById(R.id.FFSRI_image);
            FavoriteIcon = (ImageView) itemView.findViewById(R.id.FFSRI_favorite);
            Title = (TextView) itemView.findViewById(R.id.FFSRI_title);
            Story = (TextView) itemView.findViewById(R.id.FFSRI_story);
            ReadMore = (TextView) itemView.findViewById(R.id.FFSRI_read_more);
//            Date = (TextView) itemView.findViewById(R.id.FFSRI_Date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SerieActivity.class);
                    intent.putExtra("Serie data",data.get(getAdapterPosition()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
