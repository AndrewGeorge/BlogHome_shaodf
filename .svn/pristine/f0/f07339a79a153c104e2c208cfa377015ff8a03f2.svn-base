package com.example.bloghome_shaodf.collect;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class NewSQLHelper {
	private static final String DB_NAME = "NEWS.db";
	private static final String TB_NEWS = "newsTab";
	private static final int version = 1;

	private final String createTBNEWS = "create table " + " if not exists "
			+ TB_NEWS + "(_id integer primary key autoincrement not null,"
			+ "url varchar(100)  unique not null, "
			+ "title varchar(200) not null," + "newsId int not null)";

	private static Context context;

	private static NewSQL newSql = null;

	private static SQLiteDatabase db = null;

	private static NewSQLHelper mDbInstance;

	public static NewSQLHelper getDBInstance(Context c) {
		if (mDbInstance == null) {
			mDbInstance = new NewSQLHelper(c);
		}

		return mDbInstance;
	}

	private NewSQLHelper(Context c) {
		context = c;
		newSql = new NewSQL(context, DB_NAME, null, version);
		db = newSql.getWritableDatabase();

	}

	public ArrayList<NewInfo> getNews() {
		ArrayList<NewInfo> news = new ArrayList<NewInfo>();
		NewInfo n;
		Cursor cursor = db.rawQuery("select * from " + TB_NEWS, null);
		int count = cursor.getColumnCount();

		int i = 0;
		if (count <= 0)
			return null;
		while (cursor.moveToNext() && i < count) {

			n = new NewInfo();
			n.setLinks(cursor.getString(cursor.getColumnIndex("url")));
			n.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			n.setId(cursor.getInt(cursor.getColumnIndex("newsId")));
			news.add(n);
		}
		cursor.close();
		return news;

	}
// 插入新闻链接URL，Title
	public void insertIntoNews(String url, String title, int id) {

		ContentValues values = new ContentValues();
		values.clear();

		values.put("url", url);
		values.put("title", title);
		values.put("newsId", id);

		long res = db.insert(TB_NEWS, null, values);
		if (res >= 0)
			Toast.makeText(context, "存储成功", Toast.LENGTH_SHORT).show();
		else if (res < 0) {
			Toast.makeText(context, "链接已存在", Toast.LENGTH_SHORT).show();
		}
	}
// 根据URL删除新闻
	public void deleteNews(int id) {
		db.execSQL("delete from " + TB_NEWS + " where newsId=" + id);
		Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();

	}
// 删除数据库全被新闻信息
	public void deleteAllNews() {

		db.execSQL("delete  from " + TB_NEWS);
		Toast.makeText(context, "全部书签删除成功", Toast.LENGTH_SHORT).show();
	}

	class NewSQL extends SQLiteOpenHelper {
		public NewSQL(Context context, String name, CursorFactory factory,
				int version) {

			super(context, name, factory, version);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v("TAG", "create databases!");
			db.execSQL(createTBNEWS);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.v("TAG", "databases updated!");

		}

	}
}
