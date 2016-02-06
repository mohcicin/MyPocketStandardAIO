/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dolibarrmaroc.mypocketstandard;


import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.models.Compte;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * This is the About activity in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 * my comments
 */

public class AboutActivity extends Activity 
{
	private Compte compte;
  
	/**
	 * onCreate
	 *
	 * Called when the activity is first created. 
	 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
	 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 *
	 * @param savedInstanceState Bundle
	 */

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView (R.layout.activity_about);
		setTitleFromActivityLabel (R.id.title_text);
		
		Bundle objetbunble  = this.getIntent().getExtras();

		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
		}


		TextView test1 = (TextView)findViewById(R.id.aboutText);
		Spanned spanned = Html.fromHtml("<font size=\"15\"><b><u>En savoir plus sur notre site : <a href=\"http://dolibarrmaroc.com\">GEO.COM</a></u></b></font>");
		test1.setMovementMethod(LinkMovementMethod.getInstance());
		test1.setText(spanned);
	}


	public void setTitleFromActivityLabel (int textViewId)
	{
		TextView tv = (TextView) findViewById (textViewId);
		if (tv != null) tv.setText (getTitle ());
	} // end setTitleText


	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}

} // end class
