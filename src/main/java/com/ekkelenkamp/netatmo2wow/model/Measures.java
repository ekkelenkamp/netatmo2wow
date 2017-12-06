
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
    Double windStrength;
    Double windAngle;
    Double windgustStrength;
    Double windgustAngle;
    Double pressure;
    Double rainAccum;

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

    public Double getWindStrength() 
    {
    	if (windStrength != null)
    	{
    		return windStrength;
    	}
    	// insert correction factor calculated based on experiments with a handheld anometer. 
    	return windStrength;
    }
    
    public Double getWindAngle() {
    	return windAngle;
    }
    
    public Double getWindGustStrength() 
    {
    	if (windgustStrength != null)
    	{
    		return windgustStrength;
    	}
    	// insert correction factor calculated based on experiments with a handheld anometer. 
    	return windgustStrength;
    }

    public Double getWindGustAngle() {
    	return windgustAngle;
    }
    
    public void setWind(Double windStrength, Double windAngle, Double gustStrength, Double gustAngle) {
        this.windStrength = windStrength;
        this.windAngle = windAngle;
        this.windgustStrength = gustStrength;
        this.windgustAngle = gustAngle;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public void setRainAccumulated(Double rainAccum)
    {
    	this.rainAccum = rainAccum;
    }

    public Double getRainAccumulated()
    {
    	return rainAccum;
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
        if (getRainAccumulated() != null)
        {
        	String rain = new DecimalFormat("0.##", otherSymbols).format(getRainAccumulated() * 0.03937007874015748);
            map.put("dailyrainin", rain);   // accumulated rainfall in the last day.
        }
        if (getRainLastHour() != null) {
            // rain is accumulative.
            // convert from mm to inches.
            String rain = new DecimalFormat("0.##", otherSymbols).format(getRainLastHour() * 0.03937007874015748);
            map.put("rainin", rain);   // accumulated rainfall in the last hour.
        }
        if (getWindStrength() != null) {
            // windspeedmph convert to miles per hour.
            // mph = kph / 1.609
            String windspeedMph = new DecimalFormat("0.##", otherSymbols).format(getWindStrength() / 1.609);
            map.put("windspeedmph", windspeedMph);
        }
        if (getWindAngle() != null) {
        	String windAngle = new DecimalFormat("0.##", otherSymbols).format(getWindAngle());
        	map.put("winddir", windAngle);
        }
        if (getWindGustStrength() != null) {
            // windspeedmph convert to miles per hour.
            // mph = kph / 1.609
            String windspeedMph = new DecimalFormat("0.##", otherSymbols).format(getWindGustStrength() / 1.609);
            map.put("windgustmph", windspeedMph);
        }
        if (getWindGustAngle() != null) {
        	String windAngle = new DecimalFormat("0.##", otherSymbols).format(getWindGustAngle());
        	map.put("windgustdir", windAngle);
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
            if (measure.getWindStrength() != null) {
                windStrength = measure.getWindStrength();
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
            if (getWindStrength() == null) {
                windStrength = measure.getWindStrength();
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
                ", rainfall=" + rain +
                ", rainLastHour=" + rainLastHour +
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
