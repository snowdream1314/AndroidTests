package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {
	
	public static final int BOOK_DIR = 0;
	public static final int BOOK_ITEM = 1;
	public static final int CATEGORY_DIR = 2;
	public static final int CATEGORY_ITEM = 3;
	
	public static final String AUTHORITY = "com.example.databasetest.provider";
	
	private static UriMatcher uriMatcher ;
	
	private MyDatabaseHelper dbHelper;
	
	static {//不要把隐私数据uri加进来!
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.example.app.provider", "book", BOOK_DIR);
		uriMatcher.addURI("com.example.app.provider", "book/#", BOOK_ITEM);
		uriMatcher.addURI("com.example.app.provider", "category", CATEGORY_DIR);
		uriMatcher.addURI("com.example.app.provider", "category/#", CATEGORY_ITEM);
	}
	
	@Override
	public boolean onCreate(){
		dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		//查询数据
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			//查询Book表中的所有数据
			cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case BOOK_ITEM:
			//查询Book表中的单条数据
			String bookId = uri.getPathSegments().get(1);
			cursor = db.query("Book", projection, "id=?", new String[]{"bookId"}, null, null, sortOrder);
			break;
		case CATEGORY_DIR:
			//查询category表中的所有数据
			cursor = db.query("Category", projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case CATEGORY_ITEM:
			//查询category表中的单条数据
			String categoryId = uri.getPathSegments().get(1);
			cursor = db.query("Category", projection, "id=?", new String[]{"categoryId"}, null, null, sortOrder);
			break;
		default:
			break;
		}
		return cursor;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		//增加数据
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Uri uriReturn = null;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
		case BOOK_ITEM:
			long newBookId = db.insert("Book", null, values);
			uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
			break;
		case CATEGORY_DIR:
		case CATEGORY_ITEM:
			long newCategoryId = db.insert("Category", null, values);
			uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
			break;
		}
		return uriReturn;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		//更新数据
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		int updateRows = 0;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			updateRows = db.update("Book", values, selection, selectionArgs);
			break;
		case BOOK_ITEM:
			String bookId = uri.getPathSegments().get(1);
			updateRows = db.update("Book", values, "id=?", new String[]{bookId});
			break;
		case CATEGORY_DIR:
			updateRows = db.update("Category", values, selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			String categoryId = uri.getPathSegments().get(1);
			updateRows = db.update("Category", values, "id=?", new String[]{categoryId});
			break;
		default:
			break;
		}
		return updateRows;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		//删除数据
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		int deleteRows = 0;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			deleteRows = db.delete("Book", selection, selectionArgs);
			break;
		case BOOK_ITEM:
			String bookId = uri.getPathSegments().get(1);
			deleteRows = db.delete("Book", "id=?", new String[]{bookId});
			break;
		case CATEGORY_DIR:
			deleteRows = db.delete("Category", selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			String categoryId = uri.getPathSegments().get(1);
			deleteRows = db.delete("Category", "id=?", new String[]{categoryId});
			break;
		default:
			break;
		}
		return deleteRows;
	}
	
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
		case BOOK_DIR:
			return "vnd.android.cursor.dir/vnd.com.example.app.provider.BOOK_DIR";
		case BOOK_ITEM:
			return "vnd.android.cursor.item/vnd.com.example.app.provider.BOOK_ITEM";
		case CATEGORY_DIR:
			return "vnd.android.cursor.dir/vnd.com.example.app.provider.CATEGORY_DIR";
		case CATEGORY_ITEM:
			return "vnd.android.cursor.item/vnd.com.example.app.provider.CATEGORY_ITEM";
		default:
			break;
		}
		return null;
	}
}

