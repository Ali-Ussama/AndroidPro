package com.example.aliosama.dramatranslation.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aliosama.dramatranslation.Adapters.MoviesSearchResultRecAdapter;
import com.example.aliosama.dramatranslation.Adapters.SeriesSearchResultRecAdapter;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.SerieModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class SeriesSearchResultActivity extends AppCompatActivity {

    public static RelativeLayout seriesSearchResultRelativeLayout;
    public static RecyclerView seriesSearchResultRecyclerView;
    ArrayList<SerieModel> SeriesSearchResultData;
    ArrayList<SerieModel> AllSeries;
    public static LinearLayout seriesSearchResultindicatorViewLLCenter;
    public static SeriesSearchResultRecAdapter seriesSearchResultRecAdapter;
    public static AVLoadingIndicatorView seriesSearchResultindicatorView;
    public static SerieModel seriesSearchResultData ;
    public static TextView ResultNotFoundTV;
    Toolbar seriesSearchResultToolbar;
    String SearchString;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_search_result);


        try {
            seriesSearchResultToolbar = (Toolbar) findViewById(R.id.activity_series_search_result_toolbar);
            seriesSearchResultindicatorViewLLCenter = (LinearLayout) findViewById(R.id.activity_series_search_result_indicatorViewLLcenter);
            seriesSearchResultindicatorView = (AVLoadingIndicatorView)  findViewById(R.id.activity_series_search_result_Indicator);
            seriesSearchResultRecyclerView = (RecyclerView) findViewById(R.id.activity_series_search_result_RecyclerView);
            ResultNotFoundTV = (TextView) findViewById(R.id.activity_series_search_result_not_found_textView);
            seriesSearchResultRelativeLayout = (RelativeLayout) findViewById(R.id.activity_series_search_result_RelativeLayout);
            seriesSearchResultData = new SerieModel();

            seriesSearchResultToolbar.setTitle("نتائج البحث");
            setSupportActionBar(seriesSearchResultToolbar);

            ActionBar ab = getSupportActionBar();
            if (ab != null){
                ab.setDisplayHomeAsUpEnabled(true);
            }

            AllSeries = new ArrayList<>();
            SeriesSearchResultData = new ArrayList<>();
            seriesSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            seriesSearchResultRecAdapter = new SeriesSearchResultRecAdapter(SeriesSearchResultData,getBaseContext());
            seriesSearchResultRecyclerView.setAdapter(seriesSearchResultRecAdapter);

            mIntent = getIntent();
            AllSeries = (ArrayList<SerieModel>) mIntent.getSerializableExtra("series");
            SearchString = mIntent.getExtras().getString("Search String");
            SearchForSerie(SearchString);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void SearchForSerie(String Searchkey){
        try {
            ResultNotFoundTV.setVisibility(View.GONE);
            seriesSearchResultRelativeLayout.setVisibility(View.VISIBLE);
            seriesSearchResultRecyclerView.setVisibility(View.GONE);
            seriesSearchResultindicatorView.smoothToShow();
            seriesSearchResultindicatorViewLLCenter.setVisibility(View.VISIBLE);

            boolean Found = false;
            SeriesSearchResultData.clear();
            for(SerieModel m: AllSeries){
                if(m.getTitle().getRendered().toLowerCase().contains(Searchkey.toLowerCase())){
                    SeriesSearchResultData.add(m);
                    Found = true;
                }
            }
            if(Found){
                ResultNotFoundTV.setVisibility(View.GONE);
                seriesSearchResultRelativeLayout.setVisibility(View.VISIBLE);
                seriesSearchResultRecAdapter.notifyDataSetChanged();
            }
            else {
                seriesSearchResultRelativeLayout.setVisibility(View.GONE);
                String s = "لم يتم العثور على ";
                Searchkey = s+" \" "+Searchkey+" \"";
                ResultNotFoundTV.setText(Searchkey);
                System.out.println(ResultNotFoundTV.getText().toString());
                ResultNotFoundTV.setVisibility(View.VISIBLE);
            }

            seriesSearchResultindicatorView.smoothToHide();
            seriesSearchResultindicatorViewLLCenter.setVisibility(View.GONE);
            seriesSearchResultRecyclerView.setVisibility(View.VISIBLE);
            seriesSearchResultRecAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_series_search_result_menu, menu);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem SearchItem = menu.findItem(R.id.activity_series_search_menu_search_item);

        SearchView searchView = (SearchView)  MenuItemCompat.getActionView(SearchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setActivated(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase();
                SearchForSerie(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        seriesSearchResultRecAdapter.notifyDataSetChanged();
    }
}
