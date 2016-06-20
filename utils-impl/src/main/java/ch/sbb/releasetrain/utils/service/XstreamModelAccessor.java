/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2016.
 */
package ch.sbb.releasetrain.utils.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;

import ch.sbb.releasetrain.utils.models.MailReceiver;
import ch.sbb.releasetrain.utils.models.Recognizable;
import ch.sbb.releasetrain.utils.models.ReleaseJob;

import com.google.inject.Inject;
import com.thoughtworks.xstream.XStream;

/**
 * Representation of a release Build Job
 *
 * @author u203244 (Daniel Marthaler)
 * @version $Id: $
 * @param <T>
 * @since 2.0.6, 2015
 */
@SuppressWarnings("rawtypes")
public class XstreamModelAccessor<T extends Recognizable> {

    @Inject
    private XStream xstream;

    @Inject
    private Log log;

    public XstreamModelAccessor(Log log) {
        this.log = log;
        xstream = new XStream();
        xstream.alias("releaseJob", ReleaseJob.class);
        xstream.alias("list", List.class);
        xstream.alias("receiver", MailReceiver.class);
    }

    public XstreamModelAccessor() {

    }

    @Inject // = post Construct
    private void init() {
        log.debug("** calling post construct init()");
        xstream.alias("releaseJob", ReleaseJob.class);
        xstream.alias("list", List.class);
        xstream.alias("receiver", MailReceiver.class);
        log.debug("** init ok");
    }

    public String convertEntrys(Object in) {
        return xstream.toXML(in);
    }

    public List<T> convertEntrys(String in) {
        List<T> list = (List<T>) xstream.fromXML(in);
        Collections.sort(list);
        return list;
    }

    public void saveEntrys(List<T> list, String file) {
        String xml = xstream.toXML(list);
        try {
            FileUtils.writeStringToFile(new File(file), xml, Charset.defaultCharset());
        } catch (IOException e) {
            log.error("saving log model", e);
        }
    }

}
