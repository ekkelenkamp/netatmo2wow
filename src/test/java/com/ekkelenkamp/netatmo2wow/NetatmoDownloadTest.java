package com.ekkelenkamp.netatmo2wow;

import com.ekkelenkamp.netatmo2wow.model.Measures;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetatmoDownloadTest {

    @Test
    public void testGetMeasures() throws Exception {
        NetatmoHttpClient netatmoHttpClient = mock(NetatmoHttpClient.class);
        String json = "{\"status\":\"ok\",\"body\":{\"1430080093\":[9.6,82,null],\"1430080401\":[9.5,82,null],\"1430080709\":[9.4,83,null],\"1430081016\":[9.4,83,null],\"1430081323\":[9.4,83,null],\"1430081579\":[9.3,84,null],\"1430081887\":[9.3,84,null],\"1430082195\":[9.2,84,null],\"1430082502\":[9.2,84,null],\"1430082810\":[9.2,84,null],\"1430083117\":[9.2,84,null],\"1430083425\":[9.1,84,null]},\"time_exec\":0.0085320472717285,\"time_server\":1430083605}";

        when(netatmoHttpClient.post(any(URL.class), any(Map.class))).thenReturn(json);
        NetatmoDownload download = new NetatmoDownload(netatmoHttpClient);
        List<Measures> measures = download.getMeasures("token", "device", "module", "types", "scale", 3600);
        assertNotNull(measures);
        assertEquals(12, measures.size());
        Measures firstMeasure = measures.get(0);
        Measures lastMeasure = measures.get(11);
        assertEquals(82.0, firstMeasure.getHumidity(), 0.0);
        assertEquals(9.6, firstMeasure.getTemperature(), 0.0);
        assertEquals(84.0, lastMeasure.getHumidity(), 0.0);
        assertEquals(9.1, lastMeasure.getTemperature(), 0.0);
    }

}