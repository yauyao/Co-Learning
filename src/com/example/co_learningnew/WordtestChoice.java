package com.example.co_learningnew;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class WordtestChoice extends Activity {
	private static final String TAG = "WordtestChoice";
	Bundle bundle;
	Intent intent;
	private RadioGroup  radioGroup1=null;
	private RadioButton  radio0,radio1,radio2;
	private TextView question;
	private Button answer;
	private String where=null,ranktemp=null,r1="",r2="",r3="",r4="",selectans=null,begintime=null,finalshow="";
	int record,second;
	final String user = "eric123987";
	
	private Handler mHandler=new Handler(); 
	String[] result;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordtestchoice); 
		Log.v(TAG, "oncreate");
		viewall();
		begintime = getDateTime();
		intent = new Intent();
		bundle = this.getIntent().getExtras();
		ranktemp = bundle.getString("rank");
		record = bundle.getInt("record");
		second =  Integer.parseInt(bundle.getString("second"));
		finalshow = bundle.getString("finalshow"); 
		Log.v(TAG, "finalshow："+finalshow); 
		
		if(second==record){
			openDialog();
			Log.v(TAG, "record："+record); 
		}else{
			Log.v(TAG, "record："+record); 
		}
		
		try{
			if(ranktemp.equals("初級"))
				where =java.net.URLEncoder.encode("where word = level = 1 or level = 2","UTF-8");
			else if(ranktemp.equals("中級"))
				where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
			else if(ranktemp.equals("高級"))
				where =java.net.URLEncoder.encode("where word = level = 5 or level = 6","UTF-8");
			else 
				where="myself";
			Log.v(TAG, "where："+where);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Runnable runnable = new Runnable(){
		    @Override
		    public void run() {
		    	try{
		    		
		    		String uriAPI = "http://163.13.200.56:8080/test/Android-Learning/testchoice.jsp";
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
					ds.writeBytes(where);
					ds.writeBytes(",");
					ds.writeBytes(""+record);
					ds.writeBytes(",");
					ds.writeBytes(""+second);
					
					InputStream isi = con.getInputStream();
					InputStreamReader iss = new InputStreamReader(isi);
					BufferedReader is = new BufferedReader(iss);
					String temp = is.readLine();

					String outtemp[] = temp.split(",");
					r1=java.net.URLDecoder.decode(outtemp[0],"UTF-8");
					r2=java.net.URLDecoder.decode(outtemp[1],"UTF-8");
					r3=java.net.URLDecoder.decode(outtemp[2],"UTF-8");
					r4=java.net.URLDecoder.decode(outtemp[3],"UTF-8");
					
					randon(r1,r2,r3,r4);
					Log.v(TAG, "r1："+r1+"r2："+r2+"r3："+r3+"r4："+r4);
				}
		    	
				catch(Exception e){
					e.printStackTrace();
				}
		    }
		
		    
		};
		new Thread(runnable).start();
		
		answer.setOnClickListener(new OnClickListener() {  
	            
			public void onClick(View v) {  
        		// TODO Auto-generated method stub
				
				Log.v(TAG,selectans);
        		Runnable runnable = new Runnable(){
        		    @Override
        		    public void run() {//將結果傳送出去
        		    	try{
        					final String user = "eric123987";
        					String uriAPI = "http://163.13.200.56:8080/test/Android-Learning/userinsert.jsp";
        					URL url =new URL(uriAPI);
        					HttpURLConnection con=(HttpURLConnection)url.openConnection();
        					con.setDoInput(true);
        					con.setDoOutput(true);
        					con.setUseCaches(false);
        					con.setRequestMethod("POST");
        					con.setRequestProperty("Connection", "Keep-Alive");
        					con.setRequestProperty("Charset", "UTF-8");
        					String ans;
        					if(r2.equals(selectans)){
        						ans="null";
        						finalshow+="第"+(record+1)+"題目為："+r1+" 答案是："+r2+" \n";
        					}else{
        						ans=java.net.URLEncoder.encode(selectans,"UTF-8");
        						finalshow+="第"+(record+1)+"題目為："+r1+" 答案是："+r2+" 你選的是："+selectans+" \n";
        					}
        					
        					Log.v(TAG, finalshow);
        					String out=user+","+java.net.URLEncoder.encode(r1,"UTF-8")+","+ans+","+java.net.URLEncoder.encode(begintime,"UTF-8")+","+java.net.URLEncoder.encode(getDateTime(),"UTF-8")+",choice";
        					Log.v(TAG, out);
        					
        					DataOutputStream ds = new DataOutputStream(con.getOutputStream());
        					ds.writeBytes(out);        				       					
        					
        					InputStream isi = con.getInputStream();
        					InputStreamReader iss = new InputStreamReader(isi);
        					BufferedReader is = new BufferedReader(iss);
        					String temp = is.readLine();

        					String outtemp[] = temp.split(",");
        					String s =outtemp[0];
        					
        					Log.v(TAG, s);
        					if(s.equals("OK")){//儲存成功才可以到下一步
        						intent.setClass(WordtestChoice.this, WordtestChoice.class);
        						bundle.putString("rank", ranktemp);
        						bundle.putInt("record", record+1);
        						bundle.putString("finalshow",finalshow);
        						bundle.putString("second",""+second);
        						intent.putExtras(bundle);
        						startActivity(intent);    
        					}
        				}
        				catch(Exception e){
        					e.printStackTrace();
        				}
        		    }
        		};
        		new Thread(runnable).start();  
        	}
		});
		
		radioGroup1.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg1)
				{
					case R.id.testradio0:
						selectans=radio0.getText().toString();
						Log.v(TAG,selectans);
						break;
					case R.id.testradio1:
						selectans=radio1.getText().toString();
						Log.v(TAG,selectans);
						break;
					case R.id.testradio2:
						selectans=radio2.getText().toString();
						Log.v(TAG,selectans);
						break;
					default:
						selectans="NO select";
						Log.v(TAG,selectans);
				}
			}			
		});
	}
	private void viewall(){
		question = (TextView)findViewById(R.id.question);
		radioGroup1=(RadioGroup)findViewById(R.id.testradioGroup);
		radio1 = (RadioButton)findViewById(R.id.testradio1);
		radio0 = (RadioButton)findViewById(R.id.testradio0);
		radio2 = (RadioButton)findViewById(R.id.testradio2);
		answer =(Button)findViewById(R.id.choiceansbutt);
	}
	private void randon(final String arg1,String arg2,String arg3,String arg4){
		Log.v(TAG,"randon");
		Random random = new Random();
		result= new String[]{arg2,arg3,arg4};
		for(int i=0; i < result.length; i ++){ // 這個迴圈也可以只跑到result.length/2
			int index = random.nextInt(result.length);
		    //交換 i 跟index的原素
		    String tmp = result[index];
		    result[index] = result[i];
		    result[i] = tmp;
		}
		mHandler.post(new Runnable(){		
			@Override
			public void run() {
				selectans=result[0];
				question.setText(arg1);
				radio0.setText(result[0]);
				radio1.setText(result[1]);
				radio2.setText(result[2]);
			}
		});
		for(int i=0; i < result.length; i ++){
		    Log.v(TAG,"result["+(i+1)+"]=" + result[i]);	
		}

	}
	public String getDateTime(){
    	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    	Date date = new Date();
    	String strDate = sdFormat.format(date);
    	return strDate;
    }
	private void openDialog() {
	    	new AlertDialog.Builder(this)
	    		.setTitle("Final")
	    		.setMessage(finalshow)
	    		.setPositiveButton("OK",
	    			new DialogInterface.OnClickListener() {
	     
	    				@Override
	    				public void onClick(DialogInterface dialog, int which) {
	    					// TODO Auto-generated method stub
	    					//按下按鈕後執行的動作，沒寫則退出Dialog
	    					intent.setClass(WordtestChoice.this, WordtestMain.class);
							intent.putExtras(bundle);
							startActivity(intent); 
	    				}
	    			}
	    		)
	    		.show();
	}
	public void onBackPressed() {
   	 Toast toast = Toast.makeText(WordtestChoice.this,"還在測驗中，不可返回", Toast.LENGTH_LONG);
   	 //顯示Toast
   	 toast.show();

   }
	
}