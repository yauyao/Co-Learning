package com.example.co_learningnew;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//大概要先撰寫一隻 SQLiteOpenHelper，此角色是用來初始化資料庫，包含建 Table 、資料庫升級等對應動作。
public class MyDBHelper extends SQLiteOpenHelper {
        final private static int _DB_VERSION = 1;
        final private static String _DB_DATABASE_NAME = "notes.db";
		private static final String TAG = "MyDBHelper";
		
        public MyDBHelper(Context context) {
                super(context,MyDBHelper._DB_DATABASE_NAME,null,MyDBHelper._DB_VERSION);
        }
        public MyDBHelper(Context context, String name, CursorFactory factory, int version) {
                super(context, name, factory, version);
                // TODO Auto-generated constructor stub
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
                // TODO Auto-generated method stub
        		Log.d(TAG, "onCreate");
                db.execSQL(
                		"CREATE TABLE word_information (_id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT, meaning text,level integer,time date);"
                );
                Log.d(TAG, "word");
                db.execSQL(
                		"CREATE TABLE user_information (_id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, answer text,true_false text,error_answer text,time date);"
                );//_id, question, answer, true_false, error_answer,time
                Log.d(TAG, "user");
                db.execSQL("INSERT INTO word_information (word,meaning,level,time)VALUES ('test','test',0,'1970-01-01 00:00:00');");
                db.execSQL("INSERT INTO user_information (question,answer,true_false,error_answer,time)VALUES ('test','test','true','test','1970-01-01 00:01:00');");
                Log.d(TAG, "insert");
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // TODO Auto-generated method stub
        		Log.d(TAG, "onUpgrade");
                db.execSQL("DROP TABLE IF EXISTS word_information");
                db.execSQL("DROP TABLE IF EXISTS user_information");
                onCreate(db);
        }
}