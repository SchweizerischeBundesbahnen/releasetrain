/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.config;

import ch.sbb.releasetrain.config.model.email.MailReceiver;
import ch.sbb.releasetrain.config.model.releasecalendar.ReleaseCalendar;
import ch.sbb.releasetrain.config.model.releaseconfig.ReleaseConfig;
import ch.sbb.releasetrain.git.GITAccessor;
import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;
import ch.sbb.releasetrain.utils.yaml.YamlUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

/**
 * Provides Acces to the Release Configs, stored in a GIT Storage
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Slf4j
@Component
public class ConfigAccessorImpl implements ConfigAccessor {

	@Autowired
	private GITAccessor git;

	private YamlModelAccessor<ReleaseConfig> accessorRelease = new YamlModelAccessor<>();

	@Override
	public ReleaseConfig readConfig(String name) {
		if (!name.contains("-type")) {
			name = name + "-type";
		}
		File dir = git.getRepo().directory();
		File file = new File(dir, "/" + name + ".yml");
		if (!file.exists()) {
			return null;
		}
		try {
			return accessorRelease.convertEntry(FileUtils.readFileToString(file));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<String> readAllConfigs() {
		List<String> ret = new ArrayList<>();
		File dir = git.getRepo().directory();
		for (String file : dir.list()) {
			if (file.contains("-type.yml")) {
				ret.add(file.replace("-type.yml", ""));
			}
		}
		ret.remove("defaultConfig");
		return ret;
	}

	@Override
	public void writeConfig(String name, ReleaseConfig config) {
		File dir = git.getRepo().directory();
		File file = new File(dir, "/" + name + "-type.yml");

		try {
			FileUtils.writeStringToFile(file, accessorRelease.convertEntry(config));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		if (this.readCalendar(name) == null) {
			this.writeCalendar(new ReleaseCalendar(), name);
		}

	}

	public void deleteConfig(String name) {
		List<String> ret = new ArrayList<>();
		File dir = git.getRepo().directory();
		for (String file : dir.list()) {
			if (file.contains(name)) {
				try {
					FileUtils.forceDelete(new File(dir, file));
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public List<MailReceiver> readMailReceiver() {
		File dir = git.getRepo().directory();
		File file = new File(dir, "/mailinglists.yml");
		if (!file.exists()) {
			return null;
		}
		try {
			Yaml yaml = new Yaml();
			yaml.setBeanAccess(BeanAccess.FIELD);
			return (List<MailReceiver>) yaml.load(FileUtils.readFileToString(file));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<MailReceiver> readMailReveiverForMailinglist(String... mailinglists) {
		List<MailReceiver> ret = new ArrayList<>();
		if (readMailReceiver() == null) {
			return ret;
		}
		for (MailReceiver mailRec : readMailReceiver()) {
			for (String mailinglist : mailinglists) {
				if (mailRec.getMailinglist().contains(mailinglist)) {
					ret.add(mailRec);
				}
			}
		}
		return ret;
	}

	@Override
	public ReleaseCalendar readCalendar(String action) {
		File dir = git.getRepo().directory();
		File file = new File(dir, "/" + action + "-cal.yml");
		if (!file.exists()) {
			return null;
		}
		try {
			return (ReleaseCalendar) YamlUtil.unMarshall(FileUtils.readFileToString(file));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void writeCalendar(ReleaseCalendar cal, String action) {
		File dir = git.getRepo().directory();
		File file = new File(dir, "/" + action + "-cal.yml");

		try {
			FileUtils.writeStringToFile(file, YamlUtil.marshall(cal));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
