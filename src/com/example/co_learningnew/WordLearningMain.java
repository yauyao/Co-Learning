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
		//���ƥ[�JArrayList��
		for(int i=0; i<mPlaces.length; i++){
			HashMap<String,Object> item = new HashMap<String,Object>();
			item.put( "pic", mPics[i]);
			item.put( "title",mPlaces[i] );
			item.put("content", mFoods[i]);
			list.add( item );
		}
		Log.d(TAG, "iteminput");
		//�s�WSimpleAdapter
		adapter = new SimpleAdapter( 
				 this, 
				 list,
				 R.layout.listview,
				 new String[] { "pic","title","content" },
				 new int[] { R.id.img, R.id.title, R.id.info } );
		Log.d(TAG, "adapterinput");
		//ListActivity�]�wadapter
		wordlist.setAdapter( adapter );
		//�ҥΫ���L�o�\��A�o����Ƴ��|�i��L�o
		wordlist.setTextFilterEnabled(true);
		
		wordlist.setOnItemClickListener(new AdapterView.OnItemClickListener() 
	    {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
			{
				Log.d(TAG, "click");
				// TODO Auto-generated method stub
				//�����ϥΪ̿�����\��s��
				int position = arg2;
				Goto(position);	
			}	    	
		});
	}
 
	private static final String[] mPlaces = new String[] {
		"���", "����", "����"
	};
 
	private static final String[] mFoods = new String[] {
		"�Ш|���q1200�r", "�Ш|���q4000�r", "�Ш|���q7000�r"
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
				case 0://���
					i.setClass(WordLearningMain.this, WordCard.class);
					bundle.putString("where", java.net.URLEncoder.encode("where level =1 or level =2", "UTF-8"));
					bundle.putString("kind", "learning");
					i.putExtras(bundle);
					startActivity(i);
					break;
				case 1://����
					i.setClass(WordLearningMain.this, WordCard.class);
					bundle.putString("where", java.net.URLEncoder.encode("where level =3 or level =4", "UTF-8"));
					bundle.putString("kind", "learning");
					i.putExtras(bundle);
					startActivity(i);
					break;
				case 2://����
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