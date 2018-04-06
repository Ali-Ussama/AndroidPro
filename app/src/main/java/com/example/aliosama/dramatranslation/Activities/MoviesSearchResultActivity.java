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
import android.widget.Toast;

import com.example.aliosama.dramatranslation.Adapters.MoviesRecAdapter;
import com.example.aliosama.dramatranslation.Adapters.MoviesSearchResultRecAdapter;
import com.example.aliosama.dramatranslation.Fetch.FetchMovieSearchResult;
import com.example.aliosama.dramatranslation.R;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MoviesSearchResultActivity extends AppCompatActivity {
    public static RelativeLayout moviesSearchResultRelativeLayout;
    public static RecyclerView moviesSearchResultRecyclerView;
    ArrayList<MovieModel> MoviesSearchResultData;
    ArrayList<MovieModel> AllMovies;
    public static LinearLayout moviesSearchResultindicatorViewLLCenter;
    public static MoviesSearchResultRecAdapter moviesSearchResultRecAdapter;
    public static AVLoadingIndicatorView moviesSearchResultindicatorView;
    public static MovieModel moviesSearchResultData ;
    public static TextView ResultNotFoundTV;
    Toolbar moviesSearchResultToolbar;
    String SearchString;
    Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_search_result);

        try {
            moviesSearchResultToolbar = (Toolbar) findViewById(R.id.activity_movies_search_result_toolbar);
            moviesSearchResultindicatorViewLLCenter = (LinearLayout) findViewById(R.id.activity_movies_search_result_indicatorViewLLcenter);
            moviesSearchResultindicatorView = (AVLoadingIndicatorView)  findViewById(R.id.activity_movies_search_result_Indicator);
            moviesSearchResultRecyclerView = (RecyclerView) findViewById(R.id.activity_movies_search_result_RecyclerView);
            ResultNotFoundTV = (TextView) findViewById(R.id.activity_movies_search_result_not_found_textView);
            moviesSearchResultRelativeLayout = (RelativeLayout) findViewById(R.id.activity_movies_search_result_RelativeLayout);
            moviesSearchResultData = new MovieModel();

            moviesSearchResultToolbar.setTitle("نتائج البحث");
            setSupportActionBar(moviesSearchResultToolbar);

            ActionBar ab = getSupportActionBar();
            if (ab != null){
                ab.setDisplayHomeAsUpEnabled(true);
            }

            AllMovies = new ArrayList<>();
            MoviesSearchResultData = new ArrayList<>();
            moviesSearchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            moviesSearchResultRecAdapter = new MoviesSearchResultRecAdapter(MoviesSearchResultData,getBaseContext());
            moviesSearchResultRecyclerView.setAdapter(moviesSearchResultRecAdapter);

            mIntent = getIntent();
            AllMovies = (ArrayList<MovieModel>) mIntent.getSerializableExtra("movies");
            SearchString = mIntent.getExtras().getString("Search String");
            SearchForMovie(SearchString);

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void SearchForMovie(String Searchkey){
        try {
            //            new FetchMovieSearchResult().execute(Searchkey);


            ResultNotFoundTV.setVisibility(View.GONE);
            moviesSearchResultRelativeLayout.setVisibility(View.VISIBLE);
            moviesSearchResultRecyclerView.setVisibility(View.GONE);
            moviesSearchResultindicatorView.smoothToShow();
            moviesSearchResultindicatorViewLLCenter.setVisibility(View.VISIBLE);

            boolean Found = false;
            MoviesSearchResultData.clear();
            for(MovieModel m: AllMovies){
                if(m.getTitle().getRendered().toLowerCase().contains(Searchkey.toLowerCase())){
                    MoviesSearchResultData.add(m);
                    Found = true;
                }
            }
            if(Found){
                ResultNotFoundTV.setVisibility(View.GONE);
                moviesSearchResultRelativeLayout.setVisibility(View.VISIBLE);
                moviesSearchResultRecAdapter.notifyDataSetChanged();
            }
            else {
                moviesSearchResultRelativeLayout.setVisibility(View.GONE);
                String s = "لم يتم العثور على ";
                Searchkey = s+" \" "+Searchkey+" \"";
                ResultNotFoundTV.setText(Searchkey);
                System.out.println(ResultNotFoundTV.getText().toString());
                ResultNotFoundTV.setVisibility(View.VISIBLE);
            }

            moviesSearchResultindicatorView.smoothToHide();
            moviesSearchResultindicatorViewLLCenter.setVisibility(View.GONE);
            moviesSearchResultRecyclerView.setVisibility(View.VISIBLE);
            moviesSearchResultRecAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_movies_search_result_menu, menu);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem SearchItem = menu.findItem(R.id.activity_movies_search_menu_search_item);

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
                SearchForMovie(query);
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
}
