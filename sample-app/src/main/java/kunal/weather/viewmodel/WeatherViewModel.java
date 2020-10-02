package kunal.weather.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import kunal.weather.data.model.currentweather.CurrentWeatherModel;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;
import kunal.weather.data.source.DataRepository;


public class WeatherViewModel extends AndroidViewModel {

    public WeatherViewModel(@NonNull Application application) {
        super(application);
    }

    public Flowable<CurrentWeatherModel> getCurrentWeather(String city){
        return DataRepository.INSTANT.queryCurWeather(city);
    }

    public Flowable<List<CurrentWeatherModel>> getCurrentWeather(){
        return DataRepository.INSTANT.queryAllCurCityWeather();
    }

    public List<String> getAllCities(){
        return DataRepository.INSTANT.queryGetAllCities();
    }

    public Flowable<List<SevenDaysModel>> getFiveDaysHourlyWeather(String city, String coutry){
        return DataRepository.INSTANT.queryFiveDaysWeather(city, coutry);
    }
}

