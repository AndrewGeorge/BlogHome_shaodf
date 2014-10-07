package com.example.bloghome_shaodf.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NewsDatahelper extends SQLiteOpenHelper{
	// 数据库的名字
	private final static String DB_NANE = "NewsDB";
	// 数据库的版本
	private final static int DB_VERSION = 1;
	
	private String tablename;
	private String SQL_CREATE="create table " + tablename;

	
	private String SQL_DROPTABLE ="DROP IF TABLE EXISTS"+tablename;	 
	public NewsDatahelper(Context context, String tablename) {
		
		
		super(context, DB_NANE, null, DB_VERSION);
		
		this.tablename=tablename;
		
		SQL_CREATE="CREATE TABLE " + tablename
				+ "(" 
				+ "_id		INTEGER	PRIMARY KEY   AUTOINCREMENT ,"
				+ "title 	VARCHAR(50) 	NOT NULL ," 
				+ "content VARCHAR(500) " +")";
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

		arg0.execSQL(SQL_CREATE);
	}

	/*
	 * 版本升级是调用
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		arg0.execSQL(SQL_DROPTABLE);
		onCreate(arg0);
	}

}
