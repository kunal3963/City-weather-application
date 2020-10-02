package kunal.weather.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import kunal.weather.R;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;
import kunal.weather.viewmodel.WeatherViewModel;

public class DetailWeatherActivity extends AppCompatActivity {

    private static final String TAG = "DetailWeatherActivity";

    private RecyclerView recyclerView;
    private SevenDaysWeatherAdapter sevenDaysWeatherAdapter;
    private List<SevenDaysModel> sevenDaysModelsList;
    private WeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String country = intent.getStringExtra("country");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(city+" "+country);
        initView();

        sevenDaysModelsList = new ArrayList<>();
        getSevenDaysWeather(city, country);
    }

    private void initView(){
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sevenDaysWeatherAdapter = new SevenDaysWeatherAdapter();
        recyclerView.setAdapter(sevenDaysWeatherAdapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void getSevenDaysWeather(String city, String country) {
        Observable.just(city)
                .flatMap(o -> viewModel.getFiveDaysHourlyWeather(city, country).toObservable())
                .subscribe(value -> {
                    Log.d(TAG, "return value:" + value.toString());
                    sevenDaysModelsList.removeAll(value);
                    sevenDaysModelsList.addAll(value);
                    sevenDaysWeatherAdapter.setFiveDaysWeatherList(getApplicationContext(), sevenDaysModelsList);
                    sevenDaysWeatherAdapter.notifyDataSetChanged();
                    Log.d(TAG, "return list size:" + sevenDaysModelsList.size());
                },  e->{
                    Toast.makeText(getApplicationContext(),"City not found",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
