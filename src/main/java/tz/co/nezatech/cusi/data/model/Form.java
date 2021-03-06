package tz.co.nezatech.cusi.data.model;

import tz.co.nezatech.nezadb.model.NameDescData;

public class Form extends NameDescData {
	String display, filepath, status, version, json;
	int count;

	public Form() {
		super();
	}

	public Form(String name, String description) {
		super(name, description);
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return getName();
	}
}
