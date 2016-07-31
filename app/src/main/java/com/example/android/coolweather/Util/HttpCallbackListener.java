package com.example.android.coolweather.Util;

/**
 * Created by Danny.W on 7/30/2016.
 */
public interface HttpCallbackListener {

    void onFinish(String responce);

    void onError(Exception e);
}
