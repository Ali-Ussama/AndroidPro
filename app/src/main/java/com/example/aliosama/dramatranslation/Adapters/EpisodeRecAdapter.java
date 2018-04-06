package com.example.aliosama.dramatranslation.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliosama.dramatranslation.Activities.MovieActivity;
import com.example.aliosama.dramatranslation.Activities.SerieActivity;
import com.example.aliosama.dramatranslation.Activities.WebActivity;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.DownloadModel;
import com.example.aliosama.dramatranslation.model.EpisodeModel;
import com.example.aliosama.dramatranslation.model.RetroClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aliosama on 8/21/2017.
 */

public class EpisodeRecAdapter extends RecyclerView.Adapter<EpisodeRecAdapter.ViewHolder>{

    ArrayList<EpisodeModel> EpsidoeDataList;
    Context context;
    public EpisodeRecAdapter(Context context, ArrayList<EpisodeModel> listData) {
        this.context = context;
        EpsidoeDataList = listData;
    }

    @Override
    public EpisodeRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_serie_recyclerview_row_item,parent,false);
        return new ViewHolder(v,EpsidoeDataList);
    }

    @Override
    public void onBindViewHolder(EpisodeRecAdapter.ViewHolder holder, int position) {
        try {
            holder.EpisodeName.setText(EpsidoeDataList.get(position).getEpisodeName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return EpsidoeDataList.size();
    }

    public void UpdateEpisodes(EpisodeModel mEpisodeModel,String SerieName,int EpisodeNumber){
        try {
            if (mEpisodeModel!= null){
                EpsidoeDataList.add(mEpisodeModel);
                notifyDataSetChanged();
                SerieActivity.FetchEpisodes(SerieName,++EpisodeNumber);
            }else{
                SerieActivity.HideLoading();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView EpisodeName;
        Button Watch,Download;
        ArrayList<EpisodeModel> data;
        ArrayList<DownloadModel> AllDownloadLinks;
        ArrayList<DownloadModel> Downloads;
        ApiService api;
        Call<ArrayList<DownloadModel>> mCall1 ;
        ProgressDialog progressDialog;

        public ViewHolder(View itemView, final ArrayList<EpisodeModel> data) {
            super(itemView);
            try {
                EpisodeName = (TextView) itemView.findViewById(R.id.ASRI_Episode_Name);
                Watch = (Button) itemView.findViewById(R.id.ASRI_Episode_watch_btn);
                Download = (Button) itemView.findViewById(R.id.ASRI_Episode_download_btn);
                this.data = data;

                Watch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            CharSequence watchTypes[] = new CharSequence[data.get(getAdapterPosition()).getVideos().size()];
                            for (int i = 0 ; i < data.get(getAdapterPosition()).getVideos().size() ; i++){
                                watchTypes[i] = data.get(getAdapterPosition()).getVideos().get(i).getHost();
                            }
                            final View view = v;
                            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("اختر نوع المشاهدة");
                            builder.setItems(watchTypes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
//                                     view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(getAdapterPosition()).getVideos().get(which).getUrl())));
                                        Intent intent = new Intent(view.getContext(),WebActivity.class);
                                        String Url = data.get(getAdapterPosition()).getVideos().get(which).getUrl();
                                        if(Url.contains("cloudy")){
                                            for(int i = 0 ; i < Url.length(); i++){
                                                if(Url.charAt(i) == '='){
                                                    Url = Url.substring(i+1);
                                                    break;
                                                }
                                            }
                                            Url =  "http://www.cloudy.ec/v/"+Url;
                                        }
                                        intent.putExtra("url",Url);
                                        view.getContext().startActivity(intent);
                                        dialog.dismiss();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            builder.show();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                Download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AllDownloadLinks = new ArrayList<>();
                        if ( data.get(getAdapterPosition()).getDownload() != null && !data.get(getAdapterPosition()).getDownload().isEmpty()){
                            showDownloadLinks(v);
                        }else {
                            progressDialog = new ProgressDialog(v.getContext());
                            progressDialog.setMessage("جار التحميل..."); // Setting Message
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(true);
                            FetchEpisodeDownloadLinks(v, data.get(getAdapterPosition()).getDtString(), 1);
                        }
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void FetchEpisodeDownloadLinks(final View v, final String dtString, final int NextLinkNumber){
            try {
                //Get Download Links
                //Read Download URL
                api = RetroClient.getApiService();
                Downloads = new ArrayList<>();
                String nextLink = "";
                if(NextLinkNumber > 1){
                    nextLink = "-"+String.valueOf(NextLinkNumber);
                }
                String downloadUrl = "dt_links?slug=\"" +dtString+nextLink+"\"";

                mCall1 = api.getDownloads(downloadUrl);
                mCall1.enqueue(new Callback<ArrayList<DownloadModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<DownloadModel>> call, @NonNull Response<ArrayList<DownloadModel>> response) {
                        try {
                            if (response.isSuccessful()){
                                Downloads = response.body();
                                if (Downloads != null && !Downloads.isEmpty()) {
                                    AllDownloadLinks.add(Downloads.get(0));
                                    FetchEpisodeDownloadLinks(v,dtString,NextLinkNumber+1);
                                }else{
                                    data.get(getAdapterPosition()).setDownload(AllDownloadLinks);
                                    progressDialog.dismiss();
                                    showDownloadLinks(v);
                                }
                            }else if(response.code() == 500){
                                FetchEpisodeDownloadLinks(v,dtString,NextLinkNumber);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DownloadModel>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showDownloadLinks(final View v) {

            CharSequence watchTypes[] = new CharSequence[data.get(getAdapterPosition()).getDownload().size()];
            for (int i = 0; i < data.get(getAdapterPosition()).getDownload().size(); i++) {
                watchTypes[i] = "سيرفر " + String.valueOf(i + 1);
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("اختر سيرفر التحميل");
            builder.setItems(watchTypes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (data.get(getAdapterPosition()).getDownload().get(which) != null)
                            v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(getAdapterPosition()).getDownload().get(which).getLink())));
                        else
                            Toast.makeText(v.getContext(), "لا يوجد رابط تحميل", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        }
    }
}
