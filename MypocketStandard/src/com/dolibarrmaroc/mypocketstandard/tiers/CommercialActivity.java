package com.dolibarrmaroc.mypocketstandard.tiers;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dolibarrmaroc.mypocketstandard.AboutActivity;
import com.dolibarrmaroc.mypocketstandard.R;
import com.dolibarrmaroc.mypocketstandard.R.id;
import com.dolibarrmaroc.mypocketstandard.R.layout;
import com.dolibarrmaroc.mypocketstandard.R.string;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDao;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.DatabaseHandler;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Dictionnaire;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.ProspectData;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.offline.Offlineimpl;
import com.dolibarrmaroc.mypocketstandard.offline.ioffline;
import com.dolibarrmaroc.mypocketstandard.utils.Base64;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutNet;
import com.dolibarrmaroc.mypocketstandard.utils.CheckOutSysc;
import com.dolibarrmaroc.mypocketstandard.utils.CommandeManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.CommercialManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.PayementManagerFactory;
import com.dolibarrmaroc.mypocketstandard.utils.VendeurManagerFactory;

@SuppressLint("NewApi")
public class CommercialActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	/**************************************** GPS DATA *****************************************/
	private LocationManager mLocationManager = null;
	private int LOCATION_INTERVAL = 1000;
	private float LOCATION_DISTANCE = 16;
	private double latitude;
	private double longitude;
	
	private ioffline myoffline;
	
	//database 
		private DatabaseHandler database;

	private LocationListener		onLocationChange	= new LocationListener()
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

	public Spinner etat;
	private Spinner type;
	private Spinner ville;

	private Button btn,suivant;
	private LinearLayout myLayout;

	private List<EditText> maVue;

	//Asynchrone avec connexion 
	private ProgressDialog dialog;
	private ProgressDialog dialog2;
	String resu ;
	
	private StockVirtual sv;
	
	/************************CAMERA************************/
	MediaPlayer mp=new MediaPlayer();
	private static final int PICK_IMAGE = 1;
	private static final int PICK_Camera_IMAGE = 2;
	private Bitmap bitmap;
	private Uri imageUri;
	private ImageButton camera;
	private String ba1;
	private String lieux;
	private static boolean withimg = false;
	/*****************Dialog composant****************/
	private Dialog dialogcamera;
	private Button galerie,cam,upload,cancel,next;
	private ImageView imgView;
	
	public CommercialActivity() {
		// TODO Auto-generated constructor stub
		manager = CommercialManagerFactory.getCommercialManager();
		compte = new Compte();
		gps = new GpsTracker();
		data = new ProspectData();
		client = new Prospection();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commercial);
		
        
		try {
			
			
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
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
			}



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
			ville = (Spinner) findViewById(R.id.comm_ville);
			ville.setOnItemSelectedListener(this);

			btn = (Button) findViewById(R.id.comm_etape);
			btn.setOnClickListener(this);

			suivant = (Button) findViewById(R.id.comm_suivant);
			suivant.setOnClickListener(this);

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			
			
			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "no sleep");
			wakelock.acquire();
			
			
			myoffline = new Offlineimpl(getApplicationContext());
			/*
			if(CheckOutNet.isNetworkConnected(getApplicationContext())){
				
				
				
				myoffline = new Offlineimpl(getApplicationContext());
				if(myoffline.checkAvailableofflinestorage() > 0){
					dialog2 = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.caus15),
							getResources().getString(R.string.msg_wait_sys), true);
					new ServerSideTask().execute(); 
				}else{
					dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.map_data),
							getResources().getString(R.string.msg_wait), true);
					new ConnexionTask().execute();
				}
				
			}else{
				dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.comerciallab1),
						getResources().getString(R.string.msg_wait), true);
				new OfflineTask().execute();
			}
			*/
			dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.comerciallab1),
					getResources().getString(R.string.msg_wait), true);
			new OfflineTask().execute();
			
			/*
			Class squareClass = Class.forName("com.marocgeo.als.models.Prospection");
	        
	        Field[] fields = squareClass.getDeclaredFields(); 
	        for (Field f : fields) {
	            Log.e("field name = " ,f.getName());           
	        }
	        
	        */
			
			
			
			
			CommercialManager manager = CommercialManagerFactory.getCommercialManager();
			// manager.getInfos(compte);
			
			sv  = new StockVirtual(CommercialActivity.this);
			
			/*********************************************/
			dialogcamera = new Dialog(this);
			dialogcamera.setContentView(R.layout.commercial_camera);
			dialogcamera.setTitle(getResources().getString(R.string.comerciallab13));
			//dialogcamera.requestWindowFeature(Window.FEATURE_NO_TITLE);
			galerie = (Button) dialogcamera.findViewById(R.id.imggaleriebtn);
			galerie.setOnClickListener(this);
			
			cam 	= (Button) dialogcamera.findViewById(R.id.imgcamerabtn);
			cam.setOnClickListener(this);
			
			upload 	= (Button) dialogcamera.findViewById(R.id.imguploadbtndialog);
			upload.setOnClickListener(this);
			
			cancel 	= (Button) dialogcamera.findViewById(R.id.imgcancelbtndialog);
			cancel.setOnClickListener(this);
			
			imgView = (ImageView) dialogcamera.findViewById(R.id.ImageViewdialog);
			
			camera = (ImageButton) findViewById(R.id.comm_camera);
			camera.setOnClickListener(this);
			
			next = (Button) dialogcamera.findViewById(R.id.imgsuivantdbtndialog);
			next.setOnClickListener(this);
			/*********************************************/
			
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	private	class ConnexionTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			CommercialManager manager = CommercialManagerFactory.getCommercialManager();
			data = manager.getInfos(compte);
			Log.e("data ",data+" hopa" );
			//wakelock.acquire();
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
					addItemsOnSpinner(ville,data.getVilles());
					client.setCommercial_id(Integer.parseInt(compte.getIduser()));
					//wakelock.release();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}
	}
	
	private	class OfflineTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			data = myoffline.LoadProspect("");
			//wakelock.acquire();
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
					addItemsOnSpinner(ville,data.getVilles());
					client.setCommercial_id(Integer.parseInt(compte.getIduser()));
				//	wakelock.release();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}
	}

	public void addItemsOnSpinner(Spinner s,List<String> list) {

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(dataAdapter);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String selected = parent.getItemAtPosition(position).toString();

		if(parent.getId() == R.id.comm_ste){
			Log.i("Ste", selected+" Positin "+position);
			testeur = position;

			if (position == 0){
				/*
				 * <EditText
                android:id="@+id/comm_rc"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:hint="@string/comm_1"
                android:inputType="textPersonName" />
				 */
				//name.setHint("Nom Compl�te");
				EditText firstname = new EditText(CommercialActivity.this);
				firstname.setHint("Le Nom");
				firstname.setTag("comm_firstname");
				

				firstname.setHeight(35);

				EditText lastname = new EditText(CommercialActivity.this);
				lastname.setHint("Le Prenom");
				lastname.setTag("comm_lasttname");
				
				
				lastname.setHeight(35);//LinearLayout.LayoutParams.WRAP_CONTENT

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

			}else{
				//name.setHint("Nom soci�t�");
				EditText name = new EditText(CommercialActivity.this);
				name.setHint("Nom societe");
				name.setTag("comm_nome");
				name.setHeight(40);

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

		// ||  "".equals(tel.getText().toString())
		if("".equals(maVue != null)	){
			Toast.makeText(CommercialActivity.this, getResources().getString(R.string.comerciallab2), Toast.LENGTH_LONG).show();
		}
		else{

			if (maVue.size() > 1) {
				client.setLastname(maVue.get(1).getText().toString());
				client.setName(maVue.get(0).getText().toString()+" "+maVue.get(1).getText().toString());
				client.setFirstname(maVue.get(0).getText().toString());
			}else{
				client.setName(maVue.get(0).getText().toString());
			}

			client.setLangitude(longitude);
			client.setLatitude(latitude);
			client.setAddress(address.getText().toString());
			client.setZip(zip.getText().toString());
			client.setPhone(tel.getText().toString());
			client.setFax(fax.getText().toString());
			client.setEmail(email.getText().toString());
			client.setStatus(1);

			if (v.getId() == R.id.comm_etape) {
				
				//checkRequiredFields();
				client.setEmail(email.getText().toString());
				if(email.getText().toString().length() == 0 || email.getText().toString().equals("")){
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date()); 
					
					client.setEmail(calendar.getTime().getTime()+"_anonyme@gmail.com");
					//email.setText(calendar.getTime().getTime()+"_anonyme@gmail.com");
				}
				
				Log.e("eml ",client.getEmail()+"");
				
				if(checkRequiredFields().size() > 0){
					alertinvonan();
				}else{
					if(CheckOutNet.isNetworkConnected(getApplicationContext())){
						
						prepa_img();
						
						if(myoffline.checkRefClient(client.getName(),client.getEmail()) == -1){
							dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.comerciallab3),
									getResources().getString(R.string.msg_wait), true);
							
							new EnregistrationTask().execute();
						}else{
							
							AlertDialog.Builder localBuilder = new AlertDialog.Builder(CommercialActivity.this);
							localBuilder
							.setTitle(getResources().getString(R.string.cmdtofc10))
							.setMessage(R.string.caus16)
							.setCancelable(false)
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
							localBuilder.show();
						}
						
					}else{
						
						if(myoffline.checkRefClient(client.getName(),client.getEmail()) == -1){
							dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.comerciallab3),
									getResources().getString(R.string.msg_wait), true);
							
							prepa_img();
							new EnregistrationOfflineTask().execute();
						}else{
							
							AlertDialog.Builder localBuilder = new AlertDialog.Builder(CommercialActivity.this);
							localBuilder
							.setTitle(getResources().getString(R.string.cmdtofc10))
							.setMessage(R.string.caus16)
							.setCancelable(false)
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
							localBuilder.show();
						}
						
					}
					//String res = manager.insert(compte, client);
					//Log.d("Client",client.toString());
				}
			}else if(v == camera){
				//checkRequiredFields();
				if(email.getText().toString().length() == 0 || email.getText().toString().equals("")){

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date()); 

					client.setEmail(calendar.getTime().getTime()+"_anonyme@gmail.com");
					email.setText(calendar.getTime().getTime()+"_anonyme@gmail.com");
				}

				if(checkRequiredFields().size() > 0){
					alertinvonan();
				}else{
					if(myoffline.checkRefClient(client.getName(),client.getEmail()) == -1){
						/*
							dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.comerciallab3),
									getResources().getString(R.string.msg_wait), true);

							new EnregistrationTask().execute();
						 */
						dialogcamera.show();
					}else{

						AlertDialog.Builder localBuilder = new AlertDialog.Builder(CommercialActivity.this);
						localBuilder
						.setTitle(getResources().getString(R.string.cmdtofc10))
						.setMessage(R.string.caus16)
						.setCancelable(false)
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
						localBuilder.show();
					}

				}
			}else if(v == suivant){
				if(checkRequiredFields().size() > 0){
					alertinvonan();
				}else{
					
					prepa_img();
					
					Intent intent = new Intent(CommercialActivity.this,SecondeEtapeCommercialActivity.class);
					intent.putExtra("client", client);
					intent.putExtra("user", compte);
					intent.putStringArrayListExtra("form", (ArrayList<String>) data.getJuridique());
					intent.putStringArrayListExtra("tierce", (ArrayList<String>) data.getTypent());
					intent.putExtra("code_juridique", data.getJuridique_code());
					intent.putExtra("code_type", data.getTypent_code());
					intent.putExtra("id_type", data.getTypent_id());
					intent.putExtra("type", "0");
					intent.putExtra("ba", ba1);
					intent.putExtra("lieux", lieux);
					startActivity(intent);
				}
			}else if(v == cam){
				withimg = true;
				String fileName = "new-photo-name.jpg";
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, fileName);
				values.put(MediaStore.Images.Media.DESCRIPTION,"Image capturer par Camera");
				imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(intent, PICK_Camera_IMAGE);
				//dialogcamera.dismiss();
			}else if (v == galerie) {
				try {
					withimg = true;
					Intent gintent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					gintent.setType("image/*");
					//gintent.setAction(Intent.ACTION_GET_CONTENT,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					//gintent.setAction(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(gintent,PICK_IMAGE);
					//dialogcamera.dismiss();

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage() +" << ", e);

				}
			}else if(v == cancel){
				dialogcamera.dismiss();
			}else if (v == upload) {
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

				if (v == upload) {
					withimg = true;
					if(CheckOutNet.isNetworkConnected(getApplicationContext())){
						dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.comerciallab3),
								getResources().getString(R.string.msg_wait), true);

						new EnregistrationTask().execute();
					}else{
						dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.comerciallab3),
								getResources().getString(R.string.msg_wait), true);

						new EnregistrationOfflineTask().execute();
					}
				}

			}else if(v == next){
				
				Log.e("in next ","hello");
				prepa_img();
				
				Intent intent = new Intent(CommercialActivity.this,SecondeEtapeCommercialActivity.class);
				intent.putExtra("client", client);
				intent.putExtra("user", compte);
				intent.putStringArrayListExtra("form", (ArrayList<String>) data.getJuridique());
				intent.putStringArrayListExtra("tierce", (ArrayList<String>) data.getTypent());
				intent.putExtra("code_juridique", data.getJuridique_code());
				intent.putExtra("code_type", data.getTypent_code());
				intent.putExtra("id_type", data.getTypent_id());
				intent.putExtra("type", "1");
				intent.putExtra("ba", ba1);
				intent.putExtra("lieux", lieux);
				startActivity(intent);
			}else{
				
				prepa_img();
				
				Intent intent = new Intent(CommercialActivity.this,SecondeEtapeCommercialActivity.class);
				intent.putExtra("client", client);
				intent.putExtra("user", compte);
				intent.putStringArrayListExtra("form", (ArrayList<String>) data.getJuridique());
				intent.putStringArrayListExtra("tierce", (ArrayList<String>) data.getTypent());
				intent.putExtra("code_juridique", data.getJuridique_code());
				intent.putExtra("code_type", data.getTypent_code());
				intent.putExtra("id_type", data.getTypent_id());
				intent.putExtra("type", "1");
				intent.putExtra("ba", ba1);
				intent.putExtra("lieux", lieux);
				startActivity(intent);
				
				
				
			}

		}
	}




	private	class EnregistrationTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			
			if(CheckOutNet.isNetworkConnected(getApplicationContext())){
				//resu = manager.insert(compte, client);
				
				if (withimg) {
					prepa_img();
					resu = manager.insertWithImage(compte, client,ba1,lieux);
				}else{
					resu = manager.insert(compte, client);
				}
				
				VendeurManager vendeurManager = VendeurManagerFactory.getClientManager();
				PayementManager payemn = PayementManagerFactory.getPayementFactory();
				CategorieDao categorie = new CategorieDaoMysql(getApplicationContext());
				CommandeManager managercmd =  new CommandeManagerFactory().getManager();
				CommercialManager managercom = CommercialManagerFactory.getCommercialManager();
				sv  = new StockVirtual(CommercialActivity.this);


				myoffline = new Offlineimpl(getApplicationContext());
								if(myoffline.checkAvailableofflinestorage() > 0){
									myoffline.SendOutData(compte);
								}
			
				//CheckOutSysc.ReloadProdClt(CommercialActivity.this, myoffline, compte, vendeurManager, payemn, sv, categorie, managercmd, 3,managercom);
				CheckOutSysc.ReloadProdClt(CommercialActivity.this, myoffline, compte, vendeurManager, payemn, sv, categorie, managercmd, 7,managercom);
				//CheckOutSysc.RelaodClientSectInfoCommDicto(CommercialActivity.this, myoffline, compte, vendeurManager, managercom, 0);
			}else{
				if(!myoffline.checkFolderexsiste()){
		        	showmessageOffline();
		        	resu =getResources().getString(R.string.comerciallab4);
				}else{
					database = new DatabaseHandler(getApplicationContext());
					client.setId((int)database.addrow("clt"));
					resu = myoffline.shynchronizeProspection(client,compte);
				}
			}
			
			//wakelock.acquire();
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
					if(resu.equals("-1")){
						resu = getResources().getString(R.string.comerciallab5); 
					}else{
						resu =getResources().getString(R.string.comerciallab6); 
					}
					AlertDialog.Builder localBuilder = new AlertDialog.Builder(CommercialActivity.this);
					localBuilder
					.setMessage(resu)
					.setCancelable(false)
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
							
							onClickHome(LayoutInflater.from(CommercialActivity.this).inflate(R.layout.activity_commercial, null));
						}
					});
					localBuilder.setNegativeButton("Quitter ",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							CommercialActivity.this.finish();
							Intent intent = new Intent(CommercialActivity.this,HomeActivity.class);
							intent.putExtra("user", compte);
							startActivity(intent);
						}
					}
							);
					localBuilder.create().show();

					//wakelock.release();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}
	}
	
	private	class EnregistrationOfflineTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			
			if(!myoffline.checkFolderexsiste()){
	        	showmessageOffline();
	        	resu =getResources().getString(R.string.comerciallab4);  
			}else{
				database = new DatabaseHandler(getApplicationContext());
				client.setId((int)database.addrow("clt"));
				
				Log.e("in out goood",withimg +"  ");
				
				if (withimg) {
					prepa_img();
					client.setImage(ba1);
					client.setLieux(lieux);
					
				}
				resu = myoffline.shynchronizeProspection(client,compte);
			}

			
			
			
			//wakelock.acquire();
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

					AlertDialog.Builder localBuilder = new AlertDialog.Builder(CommercialActivity.this);
					localBuilder
					.setMessage(resu)
					.setCancelable(false)
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
							
							onClickHome(LayoutInflater.from(CommercialActivity.this).inflate(R.layout.activity_commercial, null));
						}
					});
					localBuilder.setNegativeButton("Quitter ",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							CommercialActivity.this.finish();
							Intent intent = new Intent(CommercialActivity.this,HomeActivity.class);
							intent.putExtra("user", compte);
							startActivity(intent);
						}
					}
							);
					localBuilder.create().show();

					//wakelock.release();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage() +" << ",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage() +" << ", e);
			}
		}
	}

	 
	
	public void showmessageOffline(){
		try {
			 
	         LayoutInflater inflater = this.getLayoutInflater();
	         View dialogView = inflater.inflate(R.layout.msgstorage, null);
	         
	         AlertDialog.Builder dialog =  new AlertDialog.Builder(CommercialActivity.this);
	         dialog.setView(dialogView);
 	 	     dialog.setTitle(R.string.caus1);
 	 	     dialog.setPositiveButton(R.string.caus8, new DialogInterface.OnClickListener() {
 	 	        public void onClick(DialogInterface dialog, int which) { 
 	 	        	 dialog.cancel();
 	 	        }
 	 	     });
 	 	     dialog.setCancelable(true);
 	 	     dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	class ServerSideTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			try {
				
				List<Produit> products = new ArrayList<>();
				List<Client> clients = new ArrayList<>();
				
				VendeurManager vendeurManager = VendeurManagerFactory.getClientManager();
				
				Dictionnaire dico  = new Dictionnaire();
				
				if(CheckOutNet.isNetworkConnected(getApplicationContext())){
					myoffline.SendOutData(compte);	
				}
				

				
				if(!myoffline.checkFolderexsiste()){
					showmessageOffline();
				}else{
					/*********************** offline ****************************************/
					Log.e("begin offline from network",">>start load");
					if(CheckOutNet.isNetworkConnected(getApplicationContext())){

						products = vendeurManager.selectAllProduct(compte);
						
						for (int i = 0; i < products.size(); i++) {
							for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
								//if(sv.getAllProduits(-1).get(j).getRef().equals(products.get(i).getId())){
								if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
									products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
								}
							}
						}
						
						if(products.size() > 0){
							myoffline.CleanProduits();
							myoffline.CleanPromotionProduit();
							myoffline.shynchronizeProduits(products);
							myoffline.shynchronizePromotion(vendeurManager.getPromotionProduits());	
						}
					}


					if(CheckOutNet.isNetworkConnected(getApplicationContext())){
						dico = vendeurManager.getDictionnaire();
						if(dico.getDico().size() > 0){
							myoffline.CleanDico();
							myoffline.shynchronizeDico(dico);	
						}
					}


					if(CheckOutNet.isNetworkConnected(getApplicationContext())){
						clients = vendeurManager.selectAllClient(compte);
						if(clients.size() > 0){
							myoffline.CleanClients();
							myoffline.CleanPromotionClient();
							myoffline.shynchronizeClients(clients);
							myoffline.shynchronizePromotionClient(vendeurManager.getPromotionClients());	
						}

					}


					if(CheckOutNet.isNetworkConnected(getApplicationContext())){
						CommercialManager manager = CommercialManagerFactory.getCommercialManager();
						myoffline.CleanProspectData();
						myoffline.shynchronizeProspect(manager.getInfos(compte));	
					}


					if(CheckOutNet.isNetworkConnected(getApplicationContext())){

						PayementManager payemn = PayementManagerFactory.getPayementFactory();
						myoffline.CleanPayement();
						myoffline.shynchronizePayement(payemn.getFactures(compte));	
					}
				}
				

				
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("erreu synchro",e.getMessage() +" << ");
			}
			
			return null;
		}

		protected void onPostExecute(String sResponse) {
			try {
				if (dialog2.isShowing()){
					dialog2.dismiss();
					
					dialog = ProgressDialog.show(CommercialActivity.this, getResources().getString(R.string.map_data),
							getResources().getString(R.string.msg_wait), true);
					new ConnexionTask().execute();
				}
				
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.fatal_error),
						Toast.LENGTH_LONG).show();
				Log.e("Error","");
			}
		}

	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*
			CommercialActivity.this.finish();
			Intent intent1 = new Intent(CommercialActivity.this, VendeurActivity.class);
			intent1.putExtra("user", compte);
			startActivity(intent1);
			*/
			
			onClickHome(LayoutInflater.from(CommercialActivity.this).inflate(R.layout.activity_commercial, null));
			
		}
		return false;
	}
	
	public List<String> checkRequiredFields(){
		
		// name;address;zip;tel;fax;email;
		List<String> res = new ArrayList<>();
		try {
			myoffline = new Offlineimpl(getApplicationContext());
			
			List<String> req = myoffline.LoadProspect("").getLsrequired();
			Log.e("req >> ",req.toString());
		
			//if(!req.contains("email"))req.add("email");
			if(!req.contains("name"))req.add("name");
			
	        for (int i = 0; i < req.size(); i++) {
	        	
	        	String st = req.get(i);
				if(st.equals("tel")){
						if(tel.getText().toString().length() == 0){
							res.add(getResources().getString(R.string.comerciallab7));
						}
				}
			
				if(st.equals("fax")){
					if(fax.getText().toString().length() == 0){
						res.add(getResources().getString(R.string.comerciallab8));
					}
				}

				if(st.equals("email")){
					if(client.getEmail().length() == 0 || "".equals(client.getEmail())){//if(email.getText().toString().length() == 0){
						res.add(getResources().getString(R.string.comerciallab9));
					}
				}
				
				/*
				if(st.equals("ville")){
					if(ville.getSelectedItemId() == 0){
						res.add("Ville du client");
					}
				}
				*/

				if(st.equals("address")){
					if(address.getText().toString().length() == 0){
						res.add(getResources().getString(R.string.comerciallab10));
					}
				}

				if(st.equals("zip")){
					if(zip.getText().toString().length() == 0){
						res.add(getResources().getString(R.string.comerciallab12));
					}
				}

				
				if(st.equals("name")){
					if(maVue.size() > 1){
						if(maVue.get(0).getText().toString().length() == 0 || maVue.get(1).getText().toString().length() ==0){
							res.add("Nom et prenom du client");
						}
					}else{
						if(maVue.get(0).getText().toString().length() == 0 ){
							res.add(getResources().getString(R.string.comerciallab11));
						}
					}
					
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("error data ",e.getMessage() +" << ");
		}

		
		return res;
		
	}
	
	
	
	public void alertinvonan(){
		
		List<String> req = checkRequiredFields();

		AlertDialog.Builder alert = new AlertDialog.Builder(CommercialActivity.this);
		alert.setTitle(R.string.caus21);
		
		String st ="";
		for (int i = 0; i < req.size(); i++) {
			st+= req.get(i)+"\n";
		}
		alert.setMessage(
				String.format(st
						));
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
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("user", compte);
		intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity (intent);
		this.finish();
	}
	
	public void onClickAbout (View v)
	{
		startActivity (new Intent(getApplicationContext(), AboutActivity.class));
		this.finish();
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

		imgView.setImageBitmap(bitmap);

		dialogcamera.show();
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
