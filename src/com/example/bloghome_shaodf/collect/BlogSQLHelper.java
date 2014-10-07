package com.example.bloghome_shaodf.collect;

import java.util.ArrayList;
import com.example.bloghome_shaodf.blog.BlogsInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class BlogSQLHelper {

	private static final String DB_NAME = "BLOGS.db";
	private static final String TB_OFFBLOG = "BlogOffline";
	private static final int version = 1;
		
	private final String createTBBLOG = "create table " + " if not exists "+TB_OFFBLOG
			+ "(_id integer primary key autoincrement,"
			+ // 唯一id标志
			"blogTitle varchar(200) ,"
			+ // 博客名称
			"bloger    varchar(100) ,"
			+ // 博主名称
			"blogerUrl varchar(200) ,"
			+ // 博主连接
			"blogId    integer unique," + "blogSummary varchar(200),"
			+ "storeTime long ," + // 存储时间
			"updateTime varchar ," + // 此文章最后更新时间
			"blogText  blob ," + // 文章正文
			"blogUrl   varchar(200) not null)";
	 
 
	private static Context context;
	
	private static  BlogSQL blogSql = null;
	private static  SQLiteDatabase db = null;
	
	private static BlogSQLHelper mDbInstance ;
	
	public static BlogSQLHelper getDBInstance(Context c) {
		 if(mDbInstance == null){
			 mDbInstance = new  BlogSQLHelper(c);
		 }
		 
		return mDbInstance;
	}
	private BlogSQLHelper(Context c){
		context = c;
		blogSql =  new BlogSQL(context, DB_NAME, null, version);
		db = blogSql.getWritableDatabase();

	}
	
	/**
	 * 从数据库获取所存储的博客文章，若为空或获取失败，返回NULL。<BR>
	 * 注意：数据库的打开与关闭 在使用时用户提供与关闭
	 * 
	 * @param info
	 *            blog数据
	 * @param context
	 *            当前环境
	 * @param DBName
	 *            数据库名称，可通过BlogSQLHelper.DB_NAME获取静态名称
	 * @param sqlHelper
	 *            数据库对象
	 * @param db
	 *            数据库对象
	 * @return
	 */
	public ArrayList<OffBlogInfo> getBlogDBData(){

		ArrayList<OffBlogInfo> res = new ArrayList<OffBlogInfo>();

		Cursor cursor = db.rawQuery("select * from " + TB_OFFBLOG, null);
		int count = cursor.getColumnCount();

		int i = 0;
		if (count <= 0)
			return null;
		while (cursor.moveToNext() && i < count) {
			OffBlogInfo info = new OffBlogInfo();
			info.setBlogTitle(cursor.getString(cursor
					.getColumnIndex("blogTitle")));
			info.setBlogUrl(cursor.getString(cursor
					.getColumnIndex("blogerUrl")));
			info.setBloger(cursor.getString(cursor.getColumnIndex("bloger")));
			info.setBlogId(cursor.getInt(cursor.getColumnIndex("blogId")));
			info.setBlogText(cursor.getString(cursor.getColumnIndex("blogText")));
			info.setUpdateTime(cursor.getString(cursor
					.getColumnIndex("updateTime")));
			info.setStoreTime(cursor.getLong(cursor.getColumnIndex("storeTime")));
			info.setBlogSum(cursor.getString(cursor
					.getColumnIndex("blogSummary")));

			res.add(info);
			i++;
		}
		cursor.close();
		return res;

	}
	
	/**
	 * 根据blogId从数据库查询信息， 返回null即查询为空
	 * @param blogId
	 * @return
	 */
	public OffBlogInfo getItemData(int blogId){
		Log.v("DATA", "getItemDATA");

		Cursor cursor = db.rawQuery("select * from " + TB_OFFBLOG+ " where blogId="+blogId, null); 
		 
		 Log.v("DATA", "cursor.getColumnCount()="+cursor.getColumnCount());
		 OffBlogInfo info = new OffBlogInfo();
			
			if(cursor.getColumnCount() == 0 || cursor == null){
				return null;
			}
			cursor.moveToFirst();
			
			info.setBlogTitle(cursor.getString(cursor
					.getColumnIndex("blogTitle")));
			info.setBlogUrl(cursor.getString(cursor
					.getColumnIndex("blogerUrl")));
			info.setBloger(cursor.getString(cursor.getColumnIndex("bloger")));
			info.setBlogId(cursor.getInt(cursor.getColumnIndex("blogId")));
			info.setBlogText(cursor.getString(cursor.getColumnIndex("blogText")));
			info.setUpdateTime(cursor.getString(cursor
					.getColumnIndex("updateTime")));
			info.setStoreTime(cursor.getLong(cursor.getColumnIndex("storeTime")));
			info.setBlogSum(cursor.getString(cursor
					.getColumnIndex("blogSummary")));
		 
		cursor.close();
		
		return info;
	}
	
	/**
	 * 插入数据库 BLogOffline
	 * @param text 博文正文
	 * @param info 博文其他信息,要用到其中Title、AuthorName、getAuthorUrl、Updated、Id等数据
	 */
	public void insertIntoDB(String text,BlogsInfo info){
		
		ContentValues values = new ContentValues();
		values.clear();

		String blogSummary = info.getSummary();
		String blogTitle = info.getTitle();// 获取blog标题
		String bloger = info.getAuthorName();// 获取博客博主
		String blogerUrl = info.getAuthorUrl();// 获取博主链接地址
		String blogLink = info.getLink();// 博客文章的连接地址
		long storeTime = System.currentTimeMillis();
		String updateTime = info.getUpdated(); // 博客更新时间
		int blogId = info.getId();

		values.put("blogSummary", blogSummary);
		values.put("blogTitle", blogTitle);
		values.put("bloger", bloger);
		values.put("blogerUrl", blogerUrl);
		values.put("blogUrl", blogLink);
		values.put("storeTime", storeTime);
		values.put("updateTime", updateTime);
		values.put("blogId", blogId);

		values.put("blogText", text);
		long res = db.insert(TB_OFFBLOG, null, values);
		if (res >= 0)
			Toast.makeText(context, "存储成功", Toast.LENGTH_SHORT).show();
		else if (res < 0) {
			Toast.makeText(context, "数据已存在", Toast.LENGTH_SHORT).show();
		}
		Log.v("SQL", "store success");
	}
	
	/**
	 * 根据博文id 删除数据
	 * @param id 即blogId
	 */
	public void deleteItem(int id){

		db.execSQL("delete from " + TB_OFFBLOG + " where blogId=" + id);
		Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 删除数据库全部信息
	 */
	public void deleteAll(){
		db.execSQL("delete  from " + TB_OFFBLOG);
		Toast.makeText(context, "全部数据删除成功", Toast.LENGTH_SHORT).show();
	}
	
	 class BlogSQL extends SQLiteOpenHelper {
		public BlogSQL(Context context, String name, CursorFactory factory,
				int version) {			
	
			super(context, name, factory, version);
			 
 			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v("TAG", "create databases!");
			db.execSQL(createTBBLOG);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.v("TAG", "databases updated!");

			
			// 博客链接
		}

}


}
