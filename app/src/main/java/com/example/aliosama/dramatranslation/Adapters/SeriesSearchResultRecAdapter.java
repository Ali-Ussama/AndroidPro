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
 * Created by aliosama on 9/8/2017.
 */

public class SeriesSearchResultRecAdapter  extends RecyclerView.Adapter<SeriesSearchResultRecAdapter.ViewHolder> {

    public static ArrayList<SerieModel> SeriesSearchResultAdapterData;
    Context context;
    SharedPreferences sp;

    public SeriesSearchResultRecAdapter(ArrayList<SerieModel> seriesSearchResultAdapterData, Context context) {
        SeriesSearchResultAdapterData = seriesSearchResultAdapterData;
        this.context = context;
        sp = context.getSharedPreferences("FavoriteSeries", Context.MODE_PRIVATE);

    }

    @Override
    public SeriesSearchResultRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_series_search_result_recyclerview_row_item,parent,false);
        return new SeriesSearchResultRecAdapter.ViewHolder(v,SeriesSearchResultAdapterData,context);
    }

    @Override
    public void onBindViewHolder(SeriesSearchResultRecAdapter.ViewHolder holder, int position) {
        if(sp.getInt(String.valueOf(SeriesSearchResultAdapterData.get(position).getID()),0) != 0) {
            System.out.println("Founded in Adapter");
            sp.edit().putInt(String.valueOf(SeriesSearchResultAdapterData.get(position).getID()), SeriesSearchResultAdapterData.get(position).getID()).apply();
            holder.FavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
            holder.FavoriteIcon.setTag("1");
        }else{
            System.out.println("Not Founded in Adapter");
            holder.FavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
            holder.FavoriteIcon.setTag("0");
        }
        Picasso.with(context).load(SeriesSearchResultAdapterData.get(position).getImagesUrls().get(SeriesSearchResultAdapterData.get(position).getImagesUrls().size() - 1).getImageUrl()).into(holder.SerieImage);
        holder.Title.setText(SeriesSearchResultAdapterData.get(position).getTitle().getRendered());
//        holder.Date.setText(SeriesSearchResultAdapterData.get(position).getDate());
        holder.Story.setText(SeriesSearchResultAdapterData.get(position).getStory().getRendered());
    }

    @Override
    public int getItemCount() {
        return SeriesSearchResultAdapterData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView SerieImage , FavoriteIcon;
        TextView Title,CountryType,MovieType,Story,ReadMore,Date;
        ArrayList<SerieModel> data;
        Context context;
        public ViewHolder(View itemView, final ArrayList<SerieModel> data, final Context context) {
            super(itemView);
            this.data = data;
            this.context = context;
            SerieImage  = (ImageView) itemView.findViewById(R.id.ASSRRI_image);
            FavoriteIcon = (ImageView) itemView.findViewById(R.id.ASSRRI_favorite);
            Title = (TextView) itemView.findViewById(R.id.ASSRRI_title);
            Story = (TextView) itemView.findViewById(R.id.ASSRRI_story);
            ReadMore = (TextView) itemView.findViewById(R.id.ASSRRI_read_more);
//            CountryType = (TextView) itemView.findViewById(R.id.AMSRRI_county_type);
//            MovieType = (TextView) itemView.findViewById(R.id.AMSRRI_movie_type);
//            Date = (TextView) itemView.findViewById(R.id.ASSRRI_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, SerieActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Serie data",data.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            FavoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = context.getSharedPreferences("FavoriteSeries", Context.MODE_PRIVATE);
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
