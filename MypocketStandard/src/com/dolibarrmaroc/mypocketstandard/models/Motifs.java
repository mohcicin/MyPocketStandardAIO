package com.dolibarrmaroc.mypocketstandard.models;

import java.io.Serializable;
import java.util.Date;




public class Motifs implements Serializable{

	private Tournee tour;
	private Client clt;
	private Date dt;
	private String mt;
	private String comment;
	public Motifs() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Motifs(Tournee tour, Client clt, Date dt, String mt, String comment) {
		super();
		this.tour = tour;
		this.clt = clt;
		this.dt = dt;
		this.mt = mt;
		this.comment = comment;
	}
	public Tournee getTour() {
		return tour;
	}
	public void setTour(Tournee tour) {
		this.tour = tour;
	}
	public Client getClt() {
		return clt;
	}
	public void setClt(Client clt) {
		this.clt = clt;
	}
	public Date getDt() {
		return dt;
	}
	public void setDt(Date dt) {
		this.dt = dt;
	}
	public String getMt() {
		return mt;
	}
	public void setMt(String mt) {
		this.mt = mt;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "Motifs [tour=" + tour + ", clt=" + clt + ", dt=" + dt + ", mt="
				+ mt + ", comment=" + comment + "]";
	}
}
