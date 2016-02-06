package com.dolibarrmaroc.mypocketstandard.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataSerial implements Serializable{

	private List<Serial1> s1 = new ArrayList<>();
	private List<String> categories = new ArrayList<>();
	private String title;
	private String subtitle;
	private String ydata;
	private String xdata;
	
	public DataSerial() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DataSerial(List<Serial1> s1, List<String> categories) {
		super();
		this.s1 = s1;
		this.categories = categories;
	}
	public List<Serial1> getS1() {
		return s1;
	}
	public void setS1(List<Serial1> s1) {
		this.s1 = s1;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	@Override
	public String toString() {
		return "DataSerial [s1=" + s1 + ", categories=" + categories + "]";
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getYdata() {
		return ydata;
	}
	public void setYdata(String ydata) {
		this.ydata = ydata;
	}
	public DataSerial(List<Serial1> s1, List<String> categories, String title,
			String subtitle, String ydata) {
		super();
		this.s1 = s1;
		this.categories = categories;
		this.title = title;
		this.subtitle = subtitle;
		this.ydata = ydata;
	}
	public String getXdata() {
		return xdata;
	}
	public void setXdata(String xdata) {
		this.xdata = xdata;
	}
	
	
}
