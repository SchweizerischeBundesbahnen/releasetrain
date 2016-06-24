/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.git;

public class GitClientImpl implements GitClient {
    @Override
    public GitRepo gitRepo(String repo, String branch, String user, String password) {
        return new BitRepoImpl(repo, branch, user, password);
    }
}
