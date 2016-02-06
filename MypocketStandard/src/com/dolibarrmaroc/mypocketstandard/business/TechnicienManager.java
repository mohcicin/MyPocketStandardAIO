package com.dolibarrmaroc.mypocketstandard.business;

import java.util.List;

import com.dolibarrmaroc.mypocketstandard.models.BordereauGps;
import com.dolibarrmaroc.mypocketstandard.models.BordreauIntervention;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.ImageTechnicien;
import com.dolibarrmaroc.mypocketstandard.models.Services;

public interface TechnicienManager {
	
	public String insertBordereau(BordreauIntervention bi,Compte c,GpsTracker gps,String imei,String num,String battery);
	public String insertBordereauoff(BordreauIntervention bi, Compte c);
	public List<BordereauGps> selectAllBordereau(Compte c);
	
	public List<Services> allServices(Compte c);
	public void inesrtImage(List<ImageTechnicien> imgs,String lien);

	public List<Client> selectAllClient(Compte c);
}
