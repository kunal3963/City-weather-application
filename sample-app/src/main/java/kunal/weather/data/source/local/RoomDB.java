package kunal.weather.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import kunal.weather.data.model.currentweather.CurrentWeatherModel;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;


@Database(entities = {CurrentWeatherModel.class, SevenDaysModel.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    public static final String DB_NAME = "weather.db";

    public abstract Dao dao();

    @android.arch.persistence.room.Dao
    interface Dao {
        @Insert
        void insertCurWeather(CurrentWeatherModel repos);

        @Insert
        void insertFiveDaysWeather(List<SevenDaysModel> repos);

        @Query("SELECT * FROM currentWeather WHERE city LIKE :city")
        Flowable<CurrentWeatherModel> queryCurrentWeather(String city);

        @Query("SELECT city FROM currentWeather")
        List<String> queryGetCities();

        @Query("SELECT * FROM currentWeather")
        Flowable<List<CurrentWeatherModel>> queryAllCitiesWeather();

        @Query("SELECT * FROM sevendaysweather WHERE city LIKE :city")
        Flowable<List<SevenDaysModel>> queryFiveDaysWeather(String city);

    }

}

