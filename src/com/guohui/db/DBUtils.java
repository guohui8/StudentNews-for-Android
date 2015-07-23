package com.guohui.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBUtils {
	public SQLiteDatabase database;
	private static NewsSQLiteOpenHelper mdbhelper;
	private static SQLiteDatabase db;
	private static final String TAG = "DAO_TAG";

	public DBUtils(Context context) {
		mdbhelper = new NewsSQLiteOpenHelper(context);
		database = mdbhelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public static void deleteDataToNewsTabel(
			SQLiteDatabase paramSQLiteDatabase, String paramString) {
		paramSQLiteDatabase.delete("news", "p_id='" + paramString + "'", null);
	}

	public static void deleteDataToXinwenTabel(
			SQLiteDatabase paramSQLiteDatabase, String paramString) {
		paramSQLiteDatabase
				.delete("xinwen", "p_id='" + paramString + "'", null);
	}

	// 删除类别信息
	public static void deleteFromeNewsByType_news(
			SQLiteDatabase paramSQLiteDatabase, String paramString) {
		paramSQLiteDatabase.delete("news", "p_tid in " + paramString, null);
	}

	// 删除类别信息
	public static void deleteFromeXinwenByType_news(
			SQLiteDatabase paramSQLiteDatabase, String paramString) {
		paramSQLiteDatabase.delete("xinwen", "p_tid in " + paramString, null);
	}

	// 获取类别
	public static void deleteFromeNewsByType(
			SQLiteDatabase paramSQLiteDatabase, String paramString) {
		paramSQLiteDatabase.delete("news", "p_tid = '" + paramString + "'",
				null);
	}

	// 获取类别
	public static void deleteFromeXinwenByType(
			SQLiteDatabase paramSQLiteDatabase, String paramString) {
		paramSQLiteDatabase.delete("xinwen", "p_tid = '" + paramString + "'",
				null);
	}

	public static Cursor getDataFromNewsTable(Context paramContext,
			SQLiteDatabase paramSQLiteDatabase, String typeId) {
		String[] arrayOfString = new String[1];
		arrayOfString[0] = "_id,p_id,p_title,p_tid,p_picurl,p_desc,p_is_hasimg,p_date,p_hits";
		return paramSQLiteDatabase.query("news", arrayOfString, "p_tid = ?",
				new String[] { typeId }, null, null, null, null);
	}

	public static Cursor getDataFromXinwenTable(Context paramContext,
			SQLiteDatabase paramSQLiteDatabase, String typeId) {
		String[] arrayOfString = new String[1];
		arrayOfString[0] = "_id,p_id,p_title,p_tid,p_picurl,p_desc,p_is_hasimg,p_date,p_hits";
		return paramSQLiteDatabase.query("xinwen", arrayOfString, "p_tid = ?",
				new String[] { typeId }, null, null, null, null);
	}

	public static Cursor getDataFromNewsTable22(Context paramContext,
			SQLiteDatabase paramSQLiteDatabase, String typeId) {
		String[] arrayOfString = new String[1];
		arrayOfString[0] = "_id,p_id,p_title,p_tid,p_picurl,p_desc,p_is_hasimg,p_date,p_hits";
		String where = "p_tid in" + typeId;
		String limit = "0,20";
		Cursor cursor = paramSQLiteDatabase.query("news", arrayOfString, where,
				null, null, null, "_id desc", limit);

		return cursor;
	}

	public static Cursor getDataFromXinwenTable22(Context paramContext,
			SQLiteDatabase paramSQLiteDatabase, String typeId) {
		String[] arrayOfString = new String[1];
		arrayOfString[0] = "_id,p_id,p_title,p_tid,p_picurl,p_desc,p_is_hasimg,p_date,p_hits";
		String where = "p_tid in" + typeId;
		String limit = "0,20";
		Cursor cursor = paramSQLiteDatabase.query("xinwen", arrayOfString,
				where, null, null, null, "_id desc", limit);

		return cursor;
	}

	// 判断是否有重复数据
	public static Integer isValidNews(String id) {
		Integer result = 0;
		Cursor cursor = null;
		String sql = "select * from  news where p_id='" + id + "' ";
		try {
			db = mdbhelper.getWritableDatabase();
			cursor = db.rawQuery(sql, null);
			result = cursor.getCount();
			return result;
		} catch (Exception e) {
			// e.printStackTrace();
			return 0;
		} finally {
			if (!cursor.isClosed()) {
				cursor.close();
			}
			db.close();
			mdbhelper.close();
		}
	}

	// 判断是否有重复数据
	public static Integer isValidXinwen(String id) {
		Integer result = 0;
		Cursor cursor = null;
		String sql = "select * from  xinwen where p_id='" + id + "' ";
		try {
			db = mdbhelper.getWritableDatabase();
			cursor = db.rawQuery(sql, null);
			result = cursor.getCount();
			return result;
		} catch (Exception e) {
			// e.printStackTrace();
			return 0;
		} finally {
			if (!cursor.isClosed()) {
				cursor.close();
			}
			db.close();
			mdbhelper.close();
		}
	}

	public static void insertDataToNewsTable(
			SQLiteDatabase paramSQLiteDatabase, String paramString1,
			String paramString2, String paramString3, String paramString4,
			String paramString5, String paramString6, String paramString7,
			String paramString8) {
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("p_id", paramString1);
		localContentValues.put("p_title", paramString2);
		localContentValues.put("p_tid", paramString3);
		localContentValues.put("p_picurl", paramString4);
		localContentValues.put("p_desc", paramString5);
		localContentValues.put("p_is_hasimg", paramString6);
		localContentValues.put("p_date", paramString7);
		localContentValues.put("p_hits", paramString8);
		paramSQLiteDatabase.insert("news", null, localContentValues);
	}
	public static void insertDataToXinwenTable(
			SQLiteDatabase paramSQLiteDatabase, String paramString1,
			String paramString2, String paramString3, String paramString4,
			String paramString5, String paramString6, String paramString7,
			String paramString8) {
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("p_id", paramString1);
		localContentValues.put("p_title", paramString2);
		localContentValues.put("p_tid", paramString3);
		localContentValues.put("p_picurl", paramString4);
		localContentValues.put("p_desc", paramString5);
		localContentValues.put("p_is_hasimg", paramString6);
		localContentValues.put("p_date", paramString7);
		localContentValues.put("p_hits", paramString8);
		paramSQLiteDatabase.insert("xinwen", null, localContentValues);
	}
	// 插入数据
	/*public static void insertNewsRecord(String newsid, String newstitle,
			String typeid, String author, String img, String desc,
			String adddate) {

		String sql = "insert into news(news_id,news_title,news_sub_title,news_type_id,news_author,news_img_src,description,news_is_hasimg,news_add_time)values('"
				+ newsid
				+ "','"
				+ newstitle
				+ "','','"
				+ typeid
				+ "','"
				+ author
				+ "','"
				+ img
				+ "','"
				+ desc
				+ "','1','"
				+ adddate
				+ "')";
		try {
			db = mdbhelper.getWritableDatabase();
			db.execSQL(sql);
			// Log.e("sql",sql);
		} catch (Exception e) {
			Log.e("error", "error");
		} finally {
			db.close();
			mdbhelper.close();
		}
	}*/

	// 查询是否有重复记录
	public Integer isValidHistoryNews(String newsid, String typeid) {
		Integer result = 0;
		Cursor cursor = null;
		String sql = "select * from " + NewsSQLiteOpenHelper.TABLE_NAME_HISTORY
				+ " where typeid ='" + typeid + "' and newsid='" + newsid + "'";
		try {
			db = mdbhelper.getWritableDatabase();
			cursor = db.rawQuery(sql, null);
			result = cursor.getCount();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (!cursor.isClosed()) {
				cursor.close();
			}
			db.close();
			mdbhelper.close();
		}
	}

	// 查询数据
	public Cursor select_history(String typeid) {
		db = mdbhelper.getReadableDatabase();
		String where = "typeid ='" + typeid + "'";
		Cursor cursor = db.query(NewsSQLiteOpenHelper.TABLE_NAME_HISTORY, null,
				where, null, null, null, "" + NewsSQLiteOpenHelper.COLUMN_ID
						+ "  desc");
		return cursor;
	}

	// 插入记录
	public long InsertRecord(HistoryRecords r) {
		long rows = 0;
		db = mdbhelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(NewsSQLiteOpenHelper.COLUMN_NEWSID, r.getNewsid());
		cv.put(NewsSQLiteOpenHelper.COLUMN_TITLE, r.getTitle());
		cv.put(NewsSQLiteOpenHelper.COLUMN_TYPEID, r.getTypeid());
		try {
			rows = db.insert(NewsSQLiteOpenHelper.TABLE_NAME_HISTORY, null, cv);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		db.close();
		mdbhelper.close();
		return rows;
	}

	public long add(HistoryRecords history) {
		long row = 0;
		db = mdbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("newsid", history.getNewsid());
		values.put("typeid", history.getTypeid());
		values.put("title", history.getTitle());
		row = db.insert(NewsSQLiteOpenHelper.TABLE_NAME_HISTORY, null, values);
		db.close();
		return row;
	}

	public long add(List<HistoryRecords> history) {
		long row = 0;
		for (HistoryRecords n : history) {
			row += add(n);
		}
		return row;
	}

	// 删除单条记录
	public void delRecord(String id, String typeid) {
		String sql = "delete from " + NewsSQLiteOpenHelper.TABLE_NAME_HISTORY
				+ " where _id='" + id + "' and typeid='" + typeid + "'";
		try {
			db = mdbhelper.getWritableDatabase();
			db.execSQL(sql);
		} catch (Exception e) {

		} finally {
			db.close();
			mdbhelper.close();
		}
	}

	// 删除收藏记录
	public void delFav(String id, String typeid) {
		String sql = "delete from " + NewsSQLiteOpenHelper.TABLE_NAME_HISTORY
				+ " where newsid='" + id + "' and typeid='" + typeid + "'";
		try {
			db = mdbhelper.getWritableDatabase();
			db.execSQL(sql);
		} catch (Exception e) {

		} finally {
			db.close();
			mdbhelper.close();
		}
	}

	// 清空历史记录
	public void delHistory(String type) {
		db = mdbhelper.getWritableDatabase();
		try {
			db.delete(NewsSQLiteOpenHelper.TABLE_NAME_HISTORY, " typeid='"
					+ type + "' and _id < 999999", null);

		} catch (SQLException e) {
			Log.e("error", "出错了");
		}
	}

	// 判断是否有收藏记录
	public static Cursor isExists(Context paramContext,
			SQLiteDatabase paramSQLiteDatabase, String type, String id) {
		String[] arrayOfString = new String[1];
		arrayOfString[0] = " _id, newsid,title,typeid";
		return paramSQLiteDatabase.query(
				NewsSQLiteOpenHelper.TABLE_NAME_FAVORITE, arrayOfString,
				"type = ? and newsid= ? ", new String[] { type, id }, null,
				null, "_id desc", null);
	}

}