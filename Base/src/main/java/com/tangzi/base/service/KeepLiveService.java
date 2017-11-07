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

public class KeepLiveService extends Service {
    private long time;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KeepLiveManager.startNeedKeepLiveServices();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        time = System.currentTimeMillis();
        LogUtils.i( "KeepLiveService onCreate time=" + BaseDateUtils.getCurrentDate());
        if (KeepLiveManager.debug) {
            KeepLiveManager.test(this);
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.i( "KeepLiveService onDestroy time=" + BaseDateUtils.getCurrentDate() + ",keepLiveTime【destroyTime-startTime=" + (System.currentTimeMillis() - time) / 1000 + "s");
        KeepLiveManager.startKeepLiveService(null, getClass().getName());
        super.onDestroy();
    }
}
