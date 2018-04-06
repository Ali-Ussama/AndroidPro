package com.example.aliosama.dramatranslation.Fetch;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.example.aliosama.dramatranslation.Activities.SerieActivity;
import com.example.aliosama.dramatranslation.model.ApiService;
import com.example.aliosama.dramatranslation.model.DownloadModel;
import com.example.aliosama.dramatranslation.model.EpisodeModel;
import com.example.aliosama.dramatranslation.model.RetroClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.aliosama.dramatranslation.Activities.SerieActivity.EpisodeindicatorViewBtm;
import static com.example.aliosama.dramatranslation.Activities.SerieActivity.EpisodeindicatorViewLLBottom;
import static com.example.aliosama.dramatranslation.Activities.SerieActivity.EpisodesData;
import static com.example.aliosama.dramatranslation.Activities.SerieActivity.EpisodesRecyclerView;
import static com.example.aliosama.dramatranslation.Activities.SerieActivity.mEpisodeRecAdapter;

/**
 * Created by aliosama on 8/21/2017.
 */

public class FetchEpisodes extends AsyncTask<String,Void,Void>{

    Context context;
    ArrayList<DownloadModel> AllDownloadLinks;
    ArrayList<DownloadModel> Downloads;
    ArrayList<EpisodeModel> data ;
    ArrayList<EpisodeModel> Response;
    ApiService api;
    Call<ArrayList<DownloadModel>> mCall1 ;
    Call<ArrayList<EpisodeModel>> call;

    public FetchEpisodes(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {


        try {
            EpisodesData.clear();
            data = new ArrayList<>();
            api = RetroClient.getApiService();
            String serieName = params[0];
            serieName += "-1x";

            for (int i = 1 ; ;i++) {
//              Read Episode
                String Url = "episodes?slug=\""+serieName+String.valueOf(i)+"\"";
                System.out.println(Url);
                try {
                    call = api.getEpisodes(Url);
                    Response = call.execute().body();
                    if (Response != null && Response.size() > 0) {
                        data.add(Response.get(0));
                    } else {
                        break;
                    }

                }catch (InterruptedIOException IOE){
                    if(isCancelled())
                        return null;
                } catch (Exception x){
                    x.printStackTrace();
                    break;
                }
            }
            System.out.println(data.size());
            try {
                   try {
                       //Get Download Links
                       for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {

                           //Read Download URL
                           AllDownloadLinks = new ArrayList<>();
                           Downloads = new ArrayList<>();
                           String DownloadSlug = data.get(dataIndex).getDtString();
                           String nextLink = "";
                           for(int next = 1 ; ; next++){
                               if(next > 1){
                                nextLink = "-"+String.valueOf(next);
                               }
                               String downloadUrl = "dt_links?slug=\"" +DownloadSlug+nextLink+"\"";
                               try {
                                   mCall1 = api.getDownloads(downloadUrl);
                                   Downloads = mCall1.execute().body();

                                   //If There is download link get it
                                   if(Downloads != null && Downloads.size() > 0){
                                       AllDownloadLinks.add(Downloads.get(0));
                                   }else {
                                       break;
                                   }
                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                           }
                           data.get(dataIndex).setDownload(AllDownloadLinks);
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                   }

                    EpisodesData.addAll(data);

            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            System.out.println("A7aaaa");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            System.out.println("Heerreeeeeeeeeeeeeee");
            mEpisodeRecAdapter.notifyDataSetChanged();
            EpisodeindicatorViewLLBottom.setVisibility(View.GONE);
            EpisodeindicatorViewBtm.setVisibility(View.GONE);
            EpisodesRecyclerView.setVisibility(View.VISIBLE);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
