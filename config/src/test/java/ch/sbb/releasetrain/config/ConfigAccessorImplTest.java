package ch.sbb.releasetrain.config;

import org.junit.Assert;
import org.junit.Test;

import ch.sbb.releasetrain.config.model.ReleaseConfig;
import ch.sbb.releasetrain.utils.http.HttpUtilImpl;

public class ConfigAccessorImplTest {

    @Test
    public void testReadConfigFromGitrepo() throws Exception {

        ConfigAccessorImpl accessor = new ConfigAccessorImpl();
        accessor.setBaseURL("https://github.com/SchweizerischeBundesbahnen/releasetrain/raw/feature/releasetrainconfig/");
        accessor.setHttp(new HttpUtilImpl());
        ReleaseConfig config = accessor.readConfig("demo-release");
        Assert.assertNotNull(config);
    }

}