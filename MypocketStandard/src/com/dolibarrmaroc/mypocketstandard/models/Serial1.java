package com.dolibarrmaroc.mypocketstandard.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Serial1 implements Serializable{

	private String name;
	private String subname;
	private String label;
	private List<Double> data =new ArrayList<>();
	private List<Integer> data2 =new ArrayList<>();
	public Serial1() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Serial1(String name, List<Double> data) {
		super();
		this.name = name;
		this.data = data;
	}
	
	public Serial1(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Double> getData() {
		return data;
	}
	public void setData(List<Double> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Serial1 [name=" + name + ", data=" + data + "]";
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSubname() {
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	public List<Integer> getData2() {
		return data2;
	}
	public void setData2(List<Integer> data2) {
		this.data2 = data2;
	}
}
