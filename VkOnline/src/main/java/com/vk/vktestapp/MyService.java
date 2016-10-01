package com.vk.vktestapp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.os.*;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.*;
import android.content.*;
import android.app.*;
import android.widget.*;

public class MyService extends Service {

	final String LOG_TAG = "myLogs";
	ExecutorService es;
	Object someRes;
	int time;

	

	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "MyService onCreate");
		es = Executors.newFixedThreadPool(1);
		someRes = new Object();
	}

	public void onDestroy() {
		super.onDestroy();
		Context context = getApplicationContext();
		NotificationManager notificationManager = (NotificationManager) context 
			.getSystemService(Context.NOTIFICATION_SERVICE); 

		int NOTIFY_ID = 101;
		notificationManager.cancel(NOTIFY_ID);
		Log.d(LOG_TAG, "MyService onDestroy");
		someRes = null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "MyService onStartCommand");
		int time = intent.getIntExtra("time", 1);
        
		MyTask mt = new MyTask(time);
		mt.execute();
		return super.onStartCommand(intent, flags, startId);
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

	class MyTask extends AsyncTask<Void, Void, Void> {
		boolean online;

		private int time;
		public MyTask(int time) {
			this.time = time;
			
			
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();	
			//tvInfo.setText("Begin");
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			//

			try {
				VKRequest req = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, time ,VKApiConst.FIELDS, "online, online_mobile"));
				req.executeWithListener(new VKRequest.VKRequestListener() {
						@Override
						public void onComplete(VKResponse response) {
							final VKApiUser us = ((VKList<VKApiUserFull>)response.parsedModel).get(0);


							if (us.online) {
								online = true;
								main(online);
								//Toast toastq = Toast.makeText(getApplicationContext(), "online", Toast.LENGTH_SHORT); toastq.show(); 

							}

							else{
								online = false;
								main(online);
								//Toast toastq = Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_SHORT); toastq.show(); 

							}
						}


					});		
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			new MyTask(time).execute();

		}
	}
public void main(boolean onlines){
	if(onlines==false){

		Context context = getApplicationContext();
		NotificationManager notificationManager = (NotificationManager) context 
			.getSystemService(Context.NOTIFICATION_SERVICE); 

		int NOTIFY_ID = 101;
		notificationManager.cancel(NOTIFY_ID);
	}
	else if(onlines==true) {

		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(context, MainActivity.class); 
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT); 

		Notification.Builder builder = new Notification.Builder(context); 
		builder.setContentIntent(contentIntent) .setSmallIcon(R.drawable.ic_launcher) // большая картинка
			// .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.hungrycat)) //
			.setTicker("online") // текст в строке состояния 
			//.setTicker("Последнее китайское предупреждение!") .setWhen(System.currentTimeMillis()) 
			.setAutoCancel(true) //
			// .setContentTitle(res.getString(R.string.notifytitle)) 	 // Заголовок уведомления 
			.setContentTitle("Напоминание") // .setContentText(res.getString(R.string.notifytext)) 
			.setContentText("Пользователь online"); 
		// Текст уведомления //
		Notification notification = builder.getNotification(); // до API 16
		//Notification notification = builder.build(); 
		NotificationManager notificationManager = (NotificationManager) context 
			.getSystemService(Context.NOTIFICATION_SERVICE); 

		int NOTIFY_ID = 101;
		notificationManager.notify(NOTIFY_ID, notification);



	}
}
	

	
	
}
