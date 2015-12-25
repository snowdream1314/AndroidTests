package com.example.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("MyServcice", "onCreate executed");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("MyServcice", "onStartCommand executed");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("MyServcice", "onDestroy executed");
	}
}
