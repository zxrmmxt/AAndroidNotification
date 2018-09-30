package com.xt.m_notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by xuti on 2017/10/17.
 */

public class ClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent realIntent = intent.getParcelableExtra("realIntent");
        realIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(realIntent);
    }
}
