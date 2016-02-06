package com.dolibarrmaroc.mypocketstandard.prevendeur;

import android.app.Activity;import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.string;
import com.dolibarrmaroc.mypocketstandard.adapter.MyCmdAdapter;
import com.dolibarrmaroc.mypocketstandard.adapter.MyFactureAdapterView;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDao;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.MyTicketBluetooth;
import com.dolibarrmaroc.mypocketstandard.models.MyfactureAdapter;
import com.dolibarrmaroc.mypocketstandard.models.Myinvoice;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Reglement;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.offline.ioffline;
import com.dolibarrmaroc.mypocketstandard.ticket.FactureTicketActivity;
import com.dolibarrmaroc.mypocketstandard.ticket.ReglementTicketActivity;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;
import com.dolibarrmaroc.mypocketstandard.utils.CommandeManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.CommercialManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.PayementManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.VendeurManagerFactory;
import com.google.android.gms.drive.query.SearchableField;

import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CmdViewActivity  extends Activity implements OnItemClickListener{

	private Compte compte;
	private ListView lisview;
	private ListView lisview2;
	private SearchView search;
	
	private Offlineimpl myoffline;
	private Myinvoice meinvo;
	
	private ProgressDialog dialog;
	
	private CommandeManager manager;
	
	
	private List<MyfactureAdapter> factdata;
	private List<MyfactureAdapter> factdatafilter;
	private MyCmdAdapter factadapter;
	private MyCmdAdapter regldapter;
	
	private WakeLock wakelock;
	
	
	private HashMap<Integer, Commandeview> mycmd;
	private List<Commandeview> cmddata;

	private Commandeview cicin;
	
	private int edite_cmd;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmd_view);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle objetbunble  = this.getIntent().getExtras();

		manager = new CommandeManagerFactory().getManager();
		
		
		myoffline = new Offlineimpl(getBaseContext());
		
		
		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
			//cicin.setText(compte.getLogin() + compte.getProfile());
			
			
			edite_cmd = Integer.parseInt(getIntent().getStringExtra("editcmd"));
		}
		
		lisview = (ListView) findViewById(R.id.lscicincmd);
		lisview.setOnItemClickListener(this);
		
		 
		
		factdata = new ArrayList<>();
		factdatafilter = new ArrayList<>();
		//new offlineTask().execute();
		
		factadapter = new MyCmdAdapter();
		
		mycmd = new HashMap<>();
		cmddata = new ArrayList<>();
		cicin = new Commandeview();
		
		search = (SearchView) findViewById(R.id.searchView1);
		
		search.setOnQueryTextListener(new OnQueryTextListener(){

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				
				Log.e("util", TextUtils.isEmpty(newText)+"");
				if (TextUtils.isEmpty(newText))
				{
					Log.e("search empty ",""+factdatafilter.size());
					lisview.clearTextFilter();
					/*
					factdata.clear();
					factdata.addAll(factdatafilter);
					*/
					remplireListview(factdata, 0);
				}
				else
				{
					
					lisview.setFilterText(newText.toString());
					factadapter.getFilter().filter(newText.toString());
					
					
					//bindingData = new BinderData(WeatherActivity.this, bindingData.getMap());
					factadapter.notifyDataSetChanged();
					
					
					/*
					factdata = factadapter.returnSort();
					
					if(factdata.size() == 0){
						factdata.addAll(factdatafilter);
					}
					*/
					
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
		dialog = ProgressDialog.show(CmdViewActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		
			new OfflineTask().execute();
		
		super.onStart();
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
		
		
	}
*/
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
			onClickHome(LayoutInflater.from(CmdViewActivity.this).inflate(R.layout.activity_cmd_view, null));
		}
		return false;
	}
	/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		 
		return true;
	}
	*/
	
	class ConnexionTask extends AsyncTask<Void, Void, String> {



		@Override
		protected String doInBackground(Void... params) {

			Log.e("data begin","start data ");
			cmddata = new ArrayList<>();

			
			cmddata  = manager.charger_commandes(compte);

			myoffline.shynchronizeCommandeList(cmddata);
			 

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

					mycmd = new HashMap<>();

					for (int i = 0; i < cmddata.size(); i++) {
						mycmd.put(cmddata.get(i).getRowid(), cmddata.get(i));
						factdata.add(new MyfactureAdapter(cmddata.get(i).getClt().getName(), cmddata.get(i).getRef(), cmddata.get(i).getTtc()+"",cmddata.get(i).getDt()+"", cmddata.get(i).getRowid()));
						factdatafilter.add(new MyfactureAdapter(cmddata.get(i).getClt().getName(), cmddata.get(i).getRef(), cmddata.get(i).getTtc()+"",cmddata.get(i).getDt()+"", cmddata.get(i).getRowid()));
					}

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
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		
		MyfactureAdapter selectedfact = new MyfactureAdapter();
		if(factadapter.fitredData() != null){
			selectedfact = factadapter.fitredData().get(position);
		}else{
			selectedfact = factdata.get(position);
		}
		
		 

		 cicin = mycmd.get(selectedfact.getIdfact());
		 
		 Log.e("You've selected : ",cicin.toString());
		 if(edite_cmd == 0){
			 new AlertDialog.Builder(this)
			    .setTitle(getResources().getString(R.string.cmdtofc12))
			    .setNegativeButton(R.string.tecv4, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	Intent intent1 = new Intent(CmdViewActivity.this, CmdDetailActivity.class);
		    			intent1.putExtra("user", compte);
		    			intent1.putExtra("vc", cicin);
		    			intent1.putExtra("lscmd", mycmd);
		    			startActivity(intent1);
			        }
			     })
			     .setCancelable(true)
			     .show();
		 }else{
			 new AlertDialog.Builder(this)
			    .setTitle(getResources().getString(R.string.cmdtofc12))
			    .setNegativeButton(R.string.edcmd5, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	Intent intent1 = new Intent(CmdViewActivity.this, CmdEditActivity.class);
		    			intent1.putExtra("user", compte);
		    			intent1.putExtra("vc", cicin);
		    			intent1.putExtra("lscmd", mycmd);
		    			startActivity(intent1);
			        }
			     })
			     .setPositiveButton(R.string.edcmd4, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
		    			
		    			if(cicin.getStatuts() == 0){
		    					new AlertDialog.Builder(CmdViewActivity.this)
							    .setTitle(getResources().getString(R.string.cmdtofc10))
							    .setMessage(R.string.edcmd6)
							    .setNegativeButton(R.string.cmdtofc11, new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int which) { 
							        	dialog.dismiss();
							        }
							     })
							     .setCancelable(true)
							     .show();
		    			}else{
			    				new AlertDialog.Builder(CmdViewActivity.this)
							    .setTitle(getResources().getString(R.string.cmdtofc10))
							    .setMessage(R.string.edcmd7)
							    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface d, int which) { 
							        	d.dismiss();
							        }
							     })
							     .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialogc, int which) { 
							        	if(CheckOutNet.isNetworkConnected(getApplicationContext())){
							        		 
											new CancelTask().execute();
											
											dialogc.dismiss();
							        	}else{
							        		 
											new CancelOfflineTask().execute();
											
											dialogc.dismiss();
							        	}
							        }
							     })
							     .setCancelable(true)
							     .show();
		    			}
		    			
			        }
			     })
			     .setCancelable(true)
			     .show();
		 }
		
		
         
	}
	

	
	
	class OfflineTask extends AsyncTask<Void, Void, String> {


		@Override
		protected String doInBackground(Void... params) {

			Log.e("data begin","start data ");
			cmddata = new ArrayList<>();

			

			//myoffline.shynchronizeCommandeList(cmddata);
			
			cmddata = prepa_cmd(prepa_cmd_cls(myoffline.LoadCommandeList("")));
			 

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

					mycmd = new HashMap<>();

					for (int i = 0; i < cmddata.size(); i++) {
						mycmd.put(cmddata.get(i).getRowid(), cmddata.get(i));
						factdata.add(new MyfactureAdapter(cmddata.get(i).getClt().getName(), cmddata.get(i).getRef(), cmddata.get(i).getTtc()+"",cmddata.get(i).getDt()+"", cmddata.get(i).getRowid()));
					}

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
	
	
	class CancelTask extends AsyncTask<Void, Void, String> {

		private int in;
		private String res="";

		@Override
		protected String doInBackground(Void... params) {

			manager = new CommandeManagerFactory().getManager();
			
			VendeurManager vendeurManager = VendeurManagerFactory.getClientManager();
			PayementManager payemn = PayementManagerFactory.getPayementFactory();
			CategorieDao categorie = new CategorieDaoMysql(getApplicationContext());
			CommandeManager managercmd =  new CommandeManagerFactory().getManager();
			CommercialManager managercom = CommercialManagerFactory.getCommercialManager();
			
			StockVirtual sv  = new StockVirtual(CmdViewActivity.this);
			
			myoffline = new Offlineimpl(CmdViewActivity.this);
			
			in = Integer.parseInt(manager.CancelCmd(cicin, compte));
			
			switch (in) {
			case -1:
				res = getResources().getString(R.string.edcmd8);
				break;

			case 100:
				res = getResources().getString(R.string.edcmd10);
				
				cicin.setStatuts(-100);
				myoffline.cancelCmd(cicin, compte);
				
				
				break;
			case -100: case 0:
				res = getResources().getString(R.string.edcmd9);
				break;
			}
			
			if(myoffline.checkAvailableofflinestorage() > 0){
				myoffline.SendOutData(compte);	
			}
			
			
			
			
			/*
			if(CheckOutNet.isNetworkConnected(CmdDetailActivity.this)){
				CheckOutSysc.ReloadProdClt(CmdDetailActivity.this, myoffline, compte, vendeurManager, payemn, sv, categorie, managercmd, 5,managercom);
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

					 new AlertDialog.Builder(CmdViewActivity.this)
					    .setTitle(getResources().getString(R.string.cmdtofc10))
					    .setMessage(res)
					    .setNegativeButton(R.string.cmdtofc11, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialogc, int which) { 
					        	
					        	cmddata = prepa_cmd(prepa_cmd_cls(myoffline.LoadCommandeList("")));
								 
								factdata = new ArrayList<>();
								
								mycmd = new HashMap<>();

								for (int i = 0; i < cmddata.size(); i++) {
									mycmd.put(cmddata.get(i).getRowid(), cmddata.get(i));
									factdata.add(new MyfactureAdapter(cmddata.get(i).getClt().getName(), cmddata.get(i).getRef(), cmddata.get(i).getTtc()+"",cmddata.get(i).getDt()+"", cmddata.get(i).getRowid()));
								}

								remplireListview(factdata,0);
								dialogc.dismiss();
					        }
					     })
					     .setCancelable(false)
					     .show();
					 

			        	


			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}
	
	class CancelOfflineTask extends AsyncTask<Void, Void, String> {

		private int in;
		private String res="";

		@Override
		protected String doInBackground(Void... params) {

			
			myoffline = new Offlineimpl(CmdViewActivity.this);
			
			in = (int)myoffline.shynchronizeClsCmd(cicin,compte);
			Log.e("ids>>> ",in+" >>> "+cicin.getRowid());
			
			switch (in) {
			case -1:
				res = getResources().getString(R.string.edcmd9);
				break;

			case 1:
				res = getResources().getString(R.string.edcmd10);
				myoffline.cancelCmd(cicin, compte);
				break;
			
			}
			
		 
			return "success";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {

					 new AlertDialog.Builder(CmdViewActivity.this)
					    .setTitle(getResources().getString(R.string.cmdtofc10))
					    .setMessage(res)
					    .setNegativeButton(R.string.cmdtofc11, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialogc, int which) { 
					        	
					        		cmddata = prepa_cmd(prepa_cmd_cls(myoffline.LoadCommandeList("")));
					        		
									factdata = new ArrayList<>();
									
									mycmd = new HashMap<>();
					
									for (int i = 0; i < cmddata.size(); i++) {
										mycmd.put(cmddata.get(i).getRowid(), cmddata.get(i));
										factdata.add(new MyfactureAdapter(cmddata.get(i).getClt().getName(), cmddata.get(i).getRef(), cmddata.get(i).getTtc()+"",cmddata.get(i).getDt()+"", cmddata.get(i).getRowid()));
									}
					
									remplireListview(factdata,0);
					        	
								dialogc.dismiss();
					        }
					     })
					     .setCancelable(false)
					     .show();
					 

			        	


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
	
	public List<Commandeview> prepa_cmd(List<Commandeview> in){
		List<Commandeview> tmp = new ArrayList<>();
		
		List<Commandeview> clscmd = new ArrayList<>();
		clscmd = myoffline.LoadClsCmdList("");
		
		boolean is = false;
		
		if(clscmd.size() > 0){
			for (int i = 0; i < in.size(); i++) {
				
				for (int j = 0; j < clscmd.size(); j++) {
					if(in.get(i).getRowid() == clscmd.get(j).getRowid()){
						is = true;
						break;
					}
				}
				
				if(!is){
					tmp.add(in.get(i));
				}
			}
		}else{
			tmp = in;
		}
		
		
		return tmp;
		
	}
	
	public List<Commandeview> prepa_cmd_cls(List<Commandeview> in){
		List<Commandeview> tmp = new ArrayList<>();

		boolean is = false;
		for (int i = 0; i < in.size(); i++) {
			if(in.get(i).getStatuts() != -100){
				tmp.add(in.get(i));
			}
		}
		
		return tmp;

	}
}	