package ch.sbb.releasetrain.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import ch.sbb.releasetrain.Application;

/**
 * Frontend Mojo for the ReleaseTrain
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Mojo(name = "releasetrain", defaultPhase = LifecyclePhase.VALIDATE, requiresOnline = true, requiresProject = false, threadSafe = true)
public final class ReleasetrainMojo extends AbstractMojo {

    // repo url ex: git repo
    @Parameter(property = "config.url", required = true)
    private String configUrl = "";

    // root of the repo to get the files via http (ex: raw file of git repo)
    @Parameter(property = "config.baseurl", required = true)
    private String configBaseUrl = "";

    // the branch to get the config from
    @Parameter(property = "config.branch", required = true)
    private String configBranch = "";

    // the user to access the config repo
    @Parameter(property = "config.user", required = true)
    private String configUser = "";

    // the user to access the config repo
    @Parameter(property = "config.password", required = true)
    private String configPassword = "";

    // repo url ex: git repo, where the states are stored
    @Parameter(property = "store.url", required = true)
    private String storeUrl = "";

    // the branch to store the states
    @Parameter(property = "store.branch", required = true)
    private String storeBranch = "";

    // the user to access the store
    @Parameter(property = "store.user", required = true)
    private String storeUser = "";

    // the user to access the store repo
    @Parameter(property = "store.password", required = true)
    private String storePassword = "";

    // the url for jenkins
    @Parameter(property = "jenkins.url", required = true)
    private String jenkinsUrl = "";

    // the jenkins.buildtoken
    @Parameter(property = "jenkins.buildtoken", required = true)
    private String jenkinsBuildtoken = "";

    // the jenkins.buildtoken
    @Parameter(property = "jenkins.user", required = true)
    private String httpUser = "";

    // the jenkins.buildtoken
    @Parameter(property = "jenkins.password", required = true)
    private String httpPassword = "";

    // host of the Mail Server
    @Parameter(property = "smtp.host", required = true)
    private String smtpHost = "";

    public void execute() throws MojoExecutionException {
        Application.main(new String[0]);
    }
}
