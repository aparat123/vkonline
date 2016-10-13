package com.vk.vktestapp;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.*;
import android.widget.*;
import com.vk.vktestapp.MainActivity.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.util.*;



public class MainActivity extends ActionBarActivity
{
	
	boolean isRunning;
	boolean online;
	EditText editText;
	TextView tvInfo;
	Intent inte;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	 tvInfo = (TextView)findViewById(R.id.tvtext);
	 inte = new Intent(this, MyServices.class);
	    
		}
	public void onclick(View v) {
		editText = (EditText)findViewById(R.id.EditText1);
		int userid = Integer.parseInt(editText.getText().toString());
		Intent serviceIntent = new Intent(this, MyServices.class);
      inte.putExtra("UserID", userid);
      // this.startService(inte);
	   this.startService(inte);
	}
	public void onClickStop(View v) {
		this.stopService(inte);
    }

	
	
}

