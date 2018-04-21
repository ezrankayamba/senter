package tz.co.nezatech.cusi.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import tz.co.nezatech.cusi.data.model.User;
import tz.co.nezatech.cusi.data.model.VerificationToken;
import tz.co.nezatech.cusi.data.repository.VerificationTokenRepository;
import tz.co.nezatech.cusi.event.OnRegistrationCompleteEvent;
import tz.co.nezatech.cusi.service.EmailServiceImplSync;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	@Qualifier("emailServiceImplSync")
	private EmailServiceImplSync emailService;
	@Autowired
	private VerificationTokenRepository verify;
	@Autowired
	private TemplateEngine templateEngine;
	@Value("${spring.application.name}")
	private String appName = "Service Center";

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = verify.createVerificationToken(user, token);
		String subject = "Registration Confirmation";
		String path = "/ucl/verify";
		String url = emailService.url(path, event.getRequest());

		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("verifyUrl", url);
		context.setVariable("appName", appName);
		context.setVariable("token", verificationToken.getToken());
		String msg = templateEngine.process("verifyemail", context);
		emailService.sendMail(appName, user.getEmail(), subject, msg);
	}
}
