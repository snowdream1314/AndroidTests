package com.example.networktest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import android.app.Activity;
import android.content.Context;
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
	private static TextView responseText;
	private String texts;
	private String text;
	private int i;
	private static String xmlDatas;
//	private String updateTime,cityName,shidu,pm25,suggest,quality,fengXiang,fengLi,
//	  tempNow,aqi,sunrise_1,sunset_1,MajorPollutants,xmlDatas;
	private static final int SHOW_RESPONSE = 0;
	
	private static Handler handler = new Handler() {
		
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
					URL url = new URL("http://wthrcdn.etouch.cn/WeatherApi?citykey=101010100");
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
					
//					parseXMLWithPull(response.toString());//解析XML
					handleWeatherXMLResponse(response.toString());
					
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
//					if ("d".equals(nodeName)) {
//						d1 = xmlPullParser.getAttributeValue(0);
//						d2 = xmlPullParser.getAttributeValue(1);
//						d3 = xmlPullParser.getAttributeValue(2);
//						d4 = xmlPullParser.getAttributeValue(3);
//						text = d1 + "," + d2 + "," + d3 + "," + d4;
//						texts = texts + "\n" + text;
//					}
					if ("city".equals(nodeName)) {
						d1 = xmlPullParser.nextText();
						text = d1 + "\n";
					}
					if ("updatetime".equals(nodeName)) {
						d2 = xmlPullParser.nextText();
						texts = text + "\n" + d2;
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					if ("environment".equals(nodeName)) {
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
	
	public static void handleWeatherXMLResponse(String response) {
		try {
			String cityName = "";
			String updateTime="";
			String fengLi="";
			String fengXiang="";
			String tempNow = "";
			String aqi = "";
			String shidu="";
			String sunrise_1="";
			String sunset_1="";
			String pm25="";
			String suggest="";
			String quality="";
			String MajorPollutants="";
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(response));
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				//开始解析XML节点
				case XmlPullParser.START_DOCUMENT:  
//	                zhiShus = new ArrayList<WeatherZhiShu>(); 
//	                weatherList = new ArrayList<Weather>();
	                break;  
				case XmlPullParser.START_TAG: 
					if ("city".equals(nodeName)) {
						cityName = xmlPullParser.nextText();
					}
					if ("updatetime".equals(nodeName)) {
						updateTime = xmlPullParser.nextText();
					}
					if ("wendu".equals(nodeName)) {
						tempNow = xmlPullParser.nextText();
					}
					if ("fengli".equals(nodeName)) {
						fengLi = xmlPullParser.nextText();
					}
					if ("shidu".equals(nodeName)) {
						shidu = xmlPullParser.nextText();
					}
					if ("fengxiang".equals(nodeName)) {
						fengXiang = xmlPullParser.nextText();
					}
					if ("sunrise_1".equals(nodeName)) {
						sunrise_1 = xmlPullParser.nextText();
					}
					if ("sunset_1".equals(nodeName)) {
						sunset_1 = xmlPullParser.nextText();
					}
					if ("aqi".equals(nodeName)) {
						aqi = xmlPullParser.nextText();
					}
					if ("pm25".equals(nodeName)) {
						pm25 = xmlPullParser.nextText();
					}
					if ("suggest".equals(nodeName)) {
						suggest = xmlPullParser.nextText();
					}
					if ("quality".equals(nodeName)) {
						quality = xmlPullParser.nextText();
					}
					if ("MajorPollutants".equals(nodeName)) {
						MajorPollutants = xmlPullParser.nextText();
					}
					
//					if ("weather".equals(nodeName)) {
//						weather =new Weather();
//					} else if ("date".equals(nodeName)) {
//						weather.setDate(xmlPullParser.getText());
//						eventType = xmlPullParser.next();
//					} else if ("high".equals(nodeName)) {
//						weather.setHigh(xmlPullParser.getText());
//						eventType = xmlPullParser.next();
//					} else if ("low".equals(nodeName)) {
//						weather.setLow(xmlPullParser.getText());
//						eventType = xmlPullParser.next();
//					} else if ("day".equals(nodeName)) {
//						eventType = xmlPullParser.next();
//						if ("type".equals(nodeName)) {
//							weather.getDay().setType(xmlPullParser.getText());
//							eventType = xmlPullParser.next();
//						} else if ("fengxiang".equals(nodeName)) {
//							weather.getDay().setFengXiang(xmlPullParser.getText());
//							eventType = xmlPullParser.next();
//						} else if ("fengli".equals(nodeName)) {
//							weather.getDay().setFengLi(xmlPullParser.getText());
//							eventType = xmlPullParser.next();
//						}
//					} else if ("night".equals(nodeName)) {
//						eventType = xmlPullParser.next();
//						if ("type".equals(nodeName)) {
//							weather.getNight().setType(xmlPullParser.getText());
//							eventType = xmlPullParser.next();
//						} else if ("fengxiang".equals(nodeName)) {
//							weather.getNight().setFengXiang(xmlPullParser.getText());
//							eventType = xmlPullParser.next();
//						} else if ("fengli".equals(nodeName)) {
//							weather.getNight().setFengLi(xmlPullParser.getText());
//							eventType = xmlPullParser.next();
//						}
//					} 
//					
//					if ("zhishu".equals(nodeName)) {
//						weatherZhiShu = new WeatherZhiShu();
//					} else if ("name".equals(nodeName)) {
//						weatherZhiShu.setName(xmlPullParser.getText());
//						eventType = xmlPullParser.next();
//					} else if ("value".equals(nodeName)) {
//						weatherZhiShu.setValue(xmlPullParser.getText());
//						eventType = xmlPullParser.next();
//					} else if ("detail".equals(nodeName)) {
//						weatherZhiShu.setDetail(xmlPullParser.getText());
//						eventType = xmlPullParser.next();
//					}
					break; 
				
				case XmlPullParser.END_TAG: 
//					if ("weather".equals(nodeName)) {
//						weatherList.add(weather);
//						weather = null;
//					} else if ("zhishu".equals(nodeName)) {
//						zhiShus.add(weatherZhiShu);
//						weatherZhiShu = null;
//					}
					if ("environment".equals(nodeName)) {
						xmlDatas =  cityName+ "\n" + updateTime+ "\n" + tempNow+ "\n" + fengLi+ "\n" + fengXiang+ "\n" + shidu+ "\n" + sunrise_1+ "\n" + 
								sunset_1+ "\n" + aqi+ "\n" + pm25+ "\n" + suggest+ "\n" + quality+ "\n" + MajorPollutants;
						Message message = new Message();
						message.obj = xmlDatas.toString();
						message.what = SHOW_RESPONSE;
						//将服务器返回的结果存入Message
						handler.sendMessage(message);
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
