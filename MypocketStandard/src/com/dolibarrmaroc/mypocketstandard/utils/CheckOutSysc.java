package com.dolibarrmaroc.mypocketstandard.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.dolibarrmaroc.mypocketstandard.business.AuthentificationManager;
import com.dolibarrmaroc.mypocketstandard.business.CommandeManager;
import com.dolibarrmaroc.mypocketstandard.business.CommercialManager;
import com.dolibarrmaroc.mypocketstandard.business.MouvementManager;
import com.dolibarrmaroc.mypocketstandard.business.PayementManager;
import com.dolibarrmaroc.mypocketstandard.business.TechnicienManager;
import com.dolibarrmaroc.mypocketstandard.business.VendeurManager;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDao;
import com.dolibarrmaroc.mypocketstandard.dao.CategorieDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dao.TourneeDao;
import com.dolibarrmaroc.mypocketstandard.dao.TourneeDaoMysql;
import com.dolibarrmaroc.mypocketstandard.dashboard.HomeActivity;
import com.dolibarrmaroc.mypocketstandard.database.StockVirtual;
import com.dolibarrmaroc.mypocketstandard.models.Categorie;
import com.dolibarrmaroc.mypocketstandard.models.CategorieCustomer;
import com.dolibarrmaroc.mypocketstandard.models.Client;
import com.dolibarrmaroc.mypocketstandard.models.Commandeview;
import com.dolibarrmaroc.mypocketstandard.models.Compte;
import com.dolibarrmaroc.mypocketstandard.models.Dictionnaire;
import com.dolibarrmaroc.mypocketstandard.models.LoadStock;
import com.dolibarrmaroc.mypocketstandard.models.Payement;
import com.dolibarrmaroc.mypocketstandard.models.Produit;
import com.dolibarrmaroc.mypocketstandard.models.Promotion;
import com.dolibarrmaroc.mypocketstandard.models.ProspectData;
import com.dolibarrmaroc.mypocketstandard.models.Services;
import com.dolibarrmaroc.mypocketstandard.models.Societe;
import com.dolibarrmaroc.mypocketstandard.models.Tournee;
import com.dolibarrmaroc.mypocketstandard.offline.ioffline;

import android.R.integer;
import android.content.Context;
import android.util.Log;

public class CheckOutSysc implements Serializable{


	/**************************  get Data from Dolibarr ****************************************/
	public static List<Produit> checkOutProducts(VendeurManager vendeurManager,Compte c){
		return vendeurManager.selectAllProduct(c);
	}

	public static Dictionnaire checkOutDictionnaire(VendeurManager vendeurManager,Compte c){
		return vendeurManager.getDictionnaire();
	}

	public static List<Client> checkOutClient(VendeurManager vendeurManager,Compte c,ioffline myoffline){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String dt ="";
		try {
			dt=  sdf.format(new Date());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(c.getIstour() == 1){//URL.is_tour){
			return  LoadClients(vendeurManager.consulterMesTournee(c,dt),myoffline);
		}else{
			return vendeurManager.selectAllClient(c);
		}
	}
	
	public static List<Client> LoadClients(List<Tournee> trs,ioffline myoffline){
		
		 myoffline.shynchornizeTournee(trs);

		List<Client> clients = new ArrayList<>();
		
		HashMap<Integer, Client> srt = new HashMap<>();
		for (int i = 0; i < trs.size(); i++) {
			clients.addAll(trs.get(i).getLsclt());
		}
		
		for (int i = 0; i < clients.size(); i++) {
			srt.put(clients.get(i).getId(), clients.get(i));
		}
		
		 clients = new ArrayList<>();
		 
		 for (Integer i:srt.keySet()) {
			clients.add(srt.get(i));
		}
		
		return clients;
	}

	public static List<Client> checkOutLastClient(VendeurManager vendeurManager,Compte c,StockVirtual sv){

		int mx = sv.getLastRow("addclt");
		if(mx != -1){
			return vendeurManager.selectAllLastClient(c, mx+"");
		}
		return  vendeurManager.selectAllClient(c);
	}

	public static HashMap<Integer, HashMap<Integer, Promotion>> checkOutPromotionProduits(VendeurManager vendeurManager,Compte c){
		return  vendeurManager.getPromotionProduits();
	}

	public static HashMap<Integer, List<Integer>> checkOutPromotionClients(VendeurManager vendeurManager,Compte c){
		return vendeurManager.getPromotionClients();
	}

	public static ProspectData checkOutCommercialInfo(CommercialManager comercialmanager,Compte c){
		return comercialmanager.getInfos(c);
	}

	public static List<Payement> checkOutPayement(PayementManager payemnmanager,Compte c){
		return payemnmanager.getFactures(c);
	}

	public static List<Payement> checkOutLastPayement(PayementManager payemnmanager,Compte c,StockVirtual sv){
		int mx = sv.getLastRow("pay");
		if(mx != -1){
			return payemnmanager.getLastFactures(c, mx+"");
		}
		return payemnmanager.getFactures(c);
	}

	public static List<CategorieCustomer> checkOutClientSecteur(VendeurManager vendeurManager,Compte c){
		return  vendeurManager.getAllCategorieCustomer(c);
	}

	public static List<Commandeview> checkOutCommandes(CommandeManager comamndemanager,Compte c){
		return comamndemanager.charger_commandes(c);
	}
	
	public static List<Commandeview> checkOutCommandesLast(CommandeManager comamndemanager,Compte c,StockVirtual sv){
		int max = sv.getLastRow("cmd");
		
		if(max != -1){
			return comamndemanager.charger_commandesLast(c, max+"");
		}else{
			return comamndemanager.charger_commandes(c);
		}
		
	}

	public static List<Categorie> checkOutCatalogueProduit(CategorieDao categorie,Compte c){
		return categorie.LoadCategories(c);
	}

	public static List<Societe> checkOutAllSociete(CommercialManager comercialmanager,Compte c){
		return comercialmanager.getAll(c);
	}
	
	public static List<Societe> checkOutAllSocieteNew(VendeurManager vendeurManager,Compte c){
		//vendeurManager.selectAllClient(c);
		return vendeurManager.selectSociete();
	}

	public static LoadStock checkOutStock(MouvementManager stockManager,Compte c){
		return stockManager.laodStock(c);
	}
	
	public static List<Services> checkOutServices(AuthentificationManager aut,Compte c){
		List<Services> ls = new ArrayList<>();
		Services s = new Services();
		s = aut.getService(c.getLogin(), c.getPassword(), c.getId_service());
		
		ls.add(s);
		
		return ls;
	}

	/**************************  Set Data into cache mobile ****************************************/
	public static long checkInProductsPromotion(ioffline myoffline,Compte c,List<Produit> prod,HashMap<Integer, HashMap<Integer, Promotion>> promo){
		long sysnbr = 0;
		myoffline.CleanProduits();
		myoffline.CleanPromotionProduit();
		sysnbr += myoffline.shynchronizeProduits(prod);
		sysnbr += myoffline.shynchronizePromotion(promo);

		return sysnbr;
	}

	public static long checkInClientsPromotion(ioffline myoffline,Compte c,List<Client> clts,HashMap<Integer, List<Integer>> clt){
		long sysnbr = 0;
		myoffline.CleanClients();
		myoffline.CleanPromotionClient();
		sysnbr += myoffline.shynchronizeClients(clts);
		sysnbr += myoffline.shynchronizePromotionClient(clt);

		return sysnbr;
	}

	public static long checkInDictionnaire(ioffline myoffline,Dictionnaire dico){
		myoffline.CleanDico();
		return myoffline.shynchronizeDico(dico);
	}

	public static long checkInCommercialInfo(ioffline myoffline,ProspectData data,Compte c){
		myoffline.CleanProspectData();
		return myoffline.shynchronizeProspect(data);
	}

	public static long checkInPayement(ioffline myoffline,List<Payement>  data,Compte c){
		if(data.size() > 0){
			myoffline.CleanPayement();
			return myoffline.shynchronizePayement(data);
		}
		return 0;
	}

	public static long checkInClientSecteur(ioffline myoffline,List<CategorieCustomer> lscat,Compte c){
		if(lscat.size() > 0){
			myoffline.CleanCategorieClients();
			Log.e("secto in sysc  "," "+lscat.size());
			for (int i = 0; i < lscat.size(); i++) {
				myoffline.shnchronizeCategorieClients(lscat.get(i), c);
			}
		}
		return 0;
	}

	public static long checkInCommandeview(ioffline myoffline,List<Commandeview>  data,Compte c){
		if(data.size() > 0){
			myoffline.shynchronizeCommandeList(data);
		}
		return 0;
	}

	public static long checkInCatalogueProduit(ioffline myoffline,List<Categorie> lscats,Compte c){
		if(lscats.size() > 0){
			myoffline.shynchronizeCategoriesList(lscats);
		}
		return 0;
	}

	public static long checkInSocietes(ioffline myoffline,List<Societe> lscats,Compte c){
		if(lscats.size() > 0){
			myoffline.shnchronizeSocietesClients(lscats, c);
		}
		return 0;
	}
	
	public static long checkInServices(ioffline myoffline,List<Services> lscats,Compte c){
		if(lscats.size() > 0){
			myoffline.CleanService();
			myoffline.shynchronizeService(lscats);
		}
		return 0;
	}


	/******************************  Reload data **************************************************************/
	/*
	 * in : 0 == all // 1 == without payement // 2 == only products  // 3 == only clients  // 4 == catalogue // 5 == payement
	 */
	public static HashMap<String, Integer> ReloadProdClt(Context context,ioffline myoffline,Compte compte,VendeurManager vendeurManager,PayementManager payemnmanager,StockVirtual sv,CategorieDao categorie,CommandeManager managercmd,int in,CommercialManager manager){

		int nbprod =0,nbclt =0,nbpay =0;
		HashMap<String, Integer> res = new HashMap<>();

		List<Client> clients = new ArrayList<>();
		List<Produit> products = new ArrayList<>();
		List<Payement> paym = new ArrayList<>();
		List<Societe> societes = new ArrayList<>();
		try {

			switch (in) {
			case 0:
				products = new ArrayList<>();
				products =  checkOutProducts(vendeurManager, compte);//   vendeurManager.selectAllProduct(compte);

				clients = new ArrayList<>();
				clients = checkOutClient(vendeurManager, compte,myoffline); //   vendeurManager.selectAllClient(compte);
				

				societes = new ArrayList<>();
				societes = checkOutAllSocieteNew(vendeurManager, compte);
				if(societes.size() > 0){
					checkInSocietes(myoffline, societes, compte);
				}

				
				if(products.size() > 0){
					nbprod = products.size();
					for (int i = 0; i < products.size(); i++) {
						for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
							//if(sv.getAllProduits(-1).get(j).getRef().equals(products.get(i).getId()+"")){
							if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
								products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
							}
						}
					}
					checkInProductsPromotion(myoffline, compte, products, vendeurManager.getPromotionProduits());
				} 



				if(clients.size() > 0){
					nbclt =clients.size(); 
					checkInClientsPromotion(myoffline, compte, clients, vendeurManager.getPromotionClients());
				}


				if(compte.getPermissionbl() == 1){

					List<Categorie> lscats = checkOutCatalogueProduit(categorie, compte);

					for (int i = 0; i < products.size(); i++) {
						for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
							//Log.e(products.get(i).getId()+"",sv.getAllProduits().get(j).getRef());
							if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
								products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
							}
						}
					}
					

					for (int j = 0; j < lscats.size(); j++) {
						for (int i = 0; i < lscats.get(j).getProducts().size(); i++) {
							for (int k = 0; k < products.size(); k++) {
								if(lscats.get(j).getProducts().get(i).getId() == products.get(k).getId()){
									lscats.get(j).getProducts().get(i).setQteDispo(products.get(k).getQteDispo());
								}
							}
						}
					}

					if(lscats.size() > 0){
						checkInCatalogueProduit(myoffline, lscats, compte);
					}

					checkInCommandeview(myoffline, checkOutCommandes(managercmd, compte), compte);

				}


				checkInDictionnaire(myoffline, checkOutDictionnaire(vendeurManager, compte));


				paym = new ArrayList<>();
				paym = checkOutPayement(payemnmanager, compte);

				checkInPayement(myoffline, paym, compte);
				nbpay = paym.size();

				break;

			case 1:
				products = new ArrayList<>();
				products =  checkOutProducts(vendeurManager, compte);//   vendeurManager.selectAllProduct(compte);

				clients = new ArrayList<>();
				clients = checkOutClient(vendeurManager, compte,myoffline); //   vendeurManager.selectAllClient(compte);


				societes = new ArrayList<>();
				societes = checkOutAllSocieteNew(vendeurManager, compte);
				if(societes.size() > 0){
					checkInSocietes(myoffline, societes, compte);
				}
				
				if(products.size() > 0){
					nbprod = products.size();
					for (int i = 0; i < products.size(); i++) {
						for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
							//if(sv.getAllProduits(-1).get(j).getRef().equals(products.get(i).getId()+"")){
							if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
								products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
							}
						}
					}
					checkInProductsPromotion(myoffline, compte, products, vendeurManager.getPromotionProduits());
				} 



				if(clients.size() > 0){
					nbclt =clients.size(); 
					checkInClientsPromotion(myoffline, compte, clients, vendeurManager.getPromotionClients());
				}


				if(compte.getPermissionbl() == 1){

					List<Categorie> lscats = checkOutCatalogueProduit(categorie, compte);

					for (int i = 0; i < products.size(); i++) {
						for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
							//Log.e(products.get(i).getId()+"",sv.getAllProduits().get(j).getRef());
							if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
								products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
							}
						}
					}

					for (int j = 0; j < lscats.size(); j++) {
						for (int i = 0; i < lscats.get(j).getProducts().size(); i++) {
							for (int k = 0; k < products.size(); k++) {
								if(lscats.get(j).getProducts().get(i).getId() == products.get(k).getId()){
									lscats.get(j).getProducts().get(i).setQteDispo(products.get(k).getQteDispo());
								}
							}
						}
					}

					if(lscats.size() > 0){
						checkInCatalogueProduit(myoffline, lscats, compte);
					}

					checkInCommandeview(myoffline, checkOutCommandes(managercmd, compte), compte);

				}


				checkInDictionnaire(myoffline, checkOutDictionnaire(vendeurManager, compte));

				break;

			case 2:
				products = new ArrayList<>();
				products =  checkOutProducts(vendeurManager, compte);//   vendeurManager.selectAllProduct(compte);


				if(products.size() > 0){
					nbprod = products.size();
					for (int i = 0; i < products.size(); i++) {
						for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
							//if(sv.getAllProduits(-1).get(j).getRef().equals(products.get(i).getId()+"")){
							if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
								products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
							}
						}
					}
					checkInProductsPromotion(myoffline, compte, products, vendeurManager.getPromotionProduits());
				} 
				checkInDictionnaire(myoffline, checkOutDictionnaire(vendeurManager, compte));
				break;

			case 3:
				clients = new ArrayList<>();
				clients = checkOutClient(vendeurManager, compte,myoffline); //   vendeurManager.selectAllClient(compte);

				
				societes = new ArrayList<>();
				societes = checkOutAllSocieteNew(vendeurManager, compte);
				if(societes.size() > 0){
					checkInSocietes(myoffline, societes, compte);
				}
				
				
				if(clients.size() > 0){
					nbclt =clients.size(); 
					checkInClientsPromotion(myoffline, compte, clients, vendeurManager.getPromotionClients());
				}

				break;
			case 4:
				//if(compte.getPermissionbl() == 1){

				nbprod =0;

				List<Categorie> lscats = checkOutCatalogueProduit(categorie, compte);

				products = new ArrayList<>();

				HashMap<Integer, Produit> psres = new HashMap<>();

				products = myoffline.LoadProduits("");

				Log.e("products ",products+" <<<");
				if(products != null){

					if(products.size() == 0){
						for (int i = 0; i < lscats.size(); i++) {
							for (int j = 0; j < lscats.get(i).getProducts().size(); j++) {
								psres.put(lscats.get(i).getProducts().get(j).getId(), lscats.get(i).getProducts().get(j));
							}
						}

						if(psres.size() != 0){
							for(Integer pp:psres.keySet()){
								products.add(psres.get(pp));
							}
						}

						Log.e("products size ",products.size()+" <<<");
						myoffline.shynchronizeProduits(products);

					}
					for (int i = 0; i < products.size(); i++) {
						for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
							//Log.e(products.get(i).getId()+"",sv.getAllProduits().get(j).getRef());
							if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
								products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
							}
						}
					}


					for (int j = 0; j < lscats.size(); j++) {
						for (int i = 0; i < lscats.get(j).getProducts().size(); i++) {
							for (int k = 0; k < products.size(); k++) {
								if(lscats.get(j).getProducts().get(i).getId() == products.get(k).getId()){
									lscats.get(j).getProducts().get(i).setQteDispo(products.get(k).getQteDispo());
								}
							}
						}

						nbprod += lscats.get(j).getProducts().size();
					}
				}




				if(lscats.size() > 0){
					checkInCatalogueProduit(myoffline, lscats, compte);
				}

				checkInCommandeview(myoffline, checkOutCommandes(managercmd, compte), compte);

				//}
				break;

			case 5:
				paym = new ArrayList<>();
				paym = checkOutPayement(payemnmanager, compte);

				checkInPayement(myoffline, paym, compte);
				nbpay = paym.size();
				break;

			case 6:
				paym = new ArrayList<>();
				paym = checkOutLastPayement(payemnmanager, compte,sv);

				paym.addAll(myoffline.LoadPayements(""));

				checkInPayement(myoffline, paym, compte);
				nbpay = paym.size();
				break;
			case 7:
				clients = new ArrayList<>();
				clients = checkOutLastClient(vendeurManager, compte,sv); //   vendeurManager.selectAllClient(compte);

				clients.addAll(myoffline.LoadClients(""));

				HashMap<Integer, List<Integer>> prom = new HashMap<>();
				prom = vendeurManager.getPromotionClients();

				prom.putAll(myoffline.LoadPromoClient(""));


				if(clients.size() > 0){
					nbclt =clients.size(); 
					checkInClientsPromotion(myoffline, compte, clients, vendeurManager.getPromotionClients());
				}

				
				societes = new ArrayList<>();
				societes = checkOutAllSocieteNew(vendeurManager, compte);
				
				societes.addAll(myoffline.LoadSocietesClients(""));
				if(societes.size() > 0){
					checkInSocietes(myoffline, societes, compte);
				}
				
				break;
			case 8:
				//if(compte.getPermissionbl() == 1){

				nbprod =0;

				List<Categorie> lscatsa = checkOutCatalogueProduit(categorie, compte);

				products = new ArrayList<>();

				HashMap<Integer, Produit> psresa = new HashMap<>();

				products = myoffline.LoadProduits("");

				Log.e("products ",products+" <<<");
				if(products != null){

					if(products.size() == 0){
						for (int i = 0; i < lscatsa.size(); i++) {
							for (int j = 0; j < lscatsa.get(i).getProducts().size(); j++) {
								psresa.put(lscatsa.get(i).getProducts().get(j).getId(), lscatsa.get(i).getProducts().get(j));
							}
						}

						if(psresa.size() != 0){
							for(Integer pp:psresa.keySet()){
								products.add(psresa.get(pp));
							}
						}

						Log.e("products size ",products.size()+" <<<");
						myoffline.shynchronizeProduits(products);

					}
					for (int i = 0; i < products.size(); i++) {
						for (int j = 0; j < sv.getAllProduits(-1).size(); j++) {
							//Log.e(products.get(i).getId()+"",sv.getAllProduits().get(j).getRef());
							if(Integer.parseInt(sv.getAllProduits(-1).get(j).getRef()) == products.get(i).getId()){
								products.get(i).setQteDispo(products.get(i).getQteDispo() - sv.getAllProduits(-1).get(j).getQteDispo());
							}
						}
					}


					for (int j = 0; j < lscatsa.size(); j++) {
						for (int i = 0; i < lscatsa.get(j).getProducts().size(); i++) {
							for (int k = 0; k < products.size(); k++) {
								if(lscatsa.get(j).getProducts().get(i).getId() == products.get(k).getId()){
									lscatsa.get(j).getProducts().get(i).setQteDispo(products.get(k).getQteDispo());
								}
							}
						}

						nbprod += lscatsa.get(j).getProducts().size();
					}
				}




				if(lscatsa.size() > 0){
					checkInCatalogueProduit(myoffline, lscatsa, compte);
				}
				
				List<Commandeview> cm = new ArrayList<>();
				cm = checkOutCommandesLast(managercmd, compte, sv);
				cm.addAll(myoffline.LoadCommandeList(""));
				

				checkInCommandeview(myoffline, cm, compte);

				//}
				break;
			case 9:
				List<Commandeview> cm8 = new ArrayList<>();
				cm8 = checkOutCommandesLast(managercmd, compte, sv);
				cm8.addAll(myoffline.LoadCommandeList(""));
				

				checkInCommandeview(myoffline, cm8, compte);
				break;
			}


			/*
				List<Societe> lsosc = new ArrayList<>();
				lsosc = checkOutAllSociete(manager, compte);
				if(lsosc.size() > 0){
					checkInSocietes(myoffline, lsosc, compte);
				}

				checkInClientSecteur(myoffline, checkOutClientSecteur(vendeurManager, compte), compte);
			 */
			res.put("prod", nbprod);
			res.put("clt", nbclt);
			res.put("pay", nbpay);

		} catch (Exception e) {
			// TODO: handle exception
			res.put("prod", nbprod);
			res.put("clt", nbclt);
			res.put("pay", nbpay);
		}

		return res;
	}


	/*********************** Reload basic data ***************************************/
	// in == 0 all else in == 1 only client
	public static int RelaodClientSectInfoCommDicto(Context context,ioffline myoffline,Compte compte,VendeurManager vendeurManager,CommercialManager manager,int in){
		try {
			switch (in) {
			case 0:
				List<Societe> lsosc = new ArrayList<>();
				lsosc = checkOutAllSociete(manager, compte);
				if(lsosc.size() > 0){
					checkInSocietes(myoffline, lsosc, compte);
				}

				checkInClientSecteur(myoffline, checkOutClientSecteur(vendeurManager, compte), compte);

				checkInDictionnaire(myoffline, checkOutDictionnaire(vendeurManager, compte));

				checkInCommercialInfo(myoffline, checkOutCommercialInfo(manager, compte), compte);


				break;

			case 1:

				checkInClientSecteur(myoffline, checkOutClientSecteur(vendeurManager, compte), compte);

				break;
			case 2:

				checkInClientSecteur(myoffline, checkOutClientSecteur(vendeurManager, compte), compte);

				checkInDictionnaire(myoffline, checkOutDictionnaire(vendeurManager, compte));

				checkInCommercialInfo(myoffline, checkOutCommercialInfo(manager, compte), compte);

				break;
			case 3:

				List<Societe> lsosc1 = new ArrayList<>();
				lsosc1 = checkOutAllSociete(manager, compte);
				if(lsosc1.size() > 0){
					checkInSocietes(myoffline, lsosc1, compte);
				}

				break;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		return 0;
	}
}
