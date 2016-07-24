/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.git;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Base Thread to initialize a GIT Connection read / write 
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Slf4j
@Data
public abstract class GITAccessorThread implements GitRepo{
	
	protected boolean connecting = true;
	protected boolean read = false;
	protected boolean write = false;
	protected String err = "";
	
	protected GitRepo repo;
	
	@Autowired
	private GitClient client;
	
	protected String url;
	protected String branch;
	protected String user;
	protected String password;
	
	protected abstract void init();
	
	@Async
	protected void asyncInit(){
		repo = client.gitRepo(url, branch, user, password);

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

	
    public void cloneOrPull(){
    	repo.cloneOrPull();
    }

    public void addCommitPush(){
    	if(!connecting && write){
    		repo.addCommitPush();
    	}
    	throw new GitException("git connection not ready");
    }

    public File directory(){
    	if(!connecting && read){
    		return repo.directory();
    	}
    	throw new GitException("git connection not ready");
    }

    public void reset(){
    	connecting = true;
		repo.reset();
		asyncInit();
    }
    
}
