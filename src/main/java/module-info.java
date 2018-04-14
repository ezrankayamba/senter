
module tz.co.nezatech.senter {
	requires java.sql;
	requires java.se;
	requires spring.beans;
	requires spring.core;
	requires spring.context;
	requires spring.tx;
	requires spring.web;
	requires spring.webmvc;
	requires spring.boot;
	requires spring.jdbc;
	requires spring.boot.autoconfigure;
	requires tomcat.embed.core;
	requires tz.co.nezatech.nezadb;
	requires spring.security.core;
	requires spring.security.web;
	requires spring.security.config;
	requires org.thymeleaf;
	requires javax.mail.api;
	requires slf4j.api;
	requires spring.context.support;
	requires reflections;	
	exports tz.co.nezatech.senter;
	exports tz.co.nezatech.senter.web.config;
	exports tz.co.nezatech.senter.data.repository;
	exports tz.co.nezatech.senter.web.ui;
	exports tz.co.nezatech.senter.service to spring.beans;
	exports tz.co.nezatech.senter.event.listener to spring.beans;
	exports tz.co.nezatech.senter.handler to spring.beans;
	exports tz.co.nezatech.senter.data.model;
	opens tz.co.nezatech.senter to spring.core;
	opens tz.co.nezatech.senter.web.config to spring.core;
	opens tz.co.nezatech.senter.data.repository to spring.core;
	opens tz.co.nezatech.senter.web.ui to spring.core;
	opens tz.co.nezatech.senter.event.listener to spring.core;
	opens tz.co.nezatech.senter.handler to spring.core;

}
