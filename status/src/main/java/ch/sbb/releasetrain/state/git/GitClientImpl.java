/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */

package ch.sbb.releasetrain.state.git;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author u206123 (Florian Seidl)
 * @since 0.0.6, 2016.
 */
@Component
public class GitClientImpl implements GitClient {

    private final File tempDir;

    public GitClientImpl() {
        tempDir = new File(System.getProperty("java.io.tmpdir"));
    }

    public GitClientImpl(File tempDir) {
        this.tempDir = tempDir;
    }

    @Override
    public GitRepo gitRepo(String url, String branch, String user, String password) {
        return new GitRepoImpl(url, branch, user, password, new File(tempDir, filenameFromUrl(url)));
    }

    static String filenameFromUrl(final String url) {
        return url.trim().replaceAll("[\\W]", "_");
    }
}
