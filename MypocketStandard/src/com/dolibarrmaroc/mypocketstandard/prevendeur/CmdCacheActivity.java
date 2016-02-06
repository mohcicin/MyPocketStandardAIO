package com.dolibarrmaroc.mypocketstandard.prevendeur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.menu;
import com.dolibarrmaroc.mypocketstandard.adapter.MyCmdAdapter;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Commande;
import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.MyfactureAdapter;
import com.dolibarrmaroc.mypocketstandard.models.Myinvoice;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.prevendeur.CmdViewActivity.OfflineTask;
import com.dolibarrmaroc.mypocketstandard.utils.CommandeManagerFactory;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class CmdCacheActivity extends Activity {

	private Compte compte;
	private ListView lisview;
	private ListView lisview2;
	private SearchView search;
	
	private Offlineimpl myoffline;
	
	private ProgressDialog dialog;
	
	
	private List<MyfactureAdapter> factdata;
	private List<MyfactureAdapter> factdatafilter;
	private MyCmdAdapter factadapter;
	private MyCmdAdapter regldapter;
	
	private WakeLock wakelock;
	
	
	private List<Commande> cmddataof;
	private List<Client> lsclts = new ArrayList<>();
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmd_cache);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle objetbunble  = this.getIntent().getExtras();

		
		myoffline = new Offlineimpl(getBaseContext());
		
		
		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
			//cicin.setText(compte.getLogin() + compte.getProfile());
		}
		
		lisview = (ListView) findViewById(R.id.lscicincmdcache);
		
		 
		
		factdata = new ArrayList<>();
		factdatafilter = new ArrayList<>();
		//new offlineTask().execute();
		
		factadapter = new MyCmdAdapter();
		
		
		search = (SearchView) findViewById(R.id.searchView1);
		
		search.setOnQueryTextListener(new OnQueryTextListener(){

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				Log.e("search data",""+newText);
				Log.e("util", TextUtils.isEmpty(newText)+"");
				if (TextUtils.isEmpty(newText))
				{
					lisview.clearTextFilter();
					remplireListview(factdata, 0);
				}
				else
				{
					lisview.setFilterText(newText.toString());
					factadapter.getFilter().filter(newText.toString());
					
					
					//bindingData = new BinderData(WeatherActivity.this, bindingData.getMap());
					factadapter.notifyDataSetChanged();
					lisview.invalidateViews();
					lisview.setAdapter(factadapter);
					lisview.refreshDrawableState();
				}

				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				Log.e("search data","is me submit");
				return false;
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.e("on resume","start");
		super.onResume();
	}
	
	
	
	public void remplireListview(List<MyfactureAdapter> fc,int n){
		 
		if(n == 0){
		Log.e("fc siz****e ",factdata.size()+"");
		factadapter = new MyCmdAdapter(this, fc);
		
		factadapter.notifyDataSetChanged();
		lisview.invalidateViews();
		lisview.setAdapter(factadapter);
		lisview.refreshDrawableState();
		
		}else if(n == 1){
			regldapter = new MyCmdAdapter(this,fc);
			lisview2.setAdapter(regldapter);
		}
	}
	
	
	public void FilterSearch(String st){
		for(MyfactureAdapter data : factdata)
        {
            //In this loop, you'll filter through originalData and compare each item to charSequence.
            //If you find a match, add it to your new ArrayList
            //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
            if(data.getReffact().equals(st))
            {
                factdatafilter.add(data);
            }
        }            
		remplireListview(factdatafilter,0);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		//new ConnexionTask().execute();
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();
		
		Log.e("on start","start");
		/*
		if(CheckOutNet.isNetworkConnected(getApplicationContext())){
			dialog = ProgressDialog.show(CmdViewActivity.this, getResources().getString(R.string.map_data),
					getResources().getString(R.string.msg_wait), true);
			
				new ConnexionTask().execute();
		}else{
			dialog = ProgressDialog.show(CmdViewActivity.this, getResources().getString(R.string.map_data),
					getResources().getString(R.string.msg_wait), true);
			
				new OfflineTask().execute();
		}
		*/
		dialog = ProgressDialog.show(CmdCacheActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		
			new OfflineTask().execute();
		
		super.onStart();
	}

	 
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*
			CmdViewActivity.this.finish();
			Intent intent1 = new Intent(CmdViewActivity.this, CatalogeActivity.class);
			intent1.putExtra("user", compte);
			intent1.putExtra("cmd", "1");
			startActivity(intent1);
			*/
			onClickHome(LayoutInflater.from(CmdCacheActivity.this).inflate(R.layout.activity_cmd_view, null));
		}
		return false;
	}
	 
	 
	class OfflineTask extends AsyncTask<Void, Void, String> {


		@Override
		protected String doInBackground(Void... params) {

			Log.e("data begin","start data ");
			cmddataof = new ArrayList<>();
			lsclts = new ArrayList<>();
			
			
			lsclts = myoffline.LoadClients("");

			cmddataof = myoffline.LoadCmdList("");
			

			factdata = new ArrayList<>();

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


					for (int i = 0; i < cmddataof.size(); i++) {
						factdata.add(new MyfactureAdapter(Load_clt_name(cmddataof.get(i).getClt()), cmddataof.get(i).getRef(), calcul_ttc(cmddataof.get(i))+"",cmddataof.get(i).getDt()+"", (int)cmddataof.get(i).getIdbo()));
					}
					
					Log.e("factdata ",factdata+"  ");

					remplireListview(factdata,0);


				}
				Log.e("end ","cnx");

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}
	
	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
	
	private double calcul_ttc(Commande mcd){
		double rs =0;
		
		for (int i = 0; i < mcd.getProds().size(); i++) {
			rs +=  mcd.getProds().get(i).getQtedemander() *  mcd.getProds().get(i).getPrixttc();
		}
		return rs;
	}
	
	private String Load_clt_name(String ref){
		String rs =ref;
		for (int i = 0; i < lsclts.size(); i++) {
			if(rs.equals(lsclts.get(i).getId()+"")){
				rs = lsclts.get(i).getName();
			}
		}
		return rs;
	}
}	