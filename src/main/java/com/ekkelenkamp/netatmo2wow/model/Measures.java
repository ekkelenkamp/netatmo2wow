
package com.ekkelenkamp.netatmo2wow.model;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class Measures implements Comparable<Measures> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    long timestamp;
    Double temperature;
    Double humidity;

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

    public Map<String, String> getWowParameters() {
        Map<String, String> map = new HashMap<String, String>();
        // map netatmo domain to wow domain.
        // tempf
        // humidity

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        String tempFarenheit = new DecimalFormat("0.##", otherSymbols).format(((getTemperature() * 9) / 5) + 32);
        map.put("tempf", "" + tempFarenheit); // convert from celcius to farenheit.
        map.put("humidity", "" + getHumidity());
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
