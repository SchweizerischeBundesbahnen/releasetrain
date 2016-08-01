/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.git;

import java.io.File;
import java.util.Date;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
@Scope("prototype")
public class GITAccessorThread implements GitRepo{
	
    protected GitRepo repo;
	private boolean connecting = true;
	private boolean read = false;
	private boolean write = false;
	private String err = "";
	@Autowired
	private GitClient client;
	
	private String url;
	private String branch;
	private String user;
	private String password;
	
	@Override
	public void reset(){
		
		repo = client.gitRepo(url, branch, user, password);
		
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
    	}
    	throw new GitException("git connection not ready");
    }

    @Override
	public File directory(){
    	if(!connecting && read){
    		return repo.directory();
    	}
    	throw new GitException("git connection not ready");
    }

    
}
