package tz.co.nezatech.cusi.data.model;

import tz.co.nezatech.nezadb.model.BaseData;

public class RolePermission extends BaseData{
	private Long permissionId;
	private boolean enabled;
	
	public RolePermission() {
		super();
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "RolePermission [permissionId=" + permissionId + ", enabled=" + enabled + "]";
	}
	
}
