/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.git;

public interface GitClient {

    GitRepo gitRepo(final String repo, final String branch, final String user, final String password);
}
