package com.dolibarrmaroc.mypocketstandard.models;

import java.io.Serializable;
import java.util.List;

public class Client implements Serializable{

	private int id;
	private String name;
	private String zip;
	private String town;
	private String email;
	private String tel;
	private String address;
	private double latitude;
	private double longitude;
	
	private String phone;
	private String fax;
	private int type;
	private int company;
	private String logo;
	
	public Client() {
	}

	public Client(int id, String name, String zip, String town, String email,
			String tel, String address) {
		super();
		this.id = id;
		this.name = name;
		this.zip = zip;
		this.town = town;
		this.email = email;
		this.tel = tel;
		this.address = address;
	}
	
	

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", zip=" + zip
				+ ", town=" + town + ", email=" + email + ", tel=" + tel
				+ ", address=" + address + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCompany() {
		return company;
	}

	public void setCompany(int company) {
		this.company = company;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
