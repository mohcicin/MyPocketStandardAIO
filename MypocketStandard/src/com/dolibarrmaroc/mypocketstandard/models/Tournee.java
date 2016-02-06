package com.dolibarrmaroc.mypocketstandard.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tournee implements Serializable{

	private long rowid;
	private String label;
	private String color;
	private String debut;
	private String fin;
	private String secteur;
	private long idsecteur;
	
	private List<Client> lsclt = new ArrayList<>();
	
	private String grp;
	private long idgrp;
	
	private List<Integer> recur = new ArrayList<>();
	
	public Tournee() {
		super();
		// TODO Auto-generated constructor stub
	}
	 
	public long getRowid() {
		return rowid;
	}
	public void setRowid(long rowid) {
		this.rowid = rowid;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDebut() {
		return debut;
	}
	public void setDebut(String debut) {
		this.debut = debut;
	}
	public String getFin() {
		return fin;
	}
	public void setFin(String fin) {
		this.fin = fin;
	}
	public String getSecteur() {
		return secteur;
	}
	public void setSecteur(String secteur) {
		this.secteur = secteur;
	}
	public long getIdsecteur() {
		return idsecteur;
	}
	public void setIdsecteur(long idsecteur) {
		this.idsecteur = idsecteur;
	}
	public List<Client> getLsclt() {
		return lsclt;
	}
	public void setLsclt(List<Client> lsclt) {
		this.lsclt = lsclt;
	}
	public String getGrp() {
		return grp;
	}
	public void setGrp(String grp) {
		this.grp = grp;
	}
	public long getIdgrp() {
		return idgrp;
	}
	public void setIdgrp(long idgrp) {
		this.idgrp = idgrp;
	}
	@Override
	public String toString() {
		return "Tournee [rowid=" + rowid + ", label=" + label + ", color="
				+ color + ", debut=" + debut + ", fin=" + fin + ", secteur="
				+ secteur + ", idsecteur=" + idsecteur + ", lsclt=" + lsclt
				+ ", grp=" + grp + ", idgrp=" + idgrp + ", recur=" + recur
				+ "]";
	}
	public List<Integer> getRecur() {
		return recur;
	}
	public void setRecur(List<Integer> recur) {
		this.recur = recur;
	}

	public Tournee(long rowid, String label, String color, String debut,
			String fin, String secteur, long idsecteur, List<Client> lsclt,
			String grp, long idgrp, List<Integer> recur) {
		super();
		this.rowid = rowid;
		this.label = label;
		this.color = color;
		this.debut = debut;
		this.fin = fin;
		this.secteur = secteur;
		this.idsecteur = idsecteur;
		this.lsclt = lsclt;
		this.grp = grp;
		this.idgrp = idgrp;
		this.recur = recur;
	}

	public Tournee(long rowid, String label, String color, String debut,
			String fin, String secteur, long idsecteur, String grp, long idgrp,
			List<Integer> recur) {
		super();
		this.rowid = rowid;
		this.label = label;
		this.color = color;
		this.debut = debut;
		this.fin = fin;
		this.secteur = secteur;
		this.idsecteur = idsecteur;
		this.grp = grp;
		this.idgrp = idgrp;
		this.recur = recur;
	}
	
	
}
