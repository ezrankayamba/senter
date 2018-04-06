package tz.co.nezatech.senter.web.ui;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.co.nezatech.senter.data.model.VerificationToken;

@Controller
@RequestMapping("/ulc")
@PreAuthorize("permitAll")
public class UserLifeCycleController {
	@PostMapping("/verify")
	public String verifyReg(Model m, VerificationToken verificationToken) {
		return "/login";
	}
}
