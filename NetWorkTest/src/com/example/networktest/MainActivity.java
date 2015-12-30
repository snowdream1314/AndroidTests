package com.example.networktest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button sendRequest;
	private TextView responseText;
	private String texts;
	private String text;
	private int i;
	
	private static final int SHOW_RESPONSE = 0;
	
	private Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				String response = (String) msg.obj;
				//在这里进行UI操作，将结果显示到界面上
				responseText.setText(response);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sendRequest = (Button) findViewById(R.id.send_request);
		responseText = (TextView) findViewById(R.id.response);
		sendRequest.setOnClickListener(this);
//		texts = null;
//		text = null;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.send_request) {
			sendRequestWithHttpUrlConnection ();
//			sendRequestWithHttpClient();
		}
	}
	
	private void sendRequestWithHttpUrlConnection() {
		//开启线程发起网络请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL("http://mobile.weather.com.cn/js/citylist.xml");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					//post请求
//					connection.setRequestMethod("POST");
//					DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//					out.writeBytes("username=admin&password=123");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					//读取获得的输入流
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null) {
						response.append(line);
					}
//					Message message = new Message();
//					message.obj = response.toString();
//					message.what = SHOW_RESPONSE;
//					//将服务器返回的结果存入Message
//					handler.sendMessage(message);
					
					parseXMLWithPull(response.toString());//解析XML
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
	
	
	private void sendRequestWithHttpClient() {
		//开启线程发起网络请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet("http://mobile.weather.com.cn/js/citylist.xml");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						//请求成功
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity, "utf-8");
						parseXMLWithPull(response);
//						Message message = new Message();
//						message.obj = response.toString();
//						message.what = SHOW_RESPONSE;
//						//将服务器返回的结果存入Message
//						handler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}).start();
	}
	
	private void parseXMLWithPull(String xmlData) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlData));
			int eventType = xmlPullParser.getEventType();
			String d1 = "";
			String d2 = "";
			String d3 = "";
			String d4 = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				//开始解析节点
				case XmlPullParser.START_TAG: {
//					texts = null;
//					i = 0;
					if ("d".equals(nodeName)) {
						d1 = xmlPullParser.getAttributeValue(0);
						d2 = xmlPullParser.getAttributeValue(1);
						d3 = xmlPullParser.getAttributeValue(2);
						d4 = xmlPullParser.getAttributeValue(3);
						text = d1 + "," + d2 + "," + d3 + "," + d4;
						texts = texts + "\n" + text;
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					if ("c".equals(nodeName)) {
//						responseText.setText(texts);
						Message message = new Message();
						message.obj = texts.toString();
						message.what = SHOW_RESPONSE;
						//将服务器返回的结果存入Message
						handler.sendMessage(message);
//						Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
					}
				}
					break;
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
