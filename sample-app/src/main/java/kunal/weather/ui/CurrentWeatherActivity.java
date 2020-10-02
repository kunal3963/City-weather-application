package kunal.weather.ui;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import kunal.weather.R;
import kunal.weather.data.model.currentweather.CurrentWeatherModel;
import kunal.weather.viewmodel.WeatherViewModel;

public class CurrentWeatherActivity extends AppCompatActivity implements CurrentWeatherAdapter.OnItemClickListener {

    private static final String TAG = "CurrentWeatherActivity";
    private SearchView searchView;
    private RecyclerView recyclerView;
    private CurrentWeatherAdapter currentWeatherAdapter;
    private List<CurrentWeatherModel> curWeatherList;
    private WeatherViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        curWeatherList = new ArrayList<>();
        disposable = new CompositeDisposable();
        getAllCitiesWeather();
    }

    public void initViews(){
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        currentWeatherAdapter = new CurrentWeatherAdapter(this);
        recyclerView.setAdapter(currentWeatherAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            getAllCities();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentWeatherAdapter.getFilter().filter(query);
                getCityWeather(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                currentWeatherAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    private void getCityWeather(String city) {
        Log.d(TAG, "getCityWeather called:"+city);
        disposable.add(Observable.just(city)
                .flatMap(o -> viewModel.getCurrentWeather(city).toObservable())
                .subscribe(value -> {
                    curWeatherList.remove(value);
                    curWeatherList.add(value);
                    currentWeatherAdapter.setCurWeatherList(getApplicationContext(), curWeatherList);
                    currentWeatherAdapter.notifyDataSetChanged();
                }, e->{
                    Toast.makeText(getApplicationContext(),city+" city not found",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }));
    }

    private void getAllCitiesWeather() {
        Log.d(TAG, "getAllCitiesWeather called:");
        Observable<List<CurrentWeatherModel>> list = viewModel.getCurrentWeather().toObservable();

        disposable.add(list.subscribe(value -> {
            for(CurrentWeatherModel model: value){
                curWeatherList.remove(model);
                curWeatherList.add(model);
            }
            currentWeatherAdapter.setCurWeatherList(getApplicationContext(),curWeatherList);
        }, e-> {
            Log.d(TAG, "No values in DB");
            e.printStackTrace();
        }));
    }

    private void getAllCities() {
        Log.d(TAG, "getAllCities called:");
        List<String> listCities = viewModel.getAllCities();

        Set<String> items = new HashSet<>();
        items.addAll(listCities);
        Log.d(TAG, "getAllCities size:"+items.size());
        for(String item: items){
            getCityWeather(item);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onItemClick(CurrentWeatherModel item) {
        Log.d(TAG, "Item Clicked:"+item.toString());
        Intent myIntent = new Intent(CurrentWeatherActivity.this, DetailWeatherActivity.class);
        myIntent.putExtra("city", item.getCity());
        myIntent.putExtra("country", item.getCountry());
        startActivity(myIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
