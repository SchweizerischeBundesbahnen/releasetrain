/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
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

    /**
     * git.setRepo(gitrepo);
     * git.setUser(gituser);
     * git.setPassword(gitpassword);
     * git.setBranch(gitbranch);
     * git.writeFile("info.txt", new Date().toString(), "master");
     * 
     * if (new File(git.getGitDir(), "/config.properties").exists()) {
     * log.info("repo is already initialized with a release train config branch ... will not update");
     * return;
     * }
     * 
     * git.wipeGitWorkspace(null, ".git", "config.properties");
     * git.stageAndPushDeletedFile();
     * writer.setWorkspace(git.getGitDir().getAbsolutePath());
     * writer.writeFileFromCPToWorkspace("/", "config.properties");
     * git.writeFile("info.txt", new Date().toString(), "");
     * }
     **/

    private String ask(String text) {
        Scanner sc = new Scanner(System.in);
        System.out.println(text);
        while (sc.hasNextLine()) {
            return sc.nextLine();
        }
        return "";
    }
}
