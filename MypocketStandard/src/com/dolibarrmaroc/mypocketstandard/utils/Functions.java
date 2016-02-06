package com.dolibarrmaroc.mypocketstandard.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;

import android.util.Log;

import com.dolibarrmaroc.mypocketstandard.models.Tournee;

public class Functions {


	private static List<DateTime> getDatesBetween(final DateTime start, final DateTime end) {

		List<DateTime> dates = new ArrayList<DateTime>();

		DateTimeZone zone = DateTimeZone.forID("Africa/Casablanca");

		//DateTime start = new DateTime(2015, 9, 1, 0, 0, 0, 0);
		//DateTime end = new DateTime(2015, 9, 12, 0, 0, 0, 0);

		// period of 1 year and 7 days
		Period period = new Period(start, end);

		// calc will equal end
		DateTime calc = start.plus(period);

		// able to calculate whole days between two dates easily
		Days days = Days.daysBetween(start, end);



		int dx = days.getDays();
		for (int i=0; i <= dx; i++) {
			DateTime d = start.withFieldAdded(DurationFieldType.days(), i);
			dates.add(d);
		}



		for (int i = 0; i < dates.size(); i++) {
			System.out.println(dates.get(i).toString()+" >>> "+dates.get(i).getDayOfMonth()+ " ## "+dates.get(i).getDayOfWeek());
		}

		return dates;
	}

	public static HashMap<Integer, List<Tournee>> prepaTourneeData(List<Tournee> in){
		List<Tournee> l = new ArrayList<>();
		List<Tournee> m = new ArrayList<>();
		List<Tournee> mr = new ArrayList<>();
		List<Tournee> jd = new ArrayList<>();
		List<Tournee> v = new ArrayList<>();
		List<Tournee> s = new ArrayList<>();

		Date dt = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt2 = new Date();
		Date dt3 = new Date();
		
		
		for (int i = 0; i < in.size(); i++) {

			try {
				dt = sdf.parse(sdf.format(new Date()));
				dt2 = sdf.parse(in.get(i).getFin());
				dt3 = sdf.parse(in.get(i).getDebut());
				
				if(dt2.getTime() >= dt.getTime() && dt.getTime() >= dt3.getTime()){
					for (int j = 0; j < in.get(i).getRecur().size(); j++) {
						switch (in.get(i).getRecur().get(j)) {
						case 1:
							l.add(in.get(i));
							break;
						case 2:
							m.add(in.get(i));
							break;
						case 3:
							mr.add(in.get(i));
							break;
						case 4:
							jd.add(in.get(i));
							break;
						case 5:
							v.add(in.get(i));
							break;
						default:
							s.add(in.get(i));
							break;
						}
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		HashMap<Integer, List<Tournee>> res = new HashMap<>();
		res.put(1, l);
		res.put(2, m);
		res.put(3, mr);
		res.put(4, jd);
		res.put(5, v);
		res.put(6, s);

		Log.e("laod days ",res.toString());
		return res;

	}
	
	public static int getNumberOfDay(Date in){
		DateTime dt = new DateTime(in);
		DateTime.Property pDoW = dt.dayOfWeek();

		int n = 1;

		switch (pDoW.getAsText().toLowerCase()) {
		case "monday": 
		case "lundi":
			n = 1;
			break;

		case "tuesday ": 
		case "mardi":
			n = 2;
			break;
		case "wednesday": 
		case "mercredi":
			n = 3;
			break;
		case "thursday": 
		case "jeudi":
			n = 4;
			break;
		case "friday": 
		case "vendredi":
			n = 5;
			break;
		case "saturday": 
		case "samedi":
			n = 6;
			break;
		}
		
		Log.e(">daysss> ",n+"");
		return n;
	}
}
