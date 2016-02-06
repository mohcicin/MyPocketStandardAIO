package com.dolibarrmaroc.mypocketstandard.maps;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android2ee.formation.librairies.google.map.utils.direction.DCACallBack;
import com.android2ee.formation.librairies.google.map.utils.direction.GDirectionsApiUtils;
import com.android2ee.formation.librairies.google.map.utils.direction.model.GDirection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.drawable;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.menu;
import com.dolibarrmaroc.mypocketstandard.R.string;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.FactureManager;
import com.dolibarrmaroc.mypocketstandard.business.TechnicienManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.commercial.FactureActivity;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.models.BordereauGps;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.FactureGps;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;
import com.dolibarrmaroc.mypocketstandard.utils.CommandeManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.FactureManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.ForcerActivationGps;
import com.dolibarrmaroc.mypocketstandard.utils.MyLocationListener;
import com.dolibarrmaroc.mypocketstandard.utils.TechnicienManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.VendeurManagerFactory;


@SuppressLint("NewApi")
public class MainActivity extends android.support.v4.app.FragmentActivity implements
ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener,
OnMyLocationButtonClickListener,DCACallBack{

	//FOrcer Activation GPS
	private ForcerActivationGps forcer;

	/*************************** Variables ******************************************************/
	//Declaration Objet
	private VendeurManager vendeurManager;
	private FactureManager factureManager;
	private CommandeManager cmdmanager;
	private TechnicienManager technicien;

	private Compte compte;
	private FactureGps cmd;
	private FactureGps facture;
	private BordereauGps bord;
	private Client client;
	private Client clientLocation;

	//INTERFACTE UI
	private Button factbtn;
	private Button clientbtn;
	private Spinner facturespinner;//clientspinner
	private AutoCompleteTextView factcomplete;
	private AutoCompleteTextView clientspinner;
	private AutoCompleteTextView bordereauspinner;

	//Spinner Remplissage
	private List<String> listclt;
	private List<String> listfact;
	private List<String> listcmd;
	private List<String> listinterv;
	private List<FactureGps> factures;
	private List<FactureGps> cmds;
	private List<Client> clients;
	private List<BordereauGps> interv;
	

	//Asynchrone avec connexion 
	private ProgressDialog dialog;

	//Dialogue Button
	private Dialog dialogbtnfact,dialogbtnclt,dialogbtnintrv;

	//CE QUI CONCERN MAP
	private LocationClient mLocationClient;

	private GoogleMap map;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000)         // 5 seconds
			.setFastestInterval(16)    // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private LatLng myPosition = new LatLng(0, 0);
	private UiSettings mUiSettings;

	private List<MarkerOptions> mesPositions;
	private LatLng myfact,myClient;

	private WakeLock wakelock;

	private int zoom = 11;

	private int type =-1;

	/*************************** METHOD ******************************************************/

	public MainActivity() {
		vendeurManager = VendeurManagerFactory.getClientManager();
		factureManager = FactureManagerFactory.getFactureManager();

		listclt = new ArrayList<String>();
		listfact = new ArrayList<String>();
		listcmd = new ArrayList<>();
		listinterv = new ArrayList<>();

		factures = new ArrayList<>();
		clients = new ArrayList<>();
		cmds = new ArrayList<>();
		interv = new ArrayList<>();

		facture = new FactureGps();
		cmd = new FactureGps();
		client = new Client();
		clientLocation = new Client();

		mesPositions = new ArrayList<>();
		
		//forcer = new ForcerActivationGps(getApplicationContext());
	}

	@Override
	protected void onStart() {
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
		wakelock.acquire();

		dialog = ProgressDialog.show(MainActivity.this, getResources().getString(R.string.map_data),
				getResources().getString(R.string.msg_wait), true);
		new TrackingMapTask().execute();

		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			dialogbtnfact = new Dialog(this);
			dialogbtnfact.setContentView(R.layout.facturelayout);
			dialogbtnfact.setTitle(getResources().getString(R.string.facture_action));

			dialogbtnclt = new Dialog(this);
			dialogbtnclt.setContentView(R.layout.clientlayout);
			dialogbtnclt.setTitle(getResources().getString(R.string.client_action));
			
			dialogbtnintrv = new Dialog(this);
			dialogbtnintrv.setContentView(R.layout.bordereau);
			dialogbtnintrv.setTitle(getResources().getString(R.string.client_action));

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			forcer = new ForcerActivationGps(getApplicationContext());
			
			if(isNetworkConnected(this)){
				//map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

				//map.setMyLocationEnabled(true);
				//mUiSettings = map.getUiSettings();

				getGpsApplicationAlert();

				/*
				if (map == null) {
					Toast.makeText(this, "Google Maps not available", 
							Toast.LENGTH_LONG).show();
				}
				 */

				SupportMapFragment mapFragment =
						(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

				if (savedInstanceState == null) {
					// First incarnation of this activity.
					mapFragment.setRetainInstance(true);
				} else {
					// Reincarnated activity. The obtained map is the same map instance in the previous
					// activity life cycle. There is no need to reinitialize it.
					map = mapFragment.getMap();
				}
				setUpMapIfNeeded();

				Bundle objetbunble  = this.getIntent().getExtras();

				if (objetbunble != null) {
					compte = (Compte) getIntent().getSerializableExtra("user");
					type = Integer.parseInt(getIntent().getStringExtra("type"));
				}

				/*
				clientbtn = (Button) findViewById(R.id.pointerClt);
				factbtn = (Button) findViewById(R.id.pointerFact);
				clientbtn.setOnClickListener(this);
				factbtn.setOnClickListener(this);
				 */

				myHomDialog();
				
				

			}else{
				erreurNetwork();
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}

	public boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected());
	}


	public void getGpsApplicationAlert(){

		LocationManager mlocManager=null;
		android.location.LocationListener mlocListener;
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createGpsDisabledAlert();
		}

	}

	private void createGpsDisabledAlert() {
		Log.e("gps forcer ",forcer + "");
		
		forcer.turnGPSOn();
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder
		.setMessage(getResources().getString(R.string.msg_gps_desactive))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.btn_gps_activer),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				MainActivity.this.showGpsOptions();
				forcer.turnGPSOn();
			}
		}
				);
		localBuilder.setNegativeButton(getResources().getString(R.string.btn_gps_deactiver),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				paramDialogInterface.cancel();
				MainActivity.this.finish();
			}
		}
				);
		localBuilder.create().show();
	}

	private void erreurNetwork(){
		AlertDialog.Builder local = new AlertDialog.Builder(this);

		local
		.setTitle(getResources().getString(R.string.msg_network))
		.setMessage(getResources().getString(R.string.msg_network_alert))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				MainActivity.this.finish();
			}
		}
				);
		local.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				return;
			}
		}
				);
		local.create().show();
	}

	private void showGpsOptions() {
		startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (map == null) {
			// Try to obtain the map from the SupportMapFragment.
			map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				//map.setMyLocationEnabled(true);
				mUiSettings = map.getUiSettings();

				map.setMyLocationEnabled(true);
				map.setOnMyLocationButtonClickListener(this);
			}
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(
					getApplicationContext(),
					this,  // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	/**
	 * Button to get current Location. This demonstrates how to get the current Location as required
	 * without needing to register a LocationListener.
	 */
	public void showMyLocation(View view) {
		if (mLocationClient != null && mLocationClient.isConnected()) {
			String msg = "Location = " + mLocationClient.getLastLocation();
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
		myPosition = new LatLng(location.getLatitude(), location.getLongitude());
		/*if(myfact.longitude != 0 || myClient.latitude != 0 ){
			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(myPosition)
			.zoom(zoom)// Sets the zoom
			.build();    // Creates a CameraPosition from the builder
			map.animateCamera(CameraUpdateFactory.newCameraPosition(
					cameraPosition));
		}
		 */

	}

	/**
	 * Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(
				REQUEST,
				this);  // LocationListener
	}

	/**
	 * Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
	 */
	@Override
	public void onDisconnected() {
		// Do nothing
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	class TrackingMapTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			
			Log.e("this type act ",type+" ");
			switch (type) {
			case 1:
				clients = new ArrayList<>();
				listclt = new ArrayList<String>();
				
				clients = vendeurManager.selectAllClient(compte);
				for (int i = 0; i < clients.size(); i++) {
					listclt.add(clients.get(i).getName());
				}
				
				break;

			case 2:
				factures = new ArrayList<>();
				listfact = new ArrayList<String>();
				
				factures = factureManager.listFacture(compte);

				for (int i = 0; i < factures.size(); i++) {
					FactureGps f = new FactureGps();
					f = factures.get(i);
					listfact.add(f.getNumero());
				}
				break;

			case 3:
				listcmd = new ArrayList<>();
				cmds = new ArrayList<>();
				
				cmdmanager = CommandeManagerFactory.getManager();
				TelephonyManager tManager = (TelephonyManager)MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
				cmds = cmdmanager.charger_commandes_gps(compte, tManager.getDeviceId());
				
				String tmp="";
				
				
				for (int i = 0; i < cmds.size(); i++) {
					FactureGps f = new FactureGps();
					f = cmds.get(i);
					if(f.getNumero().contains("(")){
						tmp = f.getNumero().replace("(", "");
						tmp = tmp.replace(")", "");
						f.setNumero(tmp);
					}
					listcmd.add(f.getNumero());
				}
				break;
				
			case 4:
				interv = new ArrayList<>();
				listinterv = new ArrayList<String>();
				
				technicien = TechnicienManagerFactory.getClientManager();
				
				interv = technicien.selectAllBordereau(compte);

				for (int i = 0; i < interv.size(); i++) {
					BordereauGps f = new BordereauGps();
					f = interv.get(i);
					listinterv.add(f.getNumero());
				}
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
				if (dialog.isShowing()){
					dialog.dismiss();
					myHomDialog();
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}

	}

	private void hideSoftKeyboard() {
		if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText)
		{
			InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}

	}

	/*
	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.pointerClt){
			clientspinner = (Spinner) dialogbtnclt.findViewById(R.id.produitpointer);

			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, listclt);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			clientspinner.setAdapter(dataAdapter);

			Log.i("Clients ", clients.toString());
			Log.d("List pour spinner ", listclt.toString());

			Button annul = (Button) dialogbtnclt.findViewById(R.id.annulershowme); 
			annul.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogbtnclt.dismiss();
				}
			});

			dialogbtnclt.show();

		}else if(v.getId() == R.id.pointerFact){
			//facturespinner= (Spinner) dialogbtnfact.findViewById(R.id.facturepointer);
			factcomplete = (AutoCompleteTextView) dialogbtnfact.findViewById(R.id.facturepointer);

			if(!factcomplete.hasFocus()){
				hideSoftKeyboard();
			}


			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,

					android.R.layout.simple_spinner_item, listfact);
			dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

			//facturespinner.setAdapter(dataAdapter);
			factcomplete.setAdapter(dataAdapter);
			factcomplete.setThreshold(1);
			factcomplete.setTextColor(Color.RED); 
			factcomplete.setOnItemClickListener(new OnItemClickListener() {


				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					factcomplete.showDropDown();
					String selected = (String) parent.getItemAtPosition(position);
					final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

					factcomplete.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
					//Toast.makeText(MainActivity.this, selected, Toast.LENGTH_LONG).show();

					for (int i = 0; i < factures.size(); i++) {
						if(selected.equals(factures.get(i).getNumero())){
							facture = factures.get(i);
							break;
						}
					}
					//factcomplete.setInputType(InputType.TYPE_NULL);
				}
			});

			Button showme = (Button) dialogbtnfact.findViewById(R.id.factshowme);
			showme.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(Double.parseDouble(facture.getLat()) > 0){
						myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
					}else{
						myfact = myPosition;
					}


					CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(myfact)
					.zoom(10)// Sets the zoom
					.build();    // Creates a CameraPosition from the builder
					map.animateCamera(CameraUpdateFactory.newCameraPosition(
							cameraPosition));


					MarkerOptions markMe = new MarkerOptions().position(myfact).title("Facture Numero :"+facture.getNumero())
							.icon(BitmapDescriptorFactory.defaultMarker(
									BitmapDescriptorFactory.HUE_YELLOW));
					/*
	 *	map.addMarker(markMe);
	 */

	/*
					mesPositions.add(markMe);
					clearMap(map);

					dialogbtnfact.dismiss();
				}
			});

			Button itinerer = (Button) dialogbtnfact.findViewById(R.id.itenermoifact);
			itinerer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					//myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));

					if(Double.parseDouble(facture.getLat()) > 0){
						myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
					}else{
						myfact = myPosition;
					}

					CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(myfact)
					.zoom(6)// Sets the zoom
					.build();    // Creates a CameraPosition from the builder
					map.animateCamera(CameraUpdateFactory.newCameraPosition(
							cameraPosition));


					MarkerOptions markMe = new MarkerOptions().position(myfact).title("Facture Numero :"+facture.getNumero())
							.icon(BitmapDescriptorFactory.defaultMarker(
									BitmapDescriptorFactory.HUE_YELLOW));
					/*
	 *	map.addMarker(markMe);
	 */

	/*
					mesPositions.add(markMe);
					clearMap(map);

					getDirections(myPosition,myfact);
					dialogbtnfact.dismiss();
				}
			});

			Button annul = (Button) dialogbtnfact.findViewById(R.id.annulershowme); 
			annul.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogbtnfact.dismiss();
				}
			});
			dialogbtnfact.show();
		}
	}
	 */
	public void clearMap(GoogleMap myMap){
		myMap.clear();

		LatLngBounds newarkBounds = new LatLngBounds(
				new LatLng(21.3381506, -16.9961064),       // South west corner
				new LatLng(28.6727069, -8.6560507));      // North east corner
		GroundOverlayOptions newarkMap = new GroundOverlayOptions()
		.image(BitmapDescriptorFactory.fromResource(R.drawable.sahara))
		.positionFromBounds(newarkBounds);
		myMap.getUiSettings().setCompassEnabled(true);
		//Log.d("z index ", newarkMap.getZIndex());
		myMap.addGroundOverlay(newarkMap);
		mUiSettings.setMyLocationButtonEnabled(true);

		if(mesPositions.size() > 0){
			for (int i = 0; i < mesPositions.size(); i++) {
				map.addMarker(mesPositions.get(i));
			}
		}
	}

	private void getDirections(LatLng mypos,LatLng point) {
		GDirectionsApiUtils.getDirection(this, mypos, point, GDirectionsApiUtils.MODE_DRIVING);
	}

	@Override
	public void onDirectionLoaded(List<GDirection> directions) {
		for(GDirection direction:directions) {
			GDirectionsApiUtils.drawGDirection(direction, map);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("item", "msg");
			switch (item.getItemId()) {
			/************************MENU Client *****************************************/
			case R.id.pointerClt:
				
				 
				clientspinner = (AutoCompleteTextView) dialogbtnclt.findViewById(R.id.produitpointer);
				
				if(!clientspinner.hasFocus()){
					hideSoftKeyboard();
				}

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, listclt);
				dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
				clientspinner.setAdapter(dataAdapter);
				clientspinner.setThreshold(1);
				clientspinner.setTextColor(Color.RED); 

				///Log.i("Clients ", clients.toString());
				//Log.d("List pour spinner ", listclt.toString());

				Button annul = (Button) dialogbtnclt.findViewById(R.id.annulershowme); 
				annul.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogbtnclt.dismiss();
					}
				});

				clientspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,int pos, long arg3) {
						//String selected = parent.getItemAtPosition(pos).toString();

						 
						clientspinner.showDropDown();
						String selected = (String) parent.getItemAtPosition(pos);
						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

						factcomplete.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
						//Toast.makeText(MainActivity.this, selected, Toast.LENGTH_LONG).show();

						for (int i = 0; i < factures.size(); i++) {
							if(selected.equals(factures.get(i).getNumero())){
								clientLocation = clients.get(i);
								break;
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				Button showm = (Button) dialogbtnclt.findViewById(R.id.clientshowme);
				showm.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Log.e("Client Location",clientLocation.toString()+"");
						if(clientLocation != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
							if(clientLocation.getLatitude() != 0){
								myClient = new LatLng(clientLocation.getLongitude(),clientLocation.getLatitude());
							}else{
								myClient = myPosition;
							}


							CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(myClient)
							.zoom(zoom)// Sets the zoom
							.build();    // Creates a CameraPosition from the builder
							map.animateCamera(CameraUpdateFactory.newCameraPosition(
									cameraPosition));


							MarkerOptions markMe = new MarkerOptions().position(myClient).title(clientLocation.getName())
									.icon(BitmapDescriptorFactory.defaultMarker(
											BitmapDescriptorFactory.HUE_GREEN));
							/*
							 *	map.addMarker(markMe);
							 */


							mesPositions.add(markMe);
							clearMap(map);

							dialogbtnclt.dismiss();
						}else{
							Log.e("client","null");
							alertmaps();
						}
						
					}
				});

				Button itinere = (Button) dialogbtnclt.findViewById(R.id.itenermoiclient);
				itinere.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						//myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
						Log.e("Client itener",clientLocation.toString());
						if(clientLocation != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
							if(clientLocation.getLatitude() != 0){
								myClient = new LatLng(clientLocation.getLongitude(),clientLocation.getLatitude());
							}else{
								myClient = myPosition;
							}

							CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(myClient)
							.zoom(zoom)// Sets the zoom
							.build();    // Creates a CameraPosition from the builder
							map.animateCamera(CameraUpdateFactory.newCameraPosition(
									cameraPosition));


							MarkerOptions markMe = new MarkerOptions().position(myClient).title(clientLocation.getName())
									.icon(BitmapDescriptorFactory.defaultMarker(
											BitmapDescriptorFactory.HUE_YELLOW));
							/*
							 *	map.addMarker(markMe);
							 */

							mesPositions.add(markMe);
							clearMap(map);

							getDirections(myPosition,myClient);
							dialogbtnclt.dismiss();
						}else{
							Log.e("clint ","null");
							alertmaps();
						}
						


					}
				});

				dialogbtnclt.show();
				break;

				/************************MENU FACTURE *****************************************/
			case R.id.pointerFact:
				//facturespinner= (Spinner) dialogbtnfact.findViewById(R.id.facturepointer);
				factcomplete = (AutoCompleteTextView) dialogbtnfact.findViewById(R.id.autocomplate);

				if(!factcomplete.hasFocus()){
					hideSoftKeyboard();
				}


				ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,

						android.R.layout.simple_spinner_item, listfact);
				dataAdapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

				//facturespinner.setAdapter(dataAdapter);
				factcomplete.setAdapter(dataAdapter1);
				factcomplete.setThreshold(1);
				factcomplete.setTextColor(Color.RED); 
				factcomplete.setOnItemClickListener(new OnItemClickListener() {


					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						factcomplete.showDropDown();
						String selected = (String) parent.getItemAtPosition(position);
						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

						factcomplete.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
						//Toast.makeText(MainActivity.this, selected, Toast.LENGTH_LONG).show();

						for (int i = 0; i < factures.size(); i++) {
							if(selected.equals(factures.get(i).getNumero())){
								facture = factures.get(i);
								break;
							}
						}
						//factcomplete.setInputType(InputType.TYPE_NULL);
					}
				});

				Button showme = (Button) dialogbtnfact.findViewById(R.id.factshowme);
				showme.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if(facture != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
							if(Double.parseDouble(facture.getLat()) > 0){
								myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
							}else{
								myfact = myPosition;
							}


							CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(myfact)
							.zoom(zoom)// Sets the zoom
							.build();    // Creates a CameraPosition from the builder
							map.animateCamera(CameraUpdateFactory.newCameraPosition(
									cameraPosition));


							MarkerOptions markMe = new MarkerOptions().position(myfact).title(getResources().getString(R.string.facture_num)+facture.getNumero())
									.icon(BitmapDescriptorFactory.defaultMarker(
											BitmapDescriptorFactory.HUE_YELLOW));
							/*
							 *	map.addMarker(markMe);
							 */


							mesPositions.add(markMe);
							clearMap(map);

							dialogbtnfact.dismiss();
						}else{
							Log.e("facture ","null");
							alertmaps();
						}

						
					}
				});

				Button itinerer = (Button) dialogbtnfact.findViewById(R.id.itenermoifact);
				itinerer.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						//myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));

						if(facture != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
							if(Double.parseDouble(facture.getLat()) > 0){
								myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
							}else{
								myfact = myPosition;
							}

							CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(myfact)
							.zoom(10)// Sets the zoom
							.build();    // Creates a CameraPosition from the builder
							map.animateCamera(CameraUpdateFactory.newCameraPosition(
									cameraPosition));


							MarkerOptions markMe = new MarkerOptions().position(myfact).title("Facture Numero :"+facture.getNumero())
									.icon(BitmapDescriptorFactory.defaultMarker(
											BitmapDescriptorFactory.HUE_YELLOW));
							/*
							 *	map.addMarker(markMe);
							 */

							mesPositions.add(markMe);
							clearMap(map);

							getDirections(myPosition,myfact);
							dialogbtnfact.dismiss();
						}else{
							Log.e("facture ","null");
							alertmaps();
						}
						


					}
				});

				Button annul1 = (Button) dialogbtnfact.findViewById(R.id.annulershowme); 
				annul1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogbtnfact.dismiss();
					}
				});
				dialogbtnfact.show();
				break;
				
				/************************MENU Bordereau *****************************************/
			case R.id.pointerBor:
				Log.e("click ","Bordereau");
				//facturespinner= (Spinner) dialogbtnfact.findViewById(R.id.facturepointer);
				bordereauspinner = (AutoCompleteTextView) dialogbtnintrv.findViewById(R.id.bordereaupointer);

				if(!factcomplete.hasFocus()){
					hideSoftKeyboard();
				}


				ArrayAdapter<String> dataAdapte1 = new ArrayAdapter<String>(this,

						android.R.layout.simple_spinner_item, listinterv);
				dataAdapte1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

				//facturespinner.setAdapter(dataAdapter);
				bordereauspinner.setAdapter(dataAdapte1);
				bordereauspinner.setThreshold(1);
				bordereauspinner.setTextColor(Color.RED); 
				bordereauspinner.setOnItemClickListener(new OnItemClickListener() {


					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						bordereauspinner.showDropDown();
						String selected = (String) parent.getItemAtPosition(position);
						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

						bordereauspinner.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
						//Toast.makeText(MainActivity.this, selected, Toast.LENGTH_LONG).show();

						for (int i = 0; i < interv.size(); i++) {
							if(selected.equals(interv.get(i).getNumero())){
								bord = interv.get(i);
								break;
							}
						}
						//factcomplete.setInputType(InputType.TYPE_NULL);
					}
				});

				Button showmen = (Button) dialogbtnintrv.findViewById(R.id.clientshowme);
				showmen.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if(bord != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
							if(Double.parseDouble(bord.getLat()) > 0){
								myfact = new LatLng(Double.parseDouble(bord.getLat()), Double.parseDouble(bord.getLng()));
							}else{
								myfact = myPosition;
							}


							CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(myfact)
							.zoom(zoom)// Sets the zoom
							.build();    // Creates a CameraPosition from the builder
							map.animateCamera(CameraUpdateFactory.newCameraPosition(
									cameraPosition));


							MarkerOptions markMe = new MarkerOptions().position(myfact).title("Bordereau Numero :"+bord.getNumero())
									.icon(BitmapDescriptorFactory.defaultMarker(
											BitmapDescriptorFactory.HUE_YELLOW));
							/*
							 *	map.addMarker(markMe);
							 */


							mesPositions.add(markMe);
							clearMap(map);

							dialogbtnintrv.dismiss();
						}else{
							alertmaps();
						}

						
					}
				});

				Button itinerern = (Button) dialogbtnintrv.findViewById(R.id.itenermoiclient);
				itinerern.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						//myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));

						if(bord != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
							if(Double.parseDouble(bord.getLat()) > 0){
								myfact = new LatLng(Double.parseDouble(bord.getLat()), Double.parseDouble(bord.getLng()));
							}else{
								myfact = myPosition;
							}

							CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(myfact)
							.zoom(10)// Sets the zoom
							.build();    // Creates a CameraPosition from the builder
							map.animateCamera(CameraUpdateFactory.newCameraPosition(
									cameraPosition));


							MarkerOptions markMe = new MarkerOptions().position(myfact).title("Bordereau Numero :"+bord.getNumero())
									.icon(BitmapDescriptorFactory.defaultMarker(
											BitmapDescriptorFactory.HUE_YELLOW));
							/*
							 *	map.addMarker(markMe);
							 */

							mesPositions.add(markMe);
							clearMap(map);

							getDirections(myPosition,myfact);
							dialogbtnintrv.dismiss();
						}else{
							Log.e("facture ","null");
							alertmaps();
						}
						


					}
				});

				Button annul1n = (Button) dialogbtnintrv.findViewById(R.id.annulershowme); 
				annul1n.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogbtnintrv.dismiss();
					}
				});
				
				Log.e("End ","SHow end bor");
				dialogbtnintrv.show();
				break;
				
			}
			
			
		
		return true;

	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}
	*/

	public void onClickHome(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
	
	private void myHomDialog(){
		
		Log.e("in home duialog ",type +"  >>>");
			switch (type) {
			case 1:
				
				factcomplete = (AutoCompleteTextView) dialogbtnclt.findViewById(R.id.produitpointer);

				if(!factcomplete.hasFocus()){
					hideSoftKeyboard();
				}


				ArrayAdapter<String> dataAdapter11 = new ArrayAdapter<String>(this,

						android.R.layout.simple_spinner_item, listclt);
				dataAdapter11.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

				//facturespinner.setAdapter(dataAdapter);
				factcomplete.setAdapter(dataAdapter11);
				factcomplete.setThreshold(1);
				factcomplete.setTextColor(Color.RED); 
				factcomplete.setOnItemClickListener(new OnItemClickListener() {


					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						factcomplete.showDropDown();
						String selected = (String) parent.getItemAtPosition(position);
						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

						factcomplete.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
						 
	 
						for (int i = 0; i < clients.size(); i++) {
							if(selected.equals(clients.get(i).getName())){
								clientLocation = clients.get(i);
								Log.e("Client in home selected",clientLocation.toString());
								break;
							}
						}
					}
				});
				

				Button showm = (Button) dialogbtnclt.findViewById(R.id.clientshowme);
				showm.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(clientLocation != null){
							Log.e("Client Location",clientLocation.toString());
							if(clientLocation != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
								if(clientLocation.getLatitude() != 0){
									myClient = new LatLng(clientLocation.getLongitude(),clientLocation.getLatitude());
								}else{
									myClient = myPosition;
								}


								CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(myClient)
								.zoom(zoom)// Sets the zoom
								.build();    // Creates a CameraPosition from the builder
								map.animateCamera(CameraUpdateFactory.newCameraPosition(
										cameraPosition));


								MarkerOptions markMe = new MarkerOptions().position(myClient).title(clientLocation.getName())
										.icon(BitmapDescriptorFactory.defaultMarker(
												BitmapDescriptorFactory.HUE_GREEN));
								/*
								 *	map.addMarker(markMe);
								 */


								mesPositions.add(markMe);
								clearMap(map);

								dialogbtnclt.dismiss();
							}else{
								Log.e("clien ","null");alertmaps();
							}
						}else{
							Log.e("clien ","null");alertmaps();
						}
						
					}
				});

				Button itinere = (Button) dialogbtnclt.findViewById(R.id.itenermoiclient);
				itinere.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						//myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
						if(clientLocation != null){
							if(clientLocation != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
								if(clientLocation.getLatitude() != 0){
									myClient = new LatLng(clientLocation.getLongitude(),clientLocation.getLatitude());
								}else{
									myClient = myPosition;
								}

								CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(myClient)
								.zoom(zoom)// Sets the zoom
								.build();    // Creates a CameraPosition from the builder
								map.animateCamera(CameraUpdateFactory.newCameraPosition(
										cameraPosition));


								MarkerOptions markMe = new MarkerOptions().position(myClient).title(clientLocation.getName())
										.icon(BitmapDescriptorFactory.defaultMarker(
												BitmapDescriptorFactory.HUE_YELLOW));
								/*
								 *	map.addMarker(markMe);
								 */

								mesPositions.add(markMe);
								clearMap(map);

								getDirections(myPosition,myClient);
								dialogbtnclt.dismiss();
							}else{
								Log.e("clien ","null");alertmaps();
							}
						}else{
							Log.e("clien ","null");alertmaps();
						}


					}
				});
				
				Button annul11 = (Button) dialogbtnclt.findViewById(R.id.annulershowme); 
				annul11.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogbtnclt.dismiss(); 
					}
				});

				dialogbtnclt.show();
				break;
			case 2:
				factcomplete = (AutoCompleteTextView) dialogbtnfact.findViewById(R.id.autocomplate);

				if(!factcomplete.hasFocus()){
					hideSoftKeyboard();
				}


				ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,

						android.R.layout.simple_spinner_item, listfact);
				dataAdapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

				//facturespinner.setAdapter(dataAdapter);
				factcomplete.setAdapter(dataAdapter1);
				factcomplete.setThreshold(1);
				factcomplete.setTextColor(Color.RED); 
				factcomplete.setOnItemClickListener(new OnItemClickListener() {


					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						factcomplete.showDropDown();
						String selected = (String) parent.getItemAtPosition(position);
						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

						factcomplete.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
						//Toast.makeText(MainActivity.this, selected, Toast.LENGTH_LONG).show();

						for (int i = 0; i < factures.size(); i++) {
							if(selected.equals(factures.get(i).getNumero())){
								facture = factures.get(i);
								break;
							}
						}
						//factcomplete.setInputType(InputType.TYPE_NULL);
					}
				});

				Button showme = (Button) dialogbtnfact.findViewById(R.id.factshowme);
				showme.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(facture != null){
							if(facture.getLat() != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
								if(Double.parseDouble(facture.getLat()) > 0){
									myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
								}else{
									myfact = myPosition;
								}


								CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(myfact)
								.zoom(zoom)// Sets the zoom
								.build();    // Creates a CameraPosition from the builder
								map.animateCamera(CameraUpdateFactory.newCameraPosition(
										cameraPosition));


								MarkerOptions markMe = new MarkerOptions().position(myfact).title(getResources().getString(R.string.facture_num)+facture.getNumero())
										.icon(BitmapDescriptorFactory.defaultMarker(
												BitmapDescriptorFactory.HUE_YELLOW));
								/*
								 *	map.addMarker(markMe);
								 */


								mesPositions.add(markMe);
								clearMap(map);

								dialogbtnfact.dismiss();
							}else{
								Log.e("clint ","null");alertmaps();
							}
						}else{
							Log.e("clint ","null");alertmaps();
						}

						
					}
				});

				Button itinerer = (Button) dialogbtnfact.findViewById(R.id.itenermoifact);
				itinerer.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						//myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
						if(facture != null){
							if(facture.getLat() != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
								if(Double.parseDouble(facture.getLat()) > 0){
									myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
								}else{
									myfact = myPosition;
								}

								Log.e("myPoss",myPosition +" ##myfact "+myfact);

								CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(myfact)
								.zoom(10)// Sets the zoom
								.build();    // Creates a CameraPosition from the builder
								map.animateCamera(CameraUpdateFactory.newCameraPosition(
										cameraPosition));


								MarkerOptions markMe = new MarkerOptions().position(myfact).title("Facture Numero :"+facture.getNumero())
										.icon(BitmapDescriptorFactory.defaultMarker(
												BitmapDescriptorFactory.HUE_YELLOW));
								/*
								 *	map.addMarker(markMe);
								 */

								mesPositions.add(markMe);
								clearMap(map);

								getDirections(myPosition,myfact);
								dialogbtnfact.dismiss();

							}else{
								Log.e("clint ","null");alertmaps();
							}

						}else{
							Log.e("clint ","null");alertmaps();
						}


					}
				});

				Button annul1 = (Button) dialogbtnfact.findViewById(R.id.annulershowme); 
				annul1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogbtnfact.dismiss();
					}
				});
				dialogbtnfact.show();
				break;

			case 3:
				factcomplete = (AutoCompleteTextView) dialogbtnfact.findViewById(R.id.autocomplate);

				if(!factcomplete.hasFocus()){
					hideSoftKeyboard();
				}


				ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listcmd);
				dataAdapter3.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

				//facturespinner.setAdapter(dataAdapter);
				factcomplete.setAdapter(dataAdapter3);
				factcomplete.setThreshold(1);
				factcomplete.setTextColor(Color.RED); 
				factcomplete.setOnItemClickListener(new OnItemClickListener() {


					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						factcomplete.showDropDown();
						String selected = (String) parent.getItemAtPosition(position);
						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

						factcomplete.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
						//Toast.makeText(MainActivity.this, selected, Toast.LENGTH_LONG).show();

						for (int i = 0; i < cmds.size(); i++) {
							if(selected.equals(cmds.get(i).getNumero())){
								cmd = cmds.get(i);
								break;
							}
						}
						//factcomplete.setInputType(InputType.TYPE_NULL);
					}
				});

				Button showme2 = (Button) dialogbtnfact.findViewById(R.id.factshowme);
				showme2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(cmd != null){
							if(cmd.getLat() != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
								if(Double.parseDouble(cmd.getLat()) > 0){
									myfact = new LatLng(Double.parseDouble(cmd.getLat()), Double.parseDouble(cmd.getLng()));
								}else{
									myfact = myPosition;
								}


								CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(myfact)
								.zoom(zoom)// Sets the zoom
								.build();    // Creates a CameraPosition from the builder
								map.animateCamera(CameraUpdateFactory.newCameraPosition(
										cameraPosition));


								MarkerOptions markMe = new MarkerOptions().position(myfact).title(getResources().getString(R.string.commande_num)+cmd.getNumero())
										.icon(BitmapDescriptorFactory.defaultMarker(
												BitmapDescriptorFactory.HUE_YELLOW));
								/*
								 *	map.addMarker(markMe);
								 */


								mesPositions.add(markMe);
								clearMap(map);

								dialogbtnfact.dismiss();
							}else{
								Log.e("clint ","null");alertmaps();
							}
						}else{
							Log.e("clint ","null");alertmaps();
						}

						
					}
				});

				Button itinerer2 = (Button) dialogbtnfact.findViewById(R.id.itenermoifact);
				itinerer2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						//myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));
						if(cmd != null){
							if(cmd.getLat() != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
								if(Double.parseDouble(cmd.getLat()) > 0){
									myfact = new LatLng(Double.parseDouble(cmd.getLat()), Double.parseDouble(cmd.getLng()));
								}else{
									myfact = myPosition;
								}

								Log.e("myPoss",myPosition +" ##myfact "+myfact);

								CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(myfact)
								.zoom(10)// Sets the zoom
								.build();    // Creates a CameraPosition from the builder
								map.animateCamera(CameraUpdateFactory.newCameraPosition(
										cameraPosition));


								MarkerOptions markMe = new MarkerOptions().position(myfact).title(getResources().getString(R.string.commande_num)+cmd.getNumero())
										.icon(BitmapDescriptorFactory.defaultMarker(
												BitmapDescriptorFactory.HUE_YELLOW));
								/*
								 *	map.addMarker(markMe);
								 */

								mesPositions.add(markMe);
								clearMap(map);

								getDirections(myPosition,myfact);
								dialogbtnfact.dismiss();
							}else{
								Log.e("clint ","null");
								alertmaps();
							}

						}else{
							Log.e("clint ","null");
							alertmaps();
						}

					}
				});

				Button annul12 = (Button) dialogbtnfact.findViewById(R.id.annulershowme); 
				annul12.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogbtnfact.dismiss();
					}
				});
				dialogbtnfact.show();
				break;	
			case 4:
				bordereauspinner = (AutoCompleteTextView) dialogbtnintrv.findViewById(R.id.bordereaupointer);

				if(!bordereauspinner.hasFocus()){
					hideSoftKeyboard();
				}


				ArrayAdapter<String> dataAdapter3b = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listinterv);
				dataAdapter3b.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

				//facturespinner.setAdapter(dataAdapter);
				bordereauspinner.setAdapter(dataAdapter3b);
				bordereauspinner.setThreshold(1);
				bordereauspinner.setTextColor(Color.RED); 
				bordereauspinner.setOnItemClickListener(new OnItemClickListener() {


					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						bordereauspinner.showDropDown();
						String selected = (String) parent.getItemAtPosition(position);
						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

						bordereauspinner.setFilters(new InputFilter[] {new InputFilter.LengthFilter(selected.length())});
						//Toast.makeText(MainActivity.this, selected, Toast.LENGTH_LONG).show();

						for (int i = 0; i < interv.size(); i++) {
							if(selected.equals(interv.get(i).getNumero())){
								bord = interv.get(i);
								break;
							}
						}
						//factcomplete.setInputType(InputType.TYPE_NULL);
					}
				});

				Button showme2b = (Button) dialogbtnintrv.findViewById(R.id.clientshowme);
				showme2b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(bord != null){	
							if(bord.getLat() != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
								if(Double.parseDouble(bord.getLat()) > 0){
									myfact = new LatLng(Double.parseDouble(bord.getLat()), Double.parseDouble(bord.getLng()));
								}else{
									myfact = myPosition;
								}


								CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(myfact)
								.zoom(zoom)// Sets the zoom
								.build();    // Creates a CameraPosition from the builder
								map.animateCamera(CameraUpdateFactory.newCameraPosition(
										cameraPosition));


								MarkerOptions markMe = new MarkerOptions().position(myfact).title(getResources().getString(R.string.num_bordereau) +":" +bord.getNumero())
										.icon(BitmapDescriptorFactory.defaultMarker(
												BitmapDescriptorFactory.HUE_YELLOW));
								/*
								 *	map.addMarker(markMe);
								 */


								mesPositions.add(markMe);
								clearMap(map);

								dialogbtnintrv.dismiss();
							}else{
								Log.e("clint ","null");alertmaps();
							}
						}else{
							Log.e("clint ","null");alertmaps();
						}
						
					}
				});

				Button itinerer2b = (Button) dialogbtnintrv.findViewById(R.id.itenermoiclient);
				itinerer2b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						//myfact = new LatLng(Double.parseDouble(facture.getLat()), Double.parseDouble(facture.getLng()));

					if(bord != null){	
						if(bord.getLat() != null && CheckOutNet.isNetworkConnected(MainActivity.this) && isNetworkConnected(MainActivity.this)){
							if(Double.parseDouble(bord.getLat()) > 0){
								myfact = new LatLng(Double.parseDouble(bord.getLat()), Double.parseDouble(bord.getLng()));
							}else{
								myfact = myPosition;
							}
							
							Log.e("myPoss",myPosition +" ##myfact "+myfact);

							CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(myfact)
							.zoom(10)// Sets the zoom
							.build();    // Creates a CameraPosition from the builder
							map.animateCamera(CameraUpdateFactory.newCameraPosition(
									cameraPosition));


							MarkerOptions markMe = new MarkerOptions().position(myfact).title(getResources().getString(R.string.num_bordereau) +":" +bord.getNumero())
									.icon(BitmapDescriptorFactory.defaultMarker(
											BitmapDescriptorFactory.HUE_YELLOW));
							/*
							 *	map.addMarker(markMe);
							 */

							mesPositions.add(markMe);
							clearMap(map);

							getDirections(myPosition,myfact);
							dialogbtnintrv.dismiss();
						}else{
							Log.e("clint ","null");
							alertmaps();
						}
						
					}else{
						alertmaps();
					}

					}
				});

				Button annul12b = (Button) dialogbtnintrv.findViewById(R.id.annulershowme); 
				annul12b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialogbtnintrv.dismiss();
					}
				});
				dialogbtnintrv.show();
				break;
			default:
				break;
			}
		 
		
	}
	
	public void alertmaps(){
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle(getResources().getString(R.string.mapstitle7));
		alert.setMessage(getResources().getString(R.string.mapstitle5) + " \n "+getResources().getString(R.string.mapstitle4)+ "\n"+getResources().getString(R.string.mapstitle6));
		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				return;
			}
		});
		alert.setCancelable(false);
		alert.create().show();
	}
}
