package com.example.co_learningnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

import com.example.co_learningnew.Game.MessageThread;


public class TeamDiscuss extends Activity {
		public static Handler mHandler = new Handler();
		private final String TAG = "TeamDiscuss";
		private TextView TextView01;	// 用來顯示文字訊息
		private EditText userEdit,msgEdit;	// 文字方塊
		private String tmp,user;			// 暫存文字訊息
		private Button output,connect;
		private Bundle bundle;
		private Intent intent;
		private boolean isConnected = false,RUN_THREAD = true;
		private PrintWriter writer;
		private Socket socket;
		private BufferedReader reader;
		private MessageThread messageThread;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.teamdiscuss);
			
			intent = new Intent();
			bundle = this.getIntent().getExtras();
			user="eric123987";
			// 從資源檔裡取得位址後強制轉型成文字方塊
			viewall();			
			userEdit.setText(user);																			
			// 設定按鈕的事件
			connect.setOnClickListener(new Button.OnClickListener() {		
				// 當按下按鈕的時候觸發以下的方法
				public void onClick(View v) {
					// 如果已連接則
					Log.e(TAG, "clickoutput");
					if (isConnected) {
						Toast toast = Toast.makeText(TeamDiscuss.this,"已連接至遠端", Toast.LENGTH_LONG);			
						//顯示Toast			
						toast.show();
						return;
					}else{
						try {
							Log.v(TAG, "connect begin ："+isConnected);
							connectServer(6999,"163.13.200.56","eric123987");
							Log.v(TAG, "connect end ："+isConnected);
						
							if (isConnected) {
								Toast toast = Toast.makeText(TeamDiscuss.this,"連結成功", Toast.LENGTH_LONG);			
								//顯示Toast			
								toast.show();
							}else{						
								Toast toast = Toast.makeText(TeamDiscuss.this,"連結失敗", Toast.LENGTH_LONG);			
								//顯示Toast			
								toast.show();
							}
						} catch (Exception exc) {
							Log.v(TAG, exc+"");
						}
					}
				}
			});
			output.setOnClickListener(new OnClickListener() {             
				public void onClick(View v) {
					Log.v(TAG, "寄出答案");
						send();
				}
			});
		}
		private void viewall(){
			Log.e(TAG, "viewall");
			TextView01 = (TextView) findViewById(R.id.inputstext);
			userEdit=(EditText) findViewById(R.id.playedit);
			msgEdit=(EditText) findViewById(R.id.messedit);
			output=(Button) findViewById(R.id.input);// 從資源檔裡取得位址後強制轉型成按鈕
			connect=(Button) findViewById(R.id.discussconn);
		}
		// 顯示更新訊息
		private Runnable updateText = new Runnable() {
			public void run() {
				// 加入新訊息並換行
				TextView01.append(tmp + "\n");
			}
		};
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
		public void sendMessage(String message){
			writer.println(message);
			writer.flush();
		}
		public void send() {
			if (!isConnected) {
				 //利用Toast的靜態函式makeText來建立Toast物件			
				Toast toast = Toast.makeText(TeamDiscuss.this,"還未連接至遠端", Toast.LENGTH_LONG);			
				//顯示Toast			
				toast.show();
				return;
			}
			String message = msgEdit.getText().toString().trim();
			if (message == null || message.equals("")) {
				Toast toast = Toast.makeText(TeamDiscuss.this,"訊息欄不能為空", Toast.LENGTH_LONG);			
				//顯示Toast			
				toast.show();
				return;
			}
			sendMessage(user + "@" + "ALL" + "@" + message);
			msgEdit.setText(null);
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
								message, "/@");
						String command = stringTokenizer.nextToken();// 命令
						
						if (command.equals("CLOSE"))// 伺服器已關閉
						{
							Toast toast = Toast.makeText(TeamDiscuss.this,"伺服器已關閉", Toast.LENGTH_LONG);
							toast.show();
							closeCon();// 自動關閉連接
							return;// 結束線程
						} else if (command.equals("ADD")) {// 有用戶上線時，更新在線列表
							String username = "";
							String userIp = "";
							
							if ((username = stringTokenizer.nextToken()) != null
									&& (userIp = stringTokenizer.nextToken()) != null) {				
								change(username+"/"+userIp+"\n");
							}
						} else if (command.equals("DELETE")) {// 有用戶下線時，更新在線列表
							String username = stringTokenizer.nextToken();							
								change(username+"下線\n");
						} else if (command.equals("USERLIST")) {// 加載在線用戶列表
							int size = Integer
									.parseInt(stringTokenizer.nextToken());
							String username = null;						
							for (int i = 0; i < size; i++) {
								username = stringTokenizer.nextToken();
								Toast toast = Toast.makeText(TeamDiscuss.this,username+"加入", Toast.LENGTH_LONG);
								toast.show();
							}
						} else if (command.equals("MAX")) {// 人數以達上限
							Toast toast = Toast.makeText(TeamDiscuss.this,"伺服器以滿", Toast.LENGTH_LONG);
							toast.show();
							return;// 結束線程																	
						}else {// 普通消息
							change(message + "\n");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		public void change(final String msg){
			mHandler.post(new Runnable(){		
				@Override
				public void run() {
					TextView01.append(msg);
				}
			});
		}
		protected void onDestroy() {  
	    	Log.v(TAG, "onDestroy"); 
	        super.onDestroy();  
	        try{	        	
	        	if(isConnected){
	        		closeConnection();
	        		Log.v(TAG, "停connect"); 
	        	}
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	    }
}
