package com.example.bloghome_shaodf.sql;


import java.util.ArrayList;
import java.util.List;
import com.example.bloghome_shaodf.news.SaveContent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NewsDataBaseManger {

	private NewsDatahelper newsDatahelper;
	private String tablename;


	public NewsDataBaseManger(Context context, String tablename) {

		newsDatahelper = new NewsDatahelper(context, tablename);
		this.tablename=tablename;

	}

	
	//保存新闻保存
	public void saveData(ContentValues values) {

		SQLiteDatabase db = newsDatahelper.getWritableDatabase();
		db.insert(tablename, null, values);
		db.close();

	}

	//离线浏览调用查询
	public List<SaveContent> queryData() {
		
		SQLiteDatabase db = newsDatahelper.getWritableDatabase();
		List<SaveContent> listdata=new ArrayList<SaveContent>();
		
		Cursor cursor=db.query(tablename, null, null, null, null, null, null);
		//遍历游标集来获取信息
		while (cursor.moveToNext()) {
			
			SaveContent noteBook=new SaveContent(cursor.getInt(cursor.getColumnIndex("_id")), 
					cursor.getString(cursor.getColumnIndex("title")), 
					cursor.getString(cursor.getColumnIndex("content")));
//			isChexked
			listdata.add(noteBook);
			
		}
		
		cursor.close();
		db.close();
		return listdata;
	}
	
	
	/**
	 * 
	 * 删除数据
	 */
	public void newsDeledata( String id) {
		
		SQLiteDatabase db = newsDatahelper.getWritableDatabase();
		
	
		
		    
		String[] args = {String.valueOf(id)};
		int i =db.delete(tablename, "_id=?", args);
		
		Log.i("sql", i+"delete");
		
		//db.execSQL("delete from tablename where _id="+id);
		
		db.close();

	}
	
	
}
