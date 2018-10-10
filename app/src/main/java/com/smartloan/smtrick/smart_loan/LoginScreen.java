package com.smartloan.smtrick.smart_loan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {
	TextView txtregister;
	Button login,Register;
	EditText etusername,etpassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loginscreen);

		Register = (Button) findViewById(R.id.buttonRegister);
		login = (Button) findViewById(R.id.buttonlogin);
		etusername = (EditText) findViewById(R.id.edittextusername);
		etpassword = (EditText) findViewById(R.id.edittextpassword);

		Register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(LoginScreen.this, Registeractivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
				//overridePendingTransition(R.anim.backslide_in, R.anim.backslide_out);
			}
		});

		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Toast.makeText(LoginScreen.this, "User Login successfully", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(LoginScreen.this, MainActivity.class);
				startActivity(i);
				//overridePendingTransition(R.anim.backslide_in, R.anim.backslide_out);
				overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

			}
		});

		etusername.setOnTouchListener(new View.OnTouchListener()
		{
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
				etusername.startAnimation(zoomOutAnimation);
				return false;
			}
		});


		etpassword.setOnTouchListener(new View.OnTouchListener()
		{
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
				etpassword.startAnimation(zoomOutAnimation);
				return false;
			}
		});

	}

}
