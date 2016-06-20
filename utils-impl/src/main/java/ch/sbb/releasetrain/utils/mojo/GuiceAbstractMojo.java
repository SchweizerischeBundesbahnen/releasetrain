/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import ch.sbb.releasetrain.utils.bootstrap.GuiceConfig;
import ch.sbb.releasetrain.utils.file.FileUtilImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Abstract Mojo to inject Guice Bootstrapping into creation of the Mojo
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.10, 2015
 */
public abstract class GuiceAbstractMojo extends AbstractMojo {

    @Parameter(property = "workspace")
    protected String workspace;
    @Inject
    protected FileUtilImpl fileUtil;
    private AbstractModule guiceConfig = new GuiceConfig();
    private Injector injector;
    private GuiceInjectorWrapper injectorWrapper;

    public GuiceAbstractMojo() {
        injectGuice();
    }

    private void injectGuice() {
        injector = Guice.createInjector(guiceConfig);
        // inject injector in its wrapper
        GuiceInjectorWrapper inj = injector.getInstance(GuiceInjectorWrapper.class);
        inj.setInjector(injector);
        // injects dependencies to my subclass
        injector.injectMembers(this);
        injectorWrapper = inj;
    }

    public Injector getInjector() {
        return injector;
    }

    public GuiceInjectorWrapper getInjectorWrapper() {
        return injectorWrapper;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    protected void writeFileFromCPToWorkspace(String folder, String filename) {
        String content = fileUtil.readFileToStringFromClasspath("/" + folder + "/" + filename);
        fileUtil.writeFile(workspace + "/" + filename, content);
    }

    protected void writeBootstrap() {
        writeFileFromCPToWorkspace("bootstrap", "bootstrap.min.css");
        writeFileFromCPToWorkspace("bootstrap", "bootstrap.min.js");
        writeFileFromCPToWorkspace("bootstrap", "dataTables.bootstrap.min.js");
        writeFileFromCPToWorkspace("bootstrap", "jquery-1.11.3.min.js");
        writeFileFromCPToWorkspace("bootstrap", "jquery.dataTables.css");
        writeFileFromCPToWorkspace("bootstrap", "jquery.dataTables.js");
    }

}