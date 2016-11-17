package com.example.co_learningnew;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

@SuppressLint("NewApi")
public class Middle extends ListFragment {
    
	private static final String TAG = "Middle";
	private ArrayList<String> tempword;
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
    	try{
    		Intent i = new Intent();
    		Bundle bundle = new Bundle();	
    		i.setClass(my, WordCard.class);
    		bundle.putString("where", java.net.URLEncoder.encode("where word = ':"+tempword.get(position)+":'","UTF-8"));
    		bundle.putString("kind", "manage");
    		i.putExtras(bundle);
    		startActivityForResult(i,0);
    		Log.v(TAG,java.net.URLEncoder.encode("where word = ':"+tempword.get(position)+":'","UTF-8"));
    	}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1, container, false);
    }
    private void queryAndPrintActionLogs() {
    	try {
    		Log.d(TAG, "queryactionlog");
    		// 查詢資料庫，因為是查詢，所以使用唯讀模式
    		SQLiteDatabase db = this.dbhelper.getReadableDatabase();
    		Cursor cursor = db.rawQuery("select * from word_information where level = 3 or level = 4", null);
    		my.startManagingCursor(cursor);
    		tempword=new ArrayList();       
    		while (cursor.moveToNext()) {	 
				tempword.add(java.net.URLDecoder.decode(cursor.getString(1).trim(),"UTF-8"));
				Log.d(TAG, cursor.getString(1));
				Log.d(TAG, cursor.getString(1).trim());
    		}
    	}
        catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}


