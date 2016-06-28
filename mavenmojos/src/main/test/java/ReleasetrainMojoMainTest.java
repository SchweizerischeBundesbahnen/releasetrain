package ch.sbb.releasetrain.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import ch.sbb.releasetrain.Application;

/**
 * Frontend Mojo for the ReleaseTrain
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
@Mojo(name = "releasetrain", defaultPhase = LifecyclePhase.VALIDATE, requiresOnline = true, requiresProject = false, threadSafe = true)
public final class ReleasetrainMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException {
        Application.main(new String[0]);
    }
}
