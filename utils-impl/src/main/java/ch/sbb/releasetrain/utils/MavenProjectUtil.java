/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils;

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

/**
 * Utils for the Maven project manipulations and queries
 *
 * @author mmeyer
 */
public class MavenProjectUtil {

    /** The name separator as string. */
    protected static final String SEPARATOR = FileSystems.getDefault().getSeparator();

    private MavenProjectUtil() {

    }

    public static MavenProjectUtil getInstance() {
        return new MavenProjectUtil();
    }

    /**
     * Defines a dependency.
     * 
     * @param groupId The group id
     * @param artifactId The artifact id
     * @param version The plugin version
     * @param type The type
     * @return the dependency instance
     */
    public Dependency dependency(final String groupId, final String artifactId, final String version, final String type) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        dependency.setType(type);
        return dependency;
    }

    /**
     * Returns the plugin for the given values.
     */
    public Plugin getPlugin(MavenProject mavenProject, final String groupId,
            final String artifactId,
            final String version,
            final List<Dependency> dependencies) {
        return plugin(groupId(groupId), artifactId(artifactId),
                version((version != null) ? version : getPluginVersion(mavenProject, groupId, artifactId)),
                (CollectionUtils.isEmpty(dependencies)) ? Collections.<Dependency> emptyList() : dependencies);
    }

    /**
     * Returns the version of the plugin defined in the plugin management as string for the given groupId and artifactId.
     *
     */
    public String getPluginVersion(MavenProject mavenProject, final String groupId, final String artifactId) {
        if ((groupId != null) && (artifactId != null)) {
            for (Plugin plugin : mavenProject.getPluginManagement().getPlugins()) {
                if ((groupId.equals(plugin.getGroupId())) && (artifactId.equals(plugin.getArtifactId()))) {
                    return plugin.getVersion();
                }
            }
        }
        return null;
    }

    /**
     * Returns all runtime and compile artifacts.
     */
    public Set<Artifact> getRuntimeAndCompileArtifacts(MavenProject mavenProject) {
        final Set<Artifact> artifacts = mavenProject.getArtifacts();
        CollectionUtils.filter(artifacts, new Predicate() {

            @Override
            public boolean evaluate(final Object obj) {
                if (obj instanceof Artifact) {
                    final Artifact art = (Artifact) obj;
                    return "runtime".equals(art.getScope()) || "compile".equals(art.getScope());
                }
                return false;
            }
        });
        return artifacts;
    }

    /**
     * Returns all runtime and compile dependencies.
     */
    public List<Dependency> getRuntimeAndCompileDependencies(MavenProject mavenProject) {
        final List<Dependency> dependencies = mavenProject.getDependencies();
        CollectionUtils.filter(dependencies, new Predicate() {

            @Override
            public boolean evaluate(final Object obj) {
                if (obj instanceof Dependency) {
                    final Dependency dep = (Dependency) obj;
                    return "runtime".equals(dep.getScope()) || "compile".equals(dep.getScope());
                }
                return false;
            }
        });
        return dependencies;
    }
}
