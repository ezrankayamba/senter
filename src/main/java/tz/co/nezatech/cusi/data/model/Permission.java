package tz.co.nezatech.cusi.data.model;

import tz.co.nezatech.nezadb.model.NameDescData;

public class Permission extends NameDescData{
	private Permission parent;
	private boolean enabled;

	public Permission() {
		super();
	}

	public Permission(String name, String description) {
		super(name, description);
	}

	public Permission getParent() {
		return parent;
	}

	public void setParent(Permission parent) {
		this.parent = parent;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
}