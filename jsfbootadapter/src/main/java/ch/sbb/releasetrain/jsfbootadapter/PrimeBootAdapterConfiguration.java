/**
 * Copyright (C) eMad, 2016.
 */
package ch.sbb.releasetrain.jsfbootadapter;

import java.util.EnumSet;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletContext;

import com.sun.faces.config.ConfigureListener;

import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * PrimeBootAdapterConfiguration.
 *
 * @author Author: info@emad.ch
 * @since 0.0.1
 */
@Configuration
public class PrimeBootAdapterConfiguration extends WebMvcConfigurerAdapter implements ServletContextAware {

	@Autowired
	private CacheFilter filter;

	public static void main(String[] args) {
		SpringApplication.run(PrimeBootAdapterConfiguration.class, args);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
		servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
		servletContext.setInitParameter("primefaces.THEME", "bootstrap");
		servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
		servletContext.setInitParameter("javax.faces.STATE_SAVING_METHOD", "client");

		// servletContext.setInitParameter("javax.faces.CONFIG_FILES",
		// "faces-config.xml");

		servletContext.setInitParameter("facelets.SKIP_COMMENTS", "true");

		servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");

		// develop
		servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "0");
		// Set the project stage to "Development", "UnitTest", "SystemTest", or
		// "Production". "Development"
		servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Production");

	}
	@Bean
	public FilterRegistrationBean someFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter((Filter) filter);
		registration.addUrlPatterns("/*");
		registration.setName("someFilter");
		registration.setOrder(2);
		return registration;
	}


	@Bean
	public ServletRegistrationBean facesServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new FacesServlet(), "*.htm");
		registration.setLoadOnStartup(1);
		return registration;
	}

	@Bean
	public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
		return new ServletListenerRegistrationBean<ConfigureListener>(new ConfigureListener());
	}

	@Bean
	public FilterRegistrationBean rewriteFilter() {
		FilterRegistrationBean rwFilter = new FilterRegistrationBean(new RewriteFilter());
		rwFilter.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR));
		rwFilter.addUrlPatterns("/*");
		return rwFilter;
	}

}
