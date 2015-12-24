package com.example.providertest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private String newId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button addData = (Button) findViewById(R.id.add_data);
		addData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//添加数据
				Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
				ContentValues values = new ContentValues();
				values.put("name", "A Clash of Kings");
				values.put("author", "George Martin");
				values.put("pages", 1040);
				values.put("price", 22.9);
				Uri newUri = getContentResolver().insert(uri, values);
				newId = newUri.getPathSegments().get(1);
			}
		});
		
		Button queryData = (Button) findViewById(R.id.query_data);
		queryData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//查询数据
				Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
				Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				if (cursor != null) {
					while(cursor.moveToNext()){
						String name = cursor.getString(cursor.getColumnIndex("name"));
						String author = cursor.getString(cursor.getColumnIndex("author"));
						int pages = cursor.getInt(cursor.getColumnIndex("pages"));
						double price = cursor.getDouble(cursor.getColumnIndex("price"));
						Log.d("MainActivity", "book name is " + name);
					}
					cursor.close();
				}
			}
		});
		
		Button updateData = (Button) findViewById(R.id.update_data);
		queryData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//更新数据
				Uri uri = Uri.parse("content://com.example.databasetest.provider/book" + newId);
				ContentValues values = new ContentValues();
				values.put("name", "A Storm of Swords");
				values.put("pages", 1216);
				values.put("price", 13.9);
				getContentResolver().update(uri, values, null, null);
			}
		});
		
		Button deleteData = (Button) findViewById(R.id.delete_data);
		deleteData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//查询数据
				Uri uri = Uri.parse("content://com.example.databasetest.provider/book" + newId);
				getContentResolver().delete(uri, null, null);
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
