/*
 * Copyright (C) eMad, 2016.
 */
package ch.sbb.releasetrain.jsfbootadapter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Author: info@emad.ch
 * @since 0.0.1
 */
@Component
@Slf4j
public class CacheFilter implements Filter {
	// 3 tage
	private static long maxAge = 1000 * 60 * 60 * 24 * 3;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String uri = ((HttpServletRequest) request).getRequestURI();
		if (uri.contains("jquery") || uri.contains("ln=primefaces") || uri.contains(".css") || uri.contains(".svg") || uri.contains(".gif") || uri.contains(".woff") || uri.contains(".png")
				|| uri.contains(".ico")) {
			// excludes
			if (uri.contains("custom.css")) {
				httpResponse.setDateHeader("Expires", 0);
			} else if (uri.contains("kl.png")) {
				httpResponse.setHeader("Cache-Control", "max-age=" + 1000 * 60 * 3);
			} else {
				httpResponse.setHeader("Cache-Control", "max-age=" + maxAge);
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("Emad Cache Filter started:" + CacheFilter.class);
	}

	@Override
	public void destroy() {
		log.info("Emad Cache Filter destroy:" + CacheFilter.class);
	}

}