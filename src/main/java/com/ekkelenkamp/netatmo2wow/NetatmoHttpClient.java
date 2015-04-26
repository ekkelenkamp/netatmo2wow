/* ================================================================
 * Delft FEWS 
 * ================================================================
 *
 * Project Info:  http://www.wldelft.nl/soft/fews/index.html
 * Project Lead:  Karel Heynert (karel.heynert@wldelft.nl)
 *
 * (C) Copyright 2003, by WL | Delft Hydraulics
 *                        P.O. Box 177
 *                        2600 MH  Delft
 *                        The Netherlands
 *                        http://www.wldelft.nl
 *
 * DELFT-FEWS is a sophisticated collection of modules designed 
 * for building a FEWS customised to the specific requirements 
 * of individual agencies. An open modelling approach allows users
 * to add their own modules in an efficient way.
 *
 * ----------------------------------------------------------------
 * INetatmoHttpClient.java
 * ----------------------------------------------------------------
 * (C) Copyright 2003, by WL | Delft Hydraulics
 *
 * Original Author:  rudie
 * Contributor(s):   
 *
 * Changes:
 * --------
 * 26-04-15 : Version 1 ();
 * 
 *
 */
package com.ekkelenkamp.netatmo2wow;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface NetatmoHttpClient {
    String post(URL url, Map<String, String> params) throws IOException, NoSuchAlgorithmException, KeyManagementException;
}
