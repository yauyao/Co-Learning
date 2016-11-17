package com.example.co_learningnew;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


@SuppressLint("NewApi")
public class WeaknessElementary extends ListFragment {
    
	private static final String TAG = "WeaknessElementary";
	private ArrayList<String> word;
	private Activity my;
	private String user;
    @Override
    public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "onattach");
		my=activity;
		
	}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        Intent i = new Intent();
		Bundle bundle = new Bundle();		
		user="eric123987";
		word = new ArrayList<String>();
		queryAndPrintActionLogs();
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, word));
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
    	try{
    		Intent i = new Intent();
    		Bundle bundle = new Bundle();	
    		i.setClass(my, WordCard.class);
    		bundle.putString("where", java.net.URLEncoder.encode("where word = ':"+word.get(position)+":'","UTF-8"));
    		bundle.putString("kind", "weakness");
    		i.putExtras(bundle);
    		startActivityForResult(i,0);
    		Log.v(TAG,java.net.URLEncoder.encode("where word = ':"+word.get(position)+":'","UTF-8"));
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
    	
    	Runnable runnable = new Runnable(){
		    @Override
		    public void run() {
		    	try{
		    		Log.d(TAG, "internet");
		    		String uriAPI = "http://163.13.200.56:8080/test/Android-Learning/weakness1.jsp";
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
					ds.writeBytes(",");
					ds.writeBytes("1");
					
					InputStream isi = con.getInputStream();
					InputStreamReader iss = new InputStreamReader(isi);
					BufferedReader is = new BufferedReader(iss);
					String temp = is.readLine();
					
					String outtemp[] = temp.split(",");
					for(int i = 0; i<outtemp.length;i++)
						word.add(java.net.URLDecoder.decode(outtemp[i],"UTF-8"));

					Log.d(TAG, "word.size¡G"+word.size());
				}catch(Exception e){
					e.printStackTrace();
				}
		    }				    
		};
		new Thread(runnable).start();
    }
}
