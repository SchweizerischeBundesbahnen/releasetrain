package ch.sbb.releasetrain.config;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import ch.sbb.releasetrain.config.model.ActionConfig;
import ch.sbb.releasetrain.config.model.ReleaseConfig;
import ch.sbb.releasetrain.director.modelaccessor.YamlModelAccessor;

public class YamlSerializerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testSerializeYamlConfig() throws Exception {

        // write file first
        YamlModelAccessor<ReleaseConfig> yaml = new YamlModelAccessor<>();

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

        File file = new File(testFolder.getRoot(), "release.yaml");

        FileUtils.writeStringToFile(file, yaml.convertEntry(release));

    }
}