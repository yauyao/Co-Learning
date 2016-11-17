package com.example.co_learningnew;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class WordLearningMain extends Activity {
 /** Called when the activity is first created. */
	private String TAG="WordLearning";
	ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private SimpleAdapter adapter;
	ListView wordlist ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordlearningmain);
		
		wordlist = (ListView) findViewById(R.id.listView1);
		Log.d(TAG, "view");
		//把資料加入ArrayList中
		for(int i=0; i<mPlaces.length; i++){
			HashMap<String,Object> item = new HashMap<String,Object>();
			item.put( "pic", mPics[i]);
			item.put( "title",mPlaces[i] );
			item.put("content", mFoods[i]);
			list.add( item );
		}
		Log.d(TAG, "iteminput");
		//新增SimpleAdapter
		adapter = new SimpleAdapter( 
				 this, 
				 list,
				 R.layout.listview,
				 new String[] { "pic","title","content" },
				 new int[] { R.id.img, R.id.title, R.id.info } );
		Log.d(TAG, "adapterinput");
		//ListActivity設定adapter
		wordlist.setAdapter( adapter );
		//啟用按鍵過濾功能，這兩行資料都會進行過濾
		wordlist.setTextFilterEnabled(true);
		
		wordlist.setOnItemClickListener(new AdapterView.OnItemClickListener() 
	    {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
			{
				Log.d(TAG, "click");
				// TODO Auto-generated method stub
				//紀錄使用者選取的功能編號
				int position = arg2;
				Goto(position);	
			}	    	
		});
	}
 
	private static final String[] mPlaces = new String[] {
		"初級", "中級", "高級"
	};
 
	private static final String[] mFoods = new String[] {
		"教育部訂1200字", "教育部訂4000字", "教育部訂7000字"
	};
	private static final int[] mPics=new int[]{
		 R.drawable.elementary,R.drawable.middle,R.drawable.high
	};
	private void Goto(int position){
		try {
			Intent i = new Intent();
			Bundle bundle = new Bundle();
			Log.d(TAG, "gotobegin");
			switch(position){
				case 0://初級
					i.setClass(WordLearningMain.this, WordCard.class);
					bundle.putString("where", java.net.URLEncoder.encode("where level =1 or level =2", "UTF-8"));
					bundle.putString("kind", "learning");
					i.putExtras(bundle);
					startActivity(i);
					break;
				case 1://中級
					i.setClass(WordLearningMain.this, WordCard.class);
					bundle.putString("where", java.net.URLEncoder.encode("where level =3 or level =4", "UTF-8"));
					bundle.putString("kind", "learning");
					i.putExtras(bundle);
					startActivity(i);
					break;
				case 2://高級
					i.setClass(WordLearningMain.this, WordCard.class);
					bundle.putString("where", java.net.URLEncoder.encode("where level =5 or level =6", "UTF-8"));
					bundle.putString("kind", "learning");
					i.putExtras(bundle);
					startActivity(i);
					break;
			}
			Log.d(TAG, "gotoend");
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}