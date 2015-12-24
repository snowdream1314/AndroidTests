package com.example.notificationtest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button sendNotice;
	private Notification notification;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sendNotice = (Button) findViewById(R.id.send_notice);
		sendNotice.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick (View v) {
				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification.Builder builder = new Notification.Builder(this);
				builder.setSmallIcon(R.drawable.ic_launcher);
				builder.setContentTitle("This is content title");
				builder.setContentText("This is content text");
				builder.setWhen(System.currentTimeMillis());
				notification = builder.getNotification();
//				Notification notification = new Notification(R.drawable.ic_launcher, "This is ticker text", System.currentTimeMillis());
//				notification.setLatestEventInfo(this, "This is content title", "This is content text", null);
				manager.notify(1, notification);
			}
		});
		
		
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
}
