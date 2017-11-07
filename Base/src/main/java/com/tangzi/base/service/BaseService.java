package com.tangzi.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tangzi.base.utils.BaseDateUtils;
import com.tangzi.base.utils.KeepLiveManager;
import com.tangzi.base.utils.LogUtils;

/**
 * Created by liubin on 2017/10/26.
 */

public abstract class BaseService extends Service {

    private long time;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KeepLiveManager.startKeepLiveService(getClass().getName(), KeepLiveService.class.getName());
        LogUtils.i("BaseService onStartCommand time=" + BaseDateUtils.getCurrentDate());
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i( "BaseService onCreate time=" + BaseDateUtils.getCurrentDate());
        KeepLiveManager.init(this);
        time = System.currentTimeMillis();
        if (KeepLiveManager.debug) {
            KeepLiveManager.test(this);
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.i( "BaseService onDestroy time=" + BaseDateUtils.getCurrentDate() + ",keepLiveTime【destroyTime-startTime=" + (System.currentTimeMillis() - time) / 1000 + "s");
        KeepLiveManager.startKeepLiveService(null, getClass().getName());
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
