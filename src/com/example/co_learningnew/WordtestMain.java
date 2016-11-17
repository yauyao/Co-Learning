package com.example.co_learningnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class WordtestMain extends Activity {
	private static final String TAG = "WordtestMain";
	private Spinner level,second;
	private Button choice,fill,back;
	private String[] number={"5","10","15","20"};
	private String[] rank={"初級","中級","高級","能力測驗"};
	private ArrayAdapter<String> listAdapter1,listAdapter2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordtestmain);
		
		viewall();
		listAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rank);
		listAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, number);
		level.setAdapter(listAdapter1);
		second.setAdapter(listAdapter2);
		
		choice.setOnClickListener(new Button.OnClickListener() {  
			     
			public void onClick(View v) {
				Bundle bundle = new Bundle();
                bundle.putString("rank", level.getSelectedItem().toString());
                bundle.putString("second", second.getSelectedItem().toString());
                bundle.putInt("record", 0);
                bundle.putString("finalshow","結果\n");
                Intent intent = new Intent();
                Log.v(TAG, level.getSelectedItem().toString()); 
                Log.v(TAG, second.getSelectedItem().toString()); 
                if(level.getSelectedItem().toString().equals("能力測驗")){
                	intent.setClass(WordtestMain.this, WordtestChoiceself.class);
                	intent.putExtras(bundle);
                	//開啟Activity
                	startActivity(intent);
                	Log.v(TAG, "進自我");
                }else{
                	intent.setClass(WordtestMain.this, WordtestChoice.class);
                	intent.putExtras(bundle);
                	//開啟Activity
                	startActivity(intent);
                	Log.v(TAG, "進一般");
                } 
	        }  
	    });
		fill.setOnClickListener(new Button.OnClickListener() {  
		     
        	public void onClick(View v) {  
        		Bundle bundle = new Bundle();
                bundle.putString("rank", level.getSelectedItem().toString());
                bundle.putString("second", second.getSelectedItem().toString());
                bundle.putInt("record", 0);
                bundle.putString("finalshow","結果\n");
                Intent intent = new Intent();
                Log.v(TAG, level.getSelectedItem().toString()); 
                Log.v(TAG, second.getSelectedItem().toString()); 
                //設定下一個Actitity
                if(level.getSelectedItem().toString().equals("能力測驗")){
                	intent.setClass(WordtestMain.this, WordtestInputself.class);
                	intent.putExtras(bundle);
                	//開啟Activity
                	startActivity(intent);
                	Log.v(TAG, "進自我");
                }else{
                	intent.setClass(WordtestMain.this, WordtestInput.class);
                	intent.putExtras(bundle);
                	//開啟Activity
                	startActivity(intent);
                	Log.v(TAG, "進一般");
                }  
            }
        });
		back.setOnClickListener(new Button.OnClickListener() {  
		     
        	public void onClick(View v) {  
        		Bundle bundle = new Bundle();             
                Intent intent = new Intent();
                Log.v(TAG, "返回選單"); 
                //設定下一個Actitity
                intent.setClass(WordtestMain.this, Main.class);
                intent.putExtras(bundle);
                //開啟Activity
                startActivity(intent);
        	}  
        });
		
	}
	private void viewall(){
		level=(Spinner)findViewById(R.id.testkind);
		second=(Spinner)findViewById(R.id.secondkind);
		choice=(Button)findViewById(R.id.choice);
		fill=(Button)findViewById(R.id.fill);
		back=(Button)findViewById(R.id.wordtestback);
	}
}
