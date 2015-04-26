
package com.ekkelenkamp.netatmo2wow.model;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class Measures implements Comparable<Measures> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);

    static {
        otherSymbols.setDecimalSeparator('.');
    }

    long timestamp;
    Double temperature;
    Double humidity;
    Double rain;
    Double wind;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getWind() {
        return wind;
    }

    public void setWind(Double wind) {
        this.wind = wind;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public Map<String, String> getWowParameters() {
        Map<String, String> map = new HashMap<String, String>();
        // map netatmo domain to wow domain.
        // tempf
        // humidity
        // rain (mm)
        // windspeedmph

        if (getTemperature() != null) {
            String tempFarenheit = new DecimalFormat("0.##", otherSymbols).format(((getTemperature() * 9) / 5) + 32);
            map.put("tempf", "" + tempFarenheit); // convert from celcius to farenheit.
        }
        if (getHumidity() != null) {
            map.put("humidity", "" + getHumidity());
        }
        if (getRain() != null) {
            // rain is accumulative.
        }
        if (getWind() != null) {
            // windspeedmph convert to miles per hour.
            // mph = kph / 1.609
            String windspeedMph = new DecimalFormat("0.##", otherSymbols).format(getWind() / 1.609);
            map.put("windspeedmph", windspeedMph);
        }
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeText = dateFormat.format(new Date(timestamp));
        map.put("dateutc", timeText);

        return map;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "date=" + new java.util.Date(timestamp) +
                ", timestamp=" + timestamp +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }

    @Override
    public int compareTo(Measures o) {
        if (o.timestamp < this.timestamp) return 1;
        if (o.timestamp == this.timestamp) return 0;
        return -1;
    }
}
