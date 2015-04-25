package com.ekkelenkamp.netatmo2wow;

import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;


public class NetatmoHttpClient {

    final static Logger logger = Logger.getLogger(NetatmoHttpClient.class);

    final static String USER_AGENT = "Java Netatmo Importer";
    private void NetatmoHttpClient(){};

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String post(URL url, final Map<String, String> params) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        // The netatmo ssl keys are not working without it.
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        URL URL_OBJECT = url;
        final HttpURLConnection connection = (HttpURLConnection) URL_OBJECT.openConnection();
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection connection_https = (HttpsURLConnection) connection;
            connection_https.setSSLSocketFactory(sslSocketFactory);

        }
        connection.setDefaultUseCaches(false);
        connection.setUseCaches(false);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        applyParams(connection, params);
        try {
            final int http_code = connection.getResponseCode();
            try {
                if (http_code == 200) { /* good code */
                    String response = readStream(connection.getInputStream());
                    connection.disconnect();
                    return response;
                } else { /* error code*/
                    String response = readStream(connection.getErrorStream());
                    connection.disconnect();
                    return response;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readStream(InputStream in) {
        final String M = "readStream: ";
        String rv = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) sb.append(line);
            rv = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return rv;
    }

    private static boolean applyParams(HttpURLConnection connection, Map<String, String> params_hash) {
        try {
            String params = createParamsLine(params_hash);
            logger.debug("parameters: " + params);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(params);
            writer.close();
            os.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String createParamsLine(Map<String, String> p) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        if (p.size() > 0) for (Map.Entry<String, String> pair : p.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(pair.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
