/*
  * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.jenkins;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.maven.plugin.logging.Log;

import ch.sbb.releasetrain.utils.config.GlobalConfig;
import ch.sbb.releasetrain.utils.http.HttpUtil;
import ch.sbb.releasetrain.utils.models.jenkins.JenkinsJobList;
import ch.sbb.releasetrain.utils.models.jenkins.JenkinsJobModel;

/**
 * Loads Jobs an their state from Jenkins
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @since 2.0.9, 2015
 */
public class JenkinsJobUtil {

    private Map<String, JenkinsJobModel> map = new HashMap<>();

    @Inject
    private Log log;

    @Inject
    private HttpUtil http;

    @Inject
    private GlobalConfig config;


    /**
     * A Filter can be a partial String Ex: client-server / server-tram
     * Null Filter means match
     */
    public Map<String, JenkinsJobModel> getWatchdogJobsFiltered(String... filter) {
        loadJobsFromServer();
        Map<String, JenkinsJobModel> result = new HashMap<>();
        for (String id : map.keySet()) {
            if (id.contains("watchdog")) {

                if (filter == null) {
                    result.put(id, map.get(id));
                } else {

                    for (String string : filter) {
                        if (id.contains(string)) {
                            result.put(id, map.get(id));
                        }
                    }
                }
            }
        }
        return result;
    }

    public Map<String, JenkinsJobModel> getAllProjectJobs() {
        loadJobsFromServer();
        return map;
    }

    private void loadJobsFromServer() {

        try {
            JAXBContext jc = JAXBContext.newInstance(JenkinsJobList.class);

            String contet = http.getPageAsString(config.get("jenkins.all.builds.api"));

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JenkinsJobList sc = (JenkinsJobList) unmarshaller.unmarshal(new StringReader(contet));

            for (JenkinsJobModel model : sc.getJobs()) {
                String color = model.getColor();

                if (color.contains("anime")) {
                    model.setRunning(true);
                }

                // notbuilt, blue=green, red,disabled
                // reset the colors, because we can have COLOR_anime from jenkins
                if (color.contains("blue")) {
                    model.setColor("green");
                }

                if (color.contains("yellow")) {
                    model.setColor("yellow");
                }

                if (color.contains("red")) {
                    model.setColor("red");
                }

                map.put(model.getName(), model);
            }

        } catch (JAXBException e) {
            log.error(e);
        }
    }


}
