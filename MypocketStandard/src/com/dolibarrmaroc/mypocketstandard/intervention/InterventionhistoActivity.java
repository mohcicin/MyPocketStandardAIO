package com.dolibarrmaroc.mypocketstandard.intervention;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Duration;
import org.joda.time.Hours;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.adapter.InterventionAdapterView;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.models.BordereauGps;
import com.dolibarrmaroc.mypocketstandard.models.BordreauIntervention;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.MyfactureAdapter;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.AlertDialog.Builder;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class InterventionhistoActivity extends Activity  implements OnQueryTextListener {

	private SearchView search;
	private Compte compte;
	private ListView lisview;
	private ListView lisview2;
	
	private Offlineimpl myoffline;
	
	private ProgressDialog dialog;
	private ProgressDialog dialogIN;
	
	
	private List<MyfactureAdapter> factdata;
	private InterventionAdapterView factadapter;
	private InterventionAdapterView regldapter;
	
	private WakeLock wakelock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interventionhisto);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle objetbunble  = this.getIntent().getExtras();

		
		
		
		myoffline = new Offlineimpl(getBaseContext());
		
		
		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
		}
		
		lisview = (ListView) findViewById(R.id.interhisto);
		
		factdata = new ArrayList<>();
		//new offlineTask().execute();
		
		factadapter = new InterventionAdapterView();
		
		
		search = (SearchView) findViewById(R.id.searchView1);
		
		search.setOnQueryTextListener(new OnQueryTextListener(){

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
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
	
	public void remplireListview(List<MyfactureAdapter> fc,int n){
		if(n == 0){
		factadapter = new InterventionAdapterView(this, fc);
		
		factadapter.notifyDataSetChanged();
		lisview.invalidateViews();
		lisview.setAdapter(factadapter);
		lisview.refreshDrawableState();
		
		}else if(n == 1){
			regldapter = new InterventionAdapterView(this,fc);
			lisview2.setAdapter(regldapter);
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		//new ConnexionTask().execute();
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();
		
		dialog = ProgressDialog.show(InterventionhistoActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		
			new offlineTask().execute();
		super.onStart();
	}
	
	
	class offlineTask extends AsyncTask<Void, Void, String> {


		@Override
		protected String doInBackground(Void... params) {

			
			factdata = new ArrayList<>();
			List<BordreauIntervention> mx = myoffline.LoadHistoInterventions("");
			for(BordreauIntervention mm:mx){
				
				//factdata.add(new MyfactureAdapter(mm.getTicket().getClient(), mm.getTicket().getNumFacture(), mm.getMypay().getTotal()+"",mm.getMyreg().getAmount()+"", Integer.parseInt(mm.getMyreg().getPaiementcode())));
				String du = "0 H 0 m";
				if(mm.getDuree() != null){
					if(!mm.getDuree().equals("")){
						int k = Integer.parseInt(mm.getDuree());
						du = (k/3600)+" H "+((k%3600)/60)+" m";
					}
				}
				
				Log.e(">>> ",mx.toString());
				factdata.add(new MyfactureAdapter(mm.getNmclt(), mm.getObjet(), mm.getDate_c(),mm.getStatus(), mm.getId()));
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
					remplireListview(factdata,0);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	

	 
	
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
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	 
	

	public void alertchachedel(){
		try {
			Builder dialog = new AlertDialog.Builder(InterventionhistoActivity.this);
			
				dialog.setMessage(R.string.tecv44);

				dialog.setTitle(getResources().getString(R.string.tecv36));
			
			LayoutInflater inflater = this.getLayoutInflater();
			final View dialogView = inflater.inflate(R.layout.inputsetting, null);
	         
	         dialog.setView(dialogView);
	         dialog.setPositiveButton(getResources().getString(R.string.tecv37), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					EditText txt = (EditText)dialogView.findViewById(R.id.inputpwd);
					if(txt != null){
						if(!txt.equals("")){
							if(compte.getPassword().equals(txt.getText().toString())){
								dialogIN = ProgressDialog.show(InterventionhistoActivity.this, getResources().getString(R.string.tecv37),
										getResources().getString(R.string.msg_wait), true);

								myoffline = new Offlineimpl(getApplicationContext());
								myoffline.CleanHistoIntervention();
								if (dialogIN.isShowing()){
									dialogIN.dismiss();
									onClickHome(LayoutInflater.from(InterventionhistoActivity.this).inflate(R.layout.activity_interventionhisto, null));
								}
								
							}else{
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.tecv38), Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(getApplicationContext(),getResources().getString(R.string.tecv38), Toast.LENGTH_LONG).show();
						}
					}
				}
			});
	         dialog.setNegativeButton(getResources().getString(R.string.tecv16), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					dialog.cancel();
				}
			});
	         dialog.setCancelable(true);
	         dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("erreur",e.getMessage() +" << ");
		}
	}
	
	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
	
	public void onClickDel(View v){
		alertchachedel();
	}
	
	@Override
	public void onBackPressed() {
	    // Do Here what ever you want do on back press;
		//onClickHome(LayoutInflater.from(InterventionhistoActivity.this).inflate(R.layout.activity_interventionhisto, null));
	}
}
