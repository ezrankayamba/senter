package tz.co.nezatech.cusi.data.model;

import tz.co.nezatech.nezadb.model.BaseData;

public class User extends BaseData{
	private Long id;
	private String username, password,password2, email, fullName;
	private Role role;
	private boolean enabled, resetOn;

	public User() {
		super();
	}

	public User(Long id, String username, String password, String email, String fullName) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullName = fullName;
		this.id=id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;

		return this.getId().equals(((User) obj).getId());
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isResetOn() {
		return resetOn;
	}

	public void setResetOn(boolean resetOn) {
		this.resetOn = resetOn;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	@Override
	public String toString() {
		return getUsername();
	}
}
