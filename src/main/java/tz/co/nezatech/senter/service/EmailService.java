package tz.co.nezatech.senter.service;

import javax.servlet.http.HttpServletRequest;

public interface EmailService {
	void sendMail(String from, String to, String subject, String msg);
	String url(String path, HttpServletRequest req);
}
