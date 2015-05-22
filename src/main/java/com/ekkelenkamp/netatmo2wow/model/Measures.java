
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

    Long timestamp;
    Double temperature;
    Double humidity;
    Double rain;
    Double rainLastHour;
    Double wind;
    Double pressure;


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
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

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getRainLastHour() {
        return rainLastHour;
    }

    public void setRainLastHour(Double rainLastHour) {
        this.rainLastHour = rainLastHour;
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
        if (getPressure() != null) {
            // see: http://weatherfaqs.org.uk/node/72
            // convert from mbar to inches
            String pressureMillibars = new DecimalFormat("0.##", otherSymbols).format(getPressure() * 0.02953);
            // todo. Activiate once working.
            map.put("baromin", pressureMillibars);
        }
        if (getRainLastHour() != null) {
            // rain is accumulative.
            String rain = new DecimalFormat("0.##", otherSymbols).format(getRainLastHour());
            map.put("rainin", rain);   // accumulated rainfall in the last hour.
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

    /**
     * Merge 2 measures
     *
     *
     * @param measure
     */
    public void merge(Measures measure) {
        if (measure.getTimestamp() > this.getTimestamp()) {
            // the new values prevail. Overwrite if not null
            if (measure.getHumidity() != null) {
                humidity = measure.getHumidity();
            }
            if (measure.getPressure() != null) {
                pressure = measure.getPressure();
            }
            if (measure.getRain() != null) {
                rain = measure.getRain();
            }
            if (measure.getTemperature() != null) {
                temperature = measure.getTemperature();
            }
            if (measure.getWind() != null) {
                wind = measure.getWind();
            }

        } else {
            // the current value prevail. Only overrule if not set.
            if (getHumidity() == null) {
                humidity = measure.getHumidity();
            }
            if (getPressure() == null) {
                pressure = measure.getPressure();
            }
            if (getRain() == null) {
                rain = measure.getRain();
            }
            if (getTemperature() == null) {
                temperature = measure.getTemperature();
            }
            if (getWind() == null) {
                wind = measure.getWind();
            }
        }
    }

    @Override
    public String toString() {
        return "Measure{" +
                "date=" + new java.util.Date(timestamp) +
                ", timestamp=" + timestamp +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                '}';
    }

    @Override
    public int compareTo(Measures o) {
        if (o.timestamp < this.timestamp) return 1;
        if (o.timestamp == this.timestamp) return 0;
        return -1;
    }
}
