package com.ekkelenkamp.netatmo2wow;

import com.ekkelenkamp.netatmo2wow.model.Measures;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class NetatmoDownload {

    final static Logger logger = Logger.getLogger(NetatmoDownload.class);

    // API URLs that will be used for requests, see: http://dev.netatmo.com/doc/restapi.
    protected final String URL_BASE = "https://api.netatmo.net";
    protected final String URL_REQUEST_TOKEN = URL_BASE + "/oauth2/token";
    protected final String URL_GET_DEVICES_LIST = URL_BASE + "/api/devicelist";
    protected final String URL_GET_MEASURES_LIST = URL_BASE + "/api/getmeasure";


    public List<Measures> downloadCsvData(String username, String password, String clientId, String clientSecret, String timespan) throws IOException {
        String url = URL_REQUEST_TOKEN;
        String token = login(username, password, clientId, clientSecret);
        logger.debug("Token: " + token);
        String measureTypes = "Temperature,Humidity";
        List<String> devices = getDevices(token);
        List<Measures> measures = new ArrayList<Measures>();
        for (String device: devices) {
            logger.debug("Device: " + device);
            String scale = "max";
            long timePeriod = Long.parseLong(timespan);
            // netatmo calcuates in seconds, not milliseconds.
            long currentDate = ((new java.util.Date().getTime()) / 1000) - timePeriod;
            logger.debug("start time: " + new Date(currentDate * 1000));
            logger.debug("start time seconds: " + currentDate);

            measures.addAll(getMeasures(token, device, measureTypes, scale, currentDate));
        }
        return measures;

    }

    public List<Measures> getMeasures(String token, String device, String measureTypes, String scale, long dateBegin) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", token);
        params.put("device_id", device);
        params.put("type", measureTypes);
        params.put("scale", scale);
        params.put("date_begin", "" + dateBegin);
        params.put("optimize", "false"); // easy parsing.

        List<Measures> measuresList = new ArrayList<Measures>();
        try {
            JSONParser parser = new JSONParser();
            String result = NetatmoHttpClient.post(new URL(URL_GET_MEASURES_LIST), params);
            Object obj = parser.parse(result);
            JSONObject jsonResult = (JSONObject) obj;
            if (!(jsonResult.get("body") instanceof JSONObject)) {
                logger.info("No data found");
                return measuresList;
            }
            JSONObject body = (JSONObject) jsonResult.get("body");

            for (Object o: body.keySet()) {
                String timeStamp = (String) o;
                JSONArray valuesArray = (JSONArray) body.get(timeStamp);
                Measures measures = new Measures();
                measures.setBeginTime(Long.parseLong(timeStamp) * 1000);
                measures.setTemperature(Double.parseDouble("" + valuesArray.get(0)));
                measures.setHumidity(Double.parseDouble("" + valuesArray.get(1)));
                measuresList.add(measures);
            }
            Collections.sort(measuresList);
            return measuresList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getDevices(String token) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token",token);
        List<String> devicesList = new ArrayList<String>();
        try {
            JSONParser parser = new JSONParser();
            String result = NetatmoHttpClient.post(new URL(URL_GET_DEVICES_LIST), params);
            Object obj = parser.parse(result);
            JSONObject jsonResult = (JSONObject) obj;
            JSONObject body =  (JSONObject) jsonResult.get("body");
            JSONArray devices = (JSONArray) body.get("devices");
            for (int i=0; i < devices.size(); i++) {
                JSONObject device = (JSONObject) devices.get(i);
                devicesList.add((String) device.get("_id"));
            }
            return devicesList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * This is the first request you have to do before being able to use the API.
     * It allows you to retrieve an access token in one step,
     * using your application's credentials and the user's credentials.
     */
    public String login(String email, String password, String clientId, String clientSecret) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("username", email);
        params.put("password", password);

        try {
            JSONParser parser = new JSONParser();

            String result = NetatmoHttpClient.post(new URL(URL_REQUEST_TOKEN), params);
            Object obj = parser.parse(result);
            JSONObject jsonResult = (JSONObject) obj;
            String token = (String) jsonResult.get("access_token");
            return token;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
