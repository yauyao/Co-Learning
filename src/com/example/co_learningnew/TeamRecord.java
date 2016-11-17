package com.example.co_learningnew;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.co_learningnew.WordtestChoiceself.changetext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TeamRecord extends Activity {
	private String TAG="TeamRecord";
	private Activity a = this;//抓此程式的主要活動，好讓非主執行緒的可以有效讀取
	private ListView wordlist ;
	private Bundle bundle;
	private Intent intent;
	private String user = null;
	private ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	private SimpleAdapter adapter;
	private Handler mHandler=new Handler(); 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teamrecord);		
		
		user = "eric123987";
		wordlist = (ListView) findViewById(R.id.teamlist);
		new query();		
    }
	class query implements Runnable{
    	//主要是查詢使用者現階段的rank值，若無則使用方案一
    	private Thread t;
    	private ArrayList<String> userlist,rank,time;
    	public query(){
    		t = new Thread(this);
    		t.start();
    	}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{				
				String uriAPI = "http://163.13.200.56:8080/test/Android-Learning/teamrecord.jsp";//判斷另外寫好了
				URL url =new URL(uriAPI);
				HttpURLConnection con=(HttpURLConnection)url.openConnection();
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false);
				con.setRequestMethod("POST");
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("Charset", "UTF-8");
			
				DataOutputStream ds = new DataOutputStream(con.getOutputStream());
				ds.writeBytes(user);
				
				InputStream isi = con.getInputStream();
				InputStreamReader iss = new InputStreamReader(isi);
				BufferedReader is = new BufferedReader(iss);
				String temp = is.readLine();				
				String outtemp[] = temp.split(",");	
				
				userlist = new ArrayList<String>();
				rank = new ArrayList<String>();
				time = new ArrayList<String>();
				userlist.add("組員名稱");
				rank.add("現在等級");
				time.add("累計次數");
				
				for(int j=0;j<outtemp.length;j++){
					switch(j%3){
						case 2:
							if(outtemp[j].equals("0"))
								time.add("沒有記錄");
							else
								time.add(outtemp[j]);
							break;
						case 1:
							if(outtemp[j].equals("1")){
								rank.add("初級");
								break;
							}else if (outtemp[j].equals("2")){
								rank.add("中級");
								break;
							}else if (outtemp[j].equals("3")){
								rank.add("高級");
								break;
							}else{
								rank.add("沒有記錄");
								break;
							}
						case 0:
							userlist.add(outtemp[j]);
							break;
					}
				}	
				for(int i=0; i<userlist.size(); i++){
					HashMap<String,Object> item = new HashMap<String,Object>();
					item.put( "user", userlist.get(i));
					item.put( "rank",rank.get(i));
					item.put("time",time.get(i));
					list.add( item );
					Log.d(TAG, "user："+userlist.get(i)+",rank："+rank.get(i)+",time："+time.get(i));
				}
				Log.d(TAG, "iteminput");
				adapter = new SimpleAdapter(a, 
						list,R.layout.teamlistview,
						new String[] { "user","rank","time" },
						new int[] { R.id.teamtitle, R.id.teaminfo,R.id.userranktime } );				
				Log.d(TAG, "adapterinput");	
				
				mHandler.post(new Runnable(){		
					@Override
					public void run() {
						//ListActivity設定adapter
						wordlist.setAdapter( adapter );
						//啟用按鍵過濾功能，這兩行資料都會進行過濾
						wordlist.setTextFilterEnabled(true);
					}
				});
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}    	
    }
}
