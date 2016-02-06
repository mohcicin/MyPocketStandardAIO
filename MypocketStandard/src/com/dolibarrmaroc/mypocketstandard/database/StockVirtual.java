package com.dolibarrmaroc.mypocketstandard.database;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import com.dolibarrmaroc.mypocketstandard.models.DataSerial;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Serial1;
import com.dolibarrmaroc.mypocketstandard.utils.URL;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class StockVirtual extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "storagevirtual";

	//  tableS name
	private static final String TABLE_PROD = "storageprod";
	private static final String TABLE_SYNCRO = "storagesynchronisation";
	private static final String TABLE_SYNERROR = "storagesyncroerror";
	private static final String TABLE_HISTOOPS = "storageoperation";
	private static final String TABLE_LASTROW = "storagelastrow";
	private static final String TABLE_PRODQNT = "storageprodqnt";


	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_REF = "ref";
	private static final String KEY_QTE = "qte";
	private static final String KEY_TYPE = "typeprd";
	private static final String KEY_LIB = "libelle";
	private static final String KEY_CLT = "client";
	private static final String KEY_SYS = "sysout";

	private static final String KEY_DT = "dtcheck";
	private static final String KEY_ISIT = "ischeck";
	
	private static final String KEY_DTOP = "dtop";
	private static final String KEY_MTN = "montant";
	private static final String KEY_TPOP = "TYPEOP";

	public StockVirtual(Context context) {
		super(context,Environment.getExternalStorageDirectory()+URL.path+"/"+DATABASE_NAME, null, DATABASE_VERSION);
	}



	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String CREATE_factures_TABLE = "CREATE TABLE " + TABLE_PROD + "("
					+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_REF + " INTEGER , "+KEY_QTE+" INTEGER, "+KEY_TYPE+" VARCHAR(30), "+KEY_LIB+" VARCHAR(255), "+KEY_CLT+" VARCHAR(255), "+KEY_SYS+" INTEGER )";
			db.execSQL(CREATE_factures_TABLE);


			String cr1 = "CREATE TABLE " + TABLE_SYNCRO + "("
					+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DT + " VARCHAR(30), "+KEY_ISIT+" INTEGER )";
			db.execSQL(cr1);

		
			String cr2 = "CREATE TABLE " + TABLE_SYNERROR + "("
					+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DT + " VARCHAR(30), "+KEY_ISIT+" INTEGER )";
			db.execSQL(cr2);
			

			String cr3 = "CREATE TABLE " + TABLE_HISTOOPS + "("
					+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DTOP + " VARCHAR(30), "+KEY_MTN+" REAL, " + KEY_TPOP + " VARCHAR(30) )";
			db.execSQL(cr3);
			
			String cr = "CREATE TABLE " + TABLE_LASTROW + "("
					+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ISIT+" INTEGER, " + KEY_TPOP + " VARCHAR(30) )";
			db.execSQL(cr);
			
			String pq = "CREATE TABLE " + TABLE_PRODQNT + "("
					+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ISIT+" INTEGER, " + KEY_QTE + " INTEGER )";
			db.execSQL(pq);
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("erreur ","creation data table");
		}
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			// Drop older table if existed
			// Create tables again
			onCreate(db);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("update","upgrade base");
		}
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new 
	public long addrow(String tb,int pd,int qte,String tp,String lb,String clt) {
		long id =-1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();


			ContentValues values = new ContentValues();
			values.put(KEY_REF, pd);
			values.put(KEY_QTE, qte);
			values.put(KEY_TYPE, tp);
			values.put(KEY_LIB, lb);
			values.put(KEY_CLT, clt);
			values.put(KEY_SYS, 0);

			// Inserting Row
			id = db.insert(TABLE_PROD, null, values);

			db.close(); // Closing database connection


		} catch (Exception e) {
			// TODO: handle exception
			Log.e("insert data","insert data");
		}
		return id;
	}


	public long cleantables(String tb){
		long id =-1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+TABLE_PROD);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}
	
	public long cleantablesSysc(String tb){
		long id =-1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+TABLE_SYNCRO);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}
	
	public long cleantablesCA(String tb){
		long id =-1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+TABLE_HISTOOPS);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}

	public int allresults(String tb){
		int id =-1;
		try {
			String selectQuery = "SELECT  * FROM ";

			// selectQuery += TABLE_REGLEMENT;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			id = cursor.getCount();


		} catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}
	/*
    // Getting single 
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return 
        return contact;
    }

    // Getting All 
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding  to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return  list
        return contactList;
    }

    // Updating single 
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

	 */
	// Deleting single 
	public void deleteProduit(Produit contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROD, KEY_ID + " = ?",
				new String[] { String.valueOf((int)contact.getPrixttc()) });
		db.close();
	}


	public List<Produit> getAllProduits(int in) {
		List<Produit> contactList = new ArrayList<Produit>();
		// Select All Query
		try {
			String selectQuery = "";
			String[] nm = null;
			if(in == -1){
				selectQuery = "SELECT  * FROM " + TABLE_PROD;
			}else{
				selectQuery = "SELECT  * FROM " + TABLE_PROD + " WHERE "+KEY_TYPE +" = '"+in+"'";
				//nm = new String[] {in+""};
			}


			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Produit p = new Produit(""+cursor.getInt(1), cursor.getString(3), cursor.getInt(2), "", cursor.getInt(6), cursor.getInt(0), cursor.getString(5), cursor.getString(4));
					p.setId(cursor.getInt(1));
					contactList.add(p);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			contactList = new ArrayList<Produit>();
		}

		// return  list
		return contactList;
	}


	/*************************************  check update synchronisation ******************************/
	public long addrowcheckout() {
		long id =-1;
		try {

			Log.e("INSERT date ","begin");
			deleteChechout();

			SQLiteDatabase db = this.getWritableDatabase();


			ContentValues values = new ContentValues();
			
			Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("Africa/Casablanca"));
			int y       = cl.get(Calendar.YEAR);
			int m      = cl.get(Calendar.MONTH)+1; // Jan = 0, dec = 11
			int d = cl.get(Calendar.DAY_OF_MONTH); 
			
			Log.e("INSERT date ",y+""+m+""+d);
			
			values.put(KEY_DT, y+""+m+""+d);
			values.put(KEY_ISIT, 1);

			// Inserting Row
			id = db.insert(TABLE_SYNCRO, null, values);
			
			Log.e("INSERT ","ID <<"+id);

			db.close(); // Closing database connection


		} catch (Exception e) {
			// TODO: handle exception
			Log.e("insert data","insert data");
		}
		return id;
	}

	public void deleteChechout() {
		try {
			Log.e("DELETE ","GO DELETE");
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM "+TABLE_SYNCRO);
			db.close();
			Log.e("DELETE ","GO DELETE");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("error delete sysc ","sysc");
		}
	}

	public int getSyc() {
		// Select All Query
		String dt = "";
		try {
			String selectQuery = "";
			String[] nm = null;
			selectQuery = "SELECT  * FROM " + TABLE_SYNCRO;


			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);

			// looping through all rows and adding to list
			Log.e("hello >> ",cursor.getCount()+"");
			if(cursor.getCount() == 0){
				db.close();
				addrowcheckout();
				return 1;
			}else{
				if (cursor.moveToFirst()) {
					do {
						//Log.e(">>> sysc ",cursor.getString(1)+"  "+cursor.getInt(2));
						dt = cursor.getString(1);
						//break;
					} while (cursor.moveToNext());
				}
				
				Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("Africa/Casablanca"));
				int y       = cl.get(Calendar.YEAR);
				int m      = cl.get(Calendar.MONTH)+1; // Jan = 0, dec = 11
				int d = cl.get(Calendar.DAY_OF_MONTH); 
				
				long in = Long.parseLong(y+""+m+""+d);
				if(!"".equals(dt)){
					long out = Long.parseLong(dt);
					
					
					if(in > out){
						Log.e("sys Time ",in +" in##out "+out);
						db.close();
						addrowcheckout();
						return 1;
					}
				}
				
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("errror sysc get ",e.getMessage() +"");
			return -1;
		}

		return -1;
	}   
	
	/**************************  synchronisation of error data ***********************************/
	public long addrowcheckouterror() {
		long id =-1;
		try {

			deleteChechouterror();

			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			
			Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("Africa/Casablanca"));
			int y       = cl.get(Calendar.YEAR);
			int m      = cl.get(Calendar.MONTH)+1; // Jan = 0, dec = 11
			int d = cl.get(Calendar.DAY_OF_MONTH); 
			
			values.put(KEY_DT, y+""+m+""+d);
			values.put(KEY_ISIT, 1);

			// Inserting Row
			id = db.insert(TABLE_SYNERROR, null, values);
			
			db.close(); // Closing database connection


		} catch (Exception e) {
			// TODO: handle exception
			Log.e("insert data","insert data");
		}
		return id;
	}

	public void deleteChechouterror() {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM "+TABLE_SYNERROR);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("error delete sysc ","sysc");
		}
	}

	public int getSycerror() {
		// Select All Query
		String dt = "";
		int n = -1;
		try {
			String selectQuery = "";
			String[] nm = null;
			selectQuery = "SELECT * FROM " + TABLE_SYNERROR;


			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);

			if(cursor.getCount() == 0){
				db.close();
				addrowcheckouterror();
				return 1;
			}else{
				if (cursor.moveToFirst()) {
					do {
						dt = cursor.getString(1);
						n = cursor.getInt(2);
					} while (cursor.moveToNext());
				}
				
				Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("Africa/Casablanca"));
				int y       = cl.get(Calendar.YEAR);
				int m      = cl.get(Calendar.MONTH)+1; // Jan = 0, dec = 11
				int d = cl.get(Calendar.DAY_OF_MONTH); 
				
				cl.add(Calendar.DAY_OF_MONTH, 30);
				Log.e(">>> new date ",cl.get(Calendar.YEAR)+"/"+(cl.get(Calendar.MONTH)+1)+"/"+cl.get(Calendar.DAY_OF_MONTH));
				
				long in = Long.parseLong(y+""+m+""+d);
				
				Calendar calendar = new GregorianCalendar(2013,1,28,13,24,56);
				if(!"".equals(dt)){
					long out = Long.parseLong(dt);
					
					
					if(in > out && n==1){
						Log.e("sys Time ",in +" in##out "+out);
						db.close();
						addrowcheckout();
						return 1;
					}
				}
				
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("errror sysc get ",e.getMessage() +"");
			return -1;
		}

		return -1;
	}   
	
	 
	/*********************************** Creatae Operations data ******************************************************************/
		public long addOperation_virtual(String tp,double mtn,int i) {
			long id =-1;
			try {
				SQLiteDatabase db = this.getWritableDatabase();
				
				Calendar celebration = Calendar.getInstance();
			    celebration.setTime(new Date());
			    celebration.add(Calendar.DATE, i);

			    
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				ContentValues values = new ContentValues();
				values.put(KEY_DTOP, sdf.format(celebration.getTime()));
				values.put(KEY_MTN, mtn);
				values.put(KEY_TPOP, tp);
				

				// Inserting Row
				id = db.insert(TABLE_HISTOOPS, null, values);

				db.close(); // Closing database connection


			} catch (Exception e) {
				// TODO: handle exception
				Log.e("insert data","insert opeartion "+e.getMessage());
			}
			return id;
		}
	    
	    public long addOperation(String tp,double mtn) {
		long id =-1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			Log.e("in db ad motif ",tp+" "+mtn);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ContentValues values = new ContentValues();
			values.put(KEY_DTOP, sdf.format(new Date()));
			values.put(KEY_MTN, mtn);
			values.put(KEY_TPOP, tp);
			

			// Inserting Row
			id = db.insert(TABLE_HISTOOPS, null, values);

			db.close(); // Closing database connection


		} catch (Exception e) {
			// TODO: handle exception
			Log.e("insert data","insert opeartion");
		}
		return id;
	}
	
	public HashMap<String, Double> calculCA(){
		HashMap<String, Double> res = new HashMap<>();
		double fc =0;
		double py = 0;
		
		double nbrfc =0;
		double nbrpy =0;
		try {
			String selectQuery = "";
			selectQuery = "SELECT * FROM " + TABLE_HISTOOPS+" where "+KEY_TPOP+" = ?";

			String[] nm = new String[]{"FC"};

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);

			if (cursor.moveToFirst()) {
				do {
					fc += cursor.getDouble(2);
					nbrfc++;
				} while (cursor.moveToNext());
			}
			
			String[] nm2 = new String[]{"PY"};

			Cursor cursor2 = db.rawQuery(selectQuery, nm2);

			if (cursor2.moveToFirst()) {
				do {
					py += cursor2.getDouble(2);
					nbrpy++;
				} while (cursor2.moveToNext());
			}
			
			db.close();
			
			DecimalFormat df = new DecimalFormat(".##");

			
			res.put("FC", Double.parseDouble(df.format(fc).replaceAll(",", ".")));
			res.put("PY", Double.parseDouble(df.format(py).replaceAll(",", ".")));
			res.put("NFC", nbrfc);
			res.put("NPY", nbrpy);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("errror CA get ",e.getMessage() +"");
			res.put("FC", fc);
			res.put("PY", py);
			res.put("NFC", nbrfc);
			res.put("NPY", nbrpy);
		}

		return res;
	}
	
	public DataSerial calculCAGraph(String in,String tp){
		List<String> day = new ArrayList<>();
		List<Double> val = new ArrayList<>();
		try {
			  
			
			String selectQuery = "";
			selectQuery = "SELECT strftime('%d', "+KEY_DTOP+"), SUM("+KEY_MTN+") FROM " + TABLE_HISTOOPS+" where strftime('%m', "+KEY_DTOP+") = ? AND "+KEY_TPOP+" = ?"+" AND strftime('%Y', "+KEY_DTOP+") = ?  GROUP BY strftime('%d', "+KEY_DTOP+") ORDER BY strftime('%d', "+KEY_DTOP+") ASC"; 
			//selectQuery = "SELECT * FROM " + TABLE_HISTOOPS;


			Calendar celebration = Calendar.getInstance();
		    celebration.setTime(new Date());
		    
		    Log.e("input data ",in+" >>> "+tp+" >> "+celebration.get(Calendar.YEAR)+"");
		    
			String[] nm = new String[]{in,tp,celebration.get(Calendar.YEAR)+""};
			//String[] nm = new String[]{};

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);
			
			DecimalFormat df = new DecimalFormat(".##");

			if (cursor.moveToFirst()) {
				do {
					//Log.e(">>> graph ",cursor.getString(1)+ " >> ");
					day.add(cursor.getString(0));
					val.add(Double.parseDouble(df.format(cursor.getDouble(1)).replaceAll(",", ".")));
				} while (cursor.moveToNext());
			}
			
		 
			db.close();
			
			 
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("errror CA get ",e.getMessage() +"");
		}

		List<Serial1> s1 = new ArrayList<>();
		Serial1 s = new Serial1();
		s.setData(val);
		
		s1.add(s);
		
		DataSerial data = new DataSerial(s1, day);
		return data;
	}
	
	public List<Integer> calculCAGraphMotifs(String in){
		List<Integer> val = new ArrayList<>();
		try {
			  
			HashMap<Double, Integer> data = new HashMap<>();
			String selectQuery = "";
			selectQuery = "SELECT "+KEY_MTN+", COUNT("+KEY_MTN+") FROM " + TABLE_HISTOOPS+" where "+KEY_TPOP+" = ?"+" GROUP BY "+KEY_MTN+" ORDER BY "+KEY_MTN+" ASC"; 
			//selectQuery = "SELECT * FROM " + TABLE_HISTOOPS;


			Calendar celebration = Calendar.getInstance();
		    celebration.setTime(new Date());
		    
		    
			String[] nm = new String[]{in};
			//String[] nm = new String[]{};

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);
			

			if (cursor.moveToFirst()) {
				do {
					Log.e(">> "+cursor.getDouble(0), ">> "+ cursor.getInt(1));
					//val.add(cursor.getInt(1));
					data.put(cursor.getDouble(0), cursor.getInt(1));
				} while (cursor.moveToNext());
			}
			
			
			if(data.size() > 0){
				for (int i = 1; i < 8; i++) {
					if(!data.containsKey(new Double(i))){
						val.add(0);
					}else{
						val.add(data.get(new Double(i)));
					}
				}
			}else{
				for (int i = 0; i < 7; i++) {
					val.add(0);
				}
			}
		 
			db.close();
			
			 
			Log.e("val motifs ",val.toString() +"");
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("errror CA get ",e.getMessage() +"");
		}

		
		return val;
	}
	
	/*************************************  Last row insert in offline ******************************/
	public long addlastRow(int in,String tp) {
		long id =-1;
		try {

			Log.e("INSERT date ","begin");
			deleteLastRow(tp);

			SQLiteDatabase db = this.getWritableDatabase();


			ContentValues values = new ContentValues();
			
			 
			
			values.put(KEY_ISIT, in);
			values.put(KEY_TPOP, tp);

			// Inserting Row
			id = db.insert(TABLE_LASTROW, null, values);
			
			Log.e("INSERT ","ID <<"+id);

			db.close(); // Closing database connection


		} catch (Exception e) {
			// TODO: handle exception
			Log.e("insert data","insert data");
		}
		return id;
	}

	public void	deleteLastRow(String tp) {
		try {
			//KEY_ISIT+" INTEGER, " + KEY_TPOP
			Log.e("DELETE ","DELETE FROM "+TABLE_LASTROW+" WHERE "+KEY_TPOP+" = "+tp);
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM "+TABLE_LASTROW+" WHERE "+KEY_TPOP+" = '"+tp+"'");
			db.close();
			Log.e("DELETE ","GO DELETE");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("error delete sysc ","sysc");
		}
	}
	
	public int getLastRow(String in) {
		// Select All Query
		int n = -1;
		try {
			String selectQuery = "";
			String[] nm = new String[]{in};
			selectQuery = "SELECT * FROM " + TABLE_LASTROW +" where "+KEY_TPOP+" = ?";


			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);

			if(cursor.getCount() > 0){
				if (cursor.moveToFirst()) {
					do {
						n = cursor.getInt(1);
					} while (cursor.moveToNext());
				}
			} 
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("errror sysc get ",e.getMessage() +"");
			n = -1;
			return n;
		}

		return n;
	}   
	
	public long cleantablesLR(String tb){
		long id =-1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+TABLE_LASTROW);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}
	
	/*************************************  storage qte poducts in offline *****************EY_ISIT+" INTEGER, " + KEY_QTE*************/
	public long addPdQtRow(int in,int qt) {
		long id =-1;
		try {

		 
			SQLiteDatabase db = this.getWritableDatabase();


			ContentValues values = new ContentValues();
			
			 
			
			values.put(KEY_ISIT, in);
			values.put(KEY_QTE, qt);

			// Inserting Row
			id = db.insert(TABLE_PRODQNT, null, values);
			

			db.close(); // Closing database connection


		} catch (Exception e) {
			// TODO: handle exception
			Log.e("insert data qnt prod","insert data");
		}
		return id;
	}

	public void	deletePdQt(String tp) {
		try {
			//KEY_ISIT+" INTEGER, " + KEY_TPOP
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM "+TABLE_PRODQNT);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("error delete all qte prod ","sysc");
		}
	}
	
	public int getPdQt(String in) {
		// Select All Query
		int n = 0;
		try {
			String selectQuery = "";
			String[] nm = new String[]{in};
			selectQuery = "SELECT * FROM " + TABLE_PRODQNT +" where "+KEY_ISIT+" = ?";


			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);

			if(cursor.getCount() > 0){
				if (cursor.moveToFirst()) {
					do {
						n += cursor.getInt(2);
					} while (cursor.moveToNext());
				}
			} 
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("errror get pd qte get ",e.getMessage() +"");
			n = 0;
			return n;
		}

		return n;
	}   
	 
	public List<Produit> getAllProduitsVentes(int in) {
		List<Produit> contactList = new ArrayList<Produit>();
		// Select All Query
		try {
			String selectQuery = "";
			String[] nm = null;
			selectQuery = "SELECT  * FROM " + TABLE_PRODQNT;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, nm);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					//EY_ISIT+" INTEGER, " + KEY_QTE
											//String ref, String desig, int qteDispo, String prixUnitaire,int qtedemander, double prixttc, String tva_tx, String fk_tva
					Produit p = new Produit("", "", cursor.getInt(2), "", cursor.getInt(2), 0.0, "", "");
					p.setId(cursor.getInt(1));
					contactList.add(p);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			contactList = new ArrayList<Produit>();
		}

		// return  list
		return contactList;
	}
	

}
