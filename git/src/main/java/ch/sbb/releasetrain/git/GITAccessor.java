/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.git;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import ch.sbb.releasetrain.utils.model.GitModel;
import ch.sbb.releasetrain.utils.yaml.YamlUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import javax.annotation.PostConstruct;

/**
 * Base Thread to initialize a GIT Connection read / write 
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Slf4j
@Data
@Component
public class GITAccessor implements GitRepo{

	@Getter
    protected GitRepo repo;

	private boolean connecting = true;
	private boolean read = false;
	private boolean write = false;
	private String err = "";

	@Autowired
	private GitClient client;

	private GitModel model;

	private String userhome;

	private boolean dirty = false;

	@PostConstruct
	public void init(){

		String configUrl = System.getProperty("config.url");
		String configBranch =  System.getProperty("config.branch");
		String configUser =  System.getProperty("config.user");
		String configPassword =  System.getProperty("config.password");

		userhome = System.getProperty("user.home");

		if(!StringUtils.isEmpty(configUrl) && !StringUtils.isEmpty(configBranch) && !StringUtils.isEmpty(configUser) && !StringUtils.isEmpty(configPassword)){
			model = new GitModel("",configUrl,configBranch,configUser,configPassword);
		} else {
			File file = new File(userhome + "/.releasetrain/gitConfig.yaml");
			String str = "";
			if (file.exists()) {
				try {
					str = FileUtils.readFileToString(file);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
				Yaml yaml = new Yaml();
				yaml.setBeanAccess(BeanAccess.FIELD);
				model = (GitModel) yaml.load(str);

			} else {
				model = new GitModel("","","","","");
			}
		}
		writeModel();
		reset();
	}

	public void signalCommit(){
		dirty= true;
	}

	@Override
	public void reset(){
		
		repo = client.gitRepo(model.getConfigUrl(), model.getConfigBranch(), model.getConfigUser(), model.getEncPassword());
		
		err = "";
	 	connecting = true;
    	read = false;
    	write= false;
		repo.reset();

        try {
        	repo.cloneOrPull();
        	read = true;
            File dir = repo.directory();
            FileUtils.writeStringToFile(new File(dir, "test.txt"), new Date().toString());
            repo.addCommitPush();
            write = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            err = e.getMessage();
        }
        connecting = false;

	}

    @Override
	public void cloneOrPull(){
    	repo.cloneOrPull();
    }

    @Override
	public void addCommitPush(){
    	if(!connecting && write){
    		repo.addCommitPush();
    	} else {
			throw new GitException("git connection not ready");
		}
    }

    @Override
	public File directory(){
    	if(!connecting && read){
    		return repo.directory();
    	}
    	throw new GitException("git connection not ready");
    }

	private void writeModel(){
		File file  = new File(userhome + "/.releasetrain/gitConfig.yaml");
		try {
			FileUtils.writeStringToFile(file,YamlUtil.marshall(this.model));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
