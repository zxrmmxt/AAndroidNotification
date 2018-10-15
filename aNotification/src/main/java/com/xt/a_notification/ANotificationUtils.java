package com.xt.a_notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by xuti on 2017/9/28.
 */

public class ANotificationUtils {
    /**
     * Notification构造器
     */
    NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private Context context;
    private RemoteViews remoteViews;

    public ANotificationUtils(Context context, int iconRes) {
        this.context = context;
        initNotification(iconRes);
    }

    private void initNotification(int iconRes) {
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(context);

        remoteViews = new RemoteViews("steelmate.com.iot_hardware", R.layout.my_notification);
        mBuilder.setSmallIcon(iconRes)//设置通知小ICON
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))//设置大图标
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置点击意图
                //  .setNumber(number) //设置数字 右下角
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
                .setAutoCancel(true)//设置点击 自动消失
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
//                .setContent(remoteViews);
    }

    /**
     * 显示通知栏点击跳转到指定Activity
     */
    public void showIntentActivityNotify(String title, String content, String ticker, int iconRes, Intent intent) {
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
//		notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        remoteViews.setTextViewText(R.id.myNoftificationTv, title);
        mBuilder.setContentTitle(title)//设置标题
                .setContentText(content)//设置通内容
                .setTicker(ticker);//通知首次出现在通知栏，带上升动画效果的
        //点击的意图ACTION是跳转到Intent
        intent.setData(Uri.parse("custom://" + System.currentTimeMillis()));//解决了intent传入不同的值，但是收到的都是同一个值
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, new Random().nextInt(100), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
//        mBuilder.setFullScreenIntent(pendingIntent, false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            android5.0加入了一种新的模式Notification的显示等级，共有三种：
//            VISIBILITY_PUBLIC只有在没有锁屏时会显示通知
//            VISIBILITY_PRIVATE任何情况都会显示通知
//            VISIBILITY_SECRET在安全锁和没有锁屏的情况下显示通知
            mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setPriority(Notification.PRIORITY_MAX)//设置该通知优先级
                    .setCategory(Notification.CATEGORY_MESSAGE)//设置通知类别
//.setColor(context.getResources().getColor(R.color.small_icon_bg_color))//设置smallIcon的背景色
//                    .setFullScreenIntent(pendingIntent, true)//将Notification变为悬挂式Notification,小米手机上未点击就跳到目标activity了
                    .setSmallIcon(iconRes);//设置小图标
        } else {
            mBuilder.setSmallIcon(iconRes);//设置小图标
        }

        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

    private PendingIntent getDefalutIntent(int flags) {
        return PendingIntent.getActivity(context, 1, new Intent(), flags);
    }
}
