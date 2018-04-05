package tz.co.nezatech.senter.web.ui;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tz.co.nezatech.senter.data.model.Permission;
import tz.co.nezatech.senter.data.repository.PermissionRepository;
import tz.co.nezatech.senter.data.repository.RoleRepository;


@Controller
@RequestMapping("/permissions")
@PreAuthorize("hasAnyAuthority('managePermissions')")
public class PermissionController {
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PermissionRepository permissionRepository;

	@GetMapping()
	@PreAuthorize("hasAnyAuthority('viewPermissions')")
	public String index(Model m) {
		List<Permission> permissions = permissionRepository.query(Collections.emptyList());
		m.addAttribute("permissionsMenu", true);
		m.addAttribute("permissions", permissions);
		return "permissions/index";
	}

	@PostMapping()
	@PreAuthorize("hasAnyAuthority('viewPermissions')")
	public String search(Model m, String search) {
		m.addAttribute("search", search);
		System.out.println("Search: " + search);
		if (search == null)
			search = "";
		List<Permission> permissions = permissionRepository.search(search);
		m.addAttribute("permissionsMenu", true);
		m.addAttribute("permissions", permissions);
		return "permissions/index";
	}

	@GetMapping("/edit/{id}")
	@PreAuthorize("hasAnyAuthority('editPermissions')")
	public String edit(Model m, @PathVariable Long id) {
		List<Permission> parents = permissionRepository.query(Collections.emptyList());
		Permission p = permissionRepository.query(id);
		parents.remove(p);
		m.addAttribute("permissionsMenu", true);
		m.addAttribute("parents", parents);
		m.addAttribute("permission", p);
		return "permissions/edit";
	}

	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAnyAuthority('deletePermission')")
	public String delete(Model m, @PathVariable Long id, final RedirectAttributes redirectAttributes) {
		permissionRepository.delete(permissionRepository.query(id));
		FlashData fd = new FlashData(200, "Successfully deleted the permission");
		if (fd.getResultCode() == 200) {
			fd.setStyleClass("success");
		} else {
			fd.setStyleClass("alert");
		}
		redirectAttributes.addFlashAttribute("flashData", fd);
		return "redirect:/permissions";
	}

	@GetMapping("/create")
	@PreAuthorize("hasAnyAuthority('createPermissions')")
	public String create(Model m) {
		List<Permission> parents = permissionRepository.query(Collections.emptyList());
		Permission p = new Permission();
		m.addAttribute("permissionsMenu", true);
		m.addAttribute("parents", parents);
		m.addAttribute("permission", p);
		return "permissions/edit";
	}

	@PostMapping("/save")
	@PreAuthorize("hasAnyAuthority('createPermissions')")
	public String save(Permission p, Model m, RedirectAttributes redirect) {
		if (p.getId() != null && p.getId() > 0) {
			permissionRepository.update(p);
		} else {
			permissionRepository.create(p);
		}
		FlashData fd = new FlashData(200, "Successfully edited permission");
		if (fd.getResultCode() == 200) {
			fd.setStyleClass("success");
		} else {
			fd.setStyleClass("alert");
		}
		redirect.addFlashAttribute("flashData", fd);
		return "redirect:/permissions";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
