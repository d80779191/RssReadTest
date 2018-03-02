package rssreadtest.com.rssreadtest.Module;

import java.io.Serializable;

public class RssReadDataItem implements Serializable {
    private long id;
    private String date;
    private String dayOrNight;
    private String temp;
    private String weather;

    public RssReadDataItem(){}

    public RssReadDataItem(long id, String date, String dayOrNight, String temp, String weather) {
        this.id = id;
        this.date = date;
        this.dayOrNight = dayOrNight;
        this.temp = temp;
        this.weather = weather;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setDate(String date) {this.date = date;}

    public String getDate() {return this.date;}

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
    }

    public String getDayOrNight() {
        return this.dayOrNight;
    }
    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return this.temp;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather() {
        return this.weather;
    }
}
