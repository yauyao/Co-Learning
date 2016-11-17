package com.example.co_learningnew;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class Weakness extends FragmentActivity {
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.wordmanage);

	    FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

	    tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

	    //1
	    tabHost.addTab(tabHost.newTabSpec("���")
	                          .setIndicator("���"), 
	                          WeaknessElementary.class, 
	                   null);
	    //2
	    tabHost.addTab(tabHost.newTabSpec("����")
	                          .setIndicator("����"), 
	                          WeaknessMiddle.class, 
	                   null);
	    //3
	    tabHost.addTab(tabHost.newTabSpec("����")
	                          .setIndicator("����"), 
	                          WeaknessHigh.class, 
	                   null);	  	   
	  }
	}