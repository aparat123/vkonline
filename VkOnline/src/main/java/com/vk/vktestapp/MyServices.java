package com.vk.vktestapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.*;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.*;
import android.content.*;
import android.app.*;

public class MyServices extends Service {

	final String LOG_TAG = "myLogs";

	MyBinder binder = new MyBinder();

	Timer timer;
	TimerTask tTask;
	long interval = 1000;
	boolean online;
	String UserName;

	public void onCreate(Intent intent) {
		super.onCreate();
		Log.d(LOG_TAG, "MyService onCreate");
		
	}
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "MyService onStartCommand");
		int UserId = intent.getIntExtra("UserID", 1);
		timer = new Timer();
		schedule(UserId);
		return Service.START_REDELIVER_INTENT;
	}
	public void onDestroy() {
		super.onDestroy();
		stopSelf();
		notifClose();
	}
	
	void schedule(final int UserId) {
		if (tTask != null) tTask.cancel();
		if (interval > 0) {
			tTask = new TimerTask() {
				public void run() {
					VKRequest req = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, UserId, VKApiConst.FIELDS, "online, online_mobile"));
					req.executeWithListener(new VKRequest.VKRequestListener() {
							@Override
							public void onComplete(VKResponse response) {
								final VKApiUser us = ((VKList<VKApiUserFull>)response.parsedModel).get(0);


								UserName = us.toString();

								if (us.online) {
									online = true;

									//Toast toastq = Toast.makeText(getApplicationContext(), "online", Toast.LENGTH_SHORT); toastq.show(); 

								}

								else{
									online = false;

									//Toast toastq = Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_SHORT); toastq.show(); 

								}
								VK(online);

							}


						});		
					}
			};
			timer.schedule(tTask, 1000, interval);
		}
	}

	

	public IBinder onBind(Intent arg0) {
		Log.d(LOG_TAG, "MyService onBind");
		return binder;
	}
	public void VK(boolean onl){
		if(onl==true) {
			notifOpen(UserName);
			//	new MyTask(time).execute();

		}
		else{
			notifClose();
			//	new MyTask(time).execute();
		}
		
	}

	class MyBinder extends Binder {
		MyServices getService() {
			return MyServices.this;
		}
	}
	public void notifClose(){
		Context context = getApplicationContext();
		NotificationManager notificationManager = (NotificationManager) context 
			.getSystemService(Context.NOTIFICATION_SERVICE); 
		int NOTIFY_ID = 101;
		notificationManager.cancel(NOTIFY_ID);
	}
	public void notifOpen(String name){

		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(context, MainActivity.class); 
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT); 

		Notification.Builder builder = new Notification.Builder(context); 
		builder.setContentIntent(contentIntent) 
			.setSmallIcon(R.drawable.ic_launcher)
			.setTicker("online")
			.setAutoCancel(true)
			.setContentTitle("Напоминание") 
			.setContentText(name + " в сети");
		Notification notification = builder.getNotification(); // до API 16 
		NotificationManager notificationManager = (NotificationManager) context 
			.getSystemService(Context.NOTIFICATION_SERVICE); 
		int NOTIFY_ID = 101;
		notificationManager.notify(NOTIFY_ID, notification);


	}
	
}
