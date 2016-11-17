/* second 為指定題數，所以應該 令圈數 n = second / 5 
 * 方案一：
		 * record 為現在題數，record % 5 
		 * 當 = 0 ，初級
		 * 當 = 1 ，初級
		 * 當 = 2 ，中級
		 * 當 = 3 ，中級
		 * 當 = 4 ，高級
		 * 以上為當第一次記錄都沒有的時候
* 方案二：
		 * 以下為有記錄時的內容：(若使用者被分類為初級) 
		 * 當 = 0 ，初級
		 * 當 = 1 ，初級
		 * 當 = 2 ，初級
		 * 當 = 3 ，中級
		 * 當 = 4 ，中級(當分數達60%，則判定為可有效升級yes)
		 * */
package com.example.co_learningnew;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WordtestInputself extends Activity implements OnInitListener{
	private static final String TAG = "WordtestInputself";
	private static final int REQ_TTS_STATUS_CHECK = 0; 
	private TextToSpeech mTts;
	private Bundle bundle;
	private Intent intent;
	private ImageButton testvoice;
	private TextView question;
	private EditText anstext;
	private Button ansbutton;
	private boolean first = false;
	int record,second,piont,rank,ranktime;
	private String where=null,ranktemp=null,r1=null,r2=null,begintime=null,finalshow="",user="";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordtestinput); 
		Log.v(TAG, "oncreate"); 
		viewall();
		Log.v(TAG, "firsttest："+first);
		begintime = getDateTime();
		intent = new Intent();
		bundle = this.getIntent().getExtras();
		user = "eric123987";//使用者名稱
		ranktime = bundle.getInt("ranktime");//變換等級過幾次
		ranktemp = bundle.getString("rank");//現在等級
		second = Integer.parseInt(bundle.getString("second"));//要測驗次數
		record = bundle.getInt("record");//以測驗次數
		piont = bundle.getInt("piont");//現有分數
		finalshow = bundle.getString("finalshow");//已完成之題目內容  		
		
		if(second==record){
			openDialog();
			Log.v(TAG, "record："+record); 
		}else{
			Log.v(TAG, "record："+record); 
		}
		new query();
		Intent checkIntent = new Intent();  
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);  
        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
        
        testvoice.setOnClickListener(new OnClickListener() {  
            
        	public void onClick(View v) {  
        		// TODO Auto-generated method stub  
        		mTts.speak( r1, TextToSpeech.QUEUE_ADD, null);  
        		//朗读输入框里的内容  
        	}  
        });
        ansbutton.setOnClickListener(new OnClickListener() {  
            
        	public void onClick(View v) {  
        		// TODO Auto-generated method stub       		    		            
        		Runnable runnable = new Runnable(){
        		    @Override
        		    public void run() {//將結果傳送出去
        		    	try{       					
        					String uriAPI = "http://163.13.200.56:8080/test/Android-Learning/userinsert1.jsp";
        					URL url =new URL(uriAPI);
        					HttpURLConnection con=(HttpURLConnection)url.openConnection();
        					con.setDoInput(true);
        					con.setDoOutput(true);
        					con.setUseCaches(false);
        					con.setRequestMethod("POST");
        					con.setRequestProperty("Connection", "Keep-Alive");
        					con.setRequestProperty("Charset", "UTF-8");
        					String ans;
        					Log.v(TAG, "click outtemp[1]&rank："+rank);
        					if (r1.trim().equals(anstext.getText().toString())){
        						ans="null";
        						finalshow+="第"+(record+1)+" 題目為："+r1+" 答案是："+r2+" \n";
        						piont += 100/second;
        					}else{
        						ans=anstext.getText().toString();     
        						finalshow+="第"+(record+1)+" 題目為："+r1+" 答案是："+r2+" 你選的是："+anstext.getText().toString()+" \n";
        					}
        					String out=user+","+java.net.URLEncoder.encode(r1,"UTF-8")+","+ans+","+java.net.URLEncoder.encode(begintime,"UTF-8")+","+java.net.URLEncoder.encode(getDateTime(),"UTF-8")+",input,"+piont+","+second+","+record+","+rank+","+ranktime;
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
        						int output=record+1;
        						intent.setClass(WordtestInputself.this, WordtestInputself.class);
        						bundle.putString("rank", ranktemp);
        						bundle.putInt("record", output);
        						bundle.putInt("piont", piont);
        						Log.v(TAG, ""+output);
        						bundle.putString("second", ""+second);
        						bundle.putString("finalshow",finalshow);
        						Log.v(TAG, finalshow);
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
	}
	public void viewall(){
		Log.v(TAG, "view");  
		testvoice=(ImageButton)findViewById(R.id.testvoice);
		question=(TextView)findViewById(R.id.question);
		anstext=(EditText)findViewById(R.id.answertext);
		ansbutton=(Button)findViewById(R.id.ansbutton);
	
	}
	
	protected  void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if(requestCode == REQ_TTS_STATUS_CHECK)  
        {  
            switch (resultCode) {  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:{  
                //这个返回结果表明TTS Engine可以用  
                mTts = new TextToSpeech(this, this);  
                Log.v(TAG, "TTS Engine is installed!");       
            }                    
                break;  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:  
                //需要的语音数据已损坏  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:  
                //缺少需要语言的语音数据  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:{  
                //缺少需要语言的发音数据        
                //这三种情况都表明数据有错,重新下载安装需要的数据  
                Log.v(TAG, "Need language stuff:"+resultCode);  
                Intent dataIntent = new Intent();  
                dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);  
                startActivity(dataIntent);            
            }  
                break;  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:  
                //检查失败  
            default:  
                Log.v(TAG, "Got a failure. TTS apparently not available");  
                break;  
            }  
        }else{  
            //其他Intent返回的结果  
        	}  
        }
	
	public void onInit(int status) {
		// TODO Auto-generated method stub
		 if(status == TextToSpeech.SUCCESS)  
 	    {  
 	        int result = mTts.setLanguage(Locale.US);  
 	        //设置发音语言  
 	        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)  
 	        //判断语言是否可用  
 	        {  
 	            Log.v(TAG, "Language is not available");  
 	            testvoice.setEnabled(false);  
 	        }  
 	        else  
 	        {  
 	        	Log.v(TAG, " TTS apparently available");  
 	        	testvoice.setEnabled(true);  
 	        }  
 	    }  
	}
	
	protected void onPause() {  
        // TODO Auto-generated method stub  
        super.onPause();  
        if(mTts != null){ 
            //activity暂停时也停止TTS  
            mTts.stop();  
        }  
    }  
      
    @Override  
    protected void onDestroy() {  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        //释放TTS的资源  
        mTts.shutdown();  
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
    					intent.setClass(WordtestInputself.this, WordtestMain.class);
						intent.putExtras(bundle);
						startActivity(intent); 
    				}
    			}
    		)
    		.show();
    }
      	
    class query implements Runnable{
    	//主要是查詢使用者現階段的rank值，若無則使用方案一
    	Thread t;
    	public query(){
    		t = new Thread(this);
    		t.start();
    	}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{					
				String uriAPI = "http://163.13.200.56:8080/test/Android-Learning/data.jsp";//判斷另外寫好了
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
				ds.writeBytes(",input");
				
				InputStream isi = con.getInputStream();
				InputStreamReader iss = new InputStreamReader(isi);
				BufferedReader is = new BufferedReader(iss);
				String temp = is.readLine();

				String outtemp[] = temp.split(",");
				if(outtemp[0].equals("yes"))
					first=true;
				else
					first=false;				
				
				Log.v(TAG, "query："+first);				
				new changetext(outtemp[1]);
				rank=Integer.parseInt(outtemp[1]);
				Log.v(TAG, "outtemp[1]&rank："+rank);
				ranktime = Integer.parseInt(outtemp[2]);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
    	
    }

    class changetext implements Runnable{
    	Thread t;
    	String a = null;
    	public changetext(String arg){
    		t = new Thread(this);
    		t.start();
    		a = arg;
    	}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				if(first){
					if(a.equals("1")){
						//當使用者的rank值為初級時
						switch(record%5){
							case 0:
								where =java.net.URLEncoder.encode("where word = level = 1 or level = 2","UTF-8");
								break;
							case 1:
								where =java.net.URLEncoder.encode("where word = level = 1 or level = 2","UTF-8");
								break;
							case 2:
								where =java.net.URLEncoder.encode("where word = level = 1 or level = 2","UTF-8");
								break;
							case 3:
								where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
								break;
							case 4:
								where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
								break;
						}
					}
					else if(a.equals("2")){
						//當使用者的rank值為中級時
						switch(record%5){
							case 0:
								where =java.net.URLEncoder.encode("where word = level = 1 or level = 2","UTF-8");
								break;
							case 1:
								where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
								break;
							case 2:
								where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
								break;
							case 3:
								where =java.net.URLEncoder.encode("where word = level = 5 or level = 6","UTF-8");
								break;
							case 4:
								where =java.net.URLEncoder.encode("where word = level = 5 or level = 6","UTF-8");
								break;
						}
					}
					else if(a.equals("3")){
						//當使用者的rank值為高級時
						switch(record%5){
							case 0:
								where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
								break;
							case 1:
								where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
								break;
							case 2:
								where =java.net.URLEncoder.encode("where word = level = 5 or level = 6","UTF-8");
								break;
							case 3:
								where =java.net.URLEncoder.encode("where word = level = 5 or level = 6","UTF-8");
								break;
							case 4:
								where =java.net.URLEncoder.encode("where word = level = 5 or level = 6","UTF-8");
								break;
						}
					}
				} else {
					switch(record%5){
					case 0:
						where =java.net.URLEncoder.encode("where word = level = 1 or level = 2","UTF-8");
						break;
					case 1:
						where =java.net.URLEncoder.encode("where word = level = 1 or level = 2","UTF-8");
						break;
					case 2:
						where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
						break;
					case 3:
						where =java.net.URLEncoder.encode("where word = level = 3 or level = 4","UTF-8");
						break;
					case 4:
						where =java.net.URLEncoder.encode("where word = level = 5 or level = 6","UTF-8");
						break;
					}
				}
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
				Log.v(TAG, "where："+where);
				InputStream isi = con.getInputStream();
				InputStreamReader iss = new InputStreamReader(isi);
				BufferedReader is = new BufferedReader(iss);
				String temp = is.readLine();

				String outtemp[] = temp.split(",");
				r1=java.net.URLDecoder.decode(outtemp[0],"UTF-8");
				r2=java.net.URLDecoder.decode(outtemp[1],"UTF-8");
				Log.v(TAG, r1+","+r2);
				question.setText(r2);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
    	
    }
    public void onBackPressed() {
   	 Toast toast = Toast.makeText(WordtestInputself.this,"還在測驗中，不可返回", Toast.LENGTH_LONG);
   	 //顯示Toast
   	 toast.show();

   }
}