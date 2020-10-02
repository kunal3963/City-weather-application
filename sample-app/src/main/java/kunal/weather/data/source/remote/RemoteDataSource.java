package kunal.weather.data.source.remote;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kunal.weather.data.model.currentweather.CurrentWeatherModel;
import kunal.weather.data.model.currentweather.CurrentWeatherResponse;
import kunal.weather.data.model.sevendayweather.SevenDayResponse;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;
import kunal.weather.data.source.DataSource;
import kunal.weather.data.source.local.LocalDataSource;
import kunal.weather.utils.Constants;
import kunal.weather.utils.Utils;

public enum RemoteDataSource implements DataSource {
    INSTANT;

    private final RestService.Service service;

    RemoteDataSource() {
        service = RestService.Client.INSTANT.obtain().create(RestService.Service.class);
    }

    @Override
    public Flowable<CurrentWeatherModel> queryCurWeather(String cityName) {
        Flowable<CurrentWeatherResponse> responseFlowable= service.getCurrentWeather(
                cityName, Constants.apiKey)
                .doOnNext(LocalDataSource.INSTANCE::insertCurWeather)
                .doOnError(throwable -> LocalDataSource.INSTANCE.queryCurWeather(cityName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return responseFlowable.flatMap(o -> Flowable.just(Utils.convertIntoCurrentWeatherModel(o)));
    }

    @Override
    public Flowable<List<SevenDaysModel>> queryFiveDaysWeather(String cityName, String country) {
        Flowable<SevenDayResponse> responseFlowable = service.getFiveDaysWeather(
                cityName, Constants.apiKey)
                .doOnNext(result -> {
                    LocalDataSource.INSTANCE.insertFiveDaysWeather(result, cityName, country);
                })
                .doOnError(throwable -> LocalDataSource.INSTANCE.queryFiveDaysWeather(cityName, country))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return responseFlowable.flatMap(o -> Flowable.just(Utils.convertIntoFiveDaysWeatherModel(o,cityName,country)));
    }

}
