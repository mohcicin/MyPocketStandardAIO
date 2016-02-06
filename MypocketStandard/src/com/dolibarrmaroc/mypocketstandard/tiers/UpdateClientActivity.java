package com.dolibarrmaroc.mypocketstandard.tiers;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.ConnexionActivity;
import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.string;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.commercial.VendeurActivity;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDao;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.ProspectData;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.offline.ioffline;
import com.dolibarrmaroc.mypocketstandard.utils.Base64;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutSysc;
import com.dolibarrmaroc.mypocketstandard.utils.CommandeManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.CommercialManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.PayementManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.UrlImage;
import com.dolibarrmaroc.mypocketstandard.utils.VendeurManagerFactory;
import com.karouani.cicin.widget.AutocompleteCustomArrayAdapter;
import com.karouani.cicin.widget.CustomAutoCompleteTextChangedListener;
import com.karouani.cicin.widget.CustomAutoCompleteView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class UpdateClientActivity extends Activity implements OnClickListener,OnItemSelectedListener,LocationListener{

	/**************************************** GPS DATA *****************************************/
	private LocationManager mLocationManager = null;
	private int LOCATION_INTERVAL = 1000;
	private float LOCATION_DISTANCE = 16;
	private double latitude;
	private double longitude;
	
	private String mycity="";
	
	private ioffline myoffline;

	private LocationListener onLocationChange	= new LocationListener()
	{
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}

		@Override
		public void onProviderEnabled(String provider)
		{
		}

		@Override
		public void onProviderDisabled(String provider)
		{
		}

		@Override
		public void onLocationChanged(Location location)
		{
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	};

	private int testeur;
	/******************************************************************************/
	private CommercialManager manager;
	private Compte compte;
	private GpsTracker gps;
	private WakeLock wakelock;
	private ProspectData data;
	private Prospection client;

	/********************INTERFACES COMPOSANTS *********************************/
	private EditText name;
	private EditText address;
	private EditText zip;
	private EditText tel;
	private EditText fax;
	private EditText email;
	private ImageView vs;

	private Spinner etat;
	private Spinner type;
	//private EditText ville;
	private CustomAutoCompleteView ville;

	private Button btn,suivant;
	private ImageButton btnpic;
	private LinearLayout myLayout;

	private List<EditText> maVue;
	//private Spinner clientspinner,proSpinner;
	public CustomAutoCompleteView clientspinner;
	public ArrayAdapter<String> myAdapter;
	public String[] values;
	
	private List<String> listclt;
	//Asynchrone avec connexion 
	private ProgressDialog dialog;
	private String resu ;
	private LinearLayout scroll;
	private Societe soc = new Societe();
	
	private List<String> lscity = new ArrayList<>();
	
	/************************CAMERA************************/
	MediaPlayer mp=new MediaPlayer();
	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	private Bitmap bitmap;
	private Uri imageUri;
	private String ba1;
	private String lieux;
	private static boolean withimg = false;

	public UpdateClientActivity() {
		// TODO Auto-generated constructor stub
		manager = CommercialManagerFactory.getCommercialManager();
		compte = new Compte();
		gps = new GpsTracker();
		data = new ProspectData();
		client = new Prospection();
		listclt = new ArrayList<String>();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();

		//scroll.setVisibility(LinearLayout.INVISIBLE);
		ville = (CustomAutoCompleteView) findViewById(R.id.comm_ville);

		dialog = ProgressDialog.show(UpdateClientActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		new ConnexionTask().execute();

		super.onStart();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_client);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		try {


			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,  LOCATION_INTERVAL, LOCATION_DISTANCE, onLocationChange);
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
					onLocationChange);

			if (mLocationManager == null) {
				mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
			}



			Bundle objetbunble  = this.getIntent().getExtras();

			if (objetbunble != null) {
				compte = (Compte) getIntent().getSerializableExtra("user");
				soc = (Societe) getIntent().getSerializableExtra("clt");
			}


			Log.e(">>> soc ",soc.toString() + "><<<");
			clientspinner =  (CustomAutoCompleteView) findViewById(R.id.clientspinner);
			scroll = (LinearLayout) findViewById(R.id.malineaire);

			//name = (EditText) findViewById(R.id.comm_nom);
			address = (EditText) findViewById(R.id.comm_address);
			zip = (EditText) findViewById(R.id.comm_zip);
			tel = (EditText) findViewById(R.id.comm_phone);
			fax = (EditText) findViewById(R.id.comm_fax);
			email = (EditText) findViewById(R.id.comm_email);

			etat = (Spinner) findViewById(R.id.comm_ste);
			etat.setOnItemSelectedListener(this);
			type = (Spinner) findViewById(R.id.comm_type);
			type.setOnItemSelectedListener(this);
			ville = (CustomAutoCompleteView) findViewById(R.id.comm_ville);
			/*
			ville.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long arg3) {
					String selected = (String) parent.getItemAtPosition(position);
					ville.showDropDown();
					
					Log.e("selected ",selected);
					mycity = selected;

					final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromInputMethod(parent.getWindowToken(), 0);

					ville.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});

					 

				}
			});
			*/
			
			ville.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String selected = ville.getSelected(parent, view, position, id);
					mycity = selected;					
				}
			});
			
			ville.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					CustomAutoCompleteTextChangedListener txt = new CustomAutoCompleteTextChangedListener(UpdateClientActivity.this,R.layout.list_view_row,lscity);

					myAdapter = txt.onTextChanged(s, start, before, count);
					myAdapter.notifyDataSetChanged();
					ville.setAdapter(myAdapter);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			
			vs = (ImageView) findViewById(R.id.imgcltpic);

			btn = (Button) findViewById(R.id.comm_etape);
			btn.setOnClickListener(this);
			
			btnpic = (ImageButton) findViewById(R.id.takepic);
			btnpic.setOnClickListener(this);


			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			myoffline = new Offlineimpl(UpdateClientActivity.this);

			clientspinner.setText(soc.getName());
			clientspinner.setEnabled(false);
			//clientspinner.setOnItemSelectedListener(this);
			/*
			clientspinner.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String selected = clientspinner.getSelected(parent, view, position, id);
					//String selected = (String) parent.getItemAtPosition(position);
					
					
				}
			});
			
			clientspinner.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					CustomAutoCompleteTextChangedListener txt = new CustomAutoCompleteTextChangedListener(UpdateClientActivity.this,R.layout.list_view_row,listclt);

					myAdapter = txt.onTextChanged(s, start, before, count);
					myAdapter.notifyDataSetChanged();
					clientspinner.setAdapter(myAdapter);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			*/

			remplire_view();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private	class ConnexionTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			
			myoffline = new Offlineimpl(UpdateClientActivity.this);
			
			
			//clients = manager.getAll(compte);
			
			lscity = new ArrayList<>();
			lscity = myoffline.LoadProspect("").getVilles();
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
					client.setCommercial_id(Integer.parseInt(compte.getIduser()));
					wakelock.release();
					
					addItemsOnSpinner(ville,lscity);
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}

	public void addItemsOnSpinner(AutoCompleteTextView clientspinner2,List<String> list) {

		values= new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			values[i] = list.get(i);
		}
		
		
		myAdapter = new AutocompleteCustomArrayAdapter(UpdateClientActivity.this, R.layout.list_view_row, values);
		clientspinner2.setAdapter(myAdapter);
	}
	
	public void addItemsOnSpinnerClt(AutoCompleteTextView clientspinner2,List<String> list) {

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clientspinner2.setAdapter(dataAdapter);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String selected = parent.getItemAtPosition(position).toString();

		if(parent.getId() == R.id.comm_ste){
			Log.i("Ste", selected+" Positin "+position);
			testeur = position;

			if (position == 0){

				//name.setHint("Nom Compléte");
				EditText firstname = new EditText(UpdateClientActivity.this);
				firstname.setHint("Le Nom");
				firstname.setTag("comm_firstname");

				firstname.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

				EditText lastname = new EditText(UpdateClientActivity.this);
				lastname.setHint("Le Prenom");
				lastname.setTag("comm_lasttname");

				lastname.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

				myLayout = (LinearLayout) findViewById(R.id.comm_interface);
				myLayout.removeAllViews();
				//int k = (myLayout.getWidth() / 2;
				lastname.setWidth(myLayout.getWidth()/2);
				firstname.setWidth(myLayout.getWidth()/2);
				
				firstname.setText(soc.getName());
				lastname.setText(soc.getName());

				myLayout.addView(firstname);
				myLayout.addView(lastname);

				maVue = new ArrayList<>();
				maVue.clear();
				maVue.add(firstname);
				maVue.add(lastname);

				client.setParticulier(1);

			}else{
				//name.setHint("Nom soci�t�");
				EditText name = new EditText(UpdateClientActivity.this);
				name.setText(soc.getName());
				name.setHint("Nom societe");
				name.setTag("comm_nome");
				name.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

				myLayout = (LinearLayout) findViewById(R.id.comm_interface);
				name.setWidth(myLayout.getWidth());
				myLayout.removeAllViews();
				myLayout.addView(name);

				maVue = new ArrayList<>();
				maVue.clear();
				maVue.add(name);
				client.setParticulier(0);
			}
		}
		if(parent.getId() == R.id.comm_type){
			Log.i("type", selected+" Positin "+position);
			if(position == 0){
				client.setClient(1);
				client.setProspect(0);
			}else{
				client.setClient(2);
				client.setProspect(1);
			}
		}

		if(parent.getId() == R.id.comm_ville){
			Log.i("ville", selected);
			client.setTown(selected);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		

		if("".equals(maVue != null)){ // ||"".equals(tel.getText().toString())
			Toast.makeText(UpdateClientActivity.this, "Tous les champs sont Obligatoir", Toast.LENGTH_LONG).show();
		}
		else if(v.getId() == R.id.takepic){
			
			String fileName = "new-photo-name.jpg";
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, fileName);
			values.put(MediaStore.Images.Media.DESCRIPTION,"Image capturer par Camera");
			imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, PICK_Camera_IMAGE);
		}
		else{

			
			Log.e(">> img ",withimg +"<<");
			
			client.setName(maVue.get(0).getText().toString());

			if (soc.getLatitude() == null || soc.getLatitude() == 0D) {
				client.setLangitude(longitude);
				client.setLatitude(latitude);
			}

			client.setAddress(address.getText().toString()+"");
			//client.setZip(zip.getText().toString()+"");
			client.setPhone(tel.getText().toString()+"");
			client.setFax(fax.getText().toString()+"");
			client.setEmail(email.getText().toString()+"");
			client.setStatus(1);
			client.setId(soc.getId());
			if(mycity != null || !mycity.equals("")){
				client.setTown(mycity);
			}else{
				client.setTown(ville.getText().toString()+"");
			}
			
			if (v.getId() == R.id.comm_etape) {
				dialog = ProgressDialog.show(UpdateClientActivity.this, getResources().getString(R.string.comerciallab3),
						getResources().getString(R.string.msg_wait), true);
				
				if(CheckOutNet.isNetworkConnected(UpdateClientActivity.this)){
					new EnregistrationTask().execute();
				}else{
					new EnregistrationOfflineTask().execute();
				}
			}
			
			

		}
	}




	private	class EnregistrationTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			
		 
			
			if(withimg){
				prepa_img();
				client.setImage(ba1);
				client.setLieux(lieux);
			}
			resu = manager.update(compte, client);
			
			
		//	resu = "Ce client est mise � jour avec succ�es";
			wakelock.acquire();
			
			VendeurManager vendeurManager = VendeurManagerFactory.getClientManager();
			PayementManager payemn = PayementManagerFactory.getPayementFactory();
			CategorieDao categorie = new CategorieDaoMysql(getApplicationContext());
			CommandeManager managercmd =  new CommandeManagerFactory().getManager();
			CommercialManager managercom = CommercialManagerFactory.getCommercialManager();
			StockVirtual sv  = new StockVirtual(UpdateClientActivity.this);


				myoffline = new Offlineimpl(getApplicationContext());
				if(myoffline.checkAvailableofflinestorage() > 0){
					myoffline.SendOutData(compte);
				}
		
			CheckOutSysc.RelaodClientSectInfoCommDicto(UpdateClientActivity.this, myoffline, compte, vendeurManager, managercom, 3);  // 0);
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
					//Toast.makeText(CommercialActivity.this, resu, Toast.LENGTH_LONG).show();

					AlertDialog.Builder localBuilder = new AlertDialog.Builder(UpdateClientActivity.this);
					localBuilder
					.setMessage(resu)
					.setCancelable(false);
					/*
					.setPositiveButton("Retour",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							ViewGroup group = (ViewGroup)findViewById(R.id.layoutall);

							for (int i = 0, count = group.getChildCount(); i < count; ++i)
							{
								View view = group.getChildAt(i);
								if (view instanceof EditText) {
									((EditText)view).setText("");
								}

							}

							ViewGroup group2 = (ViewGroup)findViewById(R.id.comm_interface);

							for (int i = 0, count = group2.getChildCount(); i < count; ++i)
							{
								View view = group2.getChildAt(i);
								if (view instanceof EditText) {
									((EditText)view).setText("");
								}
							}
						}
					});
					*/
					localBuilder.setNegativeButton("Quitter ",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							UpdateClientActivity.this.finish();
							onClickHome(LayoutInflater.from(UpdateClientActivity.this).inflate(R.layout.activity_update_client, null));
						}
					}
							);
					localBuilder.create().show();

					wakelock.release();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}
	
	private	class EnregistrationOfflineTask  extends AsyncTask<Void, Void, String> {

		private long rs;
		@Override
		protected String doInBackground(Void... arg0) {
			myoffline = new Offlineimpl(UpdateClientActivity.this);
			
			if(withimg){
				prepa_img();
				client.setImage(ba1);
				client.setLieux(lieux);
			}
			rs = myoffline.shnchronizeUpClients(client, compte);
		//	resu = "Ce client est mise � jour avec succ�es";
			wakelock.acquire();
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
					//Toast.makeText(CommercialActivity.this, resu, Toast.LENGTH_LONG).show();
					if(rs != -1){
						resu = getResources().getString(R.string.comm_upok);
					}else{
						resu = getResources().getString(R.string.comm_upko);
					}

					AlertDialog.Builder localBuilder = new AlertDialog.Builder(UpdateClientActivity.this);
					localBuilder
					.setMessage(resu)
					.setCancelable(false);
					/*
					.setPositiveButton("Retour",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							ViewGroup group = (ViewGroup)findViewById(R.id.layoutall);

							for (int i = 0, count = group.getChildCount(); i < count; ++i)
							{
								View view = group.getChildAt(i);
								if (view instanceof EditText) {
									((EditText)view).setText("");
								}

							}

							ViewGroup group2 = (ViewGroup)findViewById(R.id.comm_interface);

							for (int i = 0, count = group2.getChildCount(); i < count; ++i)
							{
								View view = group2.getChildAt(i);
								if (view instanceof EditText) {
									((EditText)view).setText("");
								}
							}
						}
					});
					*/
					localBuilder.setNegativeButton("Quitter ",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							
							Intent intent = new Intent(UpdateClientActivity.this, HomeActivity.class);
							intent.putExtra("user", compte);
							intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity (intent);
							UpdateClientActivity.this.finish();
						}
					}
							);
					localBuilder.create().show();

					wakelock.release();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	public void alertPrdClt(String msg){
		AlertDialog.Builder alert = new AlertDialog.Builder(UpdateClientActivity.this);
		alert.setTitle(getResources().getString(R.string.cmdtofc10));
		alert.setMessage(msg);
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				return;
			}
		});
		alert.setCancelable(true);
		alert.create().show();
	}
	
	public void onClickHome(View v){
		Intent intent = new Intent(this, PersonnePhysiqueActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onClickHome(LayoutInflater.from(UpdateClientActivity.this).inflate(R.layout.activity_update_client, null));
			return true;
		}
		return false;
	}
	
	private void remplire_view(){
		client = new Prospection();
		
		/*
		clientspinner.showDropDown();

		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromInputMethod(parent.getWindowToken(), 0);

		clientspinner.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
		*/


		Log.e("Selected Client Spinner ",soc.toString());

		//name.setHint("Nom soci�t�");
		EditText name = new EditText(UpdateClientActivity.this);
		name.setText(soc.getName());
		name.setTag("comm_nome");
		name.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

		myLayout = (LinearLayout) findViewById(R.id.comm_interface);
		name.setWidth(myLayout.getWidth());
		myLayout.removeAllViews();
		myLayout.addView(name);

		maVue = new ArrayList<>();
		maVue.clear();
		maVue.add(name);
		client.setParticulier(0);
		
		/**************** client spinner ************************/
		
		if(soc.getCompany() == 1){
			//name.setHint("Nom soci�t�");
			etat.setSelection(1);
			EditText name1 = new EditText(UpdateClientActivity.this);
			name1.setText("hello si me");
			//name1.setHint("Nom societe");
			name1.setTag("comm_nome");
			name1.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
			

			myLayout = (LinearLayout) findViewById(R.id.comm_interface);
			name1.setWidth(myLayout.getWidth());
			myLayout.removeAllViews();
			myLayout.addView(name1);

			maVue = new ArrayList<>();
			maVue.clear();
			maVue.add(name1);
			
			type.setSelection(0);
			client.setParticulier(0);
			
		}else{
			//name.setHint("Nom Compléte");
			etat.setSelection(0);
			type.setSelection(1);
			
			EditText firstname = new EditText(UpdateClientActivity.this);
			firstname.setHint("Le Nom");
			firstname.setTag("comm_firstname");

			firstname.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

			EditText lastname = new EditText(UpdateClientActivity.this);
			lastname.setHint("Le Prenom");
			lastname.setTag("comm_lasttname");

			lastname.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);


			firstname.setText(soc.getName());
			lastname.setText(soc.getName());
			
			myLayout = (LinearLayout) findViewById(R.id.comm_interface);
			myLayout.removeAllViews();
			//int k = (myLayout.getWidth() / 2;
			lastname.setWidth(myLayout.getWidth()/2);
			firstname.setWidth(myLayout.getWidth()/2);

			myLayout.addView(firstname);
			myLayout.addView(lastname);

			maVue = new ArrayList<>();
			maVue.clear();
			maVue.add(firstname);
			maVue.add(lastname);

			client.setParticulier(1);
			
		}
		 
		/****************** end *********************************/
		
		
		if(soc.getCompany() == 1) client.setClient(1);
		
		if(soc.getTown() != null && !"null".equals(soc.getTown()))
		ville.setText(soc.getTown());
		else{
			ville.setHint("Ville");
		}
		if(soc.getAddress() != null && !"null".equals(soc.getAddress()))
		address.setText(soc.getAddress());
		else{
			address.setHint("addresse");
		}
		
		if(soc.getPhone() != null && !"null".equals(soc.getPhone()))
		tel.setText(soc.getPhone());
		else{
			tel.setHint("tel");
		}
		if(soc.getFax() != null && !"null".equals(soc.getFax()))
		fax.setText(soc.getFax());
		else{
			fax.setHint("Fax");
		}
		if(soc.getEmail() != null && !"null".equals(soc.getEmail()))
		email.setText(soc.getEmail());
		else{
			email.setHint("Email");
		}
		
		if("".equals(soc.getLogo()) || soc.getLogo() == null){
        	vs.setImageResource(R.drawable.nophoto);
        }else{
        	vs.setImageURI(Uri.parse(UrlImage.pathimg+"/client_img/"+soc.getLogo()));
        }
		
	}
	
	/********************** Function For Camera **************************/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri selectedImageUri = null;
		String filePath = null;
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				selectedImageUri = data.getData();
			}
			break;
		case PICK_Camera_IMAGE:
			if (resultCode == RESULT_OK) {
				selectedImageUri = imageUri;
				/*Bitmap mPic = (Bitmap) data.getExtras().get("data");
				selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), 
				mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
			}
			break;
		}

		if(selectedImageUri != null){
			try {
				withimg = true;
				// OI FILE Manager
				String filemanagerstring = selectedImageUri.getPath();

				// MEDIA GALLERY
				String selectedImagePath = getPath(selectedImageUri);

				if (selectedImagePath != null) {
					filePath = selectedImagePath;
				} else if (filemanagerstring != null) {
					filePath = filemanagerstring;
				} else {
					Toast.makeText(getApplicationContext(), "Unknown path",
							Toast.LENGTH_LONG).show();
					Log.e("Bitmap", "Unknown path");
				}

				if (filePath != null) {
					decodeFile(filePath);
					Log.e("Lien Image ", filePath);
				} else {

					bitmap = null;
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Internal error",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}

	public String getPath(Uri uri) {
		String[] projection = {  MediaStore.MediaColumns.DATA};
		Cursor cursor;
		try{
			cursor = getContentResolver().query(uri, projection, null, null, null);
		} catch (SecurityException e){
			String path = uri.getPath();
			String result = tryToGetStoragePath(path);
			return  result;
		}
		if(cursor != null) {
			//HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			//THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			String filePath = cursor.getString(columnIndex);
			cursor.close();
			return filePath;
		}
		else
			return uri.getPath();               // FOR OI/ASTRO/Dropbox etc
	}

	private String tryToGetStoragePath(String path) {
		int actualPathStart = path.indexOf("//storage");
		String result = path;

		if(actualPathStart!= -1 && actualPathStart< path.length())
			result = path.substring(actualPathStart+1 , path.length());

		return result;
	}


	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 2048;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 2;

		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);

		vs.setImageBitmap(bitmap);

	}
	
	private void prepa_img(){
		
		if(bitmap != null){
			InputStream is;
			BitmapFactory.Options bfo;
			Bitmap bitmapOrg;
			ByteArrayOutputStream bao ;

			bfo = new BitmapFactory.Options();
			bfo.inSampleSize = 2;
			//bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);

			bao = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
			byte [] ba = bao.toByteArray();
			ba1 = Base64.encodeBytes(ba);

			//Log.e("name >> ", name.getText().toString().split("_")[0]);
			lieux = "client-"+client.getName().replaceAll(" ", "-")+".jpg";
		}
		
	}
	

}
