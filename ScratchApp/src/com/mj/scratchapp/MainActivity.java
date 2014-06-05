package com.mj.scratchapp;

import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;

import com.mj.scratchapp.*;

public class MainActivity extends Activity {
final int a = 1;
final int b = 2;
String c = "msg msg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Button b = (Button)findViewById(R.id.button);
		b.setOnClickListener(new OnClickListener(){
			public void onClick(View p1){
				
				Toast.makeText(getApplicationContext(),c , Toast.LENGTH_LONG).show();
				//int is not allowed in toast? crashes
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
