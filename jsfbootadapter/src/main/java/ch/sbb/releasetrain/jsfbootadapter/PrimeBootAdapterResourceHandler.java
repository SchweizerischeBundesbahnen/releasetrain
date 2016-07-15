package ch.sbb.releasetrain.jsfbootadapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class PrimeBootAdapterResourceHandler extends ResourceHandlerWrapper {

    public Map<String, ViewResource> cache = new HashMap<String, ViewResource>();
    private Log log = LogFactory.getLog(getClass());
    private ResourceHandler handler;

    public PrimeBootAdapterResourceHandler(ResourceHandler handler) {
        this.handler = handler;
    }

    @Override
    public ResourceHandler getWrapped() {
        return handler;
    }

    @Override
    public ViewResource createViewResource(FacesContext context, String resourceName) {

        ViewResource view = cache.get(resourceName);

        if (view != null) {
            log.debug("found view in cache: " + view.getURL());
            return view;
        }

        PathMatchingResourcePatternResolver res = new PathMatchingResourcePatternResolver();

        Resource template;

        template = res.getResource("/" + resourceName);

        if (!template.exists()) {
            template = res.getResource(resourceName);
        }

        view = getViewResource(template);
        cache.put(resourceName, view);
        return view;
    }

    private ViewResource getViewResource(Resource template) {
        URL url = null;
        URL url2 = null;
        ViewResource view = null;

        try {

            url = template.getURL();
            log.debug("found view from classpath: " + url);
        } catch (IOException e) {
            log.debug("not found view from classpath: " + url + " (" + e.getMessage() + ")");
        }


        try {
            log.debug("will check for view in src/main/resources: " + url);

            if (url == null) {
                log.error("view not found: " + url + " " + template);
                return null;
            }
            String loc = "file:" + url.getPath().replace("target/classes", "src/main/resources");

            url2 = new URL(loc);

            if (loc.contains(".war!")) {
                log.debug("we are in a war file, will not use this one: " + url);
            } else {
                log.debug("found view in src/main/resources will use this one: " + url);
                url = url2;
            }

        } catch (MalformedURLException e) {
            log.debug("not found view in src/main/resources" + url + " (" + e.getMessage() + ")");
        }

        final URL finalUrl = url;
        view = new ViewResource() {
            @Override
            public URL getURL() {
                return finalUrl;
            }
        };
        return view;
    }
}