/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.jsfbootadapter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * FileDownloadUtil.
 *
 * @author Author: info@emad.ch
 * @since 0.0.1
 */
@Controller
@Slf4j
public class FileDownloadUtil {

	@RequestMapping(value = "/static/**", method = RequestMethod.GET)
	public void getFile(HttpServletResponse response, HttpServletRequest request) {
		try {

			String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			PathMatchingResourcePatternResolver res = new PathMatchingResourcePatternResolver();

			Resource template = res.getResource(path);

			if (!template.exists()) {
				log.info("file n/a... " + path);
				return;
			}

			org.apache.commons.io.IOUtils.copy(template.getInputStream(), response.getOutputStream());
			response.flushBuffer();

		} catch (IOException ex) {
			log.info("Error writing file to output stream", ex);
		}
	}
}
