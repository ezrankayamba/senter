package tz.co.nezatech.cusi.data.model;

import tz.co.nezatech.nezadb.model.BaseData;

public class DataType extends BaseData{
	String name;
	String type;
	String lastUpdate;
	int position;
	
	public DataType() {
		super();
	}
	public DataType(String name, String type, String lastUpdate, int position) {
		super();
		this.name = name;
		this.type = type;
		this.lastUpdate=lastUpdate;
		this.position=position;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return String.format("Type: %s, Name: %s, LastUpdate: %s", getType(), getName(), getLastUpdate());
	}
}
