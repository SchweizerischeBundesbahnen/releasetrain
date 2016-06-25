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
public class GitClientImpl implements GitClient {
    @Override
    public GitRepo gitRepo(String repo, String branch, String user, String password) {
        return new GitRepoImpl(repo, branch, user, password);
    }
}
