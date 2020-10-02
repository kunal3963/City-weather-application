package kunal.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import kunal.weather.data.model.currentweather.CurrentWeatherModel;
import kunal.weather.data.model.currentweather.CurrentWeatherResponse;
import kunal.weather.data.model.sevendayweather.SevenDayResponse;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;


public final class Utils {

    private static final String TAG = "NetworkUtils";

    private Utils() {
        throw new UnsupportedOperationException("can't instantiate");
    }


    private static NetworkInfo getActiveNetworkInfo(final Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return null;
        return manager.getActiveNetworkInfo();
    }


    public static boolean isConnected(final Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    public static CurrentWeatherModel convertIntoCurrentWeatherModel(CurrentWeatherResponse response){
        CurrentWeatherModel model = new CurrentWeatherModel();
        model.setCity(response.getName());
        model.setTemp(Double.toString(response.getMain().getTemp()));
        model.setTempMax(Double.toString(response.getMain().getTempMax()));
        model.setTempMin(Double.toString(response.getMain().getTempMin()));
        model.setCountry(response.getSys().getCountry());
        model.setHumidity(Integer.toString(response.getMain().getHumidity()));
        model.setWindSpeed(Double.toString(response.getWind().getSpeed()));
        model.setMain(response.getWeather().get(0).getMain());
        model.setTimestamp(response.getDt());
        return model;
    }

    public static List<SevenDaysModel> convertIntoFiveDaysWeatherModel(SevenDayResponse repos, String city, String country){
        List<SevenDaysModel> modelList = new ArrayList<>();

        for(int i=0; i<repos.getCnt(); i++){
            SevenDaysModel model = new SevenDaysModel();
            model.setCity(city);
            model.setTemp(Double.toString(repos.getList().get(i).getMain().getTemp()));
            model.setTempMax(Double.toString(repos.getList().get(i).getMain().getTempMax()));
            model.setTempMin(Double.toString(repos.getList().get(i).getMain().getTempMin()));
            model.setCountry(country);
            model.setHumidity(Integer.toString(repos.getList().get(i).getMain().getHumidity()));
            model.setWindSpeed(Double.toString(repos.getList().get(i).getWind().getSpeed()));
            model.setMain(repos.getList().get(i).getWeather().get(0).getMain());
            model.setTimestamp(repos.getList().get(i).getDt());
            modelList.add(model);
        }
        return modelList;
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM, hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time* 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }
}
