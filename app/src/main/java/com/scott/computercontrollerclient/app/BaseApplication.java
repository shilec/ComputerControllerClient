package com.scott.computercontrollerclient.app;

import android.app.Application;
import android.content.Intent;

import com.scott.computercontrollerclient.service.CommunicationSerivce;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        startService(new Intent(this, CommunicationSerivce.class));
        super.onCreate();
    }
}
