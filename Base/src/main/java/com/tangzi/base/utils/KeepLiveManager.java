package com.tangzi.base.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.tangzi.base.service.BaseService;
import com.tangzi.base.service.KeepLiveService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liubin on 2017/10/31.
 */

public class KeepLiveManager {
    public static boolean debug = true;
    public static final int KEEPLIVE_NOTIFICATION_ID = 32;
    private static Set<String> keepServices = new HashSet<>();
    private static Context mContext;

    public static void init(BaseService baseService) {
        keepServices.add(baseService.getClass().getName());
        if (mContext == null) {
            mContext = baseService.getApplicationContext();
            Intent keepIntent = new Intent(mContext, KeepLiveService.class);
            mContext.startService(keepIntent);
        }
    }

    public static void startServiceForegroud(Service service) {
        Notification notification = new Notification();
        if (notification == null) {
            return;
        }
        service.startForeground(KEEPLIVE_NOTIFICATION_ID, notification);
    }

    public static void startNeedKeepLiveServices() {
        if (mContext == null) {
            return;
        }
        for (String name : keepServices) {
            try {
                Class cls = mContext.getClassLoader().loadClass(name);
                Intent keepIntent = new Intent(mContext, cls);
                mContext.startService(keepIntent);
                mContext.bindService(keepIntent, new KeepServiceConnection(KeepLiveService.class.getName()), Context.BIND_AUTO_CREATE);
            } catch (Exception e) {
                LogUtils.e( "startNeedKeepLiveServices", e);
            }
        }
    }

    /**
     * @param selfName
     * @param destName
     */
    public static void startKeepLiveService(String selfName, String destName) {
        try {
            if (mContext == null) {
                return;
            }
            Class cls = mContext.getClassLoader().loadClass(destName);
            Intent keepIntent = new Intent(mContext, cls);
            mContext.startService(keepIntent);
            if (BaseStringUtils.isNotBlank(selfName)) {
                mContext.bindService(keepIntent, new KeepServiceConnection(selfName), Context.BIND_AUTO_CREATE);
            }
        } catch (Exception e) {
            LogUtils.e( "startKeepLiveService", e);
        }
    }

    public static boolean isKeepLiveService(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public static void test(final Service service) {
        final long testTime = System.currentTimeMillis();
        LogUtils.i( service.getClass().getName() + " test start time=" + BaseDateUtils.getCurrentDate());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        startServiceForegroud(service);
                        Thread.sleep(1000 * 60 * 10);
                        LogUtils.i( service.getClass().getName() + " test running time=" + BaseDateUtils.getCurrentDate() +
                                ",keepLiveTime【runningTime-testTime=" + (System.currentTimeMillis() - testTime) / 1000 + "s");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static class KeepServiceConnection implements ServiceConnection {
        private String className;//类名

        public KeepServiceConnection(String className) {
            this.className = className;
            if (!KeepLiveService.class.getName().equals(className)) {
                keepServices.add(className);
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i("KeepServiceConnection onServiceConnected,name=" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.i( "KeepServiceConnection onServiceDisconnected,name=" + name);
            if (!KeepLiveService.class.getName().equals(className)) {
                startNeedKeepLiveServices();
            } else {
                startKeepLiveService(className, KeepLiveService.class.getName());
            }

        }
    }

    ;

}
