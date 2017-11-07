package com.tangzi.base.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tangzi.base.utils.NetUtils;

/**
 * Created by liubin on 2017/10/11.
 */

public class NetworkReceiver extends BroadcastReceiver {
    private NetUtils.CallBack callBack;

    public NetworkReceiver(NetUtils.CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (callBack == null) {
                return;
            }
            if (NetUtils.isConnected(context)) {
                callBack.connectNetwork();
                if (NetUtils.isWifi(context)) {
                    callBack.connectWifi();
                }
            } else {
                callBack.disConnectNetwork();
            }
        }

    }
}
