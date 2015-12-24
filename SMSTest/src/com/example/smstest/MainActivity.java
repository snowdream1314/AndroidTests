package com.example.smstest;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView sender;
	private TextView content;
	private TextView to;
	private EditText msgInput;
	private Button send;
	private String msgContent;
	private String msgNumber;
	
	private IntentFilter receiveFilter;
	private MessageReceiver messageReceiver;
	
	private IntentFilter sendFilter;
	private SendStatusReceiver sendStatusReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sender = (TextView) findViewById(R.id.sender);
		content = (TextView) findViewById(R.id.content);
		receiveFilter = new IntentFilter();
		receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		//receiveFilter.setPriority(100);//����ϵͳ����
		messageReceiver = new MessageReceiver();
		registerReceiver(messageReceiver, receiveFilter);
		
		//���Ͷ���
		to = (TextView) findViewById(R.id.to);
		msgInput = (EditText) findViewById (R.id.msg_input);
		sendFilter = new IntentFilter();
		sendFilter.addAction("SENT_SMS_ACTION");
		sendStatusReceiver = new SendStatusReceiver();
		registerReceiver(sendStatusReceiver, sendFilter);
		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				msgContent = msgInput.getText().toString();
				msgNumber = to.getText().toString();
				SmsManager smsManager = SmsManager.getDefault();
				Intent sendIntent = new Intent("SENT_SMS_ACTION");//�������ŷ���״̬
				PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, sendIntent, 0);
				//���ų��ȳ���70���ֶ�η���
				if (msgContent.length() > 70){
					ArrayList<String> contents = smsManager.divideMessage(msgContent);
					smsManager.sendMultipartTextMessage(msgNumber, null, contents, null, null);
				}else{
					smsManager.sendTextMessage(msgNumber, null, msgContent, pi, null);
				}
			}
					
		});
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(messageReceiver);
		unregisterReceiver(sendStatusReceiver);
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
			Object[] pdus = (Object[]) bundle.get("pdus");//��ȡ������Ϣ
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i=0; i<messages.length; i++){
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}
			String address =messages[0].getDisplayOriginatingAddress();//��ȡ���ͷ�����
			String fullMessage = "";
			for (SmsMessage message : messages) {
				fullMessage += message.getMessageBody();//��ȡ��������
			}
			sender.setText(address);
			content.setText(fullMessage);
			//abortBroadcast();//����ϵͳ����
		}
	}
	
	class SendStatusReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent){
			if (getResultCode() == RESULT_OK) {
				//���ŷ��ͳɹ�
				Toast.makeText(context, "Send succeeded", Toast.LENGTH_SHORT).show();
			}else {
				//���ŷ���ʧ��
				Toast.makeText(context, "Send failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
