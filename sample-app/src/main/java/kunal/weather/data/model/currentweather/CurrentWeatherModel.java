package kunal.weather.data.model.currentweather;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "currentWeather")
public class CurrentWeatherModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "main")
    private String main;

    @ColumnInfo(name = "temp")
    private String temp;

    @ColumnInfo(name = "tempMin")
    private String tempMin;

    @ColumnInfo(name = "tempMax")
    private String tempMax;

    @ColumnInfo(name = "humidity")
    private String humidity;

    @ColumnInfo(name = "windSpeed")
    private String windSpeed;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "date")
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getMain() {
        return main;
    }

    public String getTemp() {
        return temp;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "CurrentWeatherModel{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", main='" + main + '\'' +
                ", temp='" + temp + '\'' +
                ", tempMin='" + tempMin + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", country=" + country +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentWeatherModel that = (CurrentWeatherModel) o;
        return city.equalsIgnoreCase(that.city);
    }

}
