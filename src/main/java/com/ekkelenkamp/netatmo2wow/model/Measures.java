
package com.ekkelenkamp.netatmo2wow.model;


public class Measures implements Comparable<Measures> {

    long beginTime;
    Double temperature;
    Double humidity;

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
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

    @Override
    public String toString() {
        return "Measure{" +
                "date=" + new java.util.Date(beginTime) +
                ", timestamp=" + beginTime +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }

    @Override
    public int compareTo(Measures o) {
        if (o.beginTime < this.beginTime) return 1;
        if (o.beginTime == this.beginTime) return 0;
        return -1;
    }
}
