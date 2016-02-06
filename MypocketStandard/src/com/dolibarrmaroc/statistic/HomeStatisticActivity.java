package com.dolibarrmaroc.statistic;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.maps.MainActivity;
import com.dolibarrmaroc.mypocketstandard.models.Compte;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;



public class HomeStatisticActivity extends TabActivity {

	private Compte compte;
	private TabHost myTabHost; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_statistic);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle objetbunble  = this.getIntent().getExtras();

		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
		}
		
		
		
		
	}

	 private void old_version(){
		 myTabHost = getTabHost(); //=(TabHost) findViewById(R.id.TabHost01);
			//myTabHost.setup();
			
			// tab1 settings
			TabSpec spec = myTabHost.newTabSpec("tab_1");
			
			// text and image of tab 
			spec.setIndicator("Global",getResources().getDrawable(android.R.drawable.ic_menu_add));
			
			// specify layout of tab
			Intent simple = new Intent(this, StatistiqueActivity.class);
			simple.putExtra("user", compte);
			spec.setContent(simple);
			
			// adding tab in TabHost
			myTabHost.addTab(spec);
			
			// otherwise :
			
					// specify layout of tab
					Intent tab_2 = new Intent(this, VentesActivity.class);
					tab_2.putExtra("user", compte);
					tab_2.putExtra("val", ""+1);
					spec.setContent(tab_2);
		    myTabHost.addTab(myTabHost.newTabSpec("tab_2").setIndicator("Delete",getResources().getDrawable(android.R.drawable.ic_menu_edit)).setContent(tab_2));
		    
		 // specify layout of tab
		    Intent tab_3 = new Intent(this, VentesActivity.class);
			tab_3.putExtra("user", compte);
			tab_3.putExtra("val", ""+1);
			spec.setContent(tab_3);
			
		    myTabHost.addTab(myTabHost.newTabSpec("tab_3").setIndicator("Delete2",getResources().getDrawable(android.R.drawable.ic_menu_edit)).setContent(tab_3));
	 }
}
