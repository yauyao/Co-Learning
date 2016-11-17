package com.example.co_learningnew;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class WordCard extends Activity implements OnGestureListener,OnInitListener{
	
	private static final String TAG = "Wordcard";
	protected static final Locale[] LocaleCode = null;
	private Bundle bundle;
	private Intent intent;
	private GestureDetector detector;  
	private String uriAPI = "http://163.13.200.56:8080/test/Android-Learning/wordcard.jsp";
	private MyDBHelper helper=null;
	private ImageButton voice,important,importantpiont;
	private TextView word,howspeak,wordkind,mean,wordexample;
	private Button wordmain,nextword,wordback;
	private String where=null,inputword,inputmean,kind;
	private int level;
	private static final int REQ_TTS_STATUS_CHECK = 0; 
	private TextToSpeech mTts;
	private Handler mHandler=new Handler(); 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordcard); 
		Log.v(TAG, "oncreate");  
		
		intent = this.getIntent();
		bundle = intent.getExtras();
		where=bundle.getString("where");
		kind=bundle.getString("kind");
		Log.v(TAG, where);
		Log.v(TAG, kind);
		viewall();
		
		if(kind.equals("learning"))
			nextword.setVisibility(View.VISIBLE);
		else 
			nextword.setVisibility(View.INVISIBLE);
				
		Runnable runnable = new Runnable(){
		    @Override
		    public void run() {
		    	try{
					final String user = "eric123987";
					
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
					ds.writeBytes(kind);
					
					InputStream isi = con.getInputStream();
					InputStreamReader iss = new InputStreamReader(isi);
					BufferedReader is = new BufferedReader(iss);
					String temp = is.readLine();

					String outtemp[] = temp.split(",");
					final String wordtemp = java.net.URLDecoder.decode(outtemp[0],"UTF-8");
					inputword =outtemp[0];
					String howspeaktemp = java.net.URLDecoder.decode(outtemp[1],"UTF-8");
					String wordkindtemp = java.net.URLDecoder.decode(outtemp[2],"UTF-8");
					String meantemp = java.net.URLDecoder.decode(outtemp[3],"UTF-8");
					inputmean =outtemp[3];
					String wordexampletemp = java.net.URLDecoder.decode(outtemp[4],"UTF-8");
						   wordexampletemp += "</br>"+java.net.URLDecoder.decode(outtemp[5],"UTF-8");					
						   level =	Integer.parseInt(outtemp[6]);
					
					mHandler.post(new Runnable(){		
						@Override
						public void run() {
							if(queryAndPrintActionLogs(inputword)){
								important.setVisibility(View.INVISIBLE);
								importantpiont.setVisibility(View.VISIBLE);
							}else{
								important.setVisibility(View.VISIBLE);
								importantpiont.setVisibility(View.INVISIBLE);
							}
						}
					});	
					Log.v(TAG,"T/F："+queryAndPrintActionLogs(inputword) );
					settext(wordtemp,howspeaktemp,wordkindtemp,meantemp,wordexampletemp);
				}
				catch(Exception e){
					e.printStackTrace();
				}
		    }
		};
		new Thread(runnable).start();  
			
		Intent checkIntent = new Intent();  
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);  
        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
        
        voice.setOnClickListener(new OnClickListener() {  
     
        	public void onClick(View v) {  
        		// TODO Auto-generated method stub  
        		mTts.speak(word.getText().toString(), TextToSpeech.QUEUE_ADD, null);  
        		//朗读输入框里的内容  
        	}  
        });
        important.setOnClickListener(new OnClickListener() {  
            
        	public void onClick(View v) {  
        		// TODO Auto-generated method stub
        		Log.d(TAG, "insert"+inputword);
        		SQLiteDatabase db = helper.getReadableDatabase();
        		db.execSQL("insert into word_information(word,meaning,level,time)VALUES ('"+inputword+"','"+inputmean+"',"+level+",'"+new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(System.currentTimeMillis())+"');");
        		Log.v(TAG, "insert："+inputword+","+inputmean);  
        		db.close();
        		important.setVisibility(View.INVISIBLE);
            	importantpiont.setVisibility(View.VISIBLE);
        		//朗读输入框里的内容  
        	}  
        });
        importantpiont.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {  
        		// TODO Auto-generated method stub  
        		Log.d(TAG, "delete"+inputword);
        		SQLiteDatabase db3 = helper.getReadableDatabase();
                db3.execSQL("delete from word_information where word='"+inputword+"';");
                db3.close();
                Log.v(TAG, "delete："+inputword+","+inputmean);  
                important.setVisibility(View.VISIBLE);
            	importantpiont.setVisibility(View.INVISIBLE);
        	}  
        });
       
        wordmain.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(kind.equals("learning"))
					intent.setClass(WordCard.this, WordLearningMain.class);
				else if(kind.equals("weakness"))
					intent.setClass(WordCard.this, Weakness.class);
				else
					intent.setClass(WordCard.this, WordManageMain.class);
				
				bundle.putString("where", null);
	            intent.putExtras(bundle);
	            startActivity(intent);
			}  
        });
        nextword.setOnClickListener(new OnClickListener() {     	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent.setClass(WordCard.this, WordCard.class);
				bundle.putString("where",where);
	            intent.putExtras(bundle);
	            startActivity(intent);
			}  
        });
        wordback.setOnClickListener(new OnClickListener() {
	
        	@Override
        	public void onClick(View arg0) {
        		// TODO Auto-generated method stub
        			intent.setClass(WordCard.this, Main.class);
        			bundle.putString("where", null);
        			intent.putExtras(bundle);
        			startActivity(intent);
        	}  
        });
        
        detector = new GestureDetector(this);  
	}
	private void viewall(){
		Log.v(TAG, "view");  
		voice=(ImageButton)findViewById(R.id.voice);
		important=(ImageButton)findViewById(R.id.important);
		word=(TextView)findViewById(R.id.word);
		howspeak=(TextView)findViewById(R.id.howspeak);
		wordkind=(TextView)findViewById(R.id.wordkind);
		mean=(TextView)findViewById(R.id.mean);
		wordexample=(TextView)findViewById(R.id.wordexample);
		wordmain=(Button)findViewById(R.id.wordmain);
		nextword=(Button)findViewById(R.id.nextword);
		wordback=(Button)findViewById(R.id.wordback);
		importantpiont=(ImageButton)findViewById(R.id.importantpiont);
		helper=new MyDBHelper(this);
	}
	@Override  
	    public boolean onTouchEvent(MotionEvent event) {  
	        Log.i("Fling", "Activity onTouchEvent!");  
	        return this.detector.onTouchEvent(event);  
	    }
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float X,
			float Y) {
		// TODO Auto-generated method stub
		try{
	    	if(e1.getX()-e2.getX()>120){
	    		intent.setClass(WordCard.this, WordCard.class);
	    		bundle.putString("where",where);
	            intent.putExtras(bundle);
	            startActivity(intent);
				return true;
	    	}else if(e1.getX()-e2.getX()<-120){
	    		intent.setClass(WordCard.this, WordCard.class);
	    		bundle.putString("where",where);
	            intent.putExtras(bundle);
	            startActivity(intent);
				return true;
	    	}
	    }
	    catch(Exception ex){
	    	System.out.println(ex.getMessage());
	    	return false;
	    }
		return false;
	}
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	private void settext(final String arg1,final String arg2,final String arg3,final String arg4,final String arg5){
		 mHandler.post(new Runnable(){		
		    	@Override
				public void run() {
		    		word.setText(arg1);
		    		howspeak.setText(arg2);
		    		wordkind.setText("詞性："+arg3);
		    		mean.setText("KK："+arg4);
		    		wordexample.setText(Html.fromHtml("例句： </br>"+arg5));
				}
			});	
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
	@Override
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
 	           voice.setEnabled(false);  
 	        }  
 	        else  
 	        {  
 	        	Log.v(TAG, " TTS apparently available");  
 	        	voice.setEnabled(true);  
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

    private boolean queryAndPrintActionLogs(String arg) {
    	try{
    		Log.d(TAG, "queryactionlog："+arg);
    		SQLiteDatabase db2 = this.helper.getReadableDatabase();
    		Cursor cursor = db2.rawQuery("select * from word_information where word='"+arg+"'", null);
    		Log.d(TAG, "inputword："+inputword);
    		//rawQuery只適用於查詢，execSQL用於其於三種
    		this.startManagingCursor(cursor);    
    		if(cursor.getCount()>0){
    			return true; 
    		}else	
    			return false;
    	}
    	catch(Exception e){
			e.printStackTrace();
			return false;
		}
    }	
}
