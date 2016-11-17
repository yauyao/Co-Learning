package com.example.co_learningnew;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity implements OnInitListener{
	private static final String TAG = "Game";
	private static final int REQ_TTS_STATUS_CHECK = 0;
	private TextView question,username,bulletin;
	private EditText answer;
	private TextToSpeech mTts;
	private Button enter,connect,exit;
	private ImageView gameimage;
	private boolean isConnected = false,RUN_THREAD = true;
	private PrintWriter writer;
	private Socket socket;
	private BufferedReader reader;
	private MessageThread messageThread;
	private ImageButton voice;
	private Handler mHandler=new Handler();
	private Bundle bundle;
	private Intent intent;
	private String voicecontent=null,user=null;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game); 
		Log.v(TAG, "oncreate");		
		viewall();		
		intent = new Intent();
		bundle = new Bundle();
		user="eric123987";
		
		Intent checkIntent = new Intent();  
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);  
        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
        
        username.setText("UserName ："+user);
		enter.setOnClickListener(new OnClickListener() {             
			public void onClick(View v) {
				Log.v(TAG, "寄出答案");
					send();
			}
		});
		
		connect.setOnClickListener(new OnClickListener() {  
            
			public void onClick(View v) {  
				Log.v(TAG, "連接開始");	
				if (isConnected) {
					Toast toast = Toast.makeText(Game.this,"已連接至遠端", Toast.LENGTH_LONG);			
					//顯示Toast			
					toast.show();
					return;
				}else{
					try {
						Log.v(TAG, "connect begin ："+isConnected);
						connectServer(6999,"163.13.200.56","eric123987");
						Log.v(TAG, "connect end ："+isConnected);
					
						if (isConnected) {
							Toast toast = Toast.makeText(Game.this,"連結成功", Toast.LENGTH_LONG);			
							//顯示Toast			
							toast.show();
						}else{						
							Toast toast = Toast.makeText(Game.this,"連結失敗", Toast.LENGTH_LONG);			
							//顯示Toast			
							toast.show();
						}
					} catch (Exception exc) {
						Log.v(TAG, exc+"");
					}
				}
			}
		});
		exit.setOnClickListener(new OnClickListener() {  
    
			public void onClick(View v) {
				Log.v(TAG, "離開遊戲");	
				intent.setClass(Game.this, Main.class);
				intent.putExtras(bundle);
				startActivity(intent); 
			}	
		});
		
		voice.setOnClickListener(new OnClickListener() {  
            
        	public void onClick(View v) {  
        		// TODO Auto-generated method stub  
        		mTts.speak( voicecontent, TextToSpeech.QUEUE_ADD, null);  
        		//朗讀題目裡的英文內容  
        	}  
        });
	}
	
	private void viewall(){
		question = (TextView)findViewById(R.id.question);
		bulletin = (TextView)findViewById(R.id.bulletin);
		username = (TextView)findViewById(R.id.username);
		answer = (EditText)findViewById(R.id.answer);
		enter = (Button)findViewById(R.id.enter);
		connect = (Button)findViewById(R.id.connect);
		exit = (Button)findViewById(R.id.exit);
		voice = (ImageButton)findViewById(R.id.gamevoice);
		gameimage = (ImageView)findViewById(R.id.gameimage);
		Log.v(TAG, "viewall");
	}
	
	public void send() {
		if (!isConnected) {
			 //利用Toast的靜態函式makeText來建立Toast物件			
			Toast toast = Toast.makeText(Game.this,"還未連接至遠端", Toast.LENGTH_LONG);			
			//顯示Toast			
			toast.show();
			return;
		}
		String message = answer.getText().toString().trim();
		if (message == null || message.equals("")) {
			Toast toast = Toast.makeText(Game.this,"訊息欄不能為空", Toast.LENGTH_LONG);			
			//顯示Toast			
			toast.show();
			return;
		}
		sendMessage(user + "@" + "ALL" + "@" + message);
		answer.setText(null);
	}
	
	public void sendMessage(String message){
		writer.println(message);
		writer.flush();
	}
	
	public boolean connectServer(final int port, final String hostIp, final String name) {
		// 連接伺服器
		Log.v(TAG,"connectServer");
		
		Runnable runnable = new Runnable(){			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Log.v(TAG,"run中");
					socket = new Socket(hostIp, port);// 根據port號和伺服器ip建立連接
					writer = new PrintWriter(socket.getOutputStream());
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					// 發送客戶端用戶基本信息(如：用戶名和ip位置)
					sendMessage(name + "@" + socket.getLocalAddress().toString());
					// 開啟接受訊息線程
					messageThread = new MessageThread(reader);
					messageThread.start();					
					mHandler.post(new Runnable(){		
						@Override
						public void run() {
							isConnected = true;
						}
					});
					Log.v(TAG,"connect run() ："+isConnected);
				} catch (Exception e) {				
					Log.v(TAG, e+"");
					mHandler.post(new Runnable(){		
						@Override
						public void run() {
							isConnected = true;
						}
					});// 未連上	
				}
			}
		};
		new Thread(runnable).start();
		Log.v(TAG,"connectServer true："+isConnected);
		return isConnected;		 
	}
		
	/**
	 * 客戶端主動斷開訊息(使用者手動去關閉的意思，如換頁，關閉手機等)
	 */
	@SuppressWarnings("deprecation")
	public synchronized void closeConnection() {
		Log.v(TAG,"connectServer");
		Runnable runnable = new Runnable(){			
			@Override
			public void run() {
				try {
					sendMessage("CLOSE");// 發送中斷連接命令給伺服器
					RUN_THREAD = false;
					messageThread.interrupt();
					messageThread = null;// 停止接受消息線程
					// 釋放資源
					if (reader != null) {
						reader.close();
					}
					if (writer != null) {
						writer.close();
					}
					if (socket != null) {
						socket.close();
					}
					isConnected = false;			
				} catch (Exception e1) {
					e1.printStackTrace();
					isConnected = true;
		}
			}
		};
		new Thread(runnable).start();	
	}
	
	class MessageThread extends Thread {
		private BufferedReader reader;
		// 接收訊息線程的建構值
		public MessageThread(BufferedReader reader) {
			this.reader = reader;
		}
		// 被動關閉連結(如因為伺服器沒開，所以只好宣告關掉)
		public synchronized void closeCon() throws Exception {
			//synchronized 同步相關文章 http://www.jackforfun.com/2007/07/java-synchronized.html
			// 自動關閉連接並釋放資源
			RUN_THREAD = false;
			messageThread.interrupt();
			messageThread = null;
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;// 修改狀態為中斷
		}

		public void run() {//接受所有來自伺服端的指令並加以轉達
			String message = "";
			while (RUN_THREAD) {
				try {					
					message = reader.readLine();
					Log.v(TAG, "run() meassage："+message);	
					StringTokenizer stringTokenizer = new StringTokenizer(
							message, "@");
					String command = stringTokenizer.nextToken();//接收類別
					String content = stringTokenizer.nextToken();
					String member = stringTokenizer.nextToken();
					String voice = stringTokenizer.nextToken();// 命令
					Log.v(TAG, "run() command："+command);
					Log.v(TAG, "run() content："+content);
					Log.v(TAG, "run() member："+member);
					Log.v(TAG, "run() voice："+voice);
					
					if (command.equals("CLOSE")){// 伺服器已關閉									
						closeCon();// 自動關閉連接
						return;// 結束線程
					} else if (command.equals("MAX")) {// 人數以達上限
						closeCon();// 自動關閉連接
						//利用Toast的靜態函式makeText來建立Toast物件			
						Toast toast = Toast.makeText(Game.this,"人數以滿", Toast.LENGTH_LONG);		
						toast.show();
						return;// 結束線程
					} else if (command.equals("userinfo")){// 普通消息
						
						if(member.equals("0"))
							changebulletin("已成功連線，但需等待一個人");					
					} else if(command.equals("question")){	
						changequestion("題目是："+content,voice);
					} else if(command.equals("DELETE")){						
						changebulletin(content+"下線!，所以需要在等待一個人");
					} else if(command.equals("ADD")){						
						changebulletin(content+"上線!");
					} else if(command.equals("USERLIST")){						
						changebulletin(content+"上線!");
					} else if(command.equals("final")){		
						if(member.equals("right")){
							if(content.equals(user)){
								mHandler.post(new Runnable(){		
									@Override
									public void run() {
										gameimage.setImageResource(R.drawable.win);
									}
								});
							}
							else{
								mHandler.post(new Runnable(){		
									@Override
									public void run() {
										gameimage.setImageResource(R.drawable.error);
									}
								});
							}
						}
					} else {						
						changebulletin("ERROR 例外內容："+message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//TTS所需要的內容，以下四個方法皆是，方法的主要功能在方法內容有詳述
	protected  void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if(requestCode == REQ_TTS_STATUS_CHECK)  
        {  
            switch (resultCode) {  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:{  
                //返回結果表明TTS Engine 可以用  
                mTts = new TextToSpeech(this, this);  
                Log.v(TAG, "TTS Engine is installed!");       
            }                    
                break;  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:  
                //需要的語音數據已損壞  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:  
                //缺少需要語言的語音數據  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:{  
                //缺少需要語言的發音數據        
                //這三種情況都表明數據有錯，重新下載安裝需要的數據  
                Log.v(TAG, "Need language stuff:"+resultCode);  
                Intent dataIntent = new Intent();  
                dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);  
                startActivity(dataIntent);            
            }  
                break;  
            case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:  
                //檢查失敗 
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
 	        //設置發音語言  
 	        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){  
 	        //判斷語言是否可用   	          
 	            Log.v(TAG, "Language is not available");  
 	            voice.setEnabled(false);  
 	        }else{  
 	        	Log.v(TAG, " TTS apparently available");  
 	        	voice.setEnabled(true);  
 	        }  
 	    }  
	}
	protected void onPause() {  
        // TODO Auto-generated method stub  
        super.onPause();
        Log.v(TAG, "onPause");  
        try{
        	if(mTts != null){ 
        		//activity暫時停止TTS
        		Log.v(TAG, "停tts"); 
            	mTts.stop();            	
        	}
        	if(isConnected){
        		closeConnection();
        		Log.v(TAG, "停connect"); 
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
    }  
	
    @Override  
    protected void onDestroy() {  
    	Log.v(TAG, "onDestroy"); 
        // TODO Auto-generated method stub  
        super.onDestroy();  
        //釋方TTS資源  
        try{
        	mTts.shutdown();
        	if(isConnected){
        		closeConnection();
        		Log.v(TAG, "停connect"); 
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    private void changequestion(final String arg,final String arg2){
    	mHandler.post(new Runnable(){		
			@Override
			public void run() {
				question.setText(arg);
				voicecontent=arg2;
			}
		});
    }
    private void changebulletin(final String arg){
    	mHandler.post(new Runnable(){		
			@Override
			public void run() {
				bulletin.setText(arg);
			}
		});
    }
}
