package com.tt.t.tidytechtowns;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHTTPClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    private static String API_KEY = "&APPID=dd7d1ef2db1b92ecb2d1cadbf681df72";
    private static String FILE_EXT = ".png";


    public String getWeatherData(String location) {
        HttpURLConnection con = null ;
        InputStream stream = null;

        try {
            con = (HttpURLConnection) ( new URL(BASE_URL + location + API_KEY)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            stream = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            stream.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { stream.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

    public byte[] getImage(String code) {
        InputStream stream = null;
        try {
            URL url = new URL(IMG_URL + code + FILE_EXT);
            stream = (InputStream) url.getContent();
            int bytesRead;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            while ((bytesRead = stream.read(buffer)) != -1)
                output.write(buffer, 0, bytesRead);
            stream.close();
            return output.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { stream.close(); } catch(Throwable t) {}
        }

        return null;

    }
}
