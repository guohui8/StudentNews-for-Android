package com.guohui.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "sjb.db";
	public static final String DEFAULT_SORT_ORDER = "id DESC";
	public static final String NEWS_TABLE = "news";
	public static final String XINWEN_TABLE = "xinwen";
	public static final String TABLE_NAME_HISTORY = "history";	
	public static final String TABLE_NAME_FAVORITE = "favorite";
	//图片表
	public static final String P_ID = "p_id";
	public static final String P_TITLE = "p_title";
	public static final String P_PICURL = "p_picurl";
	public static final String P_DESC = "p_desc";
	public static final String P_IS_HASIMG = "p_is_hasimg";
	public static final String P_DATE = "p_date";
	public static final String P_HITS = "p_hits";
	//新闻表
	public static final String X_ID = "p_id";
	public static final String X_TITLE = "p_title";
	public static final String X_PICURL = "p_picurl";
	public static final String X_DESC = "p_desc";
	public static final String X_IS_HASIMG = "p_is_hasimg";
	public static final String X_DATE = "p_date";
	public static final String X_HITS = "p_hits";
	//历史记录表
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NEWSID = "newsid";
	public static final String COLUMN_TYPEID = "typeid";
	public static final String COLUMN_TITLE = "title";
	public NewsSQLiteOpenHelper(Context paramContext) {
		super(paramContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public NewsSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public NewsSQLiteOpenHelper(Context context, String name, int version) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public NewsSQLiteOpenHelper(Context context, String name) {
		this(context, DATABASE_NAME, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		paramSQLiteDatabase.execSQL("CREATE TABLE news (_id INTEGER PRIMARY KEY AUTOINCREMENT,p_id INTEGER, p_title NTEXT, p_tid NTEXT,  p_picurl varchar(150), p_desc TEXT, p_is_hasimg NTEXT, p_date NTEXT,p_hits INTEGER);");
		//创建历史表
		paramSQLiteDatabase.execSQL("CREATE TABLE history (_id INTEGER PRIMARY KEY AUTOINCREMENT, title varchar(200), newsid varchar(20), typeid varchar(10));");
		//创建新闻表
		paramSQLiteDatabase.execSQL("CREATE TABLE xinwen (_id INTEGER PRIMARY KEY AUTOINCREMENT,p_id INTEGER, p_title NTEXT, p_tid NTEXT,  p_picurl varchar(150), p_desc TEXT, p_is_hasimg NTEXT, p_date NTEXT,p_hits INTEGER);");
				
	}

	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
			int paramInt2) {
		paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HISTORY);
		paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE);
		paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + XINWEN_TABLE);
		onCreate(paramSQLiteDatabase);
	}

	public void updateNewsDB(SQLiteDatabase paramSQLiteDatabase) {

	}
}