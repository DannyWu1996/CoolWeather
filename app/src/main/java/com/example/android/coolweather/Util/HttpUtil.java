package com.example.android.coolweather.Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Danny.W on 7/30/2016.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder responce = new StringBuilder();
                    String line;

                    while((line = reader.readLine()) != null){

                        responce.append(line);
                    }

                    if(listener != null){
                        listener.onFinish(responce.toString());
                    }


                }catch(Exception ex){

                    if(listener != null){
                        listener.onError(ex);
                    }
                }finally {

                    if(connection != null){

                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


}
