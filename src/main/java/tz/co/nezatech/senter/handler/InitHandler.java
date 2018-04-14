package tz.co.nezatech.senter.handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import tz.co.nezatech.nezadb.model.NamedQueryParam;
import tz.co.nezatech.nezadb.model.NamedQueryParam.Operator;
import tz.co.nezatech.senter.data.model.Permission;
import tz.co.nezatech.senter.data.model.Role;
import tz.co.nezatech.senter.data.repository.PermissionRepository;
import tz.co.nezatech.senter.data.repository.RoleRepository;

@Component
public class InitHandler implements ApplicationListener<ApplicationReadyEvent> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	PermissionRepository permissionRepository;
	@Autowired
	RoleRepository roleRepository;

	static final String ROOT_ROLE_NAME = "Root";

	public InitHandler() {
		super();
	}

	void initPermissions() {
		logger.debug("Initiatizing permissions");

		Reflections ref = new Reflections("tz.co.nezatech.senter.web.ui");

		String hasAuthRegex = "^hasAnyAuthority\\('([a-zA-Z][ a-zA-Z0-9]+)'\\)$";
		Pattern pattern = Pattern.compile(hasAuthRegex);

		try {
			List<Long> inserts = new LinkedList<>();
			for (Class<?> cl : ref.getTypesAnnotatedWith(PreAuthorize.class)) {
				logger.debug("Controller: " + cl.getSimpleName());
				PreAuthorize clsPreAuth = cl.getAnnotation(PreAuthorize.class);
				String strClsAuth = clsPreAuth.value();
				Matcher matcher = pattern.matcher(strClsAuth);
				String p = null;
				if (matcher.find() || strClsAuth.equalsIgnoreCase("permitAll")) {
					p = strClsAuth.equalsIgnoreCase("permitAll") ? "permitAll" : matcher.group(1);
					logger.debug("\tPermission: " + p);
					List<Permission> tmp = permissionRepository.query(
							Arrays.asList(new NamedQueryParam[] { new NamedQueryParam("p.name", p, Operator.EQ) }));
					Permission parentPermission = null;
					if (tmp.isEmpty() && !p.equalsIgnoreCase("permitAll")) {
						parentPermission = new Permission(p, p);
						Permission res = permissionRepository.create(parentPermission);
						inserts.add(res.getId());
						parentPermission = res;
					} else {
						if (!p.equalsIgnoreCase("permitAll")) {
							parentPermission = tmp.get(0);
							parentPermission.setName(p);
							permissionRepository.update(parentPermission);
						}
					}

					Reflections refM = new Reflections(
							"tz.co.nezatech.apps.survey.web.controller." + cl.getSimpleName(),
							new MethodAnnotationsScanner());

					logger.debug("\tMethods: ");
					for (Method md : refM.getMethodsAnnotatedWith(PreAuthorize.class)) {

						PreAuthorize preAuth = md.getAnnotation(PreAuthorize.class);
						String strAuth = preAuth.value();
						Matcher mat = pattern.matcher(strAuth);
						String mp = null;
						if (mat.find()) {
							mp = mat.group(1);
							logger.debug("\t\t" + md.getName() + ": " + mp);
							tmp = permissionRepository.query(Arrays
									.asList(new NamedQueryParam[] { new NamedQueryParam("p.name", mp, Operator.EQ) }));
							Permission permission = null;
							if (tmp.isEmpty()) {
								permission = new Permission(mp, mp);
								permission.setParent(parentPermission);
								Permission resM = permissionRepository.create(permission);
								inserts.add(resM.getId());
							} else {
								permission = tmp.get(0);
								permission.setName(mp);
								permissionRepository.update(permission);
							}
						}
					}
				}

			}

			if (!inserts.isEmpty()) {
				List<Role> tmp = roleRepository.query(Arrays
						.asList(new NamedQueryParam[] { new NamedQueryParam("r.name", ROOT_ROLE_NAME, Operator.EQ) }));
				if (!tmp.isEmpty()) {
					Role e = tmp.get(0);
					roleRepository.matrixInserts(inserts, e);
					logger.debug("Successfully added permissions for root: " + ROOT_ROLE_NAME);
				} else {
					logger.error("There is no role: " + ROOT_ROLE_NAME);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		initPermissions();
	}
}
