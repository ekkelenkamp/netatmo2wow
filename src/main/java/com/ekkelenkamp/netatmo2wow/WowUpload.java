package com.ekkelenkamp.netatmo2wow;

import com.ekkelenkamp.netatmo2wow.model.Measures;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Documentation from WOW website:
 * <p/>
 * Mandatory Information:
 * All uploads must contain 4 pieces of mandatory information plus at least 1 piece of weather data.
 * Site ID - siteid:
 * The unique numeric id of the site
 * AWS Pin - siteAuthenticationKey:
 * A pin number, chosen by the user to authenticate with WOW.
 * Date - dateutc:
 * Each observation must have a date, in the date encoding specified below.
 * Software Type - softwaretype
 * The name of the software, to identify which piece of software and which version is uploading data
 * <p/>
 * http://wow.metoffice.gov.uk/automaticreading?siteid=123456&siteAuthenticationKey=654321&dateutc=2011-02-02+10%3A32%3A55&winddir=230&windspeedmph=12&windgustmph=12& windgustdir=25&humidity=90&dewptf=68.2&tempf=70&rainin=0&dailyrainin=5&baromin=29.1&soiltempf=25&soilmoisture=25&visibility=25&softwaretype=weathersoftware1.0
 */

public class WowUpload {

    public static final String WOW_URL = "http://wow.metoffice.gov.uk/automaticreading?";

    private static final Logger log = Logger.getLogger(WowUpload.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'%20'HH'%3A'mm'%3A'ss");

    private int connectionTimeout = 60000;
    private int readTimeout = 60000;

    private int awsPin = -1;
    private String softwareType = Info.SOFTWARE_NAME + " " + Info.SOFTWARE_VERSION;

    public void upload(List<Measures> measures, final String siteId, final int awsPin) throws Exception {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        int numberOfSuccesfulUploads = 0;
        for (Measures measure : measures) {
            HttpURLConnection connection = getHttpURLConnection(new URL(WOW_URL));
            try {
                setRequestParameters(connection, siteId, awsPin, softwareType, measure);
                log.debug(String.format("Start execution of WOW upload. URL=%s", connection.toString()));
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    log.debug(String.format("Successfully uploaded data for siteId %s.", siteId));
                    numberOfSuccesfulUploads++;
                } else {
                    log.warn(String.format("Invalid response code %d: %s.", responseCode));
                }
            } finally {
                connection.disconnect();
            }
        }
        log.info("Number of WOW measurements uploadedL " + numberOfSuccesfulUploads);
    }

    private static void setRequestParameters(HttpURLConnection connection, String siteId, int awsPin, String softwareType, Measures measure) throws IOException {

        StringBuilder requestBuilder = new StringBuilder(10);
        String urlString = connection.getURL().toString();
        if (!urlString.endsWith("?")) {
            requestBuilder.append('?');
        }
        requestBuilder.append("siteid=");
        requestBuilder.append(siteId);
        requestBuilder.append('&');
        requestBuilder.append("siteAuthenticationKey=");
        requestBuilder.append(URLEncoder.encode(Integer.toString(awsPin), "utf-8"));
        requestBuilder.append('&');
        requestBuilder.append("softwaretype=");
        requestBuilder.append(URLEncoder.encode(softwareType, "utf-8"));
        for (String parameter : measure.getWowParameters().keySet()) {
            requestBuilder.append('&');
            requestBuilder.append(parameter);
            requestBuilder.append('=');
            requestBuilder.append(URLEncoder.encode(measure.getWowParameters().get(parameter), "utf-8"));
        }
        String parameterString = requestBuilder.toString();
        log.debug(String.format("Executing URL command: %s%s", urlString, parameterString));
        OutputStream outputStream = null;
        try {
            outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(parameterString);
            bufferedWriter.flush();
        } finally {
            if (outputStream != null) outputStream.close();
        }
    }

    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        return connection;
    }
}
