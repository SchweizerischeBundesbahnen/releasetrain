package ch.sbb.releasetrain.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import ch.sbb.releasetrain.director.guice.GuiceAbstractMojo;
import ch.sbb.releasetrain.director.jenkins.JenkinsJobThread;
import ch.sbb.releasetrain.utils.http.HttpUtilImpl;



/**
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 0.0.1, 2016
 */
@Mojo(name = "demo", defaultPhase = LifecyclePhase.VALIDATE, requiresOnline = true, requiresProject = false, threadSafe = true)
public final class DemoMojo extends GuiceAbstractMojo {

    // Anzahl Applikcationen, die nachtlich im Cisi-Deployment gefunden werden
    // muessen
    @Parameter(property = "productcount")
    protected int productcount = 5;

    @Parameter(property = "version")
    protected String version;


    private HttpUtilImpl http;

    public static void main(String[] args) {
        DemoMojo mojo = new DemoMojo();
        try {
            mojo.version = "1.15.1011-SNAPSHOT";
            mojo.execute();
        } catch (MojoExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute() throws MojoExecutionException {
        http.setPassword("releasetrain11");
        http.setUser("releasetrain");
        JenkinsJobThread th = new JenkinsJobThread("releasetrain-demo", "cause", "test=test");

        th.startBuildJobOnJenkins(true);

        String user = th.getLatestUserForJob();

        System.out.print("u: " + user);

    }

}
