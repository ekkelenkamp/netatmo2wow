package com.ekkelenkamp.netatmo2wow;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface NetatmoHttpClient {
    String post(URL url, Map<String, String> params) throws IOException, NoSuchAlgorithmException, KeyManagementException;
}
