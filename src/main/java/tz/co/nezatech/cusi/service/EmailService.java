package tz.co.nezatech.cusi.service;

import javax.servlet.http.HttpServletRequest;

public interface EmailService {
	void sendMail(String from, String to, String subject, String msg);
	String url(String path, HttpServletRequest req);
}
