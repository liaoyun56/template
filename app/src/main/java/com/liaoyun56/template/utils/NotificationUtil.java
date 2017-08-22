package com.liaoyun56.template.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.liaoyun56.template.MainActivity;

public class NotificationUtil {

    /**
     * 通知
     * @param context
     * @param icon             左侧大图标
     * @param title            标题
     * @param content          内容
     * @param when              时间, 可以为Null
     */
    public static void postNotification(Context context, int icon, String title, String content, Long when){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent intent = new Intent(context, MainActivity.class);                    //跳转指定的页面
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(icon);                                                 //设置图标
        builder.setContentTitle(title);                                              //设置标题
        builder.setContentText(content);                                            //设置内容
        if(when != null){
            builder.setWhen(when);                                                  //设置通知时间
        }
        builder.setAutoCancel(true);                                                //自己维护通知的消失
        builder.setTicker("new message");                                           //第一次提示消失的时候显示在通知栏上的
        builder.setOngoing(true);
        builder.setNumber(20);
        Notification notification = builder.build();
        //notification.flags = Notification.FLAG_NO_CLEAR;                            //只有全部清除时，Notification才会清除
        notificationManager.notify(0,notification);
    }
}