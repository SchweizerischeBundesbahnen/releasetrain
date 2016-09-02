package ch.sbb.releasetrain.webui.mojos;

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
    protected String configUrl = "";

    // the branch to get the config from
    @Parameter(property = "config.branch", required = true)
    protected String configBranch = "";

    // the user to access the config repo
    @Parameter(property = "config.user", required = true)
    protected String configUser = "";

    // the user to access the config repo
    @Parameter(property = "config.password", required = true)
    protected String configPassword = "";

    public void execute() throws MojoExecutionException {
        Application.main(new String[0]);
    }

    public static void main(String[] args) {

        ReleasetrainMojo mojo = new ReleasetrainMojo();

        mojo.configUrl = "https://u203244@code.sbb.ch/scm/~u203244/test2.git";
        mojo.configBranch = "marthaler";
        mojo.configUser = "u203244";
        mojo.configPassword = "pHrKmap9A6bLoyM4MEwEmOCfrTClh+9H";

        System.setProperty("config.url",mojo.configUrl);
        System.setProperty("config.branch",mojo.configBranch);
        System.setProperty("config.user",  mojo.configUser);
        System.setProperty("config.password",mojo.configPassword);

        try {
            mojo.execute();
        } catch (MojoExecutionException e) {
            e.printStackTrace();
        }
    }
}
