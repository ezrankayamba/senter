package tz.co.nezatech.cusi.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component("emailServiceImplSync")
public class EmailServiceImplSync implements EmailService {

	@Autowired
	public JavaMailSender emailSender;
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImplSync.class);

	@Override
	public void sendMail(String from, String to, String subject, String msg) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			message.setSubject(subject);
			MimeMessageHelper helper;
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(to);
			helper.setText(msg, true);
			emailSender.send(message);
		} catch (MessagingException ex) {
			logger.error(ex.getMessage());
		}
	}

	@Override
	public String url(String path, HttpServletRequest req) {
		String contextPath = req.getContextPath();
		String baseUrl = String.format("%s://%s:%d", req.getScheme(), req.getServerName(), req.getServerPort());
		StringBuilder sb = new StringBuilder(baseUrl);
		sb.append(contextPath).append(path);
		return sb.toString();
	}
	
}
