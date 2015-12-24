package com.example.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private MyDatabaseHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);
		//�������ݱ�
		Button createDatabase = (Button) findViewById(R.id.create_database);
		createDatabase.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				dbHelper.getWritableDatabase();
			}
		});
		//��������
		Button addData = (Button) findViewById(R.id.add_data);
		addData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				//��ʼ��װ��һ������
				values.put("name", "The Da Vince Code");
				values.put("author", "Dan Brown");
				values.put("pages", 454);
				values.put("price", 16.5);
				db.insert("Book", null, values);//�����һ������
			}
		});
		//��������
		Button updateData = (Button) findViewById(R.id.update_data);
		updateData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("price", 10.9);
				db.update("Book", values, "name=?", new String[]{"The Da Vince Code"});
			}
		});
		//ɾ������
		Button deleteData = (Button) findViewById(R.id.delete_data);
		deleteData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.delete("Book", "pages > ?", new String[]{"500"});
			}
		});
		//��ѯ����
		Button queryData = (Button) findViewById(R.id.query_data);
		queryData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				//��ѯBook������������
				Cursor cursor = db.query("Book", null, null, null, null, null, null);
				if (cursor.moveToFirst()){
					do {
						//����cursor����ȡ������
						String name = cursor.getString(cursor.getColumnIndex("name"));
						String author = cursor.getString(cursor.getColumnIndex("author"));
						int pages = cursor.getInt(cursor.getColumnIndex("pages"));
						double price = cursor.getDouble(cursor.getColumnIndex("price"));
						Log.d("MainActivity", "book name is " + name);
					}while (cursor.moveToNext());
				}
				cursor.close();
			}
		});
		//�滻����
		Button replaceData = (Button) findViewById (R.id.replace_data);
		replaceData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.beginTransaction();//��������
				try {
					db.delete("Book", null, null);
					if (true) {
						//�ֶ��׳�һ���쳣
						throw new NullPointerException();
					}
					ContentValues values = new ContentValues();
					values.put("name", "Game of Thrones");
					values.put("author", "George Martin");
					values.put("pages", 720);
					values.put("price", 20.9);
					db.insert("Book", null, values);
					db.setTransactionSuccessful();//����ִ�гɹ�
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					db.endTransaction();//��������
				}
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
