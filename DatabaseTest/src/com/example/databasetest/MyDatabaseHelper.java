package com.example.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	public static final String CREATE_BOOK = "create table Book ("
			+ "id integer primary key autoincrement, "
			+ "author text, "
			+ "price real,"
			+ "category_id integer, "
			+ "name text) ";
	
	public static final String CREATE_CATEGORY = "create table Category ("
			+ "id integer primary key autoincrement, "
			+ "category_code integer, "
			+ "category_name text) ";
	
	private Context mcontext;
	
	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version){
		super(context, name, factory, version);
		mcontext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BOOK);
		db.execSQL(CREATE_CATEGORY);
		//Toast.makeText(mcontext, "Create succeeded", Toast.LENGTH_SHORT).show();
	}
	
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		switch(oldVersion) {
		case 1: 
			db.execSQL(CREATE_CATEGORY);
		case 2:
			db.execSQL("alter table Book add column category_id integer");
		default:
		}
	}
}
