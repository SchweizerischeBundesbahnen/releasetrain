package ch.sbb.releasetrain.config;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import ch.sbb.releasetrain.config.model.ActionConfig;
import ch.sbb.releasetrain.config.model.ReleaseConfig;
import ch.sbb.releasetrain.director.modelaccessor.YamlModelAccessor;

public class ConfigAccessorImplTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testReadConfig() throws Exception {

        // write file first
        YamlModelAccessor<ReleaseConfig> xstream = new YamlModelAccessor<>();


        ReleaseConfig release = new ReleaseConfig();
        ActionConfig action = new ActionConfig();
        action.setName("my Action äü");
        action.getProperties().put("myProp1", "Hallo Welt1");
        action.getProperties().put("myProp2", "Hallo Welt2");

        ActionConfig action2 = new ActionConfig();
        action2.setName("my Action äüass");
        action2.getProperties().put("myProp2", "Hallo asxasxsxWelt");

        release.setTyp("demo-release");
        release.getActions().add(action);
        release.getActions().add(action2);

        String xml = xstream.convertEntrys(release);

        File file = new File(testFolder.getRoot(), "temp.xml");

        FileUtils.writeStringToFile(file, xml);

        // List<>xstream.convertEntrys(FileUtils.readFileToString(file));

    }

    @Test
    public void testWriterConfig() throws Exception {

    }
}