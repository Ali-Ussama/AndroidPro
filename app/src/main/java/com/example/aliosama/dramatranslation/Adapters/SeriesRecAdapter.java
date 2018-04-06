package com.example.aliosama.dramatranslation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aliosama.dramatranslation.Activities.SerieActivity;
import com.example.aliosama.dramatranslation.Fetch.FetchSeries;
import com.example.aliosama.dramatranslation.Fragments.SeriesFragment;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.SerieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aliosama on 8/14/2017.
 */

public class SeriesRecAdapter extends RecyclerView.Adapter<SeriesRecAdapter.SeriesViewHolder> {

    ArrayList<SerieModel> SeriesData;
    FragmentActivity context;
    SharedPreferences sp;
    public static boolean validSeries = true;
    private int i = 2;

    public SeriesRecAdapter(ArrayList<SerieModel> seriesData, FragmentActivity context) {
        SeriesData = seriesData;
        this.context = context;
        sp = context.getSharedPreferences("FavoriteSeries", Context.MODE_PRIVATE);

    }

    @Override
    public SeriesRecAdapter.SeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_series_recyclerview_row_item,parent,false);
        return new SeriesRecAdapter.SeriesViewHolder(v,SeriesData);
    }

    @Override
    public void onBindViewHolder(SeriesRecAdapter.SeriesViewHolder holder, int position) {
       try {
           boolean validImage = false;
           try {
               for(int i = 0 ; i < SeriesData.get(position).getImagesUrls().size(); i++){
                   if (SeriesData.get(position).getImagesUrls().get(i).getMediaDetails().getSizes() != null) {
                       Picasso.with(context).load(SeriesData.get(position).getImagesUrls().get(i).getImageUrl()).into(holder.SerieImage);
                       validImage = true;
                       break;
                   }
               }
               if(!validImage)
                    holder.SerieImage.setImageResource(R.drawable.blackimag);

           }catch (Exception e){
               e.printStackTrace();
           }

           if (SeriesData.get(position).getStory().getRendered().length() > 1){
               holder.Story.setText(SeriesData.get(position).getStory().getRendered());
           }else{
               holder.Story.setText("لا يوجد");
           }

           holder.Title.setText(SeriesData.get(position).getTitle().getRendered());

           if(sp.getInt(String.valueOf(SeriesData.get(position).getID()),0) != 0) {
               holder.FavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
               holder.FavoriteIcon.setTag("1");
           }else{
               holder.FavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
               holder.FavoriteIcon.setTag("0");
           }

//           //         Fetching Series
//           if(validSeries && position == SeriesData.size()-1){
//               SeriesFragment.SeriesindicatorViewLLBottom.setVisibility(View.VISIBLE);
//               SeriesFragment.SeriesindicatorViewBtm.setVisibility(View.VISIBLE);
//               new FetchSeries(context, i++).execute();
//           }

       }catch (Exception e){
           System.out.println(position);
           e.printStackTrace();
       }
    }

    @Override
    public int getItemCount() {
        return SeriesData.size();
    }

    public void UpdateSeries(int PageNumber,SerieModel serieModel,int TotalMoviesNumber){

        if (serieModel != null ){
            SeriesData.add(serieModel);
            SeriesFragment.hideCenterLoading();
            SeriesFragment.showBottomLoading();
            notifyDataSetChanged();
            if (SeriesData.size()% 10 == 0){
                SeriesFragment.FetchSeries(++PageNumber);
            }
        }
        if (SeriesData.size() == TotalMoviesNumber){
            SeriesFragment.hideBottonLoading();
        }
    }

    public class SeriesViewHolder extends RecyclerView.ViewHolder{

        ImageView SerieImage , FavoriteIcon;
        TextView Title,Story,ReadMore;
        ArrayList<SerieModel> data;
        public SeriesViewHolder(View itemView, final ArrayList<SerieModel> data) {
            super(itemView);
            this.data = data;
            try {
                SerieImage  = (ImageView) itemView.findViewById(R.id.FSRI_image);
                FavoriteIcon = (ImageView) itemView.findViewById(R.id.FSRI_favorite);
                Title = (TextView) itemView.findViewById(R.id.FSRI_title);
                Story = (TextView) itemView.findViewById(R.id.FSRI_story);
                ReadMore = (TextView) itemView.findViewById(R.id.FSRI_read_more);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), SerieActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Serie data",data.get(getAdapterPosition()));
                        v.getContext().startActivity(intent);
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
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
