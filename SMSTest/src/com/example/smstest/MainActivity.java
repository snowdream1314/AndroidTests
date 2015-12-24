package com.example.smstest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView sender;
	private TextView content;
	private IntentFilter receiveFilter;
	private MessageReceiver messageReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sender = (TextView) findViewById(R.id.sender);
		content = (TextView) findViewById(R.id.content);
		receiveFilter = new IntentFilter();
		receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		receiveFilter.setPriority(100);//拦截系统短信
		messageReceiver = new MessageReceiver();
		registerReceiver(messageReceiver, receiveFilter);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(messageReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class MessageReceiver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent){
			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");//提取短信消息
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i=0; i<messages.length; i++){
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}
			String address =messages[0].getDisplayOriginatingAddress();//获取发送方号码
			String fullMessage = "";
			for (SmsMessage message : messages) {
				fullMessage += message.getMessageBody();//获取短信内容
			}
			sender.setText(address);
			content.setText(fullMessage);
			abortBroadcast();//拦截系统短信
		}
	}
}
