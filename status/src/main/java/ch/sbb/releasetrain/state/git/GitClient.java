/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.git;

/**
 * Provide access to git repository.
 *
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
public interface GitClient {

    GitRepo gitRepo(final String repo, final String branch, final String user, final String password);
}
