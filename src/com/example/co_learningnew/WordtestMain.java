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
	private String[] rank={"���","����","����","��O����"};
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
                bundle.putString("finalshow","���G\n");
                Intent intent = new Intent();
                Log.v(TAG, level.getSelectedItem().toString()); 
                Log.v(TAG, second.getSelectedItem().toString()); 
                if(level.getSelectedItem().toString().equals("��O����")){
                	intent.setClass(WordtestMain.this, WordtestChoiceself.class);
                	intent.putExtras(bundle);
                	//�}��Activity
                	startActivity(intent);
                	Log.v(TAG, "�i�ۧ�");
                }else{
                	intent.setClass(WordtestMain.this, WordtestChoice.class);
                	intent.putExtras(bundle);
                	//�}��Activity
                	startActivity(intent);
                	Log.v(TAG, "�i�@��");
                } 
	        }  
	    });
		fill.setOnClickListener(new Button.OnClickListener() {  
		     
        	public void onClick(View v) {  
        		Bundle bundle = new Bundle();
                bundle.putString("rank", level.getSelectedItem().toString());
                bundle.putString("second", second.getSelectedItem().toString());
                bundle.putInt("record", 0);
                bundle.putString("finalshow","���G\n");
                Intent intent = new Intent();
                Log.v(TAG, level.getSelectedItem().toString()); 
                Log.v(TAG, second.getSelectedItem().toString()); 
                //�]�w�U�@��Actitity
                if(level.getSelectedItem().toString().equals("��O����")){
                	intent.setClass(WordtestMain.this, WordtestInputself.class);
                	intent.putExtras(bundle);
                	//�}��Activity
                	startActivity(intent);
                	Log.v(TAG, "�i�ۧ�");
                }else{
                	intent.setClass(WordtestMain.this, WordtestInput.class);
                	intent.putExtras(bundle);
                	//�}��Activity
                	startActivity(intent);
                	Log.v(TAG, "�i�@��");
                }  
            }
        });
		back.setOnClickListener(new Button.OnClickListener() {  
		     
        	public void onClick(View v) {  
        		Bundle bundle = new Bundle();             
                Intent intent = new Intent();
                Log.v(TAG, "��^���"); 
                //�]�w�U�@��Actitity
                intent.setClass(WordtestMain.this, Main.class);
                intent.putExtras(bundle);
                //�}��Activity
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
