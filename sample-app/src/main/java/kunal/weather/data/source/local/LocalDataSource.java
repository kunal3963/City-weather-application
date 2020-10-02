package kunal.weather.data.source.local;

import android.arch.persistence.room.Room;

import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.schedulers.Schedulers;
import kunal.weather.App;
import kunal.weather.data.model.currentweather.CurrentWeatherModel;
import kunal.weather.data.model.currentweather.CurrentWeatherResponse;
import kunal.weather.data.model.sevendayweather.SevenDayResponse;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;
import kunal.weather.data.source.DataSource;
import kunal.weather.utils.Utils;

public enum LocalDataSource implements DataSource {
    INSTANCE;

    private final RoomDB.Dao dao;

    LocalDataSource() {
        ObjectHelper.requireNonNull(App.INSTANCE, "LocalDataSource is not initialized");
        RoomDB db = Room.databaseBuilder(App.INSTANCE, RoomDB.class, RoomDB.DB_NAME).build();
        dao = db.dao();
    }

    @Override
    public Flowable<CurrentWeatherModel> queryCurWeather(String city){
        return dao.queryCurrentWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<CurrentWeatherModel>> queryAllCitiesWeather(){
        return dao.queryAllCitiesWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void insertCurWeather(CurrentWeatherResponse repos) {
        CurrentWeatherModel model = Utils.convertIntoCurrentWeatherModel(repos);
        Observable.just(model)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(dao::insertCurWeather);
    }

    public List<String> queryGetAllCitiesWeather(){
        Single<List<String>> observable = Single.fromCallable(() -> dao.queryGetCities())
                .subscribeOn(Schedulers.io());
        return observable.blockingGet();
    }

    @Override
    public Flowable<List<SevenDaysModel>> queryFiveDaysWeather(String city, String country){
        return dao.queryFiveDaysWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void insertFiveDaysWeather(SevenDayResponse repos, String city, String country) {
        Observable.just(Utils.convertIntoFiveDaysWeatherModel(repos, city, country))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(dao::insertFiveDaysWeather);
    }
}
