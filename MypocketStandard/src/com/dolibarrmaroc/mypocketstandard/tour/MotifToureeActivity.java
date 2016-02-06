package com.dolibarrmaroc.mypocketstandard.tour;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.menu;
import com.dolibarrmaroc.mypocketstandard.adapter.SpinnerCenter;
import com.dolibarrmaroc.mypocketstandard.commercial.VendeurActivity;
import com.dolibarrmaroc.mypocketstandard.dao.TourneeDao;
import com.dolibarrmaroc.mypocketstandard.dao.TourneeDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Motifs;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.offline.ioffline;
import com.dolibarrmaroc.mypocketstandard.tour.TourneeViewerActivity.offlineTask;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;
import com.dolibarrmaroc.mypocketstandard.utils.Functions;

import android.support.v7.app.ActionBarActivity;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MotifToureeActivity extends Activity implements OnClickListener {

	private ioffline myoffline;
	private Compte compte;
	private TourneeDao managertourne;
	private StockVirtual sv;
	
	private Spinner cltour;
	private Spinner clclt;
	private Spinner motif;
	private EditText commentaire;
	private Button send;
	
	private List<Tournee> lstour;
	private List<Client> lsclts;
	
	private List<String> vtr;
	private List<String> vclt;
	
	private Tournee tr;
	private Client clt;
	private String motf;
	
	private ProgressDialog dialog;
	private WakeLock wakelock;
	
	private HashMap<String, Double> bo_mtf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_motif_touree);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle objetbunble  = this.getIntent().getExtras();

		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
		}
		
		cltour = (Spinner) findViewById(R.id.cltour);
		cltour.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// TODO Auto-generated method stub
				Log.e("tour",cltour.getSelectedItem()+"");
				vclt = new ArrayList<>();
				tr = null;
				
				for (int i = 0; i < lstour.size(); i++) {
					if(lstour.get(i).getLabel().equals(cltour.getSelectedItem().toString())){
						tr = new Tournee();
						tr = lstour.get(i);
					}
				}
				
				if(tr != null){
					if(tr.getRowid() != 0){
						vclt = new ArrayList<>();
						vclt.add("   *****   ");
						for (int j = 0; j < tr.getLsclt().size(); j++) {
							vclt.add(tr.getLsclt().get(j).getName());
						}
						 
						prepaSpinner(vclt,clclt);
						
						clclt.setEnabled(true);
					}
				}else{
					Log.e("in null ","tour select");
					prepaSpinner(vclt,clclt);
					clclt.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		clclt = (Spinner) findViewById(R.id.clclt);
		clclt.setEnabled(false);
		clclt.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// TODO Auto-generated method stub
				Log.e("clt",clclt.getSelectedItem()+"");
				if(tr != null){
					for (int j = 0; j < tr.getLsclt().size(); j++) {
						if(tr.getLsclt().get(j).getName().equals(clclt.getSelectedItem().toString())){
							clt = tr.getLsclt().get(j); 
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		motif = (Spinner) findViewById(R.id.clmotif);
		motif.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// TODO Auto-generated method stub
				motf = motif.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		commentaire = (EditText) findViewById(R.id.commentaire);
		
		send = (Button) findViewById(R.id.addmotif);
		send.setOnClickListener(this);
		
		
		lsclts = new ArrayList<>();
		lsclts = new ArrayList<>();
		
		vtr = new ArrayList<>();
		vclt = new ArrayList<>();
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();
		
		dialog = ProgressDialog.show(MotifToureeActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		
			new offlineTask().execute();
	
		bo_mtf = new HashMap<>();
		bo_mtf.put("Autres", 1D);
        bo_mtf.put("Erreur de manipulation (pas une visite)", 2D);
        bo_mtf.put("Magasin ferme", 3D);
        bo_mtf.put("Absence patron", 4D);
        bo_mtf.put("Stock suffisant", 5D);
        bo_mtf.put("Taux de perime eleve", 6D);
        bo_mtf.put("Client a problemes", 7D);
	}
	
	
	class offlineTask extends AsyncTask<Void, Void, String> {


		@Override
		protected String doInBackground(Void... params) {

			myoffline = new Offlineimpl(MotifToureeActivity.this);
			lstour = new ArrayList<>();
			
			lstour = Functions.prepaTourneeData(myoffline.LoadTourneeList("")).get(Functions.getNumberOfDay(new Date()));
			
			
			vtr = new ArrayList<>();
			vtr.add("   *****   ");
			for (int i = 0; i < lstour.size(); i++) {
				vtr.add(lstour.get(i).getLabel());
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
					
					prepaSpinner(vtr,cltour);
				

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	
	class saveOfflineTask extends AsyncTask<Void, Void, String> {

		long ix;

		@Override
		protected String doInBackground(Void... params) {

			myoffline = new Offlineimpl(MotifToureeActivity.this);
			sv = new StockVirtual(MotifToureeActivity.this); 

			ix = myoffline.shynchronizeMotifs(new Motifs(new Tournee(tr.getRowid(), tr.getLabel(), tr.getColor(), tr.getDebut(), tr.getFin(), tr.getSecteur(), tr.getIdsecteur(), tr.getGrp(), tr.getIdgrp(), tr.getRecur()), clt, new Date(), motf, commentaire.getText().toString()), compte);
			
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
					
					if(ix > 0){
						showMsg(getResources().getString(R.string.task17),1);
						sv.addOperation("MTF", bo_mtf.get(motf));
					}else{
						showMsg(getResources().getString(R.string.task18),0);
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	
	class saveTask extends AsyncTask<Void, Void, String> {

		String ix ="";

		@Override
		protected String doInBackground(Void... params) {

			myoffline = new Offlineimpl(MotifToureeActivity.this);

			managertourne = new TourneeDaoMysql();
			
			sv = new StockVirtual(MotifToureeActivity.this); 
			
			ix = managertourne.sendMotifs(new Motifs(new Tournee(tr.getRowid(), tr.getLabel(), tr.getColor(), tr.getDebut(), tr.getFin(), tr.getSecteur(), tr.getIdsecteur(), tr.getGrp(), tr.getIdgrp(), tr.getRecur()), clt, new Date(), motf, commentaire.getText().toString()), compte, "add"); 
			
			try {
				if(CheckOutNet.isNetworkConnected(getApplicationContext())){
					myoffline.SendOutData(compte);
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("erreu synchro",e.getMessage() +" << ");
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
					
					
					if(ix.equals("ok")){
						Log.e(">> res ",ix+ "<<");
						sv.addOperation("MTF", bo_mtf.get(motf));
						showMsg(getResources().getString(R.string.task17),1);
					}else{
						Log.e(">> no ",ix+ "<<");
						showMsg(getResources().getString(R.string.task18),0);
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addmotif:
			
			Log.e(">> selected "+commentaire.getText().toString(),cltour.getSelectedItemPosition()+ "**"+clclt.getSelectedItemPosition() +" ** "+motif.getSelectedItem().toString());
			
			int t = cltour.getSelectedItemPosition();
			int c = clclt.getSelectedItemPosition() ;
			
			if(t == 0 || t < 0){
				showMsg(getResources().getString(R.string.task13),0);
			}else if(t > 0 && c <= 0){
				showMsg(getResources().getString(R.string.task15),0);
			}else{
				if(tr == null || clt == null){
					showMsg(getResources().getString(R.string.task16),0);
				}else if(tr != null && clt != null){
					motf = motif.getSelectedItem().toString();
					
					dialog = ProgressDialog.show(MotifToureeActivity.this, getResources().getString(R.string.task19),
							getResources().getString(R.string.msg_wait), true);
					
						if(CheckOutNet.isNetworkConnected(MotifToureeActivity.this)){
							new saveTask().execute();
						}else{
							new saveOfflineTask().execute();
						}
				}
			}
			
			//Log.e("data in ",tr.getLabel()+" ** "+clt.getName()+" ** "+motf+" ** "+commentaire.getText().toString());
			break;

		default:
			break;
		}
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
			 
			onClickHome(LayoutInflater.from(MotifToureeActivity.this).inflate(R.layout.activity_motif_touree, null));
			
			return true;
		}
		return false;
	}
	
	private void showMsg(String in,final int tp){
		Log.e("show showMsg ",in);
		AlertDialog.Builder alert = new AlertDialog.Builder(MotifToureeActivity.this);
		alert.setTitle(getResources().getString(R.string.cmdtofc10));
		alert.setMessage(in);
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialogc, int which) {
				//dialogc.dismiss();
				
				//if(tp == 1){
					onClickHome(LayoutInflater.from(MotifToureeActivity.this).inflate(R.layout.activity_motif_touree, null));
				//}
				return;
			}
		});
		alert.setCancelable(false);
		alert.create().show();
		
	}
	
	
	private void prepaSpinner(List<String> in,Spinner sp){
		
		SpinnerCenter spadapter = new SpinnerCenter(MotifToureeActivity.this, in);
		/*
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MotifToureeActivity.this,
				android.R.layout.simple_spinner_item, in);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		*/
		
		spadapter.notifyDataSetChanged();
		sp.refreshDrawableState();
		sp.setAdapter(spadapter);
	}
	 
}
