/**
 * Copyright (C) eMad, 2016.
 */
package ch.sbb.releasetrain.jsfbootadapter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * PrimeBootAdapterConfiguration.
 *
 * @author Author: info@emad.ch
 * @since 0.0.1
 */
@Slf4j
public class PrimeBootAdapterResourceHandler extends ResourceHandlerWrapper {

	private ResourceHandler handler;

	public PrimeBootAdapterResourceHandler(ResourceHandler handler) {
		this.handler = handler;
	}

	@Override
	public ResourceHandler getWrapped() {
		return handler;
	}

	@Override
	public ViewResource createViewResource(FacesContext context, String resourceName) {

		PathMatchingResourcePatternResolver res = new PathMatchingResourcePatternResolver();

		Resource template;

		template = res.getResource("/META-INF/resources" + resourceName);

		if (!template.exists()) {
			template = res.getResource(resourceName);
		}

		return getViewResource(template);

	}

	private ViewResource getViewResource(Resource template) {

		URL url = null;
		ViewResource view = null;

		try {
			url = template.getURL();
			log.debug("found view from classpath: " + url);
		} catch (IOException e) {
			log.debug("not found view from classpath: " + url + " (" + e.getMessage() + ")");
		}

		try {
			log.debug("will check for view in src/main/resources: " + url);

			if (url == null) {
				log.error("view not found: " + url + " " + template);
			}

			String locWeb = "file:" + url.getPath().replace("src/main/webapp/WEB-INF/classes", "src/main/resources");
			String locMod = "file:" + url.getPath().replace("target/classes", "src/main/resources");

			if (new File(locWeb.replace("file:", "")).exists()) {
				log.debug("is here: " + url + " use this one");
				url = new URL(locWeb);
			} else if (new File(locMod.replace("file:", "")).exists()) {
				log.debug("is here: " + url + " use this one");
				url = new URL(locMod);
			}

		} catch (MalformedURLException e) {
			log.debug("not found view in src/main/resources" + url + " (" + e.getMessage() + ")");
		}

		final URL finalUrl = url;
		view = new ViewResource() {
			@Override
			public URL getURL() {
				return finalUrl;
			}
		};
		return view;
	}
}