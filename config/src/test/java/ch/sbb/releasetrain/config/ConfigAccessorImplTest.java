package ch.sbb.releasetrain.config;

import org.junit.Test;

import ch.sbb.releasetrain.config.model.ActionConfig;
import ch.sbb.releasetrain.config.model.ReleaseConfig;
import ch.sbb.releasetrain.director.modelaccessor.XstreamModelAccessor;

public class ConfigAccessorImplTest {

    @Test
    public void testReadConfig() throws Exception {

        // write file first
        XstreamModelAccessor<ReleaseConfig> xstream = new XstreamModelAccessor<>();


        ReleaseConfig release = new ReleaseConfig();
        ActionConfig action = new ActionConfig();
        action.setName("my Action äü");
        action.getProperties().put("myProp", "Hallo Welt");

        release.setTyp("demo-release");
        release.getActions().add(action);

        String xml = xstream.convertEntrys(release);


    }

    @Test
    public void testWriterConfig() throws Exception {

    }
}