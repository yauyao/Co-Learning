package com.example.co_learningnew;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class WordManageMain extends FragmentActivity {
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.wordmanage);

	    FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

	    tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

	    //1
	    tabHost.addTab(tabHost.newTabSpec("初級")
	                          .setIndicator("初級"), 
	                          Elementary.class, 
	                   null);
	    //2
	    tabHost.addTab(tabHost.newTabSpec("中級")
	                          .setIndicator("中級"), 
	                          Middle.class, 
	                   null);
	    //3
	    tabHost.addTab(tabHost.newTabSpec("高級")
	                          .setIndicator("高級"), 
	                          High.class, 
	                   null);
	    //4
	    tabHost.addTab(tabHost.newTabSpec("自訂")
	                          .setIndicator("自訂"), 
	                   WordofMyself.class, 
	                   null);
	  }
	}