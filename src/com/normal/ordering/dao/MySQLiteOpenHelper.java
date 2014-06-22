package com.normal.ordering.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库操作步骤: 1) 创建数据库文件 ***.db 2) 创建数据库表
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "MySQLiteOpenHelper";

	// 数据库的名称
	private static final String DB_NAME = "ordering.db";
	// 数据库的版本号，正整数，只能升，不能降低
	private static final int DB_VERSION = 1;
	// 表名
	private static final String TAB_USER = "user";
	private static final String TAB_DISCOUNT = "discount";

	public MySQLiteOpenHelper(Context context) {
		// 父类的构造方法
		super(context, DB_NAME, null, DB_VERSION);
		Log.d(TAG, "MySQLiteOpenHelper - 构造方法 - 调用");

	}

	/**
	 * 
	 * @param context
	 *            : 上下文 Activity的上下文
	 * @param name
	 *            ：数据库名字
	 * @param factory
	 *            ：游标工厂
	 * @param version
	 *            ：指定你现在数据库的版本
	 */
	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 创建数据库: 当你的应用程序第一次MySQLiteOpenHelper对象的时候 主要是创建你的表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "MySQLiteOpenHelper - onCreate - 调用");
		String sql = "CREATE TABLE " + TAB_USER + " ("
				+ " id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " username VARCHAR(255),  " + " name VARCHAR(255), "
				+ "vip INT," + "integration INT" + ") ";
		db.execSQL(sql);
	}

	/**
	 * 升级数据库: 修改你的表 原理:根据新旧数据库的版本号来决定什么时候调用 两种情况： 1、如果以前的数据都废弃： 删除原来的表，再创建表
	 * 2、如果以前的数据保留： 更改表结构
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.d(TAG, "MySQLiteOpenHelper - onUpgrade - 调用");

		// 删除表
		String sql = "DROP TABLE  IF EXISTS " + TAB_USER;
		db.execSQL(sql);
		// 调用创建表的方法
		this.onCreate(db);

		// 修改表-加列
		// String sql = "ALTER TABLE " + TAB_STUDENT_NAME +
		// " ADD remark VARCHAR(300)";
		// db.execSQL(sql);

	}

}
