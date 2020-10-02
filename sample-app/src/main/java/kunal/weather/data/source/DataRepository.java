package kunal.weather.data.source;

import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import kunal.weather.App;
import kunal.weather.data.model.currentweather.CurrentWeatherModel;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;
import kunal.weather.data.source.local.LocalDataSource;
import kunal.weather.data.source.remote.RemoteDataSource;
import kunal.weather.utils.Utils;


public enum DataRepository implements DataSource {
    INSTANT;

    @Override
    public Flowable<CurrentWeatherModel> queryCurWeather(String city) {
        if (Utils.isConnected(App.INSTANCE))
            return RemoteDataSource.INSTANT.queryCurWeather(city);
        else {
            return LocalDataSource.INSTANCE.queryCurWeather(city);
        }
    }

    public Flowable<List<CurrentWeatherModel>> queryAllCurCityWeather() {
        return LocalDataSource.INSTANCE.queryAllCitiesWeather();
    }

    public List<String> queryGetAllCities() {
        return LocalDataSource.INSTANCE.queryGetAllCitiesWeather();
    }

    @Override
    public Flowable<List<SevenDaysModel>> queryFiveDaysWeather(String city, String country) {
        if (Utils.isConnected(App.INSTANCE))
            return RemoteDataSource.INSTANT.queryFiveDaysWeather(city, country);
        else {
            return LocalDataSource.INSTANCE.queryFiveDaysWeather(city, country);
        }
    }

}
