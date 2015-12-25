package com.example.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {
	
	public MyIntentService() {
		super("MyIntentService");//���ø�����вι��캯��
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		//��ӡ��ǰ�߳�Id
		Log.d("MyIntentService", "Thread id is " + Thread.currentThread().getId());
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("MyIntentService", "onDestroy executed");
	}
}
