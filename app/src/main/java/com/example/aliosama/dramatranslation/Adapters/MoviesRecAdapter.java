package com.example.aliosama.dramatranslation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aliosama.dramatranslation.Activities.MovieActivity;
import com.example.aliosama.dramatranslation.Fetch.FetchMovies;
import com.example.aliosama.dramatranslation.Fragments.MoviesFragment;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aliosama on 8/13/2017.
 */

public class MoviesRecAdapter extends RecyclerView.Adapter<MoviesRecAdapter.ViewHolder>{
    public static ArrayList<MovieModel> AdapterData;
    private FragmentActivity context;
    public static boolean validMovies = true;
    private int i = 2;
    SharedPreferences sp;
    public MoviesRecAdapter(ArrayList<MovieModel> adapterData, FragmentActivity context) {
        AdapterData = adapterData;
        this.context = context;
        sp = context.getSharedPreferences("FavoriteMovies", Context.MODE_PRIVATE);
    }

    @Override
    public MoviesRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movies_recyclerview_row_item,parent,false);
        return new ViewHolder(v,AdapterData);
    }

    @Override
    public void onBindViewHolder(MoviesRecAdapter.ViewHolder holder, int position) {
       try {
//          Favorite Icon
           if (sp.getInt(String.valueOf(AdapterData.get(position).getID()), 0) != 0) {
               holder.FavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
               holder.FavoriteIcon.setTag("1");
           } else {
               holder.FavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
               holder.FavoriteIcon.setTag("0");
           }
//         Image
           Picasso.with(context).load(AdapterData.get(position).getImagesUrls().get(AdapterData.get(position).getImagesUrls().size() - 1).getImageUrl()).into(holder.MovieImage);

//         Title
           holder.Title.setText(AdapterData.get(position).getTitle().getRendered());

//         Story
           if (AdapterData.get(position).getStory().getRendered().length() > 1){
               holder.Story.setText(AdapterData.get(position).getStory().getRendered());
           }else{
               holder.Story.setText("لا يوجد");
           }

//         Date
           if((AdapterData.get(position).getDate() != null && !AdapterData.get(position).getDate().matches(""))) {
               String Date = AdapterData.get(position).getDate().substring(0, 4);
               holder.Date.setText(Date);
           }else{
               holder.Date.setText(R.string.Default_Date);
           }

//         Fetching Movies
//           if(validMovies && position == AdapterData.size()-1){
//               MoviesFragment.indicatorViewLLBottom.setVisibility(View.VISIBLE);
//               MoviesFragment.indicatorViewBtm.setVisibility(View.VISIBLE);
//               new FetchMovies(context, i++).execute();
//           }
       }catch (Exception e){
           Log.i("MoviesIDException",String.valueOf(AdapterData.get(position).getID()));
           e.printStackTrace();
       }
    }

    @Override
    public int getItemCount() {
        return AdapterData.size();
    }

    public void updateMovies(MovieModel data,int pageNumber,int TotalMoviesNumber){
        try {
            if(data != null) {
                AdapterData.add(data);
                MoviesFragment.hideCenterLoading();
                MoviesFragment.showBottomLoading();
                notifyDataSetChanged();
                if (AdapterData.size()%10==0) {
                    MoviesFragment.FetchMovies(++pageNumber);
                }
            }
            Log.i("MoviesAdapterSize",String.valueOf(AdapterData.size()));
            Log.i("TotalMoviesNumber",String.valueOf(TotalMoviesNumber));
            if(AdapterData.size() == TotalMoviesNumber){
                Log.i("CheckAdapterSize","Total Movies Equal Adapter Size");
                MoviesFragment.hideBottomLoading();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView MovieImage , FavoriteIcon;
        TextView Title,Story,ReadMore,Date;
        ArrayList<MovieModel> data;
        private ViewHolder(View itemView, final ArrayList<MovieModel> data) {
            super(itemView);
            this.data = data;
            MovieImage  = (ImageView) itemView.findViewById(R.id.FMRI_image);
            FavoriteIcon = (ImageView) itemView.findViewById(R.id.FMRI_favorite);
            Title = (TextView) itemView.findViewById(R.id.FMRI_title);
            Story = (TextView) itemView.findViewById(R.id.FMRI_story);
            ReadMore = (TextView) itemView.findViewById(R.id.FMRI_read_more);
            Date = (TextView) itemView.findViewById(R.id.FMRI_Date);

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(v.getContext(),MovieActivity.class);
                        intent.putExtra("data",data.get(getAdapterPosition()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
