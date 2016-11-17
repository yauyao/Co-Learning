package com.example.co_learningnew;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class WordofMyself extends ListFragment {
    
	private static final String TAG = "WordofMyself";
	private ArrayList tempword;
	private MyDBHelper dbhelper=null;
	private Activity my;
    
    @Override
    public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "onattach");
		this.dbhelper=new MyDBHelper(activity);
		my=activity;
		
	}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryAndPrintActionLogs();
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tempword));
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        Toast.makeText(getActivity(), "您選擇項目是 : " + tempword.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1, container, false);
    }
    private void queryAndPrintActionLogs() {
		Log.d(TAG, "queryactionlog");
        // 查詢資料庫，因為是查詢，所以使用唯讀模式
        SQLiteDatabase db = this.dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from word_information where level = 7", null);
        my.startManagingCursor(cursor);
        Log.d(TAG, "queryend");
        //db.execSQL("delete from word_information where _id=1;");
        Log.d(TAG, "read2");
        tempword=new ArrayList();
        
        while (cursor.moveToNext()) {	 
        	 tempword.add(cursor.getString(1));    
        }
    }
}

