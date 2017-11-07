package com.tangzi.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tangzi.base.activity.KeepLiveActivity;
import com.tangzi.base.service.KeepLiveService;
import com.tangzi.base.utils.KeepLiveManager;
import com.tangzi.base.utils.LogUtils;
import com.tangzi.base.utils.TActivityManager;

/**
 * Created by liubin on 2017/10/27.
 */

public abstract class RebootServiceReveiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i("RebootServiceReveiver action=" + intent.getAction());
        if (Intent.ACTION_REBOOT.equals(intent.getAction())) {
            initService(context);
        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            startKeepLiveActivity(context);
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            finishKeepLiveActivity();
        }
        rebootService(context);
    }

    private void finishKeepLiveActivity() {
        TActivityManager.getInstance().finishActivity(KeepLiveActivity.class);
    }

    private void startKeepLiveActivity(Context context) {
        Intent intent = new Intent(context, KeepLiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 初始化服务，监听到重启广播时调用
     *
     * @param context
     */
    protected abstract void initService(Context context);

    /**
     * 重启保活服务
     *
     * @param context
     */
    protected void rebootService(Context context) {
        if (!KeepLiveManager.isKeepLiveService(context, KeepLiveService.class.getName())) {
            KeepLiveManager.startKeepLiveService(null, KeepLiveService.class.getName());
        }
    }
}
