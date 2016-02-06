package com.dolibarrmaroc.mypocketstandard.tour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.string;
import com.dolibarrmaroc.mypocketstandard.adapter.MyFactureAdapterView;
import com.dolibarrmaroc.mypocketstandard.adapter.TourneeAdapter;
import com.dolibarrmaroc.mypocketstandard.commercial.OfflineActivity;
import com.dolibarrmaroc.mypocketstandard.dao.TourneeDao;
import com.dolibarrmaroc.mypocketstandard.dao.TourneeDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.MyTicketBluetooth;
import com.dolibarrmaroc.mypocketstandard.models.MyfactureAdapter;
import com.dolibarrmaroc.mypocketstandard.models.Myinvoice;
import com.dolibarrmaroc.mypocketstandard.models.Reglement;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.ticket.FactureTicketActivity;
import com.dolibarrmaroc.mypocketstandard.ticket.ReglementTicketActivity;
import com.dolibarrmaroc.mypocketstandard.utils.Functions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;

public class TourneeViewerActivity extends Activity implements OnItemClickListener{

	private Compte compte;
	private ListView lisview;
	
	private Offlineimpl myoffline;
	
	private ProgressDialog dialog;
	
	TourneeDao tour = new TourneeDaoMysql();
	
	private TourneeAdapter tradapt;
	private Tournee tr;
	private List<Tournee> lstr;
	
	private WakeLock wakelock;
	
	
	private Spinner sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tournee_viewer);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle objetbunble  = this.getIntent().getExtras();

		
		
		
		myoffline = new Offlineimpl(getBaseContext());
		
		
		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
		}
		
		lisview = (ListView) findViewById(R.id.lstour);
		lisview.setOnItemClickListener(this);
		
		tour = new TourneeDaoMysql();
		 
		
	 
		tradapt = new TourneeAdapter(TourneeViewerActivity.this, lstr);
		sp = (Spinner)findViewById(R.id.clndr);
		
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// TODO Auto-generated method stub
				long n = sp.getSelectedItemId();
				
				if(n == 0){
					remplireListview(lstr);
				}else{
					
					lisview.setFilterText(n+"");
					tradapt.getFilter().filter(n+"");
					
					
					//bindingData = new BinderData(WeatherActivity.this, bindingData.getMap());
					tradapt.notifyDataSetChanged();
					lisview.invalidateViews();
					lisview.setAdapter(tradapt);
					lisview.refreshDrawableState();
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();
		
		dialog = ProgressDialog.show(TourneeViewerActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		
			new offlineTask().execute();
	}

	 
	 
	class offlineTask extends AsyncTask<Void, Void, String> {


		@Override
		protected String doInBackground(Void... params) {

			myoffline = new Offlineimpl(TourneeViewerActivity.this);
			
			lstr = new ArrayList<>();
			lstr = myoffline.LoadTourneeList("");
			
			Log.e("ls tour ", lstr.toString());
			/*
			HashMap<Integer, List<Tournee>> dt = Functions.prepaTourneeData(lstr); 
			for(Integer i:dt.keySet()){
				Log.e(">>tour ",dt.get(i).size()+ "");
			}
			*/
			
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
					remplireListview(lstr);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		tr = lstr.get(position);
		showClients();
         
	}

	
	private void showClients(){
		Dialog dialog = new Dialog(TourneeViewerActivity.this);
		   
		 LayoutInflater li = (LayoutInflater) this.getSystemService(OfflineActivity.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.reglementoffline, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        
     
        // Include dialog.xml file
       // dialog.setContentView(R.layout.reglementoffline);//listreglementoffline
        // Set dialog title
        dialog.setTitle(getResources().getString(R.string.task5));

        
        List<String> data = new ArrayList<>();
        ListView lisview2 = (ListView) dialog.findViewById(R.id.listView1);
        
        ViewGroup.LayoutParams params = lisview2.getLayoutParams();
        params.height = 250;
        lisview2.setLayoutParams(params);
        lisview2.requestLayout();
       
        for (int i = 0; i < tr.getLsclt().size(); i++) {
			data.add(tr.getLsclt().get(i).getName());
		}
       
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, data);
        lisview2.setAdapter(adapter);
        
        


        dialog.show();
	}

	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 
			onClickHome(LayoutInflater.from(TourneeViewerActivity.this).inflate(R.layout.activity_tournee_viewer, null));
		}
		return false;
	}
	

	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
	
	public void remplireListview(List<Tournee> fc){
		 
		tradapt = new TourneeAdapter(TourneeViewerActivity.this, fc);
		
		tradapt.notifyDataSetChanged();
		lisview.invalidateViews();
		lisview.setAdapter(tradapt);
		lisview.refreshDrawableState();
		 
	}
}
