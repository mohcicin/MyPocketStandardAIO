package com.dolibarrmaroc.mypocketstandard.offline;

import java.util.HashMap;
import java.util.List;

import android.view.animation.BounceInterpolator;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.dolibarrmaroc.mypocketstandard.database.DataErreur;
import com.dolibarrmaroc.mypocketstandard.models.BordereauGps;
import com.dolibarrmaroc.mypocketstandard.models.BordreauIntervention;
import com.dolibarrmaroc.mypocketstandard.models.Categorie;
import com.dolibarrmaroc.mypocketstandard.models.CategorieCustomer;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Commande;
import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Dictionnaire;
import com.dolibarrmaroc.mypocketstandard.models.FileData;
import com.dolibarrmaroc.mypocketstandard.models.GpsTracker;
import com.dolibarrmaroc.mypocketstandard.models.Motifs;
import com.dolibarrmaroc.mypocketstandard.models.Mouvement;
import com.dolibarrmaroc.mypocketstandard.models.MouvementGrabage;
import com.dolibarrmaroc.mypocketstandard.models.MyGpsInvoice;
import com.dolibarrmaroc.mypocketstandard.models.MyProdRemise;
import com.dolibarrmaroc.mypocketstandard.models.MyTicketBluetooth;
import com.dolibarrmaroc.mypocketstandard.models.MyTicketPayement;
import com.dolibarrmaroc.mypocketstandard.models.MyTicketWitouhtProduct;
import com.dolibarrmaroc.mypocketstandard.models.Myinvoice;
import com.dolibarrmaroc.mypocketstandard.models.Payement;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Promotion;
import com.dolibarrmaroc.mypocketstandard.models.ProspectData;
import com.dolibarrmaroc.mypocketstandard.models.Prospection;
import com.dolibarrmaroc.mypocketstandard.models.Reglement;
import com.dolibarrmaroc.mypocketstandard.models.Services;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.models.TotauxTicket;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.utils.GpsTrackingServiceDao;
import com.dolibarrmaroc.mypocketstandard.utils.ServiceDao;

public interface ioffline {

	/* Step synchronisation of data in offline */
	public long shynchronizeCompte(Compte clt);
	
	public long shynchronizeClients(List<Client> clt);
	public long shynchronizeProduits(List<Produit> clt);
	public long shynchronizePromotion(HashMap<Integer, HashMap<Integer, Promotion>> clt);
	public long shynchronizePromotionClient(HashMap<Integer, List<Integer>> clt);
	public long shynchronizeDico(Dictionnaire clt);
	public long shynchronizeService(List<Services> clt);
	public long shynchronizeProspect(ProspectData pd);
	public long shynchronizePayement(List<Payement> ps);
	public long shynchronizeSociete(MyTicketWitouhtProduct me);
	 
	public Myinvoice shynchronizeInvoice(String invoice, List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement, String amount,
			String numChek, int typeImpriment, List<MyProdRemise> remises,
			GpsTracker gps, String imei, String num, String battery,TotauxTicket tt,int type_invoice);
	public long shynchronizeReglement(Reglement reg);
	public long shynchronizeBluetooth(MyTicketBluetooth bpd);
	public String shynchronizeProspection(Prospection ps,Compte cp);
	public long shynchronizeProspection_out(Prospection ps,Compte cp);
	public long shynchronizeGpsTracker(GpsTrackingServiceDao gsp);
	public long shynchronizePayemntTicket(MyTicketPayement tp);
	public long shynchronizeGpsInvoice(MyGpsInvoice ds);
	public long shynchronizeIntervention(BordreauIntervention bi);
	public long historiqueIntervention(BordreauIntervention bi);
	public long shynchronizeCommandeList(List<Commandeview> cmd);
	public long shynchronizeCategoriesList(List<Categorie> cat);
	public long shynchornizeCmd(Commande cm); 
	public long shynchornizeCmdToFact(Commandeview cm); 
	public long shnchronizeMouvement(MouvementGrabage mv,Compte cp);
	public long shnchronizeCategorieClients(CategorieCustomer ct,Compte cp);
	public long shnchronizeSocietesClients(List<Societe> ct,Compte cp);
	public long shnchronizeUpClients(Prospection ct,Compte cp);
	public long PutDeniededData(Object in,int cl);
	public long PutDeniededDataFw(String in,int cl);
	public long PutDeniededInterv(BordreauIntervention c,String in);
	public long shynchornizeUpdateCmd(Commandeview cm); 
	public long shynchornizeTournee(List<Tournee> in); 
	public long shynchronizeClsCmd(Commandeview cv,Compte cp);
	public long shynchronizeMotifs(Motifs cv,Compte cp);

	
	/* Load data from offline */
	public List<Client> LoadClients(String fl);
	public List<Produit> LoadProduits(String fl);
	public Dictionnaire LoadDeco(String fl);
	public Services LoadServices(String fl);
	public MyTicketWitouhtProduct LoadSociete(String fl);
	public HashMap<Integer, HashMap<Integer, Promotion>> LoadPromotion(String fl);
	public HashMap<Integer, List<Integer>> LoadPromoClient(String ref);
	public List<Myinvoice> LoadInvoice(String fl);
	public ProspectData LoadProspect(String fl);
	public List<Prospection> LoadProspection(String fl);
	public List<Payement> LoadPayements(String fl);
	public List<Reglement> LoadReglement(String fl);
	public List<MyTicketBluetooth> LoadBluetooth(String fl);
	public List<GpsTrackingServiceDao> LoadGpsTracker(String fl);
	public List<MyTicketPayement> LoadTicketPayement(String fl);
	public List<MyGpsInvoice> LoadGpsInvoice(String fl);
	public List<BordreauIntervention> LoadInterventions(String fl);
	public List<BordreauIntervention> LoadHistoInterventions(String fl);
	public List<Commandeview> LoadCommandeList(String fl);
	public List<Categorie> LoadCategorieList(String fl);
	public List<Commande> LoadCmdList(String fl);
	public List<Commandeview> LoadCmdToFact(String fl);
	public List<MouvementGrabage> LoadMouvement(String fl);
	public List<CategorieCustomer> LoadCategorieClients(String fl);
	public List<Societe> LoadSocietesClients(String fl);
	public List<Prospection> LoadUpClients(String fl);
	public List<String> LoadDenided(String fl);
	public List<Commandeview> LoadUpdateCmdList(String fl);
	public List<Tournee> LoadTourneeList(String fl);
	public List<Commandeview> LoadClsCmdList(String fl);
	public List<Motifs> LoadMotifsList(String fl);
	public List<String> LoadDeniededInterv();
	
	public Compte LoadCompte(String log,String pwd);
	
	
	/*Clean offline data */
	public void CleanClients();
	public void CleanProduits();
	public void CleanDico();
	public void CleanCompte();
	public void CleanService();
	public void CleanPromotionProduit();
	public void CleanPromotionClient();
	public void CleanInvoice();
	public void CleanProspectData();
	public void CleanSociete();
	public void CleanProspection();
	public void CleanPayement();
	public void CleanReglement();
	public void CleanBluetooth();
	public void CleanGpsTracker();
	public void CleanPayementTicket();
	public void CleanGpsInvoice();
	public void CleanIntervention();
	public void CleanHistoIntervention();
	public void CleanCommandeList();
	public void CleanCategorieList();
	public void CleanCmdList();  //clean prise de commande
	public void CleanCmdToFactList();
	public void CleanMouvement();
	public void CleanCategorieClients();
	public void CleanSocieteClients();
	public void CleanUpClients();
	public void CleanAllDeniededData();
	public void CleanUpdateCmd();
	public void CleanTournee();
	public void CleanClsCmd();
	public void CleanMotif();
	public void CleanDeniededInterv();
	
	public List<Promotion> getPromotions(int idclt, int idprd);
	public Client seeClient(List<Client> ls,String id);
	
	public void updateProduits(Myinvoice me);
	public void updateProduitsInv(Myinvoice me);
	public Produit showProduct(List<Produit> pd,int id);
	
	public long checkRefClient(String ref,String eml);
	public List<Client> prepaOfflineClient(List<Prospection> ps);
	public List<Payement> prepaOfflinePayement(List<Myinvoice> ms);
	public MyTicketBluetooth checkMyFactureticket(int rf);
	public MyTicketPayement checkMyReglementicket(int ref);
	public List<Reglement> showRegInvo(int id);
	public List<Reglement> showServerside(int id);
	public long checkClient_is_Prospect(int ref);
	public long checkPayement_is_Invoice(int ref);
	public MyGpsInvoice checkGpsInvoice(String fl);
	public Myinvoice prepaValideIvoice(String invoice, List<Produit> prd, String idclt, int nmb,
			String commentaire, Compte compte, String reglement, String amount,
			String numChek, int typeImpriment, List<MyProdRemise> remises,
			GpsTracker gps, String imei, String num, String battery,TotauxTicket tt);
	public long check_insert_invoice(String in);
	public long check_insert_reglement(String in);
	
	
	public long saveInvoice(Myinvoice m);
	
	//old version
	public HashMap<Myinvoice, Prospection> chargerInvoiceprospect(Compte cp);
	public HashMap<Myinvoice, String> chargerInvoiceclient();
	public List<Reglement> chergerReglementClt(Compte cp);
	
	
	
	
	
	/*synchronisation en ligne */
	public List<Prospection> synchronisationClientsEnligne(Compte cp);
	public List<Myinvoice> synchronisationFactureEnligne(Compte cp);
	public List<Reglement> synchronisationReglementEnligne(Reglement reg,Compte c);
	public void synchronisationPositionsGPS();
	public List<MyGpsInvoice> synchronisationGPSInvoiceEnligne(Compte cp);
	public long cleancache();
	public long checkAvailableofflinestorage();
	public long checkAvailableofflinestorage2();
	
	
	//old version
	public DataErreur synchronisationInvoiceOut(Compte cp);
	
	
	
	//new server === side
	public DataErreur synchronisation_Invoice_Out(HashMap<Prospection, List<Myinvoice>> in,Compte cp,HashMap<Prospection, List<Commande>> cmd_ps);
	public Commande SendCommande(Commande cmd,Compte cp);
	public DataErreur synchronisationPayementOut(HashMap<Myinvoice, String> in,HashMap<Commande,String> cmd_cl);
	public List<Reglement> synchronisationReglementOut(Compte cp);
	public List<Commandeview> synchronisationCmdToFacOut(Compte cp);
	
	//charger facture offline == line
	public HashMap<String, HashMap<Prospection, List<Myinvoice>>> chargerInvoice_prospect(Compte cp);
	public HashMap<Myinvoice,String> chargerInvoice_client(HashMap<String, HashMap<Prospection, List<Myinvoice>>> da,Compte cp);
	
	// load commande offline==line
	public HashMap<String, HashMap<Prospection, List<Commande>>> chargerCmd_prospect(Compte cp);
	public HashMap<Commande,String> chargerCmd_client(HashMap<String, HashMap<Prospection, List<Commande>>> da,Compte cp);
	
	// send mouvement (factures echanges )
	public long sendMouvements(Compte cp);
	
	//send update commande 
	public List<Commandeview> sendUpdateCmd(Compte cp);
	
	//send updated clients
	public long sendUpClients(Compte cp);
	
	//send canceled commandes
	public long sendClsCmd(Compte cp);
	
	public long CleanAlldataOut(DataErreur data,Compte cp);
	public long SendOutData(Compte cp);
	
	
	//Intervention offline
	public long sendOutIntervention(Compte cp);
	
	
	// check for avilable espace to storage
	public boolean checkFilexsiste();
	public boolean checkFolderexsiste();
	
	public boolean TotalMemory();
	public boolean FreeMemory();
	
	public long checkForUpdate();
	public long cleanForUpdate();
	
	
	//send motifs bo
	public long sendOutMotifs(Compte cp);
	
	/************************* Tools Optmization Sysc **************************************/
	public int updatePayData(long id,double pay); 
	public void cancelCmd(Commandeview id,Compte cp);
	/************************* Tools Optmization Sysc **************************************/
	
	
	
}
