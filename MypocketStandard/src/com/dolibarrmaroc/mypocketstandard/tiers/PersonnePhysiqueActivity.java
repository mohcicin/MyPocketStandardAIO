package com.dolibarrmaroc.mypocketstandard.tiers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.menu;
import com.dolibarrmaroc.mypocketstandard.adapter.MyCmdAdapter;
import com.dolibarrmaroc.mypocketstandard.adapter.MyUpCltAdapter;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.MyClientAdapter;
import com.dolibarrmaroc.mypocketstandard.models.MyfactureAdapter;
import com.dolibarrmaroc.mypocketstandard.models.Myinvoice;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.utils.CommercialManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.UrlImage;


import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;
import android.os.Build;
import android.os.PowerManager.WakeLock;

public class PersonnePhysiqueActivity extends Activity  implements OnItemClickListener{
	
	private Compte compte;
	private ListView lisview;
	private SearchView search;
	
	private CommercialManager manager;
	private Offlineimpl myoffline;
	
	private ProgressDialog dialog;
	
	
	private List<MyClientAdapter> cltdata;
	private List<MyClientAdapter> factdatafilter;
	private MyUpCltAdapter factadapter;
	
	private WakeLock wakelock;
	
	
	private HashMap<Integer, Societe> myclts;
	private List<Societe> clients;
	
	private Societe mysoc;

	
	public PersonnePhysiqueActivity() {
		// TODO Auto-generated constructor stub
		manager = CommercialManagerFactory.getCommercialManager();
		compte = new Compte();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personne_physique);
		
		Bundle objetbunble  = this.getIntent().getExtras();
		
		manager = CommercialManagerFactory.getCommercialManager();
		compte = new Compte();
		myoffline = new Offlineimpl(PersonnePhysiqueActivity.this);
		clients = new ArrayList<>();
		myclts = new HashMap<>();
		cltdata = new ArrayList<>();
		factdatafilter = new ArrayList<>();

		if (objetbunble != null) {
			compte = (Compte) getIntent().getSerializableExtra("user");
		}
		
		lisview = (ListView)findViewById(R.id.lsupclt);
		lisview.setOnItemClickListener(this);
	
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
					remplireListview(cltdata, 0);
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
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		clients = new ArrayList<>();
		myclts = new HashMap<>();
		cltdata = new ArrayList<>();
		factdatafilter = new ArrayList<>();
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();
		
		dialog = ProgressDialog.show(PersonnePhysiqueActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		new ConnexionTask().execute();
	}
	
	private	class ConnexionTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			
			myoffline = new Offlineimpl(PersonnePhysiqueActivity.this);
			
			
			//clients = manager.getAll(compte);
			clients.clear();
			cltdata.clear();
			clients = myoffline.LoadSocietesClients("");
			
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing()){
					dialog.dismiss();
					wakelock.release();

					for (int i = 0; i < clients.size(); i++) {
						Log.e("clt ",clients.get(i).toString());
						myclts.put(clients.get(i).getId(), clients.get(i));
						cltdata.add(new MyClientAdapter(clients.get(i).getId(), clients.get(i).getName(), clients.get(i).getAddress(), clients.get(i).getPhone(), clients.get(i).getLogo()));
					}
					remplireListview(cltdata,0);
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}
	
	public void remplireListview(List<MyClientAdapter> fc,int n){
		 
	
		Log.e("fc siz****e ",cltdata.size()+"");
		factadapter = new MyUpCltAdapter(this, fc);
		
		factadapter.notifyDataSetChanged();
		lisview.invalidateViews();
		lisview.setAdapter(factadapter);
		lisview.refreshDrawableState();
	}
	
	public void FilterSearch(String st){
		for(MyClientAdapter data : cltdata)
        {
            //In this loop, you'll filter through originalData and compare each item to charSequence.
            //If you find a match, add it to your new ArrayList
            //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
            if(st.equals(data.getRefclient()+""))
            {
                factdatafilter.add(data);
            }
        }            
		remplireListview(factdatafilter,0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
	    MyClientAdapter selectedfact = new MyClientAdapter();
		
		if(factadapter.fitredData() != null){
			selectedfact = factadapter.fitredData().get(position);
		}else{
			selectedfact = cltdata.get(position);
		}
		
		 Log.e("You've selected : ",selectedfact.toString());

		mysoc = myclts.get((int)selectedfact.getRefclient());
		
		Builder dialog = new AlertDialog.Builder(PersonnePhysiqueActivity.this);
		//dialog.setMessage(R.string.caus14);
		dialog.setTitle(selectedfact.getName());
		
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.cltdialog, null);
         
		
		final ImageView iv = (ImageView)dialogView.findViewById(R.id.imgclt);
		
		if("".equals(selectedfact.getLogo()) || selectedfact.getLogo() == null){
        	iv.setImageResource(R.drawable.nophoto);
        }else{
        	iv.setImageURI(Uri.parse(UrlImage.pathimg+"/client_img/"+selectedfact.getLogo()));
        }
		
		dialog.setView(dialogView);
		
		
		dialog.setPositiveButton(getResources().getString(R.string.upcltnext), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.e("mysoc>> ",mysoc.toString());
				Intent intent1 = new Intent(PersonnePhysiqueActivity.this, UpdateClientActivity.class);
    			intent1.putExtra("user", compte);
    			intent1.putExtra("clt", mysoc);
    			startActivity(intent1);
    			
			}
		});
		dialog.setNegativeButton(getResources().getString(R.string.btn_cancel), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		dialog.setCancelable(true);
		dialog.show();
		
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
			onClickHome(LayoutInflater.from(PersonnePhysiqueActivity.this).inflate(R.layout.activity_update_client, null));
			return true;
		}
		return false;
	}


}
