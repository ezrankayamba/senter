package tz.co.nezatech.cusi.data.model;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import tz.co.nezatech.nezadb.model.IData;

public class VerificationToken implements IData{
	private Long id;
	private Long userId;
	private String token;
	private Date recordDate;
	@Value("${user.verification.token.expiry(hours)}")
	private double expiryInHours = 1;
	private boolean tnc;

	public VerificationToken() {
		super();
	}
	public VerificationToken(Long userId, String token, Date recordDate) {
		this(null, userId, token, recordDate);
	}
	public VerificationToken(Long id, Long userId, String token, Date recordDate) {
		super();
		this.id=id;
		this.userId = userId;
		this.token = token;
		this.recordDate = recordDate;
	}
	

	public boolean tncAccepted() {
		return tnc;
	}
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getUserId() {
		return userId;
	}

	public String getToken() {
		return token;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public double getExpiryInHours() {
		return expiryInHours;
	}

	public boolean expired() {
		int expiryInMin = (int) (expiryInHours * 60);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, -expiryInMin);
		Calendar recorded = Calendar.getInstance();
		recorded.setTime(getRecordDate());
		return now.after(recorded);
	}
}
