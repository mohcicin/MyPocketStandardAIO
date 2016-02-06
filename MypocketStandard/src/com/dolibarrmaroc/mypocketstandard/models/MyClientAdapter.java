package com.dolibarrmaroc.mypocketstandard.models;

import java.io.Serializable;

public class MyClientAdapter implements Serializable{

	private long refclient;
	private String name;
	private String addresse;
	private String tel;
	private String logo;
	public MyClientAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public long getRefclient() {
		return refclient;
	}
	public void setRefclient(long refclient) {
		this.refclient = refclient;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddresse() {
		return addresse;
	}
	public void setAddresse(String addresse) {
		this.addresse = addresse;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	@Override
	public String toString() {
		return "MyClientAdapter [refclient=" + refclient + ", name=" + name
				+ ", addresse=" + addresse + ", tel=" + tel + ", logo=" + logo
				+ "]";
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}

	public MyClientAdapter(long refclient, String name, String addresse,
			String tel, String logo) {
		super();
		this.refclient = refclient;
		this.name = name;
		this.addresse = addresse;
		this.tel = tel;
		this.logo = logo;
	}
}
