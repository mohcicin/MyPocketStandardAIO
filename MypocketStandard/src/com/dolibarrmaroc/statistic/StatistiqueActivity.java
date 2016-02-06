package com.dolibarrmaroc.statistic;

import java.util.ArrayList;
import java.util.HashMap;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.menu;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.commercial.VendeurActivity;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.offline.ioffline;
import com.dolibarrmaroc.mypocketstandard.utils.CommercialManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.PayementManagerFactory;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class StatistiqueActivity extends Activity {
	
	private Compte compte;
	private ioffline myoffline;
	private StockVirtual sv;
	
	private int nbr_fc;
	private int nbr_cl;
	private int nbr_py;
	
	private int nbr_s_pd;
	private int nbr_s_cl;
	private int nbr_s_fc;
	
	private double nbr_ca_fc;
	private double nbr_ca_py;
	
	private double nbr_ca,nbr_ca2;
	
	private TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10;
	
	
	//Asynchrone avec connexion 
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistique);
		
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle objetbunble  = this.getIntent().getExtras();

		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
		}
		
		
		
		txt1 = (TextView)findViewById(R.id.instc1);
		txt2 = (TextView)findViewById(R.id.instc2);
		txt3 = (TextView)findViewById(R.id.instc3);
		
		txt4 = (TextView)findViewById(R.id.instc4);
		txt5 = (TextView)findViewById(R.id.instc5);
		txt6 = (TextView)findViewById(R.id.instc6);
		
		txt7 = (TextView)findViewById(R.id.instca1);
		txt8 = (TextView)findViewById(R.id.instca2);
		
		txt9 = (TextView) findViewById(R.id.canbr);
		txt10 = (TextView) findViewById(R.id.canbr1);
		
		dialog = ProgressDialog.show(StatistiqueActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
			new ConnexionTask().execute();	
	}

	
	class ConnexionTask extends AsyncTask<Void, Void, String> {

		
		@Override
		protected String doInBackground(Void... params) {

			
			myoffline = new Offlineimpl(StatistiqueActivity.this);
			sv = new StockVirtual(StatistiqueActivity.this);
			
			try {
				nbr_fc = myoffline.LoadInvoice("").size();
				nbr_cl = myoffline.LoadProspection("").size();
				nbr_py = myoffline.LoadReglement("").size();
				
				nbr_s_pd = myoffline.LoadProduits("").size();
				nbr_s_cl = myoffline.LoadClients("").size();
				nbr_s_fc = myoffline.prepaOfflinePayement(null).size();
				
				HashMap<String, Double> rs = sv.calculCA();
				
				Log.e("res>> ",rs.toString());
				nbr_ca_fc = rs.get("FC");
				nbr_ca_py = rs.get("PY");
				
				nbr_ca = rs.get("NFC");
				nbr_ca2 = rs.get("NPY");
			
			} catch (Exception e) {
				// TODO: handle exception
				nbr_fc = 0;
				nbr_cl = 0;
				nbr_py = 0;
				
				nbr_s_pd = 0;
				nbr_s_cl = 0;
				nbr_s_fc = 0;
				
				nbr_ca_fc = 0;
				nbr_ca_py = 0;
			}
			
			
			
			return "success";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing()){
					dialog.dismiss();
					
					input_data();
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}
	
	
	private void input_data(){
		txt1.setText(" "+nbr_fc);
		txt2.setText(" "+nbr_cl);
		txt3.setText(" "+nbr_py);
		
		txt4.setText(" "+nbr_s_pd);
		txt5.setText(" "+nbr_s_cl);
		txt6.setText(" "+nbr_s_fc);
		
		txt7.setText(" "+nbr_ca_fc);
		txt8.setText(" "+nbr_ca_py);
		
		txt9.setText(txt9.getText().toString()+"  [Nbr "+nbr_ca+" ]");

		txt10.setText(txt10.getText().toString()+"  [Nbr "+nbr_ca2+" ]");
	}
	
	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	 if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			/*
			new AlertDialog.Builder(this)
			.setTitle("Vraiment d�connecter?")
			.setMessage("Vous voulez vraiment d�connecter?")
			.setNegativeButton(android.R.string.no, null)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					//VendeurActivity.super.onBackPressed();
					VendeurActivity.this.finish();
					Intent intent1 = new Intent(VendeurActivity.this, ConnexionActivity.class);
					intent1.putExtra("user", compte);
					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent1);
					
					Intent intentService = new Intent(VendeurActivity.this, ShowLocationActivity.class);
					stopService(intentService);
					
				}

			}).create().show();
			*/
			onClickHome(LayoutInflater.from(StatistiqueActivity.this).inflate(R.layout.activity_statistique, null));
			
			return true;
		}
		return false;
	}
}
