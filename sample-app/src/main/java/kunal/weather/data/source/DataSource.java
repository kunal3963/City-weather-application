package kunal.weather.data.source;

import java.util.List;

import io.reactivex.Flowable;
import kunal.weather.data.model.currentweather.CurrentWeatherModel;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;


public interface DataSource {
    Flowable<CurrentWeatherModel> queryCurWeather(String city);
    Flowable<List<SevenDaysModel>> queryFiveDaysWeather(String city, String country);
}
