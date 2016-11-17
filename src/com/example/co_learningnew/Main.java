package com.example.co_learningnew;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.Intent;


public class Main extends Activity {
	
	private static final String TAG = "Main";
	private Bundle bundle;
	private Intent intent;
	private ImageButton learning;
	private ImageButton wordtest;
	private ImageButton wordmanage;
	private ImageButton weekness;
	private ImageButton teampoint;
	private ImageButton teamdiscuss;
	private ImageButton collaboratetest;
	private ImageButton peertopeer;
	private ImageButton comparepiont;
	private ImageButton update;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		intent = this.getIntent();
		bundle = intent.getExtras();
		create();
		button();
	}
	private void create(){
		learning=(ImageButton)findViewById(R.id.learnbutton);
		wordtest=(ImageButton)findViewById(R.id.testbutton);
		wordmanage=(ImageButton)findViewById(R.id.managebutton);
		weekness=(ImageButton)findViewById(R.id.weaknessbutton);
		update=(ImageButton)findViewById(R.id.updatebutton);
		teampoint=(ImageButton)findViewById(R.id.teampointbutton);
		teamdiscuss=(ImageButton)findViewById(R.id.teamdiscuss);
		collaboratetest=(ImageButton)findViewById(R.id.collaboratetestbutton);
		peertopeer=(ImageButton)findViewById(R.id.peertopeerbutton);
		comparepiont=(ImageButton)findViewById(R.id.comparebutton);
	}
	private void button(){
		learning.setOnClickListener(new View.OnClickListener(){	    		 
				@Override
				public void onClick(View arg0) {
					intent.setClass(Main.this, WordLearningMain.class);
		            startActivity(intent);
				}});
		weekness.setOnClickListener(new View.OnClickListener(){	    		 
			@Override
			public void onClick(View arg0) {
				intent.setClass(Main.this, Weakness.class);
	            startActivity(intent);
			}});
		wordmanage.setOnClickListener(new View.OnClickListener(){	    		 
			@Override
			public void onClick(View arg0) {
				intent.setClass(Main.this, WordManageMain.class);
	            startActivity(intent);
			}});
		wordtest.setOnClickListener(new View.OnClickListener(){	    		 
			@Override
			public void onClick(View arg0) {
				intent.setClass(Main.this, WordtestMain.class);
	            startActivity(intent);
			}});
		peertopeer.setOnClickListener(new View.OnClickListener(){	    		 
			@Override
			public void onClick(View arg0) {
				intent.setClass(Main.this, Game.class);
	            startActivity(intent);
			}});
		teampoint.setOnClickListener(new View.OnClickListener(){	    		 
			@Override
			public void onClick(View arg0) {
				intent.setClass(Main.this, TeamRecord.class);
	            startActivity(intent);
			}});
		teamdiscuss.setOnClickListener(new View.OnClickListener(){	    		 
			@Override
			public void onClick(View arg0) {
				intent.setClass(Main.this, TeamDiscuss.class);
	            startActivity(intent);
			}});
	}
}
