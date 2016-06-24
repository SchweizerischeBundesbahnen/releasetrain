package ch.sbb.releasetrain.utils.workspace;

import lombok.Setter;
import ch.sbb.releasetrain.utils.file.FileUtil;
/**
 * Util to write Files from classpath to the workspace (ex. jenkins workspace)
 *
 * @author u203244 (Daniel Marthaler)
 * @since 0.0.1, 2016
 */
public class ClasspathToWorkspaceWriterImpl implements ClasspathToWorkspaceWriter {

    @Setter
    protected String workspace;


    protected FileUtil fileUtil;

    @Override
    public void writeFileFromCPToWorkspace(String folder, String filename) {
        String content = fileUtil.readFileToStringFromClasspath("/" + folder + "/" + filename);
        fileUtil.writeFile(workspace + "/" + filename, content);
    }

}