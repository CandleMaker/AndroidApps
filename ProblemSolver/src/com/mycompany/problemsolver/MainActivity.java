package com.mycompany.problemsolver;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		Button solve = (Button) findViewById(R.id.solve);
		solve.setOnClickListener(new OnClickListener() {
			public void onClick(View p1){
				finish();
			}
		});
    }
}
