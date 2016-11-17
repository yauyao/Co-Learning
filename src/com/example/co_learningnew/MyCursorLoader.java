package com.example.co_learningnew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;
 
@SuppressLint("NewApi")
public class MyCursorLoader extends CursorLoader{
	private static final String TAG = "MyCursorloader";
	private MyDBHelper dbhelper=null;
	
    String[] mContactProjection={"_id","word"};
 
    @SuppressLint("NewApi")
	private Context mContext;
    public MyCursorLoader(Context context) {
        super(context);
        mContext = context;
        dbhelper=new MyDBHelper(mContext);
        Log.d(TAG, "MyCursor");
    }
    /**
     * 查询数据等操作放在这里执行 
     */
    @Override
    protected Cursor onLoadInBackground() {
    	Log.d(TAG, "onLoadInBackground");
    	SQLiteDatabase db = this.dbhelper.getReadableDatabase();
    	Log.d(TAG, "dbbegin");
        Cursor cursor = db.query("word_information", 
        		mContactProjection, null, null, null, null, null);
        Log.d(TAG, "query");
        return cursor;
    }
}