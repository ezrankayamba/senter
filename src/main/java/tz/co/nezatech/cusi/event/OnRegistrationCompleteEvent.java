package tz.co.nezatech.cusi.event;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationEvent;

import tz.co.nezatech.cusi.data.model.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private User user;
	private HttpServletRequest request;

	public OnRegistrationCompleteEvent(User user, HttpServletRequest request) {
		super(user);
		this.user = user;
		this.request = request;
	}

	public User getUser() {
		return user;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

}
