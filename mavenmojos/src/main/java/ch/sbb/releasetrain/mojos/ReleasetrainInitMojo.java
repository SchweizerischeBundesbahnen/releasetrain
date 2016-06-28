/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements;
 * and to You under the Apache License, Version 2.0.
 */
package ch.sbb.releasetrain.mojos;

import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


/**
 * Release Train Mojo
 */
@Mojo(name = "init", defaultPhase = LifecyclePhase.VALIDATE, requiresOnline = true, requiresProject = false, threadSafe = false)
@Slf4j
public class ReleasetrainInitMojo extends AbstractMojo {

    @Parameter(property = "gitrepo", required = false)
    private String gitrepo = "https://github.com/SchweizerischeBundesbahnen/releasetrain.git";

    @Parameter(property = "gituser", required = false)
    private String gituser = "marthaler.worb@gmail.com";

    @Parameter(property = "gitpassword", required = false)
    private String gitpassword = "";

    @Parameter(property = "gitbranch", required = false)
    private String gitbranch = "feature/releasetrain";

    public static void main(String[] args) throws Exception {
        ReleasetrainInitMojo mojo = new ReleasetrainInitMojo();
        mojo.execute();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (gitrepo.isEmpty()) {
            gitrepo = ask("please enter git repo url: ");
        }

        if (gituser.isEmpty()) {
            gituser = ask("please enter git username: ");
        }

        if (gitpassword.isEmpty()) {
            gitpassword = ask("please enter git password: ");
        }

        if (gitbranch.isEmpty()) {
            gitbranch = ask("please enter git branch (default: feature/releasetrain): ");
            if (gitbranch.isEmpty()) {
                gitbranch = "feature/releasetrain";
            }
        }
    }

    private String ask(String text) {
        Scanner sc = new Scanner(System.in);
        System.out.println(text);

        if (sc.hasNextLine()) {
            return sc.nextLine();
        }
        return "";
    }
}
