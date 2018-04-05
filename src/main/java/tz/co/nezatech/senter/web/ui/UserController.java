package tz.co.nezatech.senter.web.ui;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tz.co.nezatech.senter.data.model.User;
import tz.co.nezatech.senter.data.repository.RoleRepository;
import tz.co.nezatech.senter.data.repository.UserRepository;
import tz.co.nezatech.senter.service.EmailService;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority('manageUsers')")
public class UserController {
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
	EmailService emailService;

	@GetMapping()
	@PreAuthorize("hasAnyAuthority('viewUsers')")
	public String index(Model m) {
		m.addAttribute("usersMenu", true);
		m.addAttribute("users", userRepository.query(Collections.emptyList()));
		return "users/index";
	}

	@PostMapping()
	@PreAuthorize("hasAnyAuthority('viewUsers')")
	public String search(Model m, String search) {
		m.addAttribute("search", search);
		System.out.println("Search: " + search);
		if (search == null)
			search = "";
		List<User> users = userRepository.search(search);
		m.addAttribute("usersMenu", true);
		m.addAttribute("users", users);
		return "users/index";
	}

	@GetMapping("/edit/{id}")
	@PreAuthorize("hasAnyAuthority('editUsers')")
	public String edit(Model m, @PathVariable Long id) {
		m.addAttribute("usersMenu", true);
		m.addAttribute("roles", roleRepository.query(Collections.emptyList()));
		m.addAttribute("userEntity", userRepository.query(id));
		return "users/edit";
	}

	@GetMapping("/create")
	@PreAuthorize("hasAnyAuthority('createUsers')")
	public String create(Model m) {
		m.addAttribute("usersMenu", true);
		m.addAttribute("roles", roleRepository.query(Collections.emptyList()));
		m.addAttribute("userEntity", new User());
		return "users/edit";
	}

	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAnyAuthority('deleteUsers')")
	public String delete(Model m, @PathVariable Long id) {
		userRepository.delete(userRepository.query(id));
		return "redirect:/users";
	}

	@PostMapping("/save")
	@PreAuthorize("hasAnyAuthority('createUsers')")
	public String save(User e, Model m, RedirectAttributes redirect, HttpServletRequest req) {
		if (e.getId() != null && e.getId() > 0) {
			userRepository.update(e);
		} else {
			// set password
			String pwd = MyUtil.alphaNumericRandom(6);
			e.setPassword(passwordEncoder.encode(pwd));
			User u = userRepository.create(e);

			String path = "/pwd/verify/" + MyUtil.encode(u.getId() + "-" + pwd);
			String url = emailService.url(path, req);
			String thml = "<div style=\"border: 1px solid #333;padding: 0.5rem;\">Welcome to Survey Tool. Your account is reset. Kindly click below link to access your account. <br/> <br/> "
					+ "<a href=\"" + url
					+ "\" style=\"display: block; font-size:120%; text-decoration:none; width: 248px;background: #4E9CAF;text-align: center;color: white;font-weight: bold;padding: 0.5rem\" >"
					+ "Survey Tool Login</a>" + "</div>";
			String to = e.getEmail();
			String from = "Survey Tool <nezatech18@gmail.com>";
			String subject = "Complete registration";
			emailService.sendMail(from, to, subject, thml);
		}
		FlashData fd = new FlashData(200, "Successfully edited user");
		if (fd.getResultCode() == 200) {
			fd.setStyleClass("success");
		} else {
			fd.setStyleClass("alert");
		}
		redirect.addFlashAttribute("flashData", fd);
		return "redirect:/users";

	}

}
