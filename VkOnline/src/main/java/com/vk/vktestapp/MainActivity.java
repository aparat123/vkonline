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



public class MainActivity extends ActionBarActivity
{
	
	boolean isRunning;
	boolean online;
	EditText editText;
	TextView tvInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	 tvInfo = (TextView)findViewById(R.id.tvtext);
	 
		}
	public void onclick(View v) {
		editText = (EditText)findViewById(R.id.EditText1);
		int userid = Integer.parseInt(editText.getText().toString());
		startService(new Intent(this, MyService.class).putExtra("time", userid));
	//	MainActivity.MyTask mt = new MyTask();
	///	mt.execute();
	}
	public void onClickStop(View v) {
		stopService(new Intent(this, MyService.class));
    }

	
	
}

