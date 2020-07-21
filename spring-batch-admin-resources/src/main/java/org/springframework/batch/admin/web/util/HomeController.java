/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.admin.web.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UrlPathHelper;

/**
 * Component that discovers request mappings in its application context and
 * reveals their meta data. Any {@link RequestMapping} annotations in controller
 * components at method or type level are discovered.
 * 
 * @author Dave Syer
 * 
 */
@Controller
public class HomeController implements ApplicationContextAware, InitializingBean {

    private static Log logger = LogFactory.getLog(HomeController.class);

    private ApplicationContext applicationContext;

    private Set<String> urls;

    private List<ResourceInfo> defaultResources;

    private List<ResourceInfo> jsonResources;

    private String servletPath;

    private Properties defaultProperties = null;

    private Properties jsonProperties = null;

    /**
     * 
     * @see ApplicationContextAware#setApplicationContext(ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * The path that will be added to the model as an attribute ("servletPath")
     * before rendering. Defaults to the parent servlet path (as defined in the http
     * servlet request).
     * 
     * @param servletPath the servlet path to set
     */
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    /**
     * Pre-configured mapping from url path to description for default (HTML)
     * resources.
     * 
     * @param defaultResources the default resources to set
     */
    public void setDefaultResources(Properties defaultResources) {
        this.defaultProperties = defaultResources;
    }

    /**
     * Pre-configured mapping from url path to description for JSON resources. If
     * empty the description will be replaced with the one from the
     * {@link #setDefaultResources(Properties) default resources}.
     * 
     * @param jsonResources the json resources to set
     */
    public void setJsonResources(Properties jsonResources) {
        this.jsonProperties = jsonResources;
    }

    /**
     * Create the meta data by querying the context for mappings.
     * 
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (defaultProperties == null || defaultProperties.isEmpty()) {
//            findResources();
        } else {
            this.urls = buildUrlsFromProperties(defaultProperties);
            this.defaultResources = buildResourcesFromProperties(defaultProperties, defaultProperties);
            this.jsonResources = buildResourcesFromProperties(jsonProperties, defaultProperties);
        }
    }

    private List<ResourceInfo> buildResourcesFromProperties(Properties properties, Properties defaults) {
        Set<ResourceInfo> resources = new TreeSet<ResourceInfo>();
        if (properties == null) {
            if (defaults == null) {
                return new ArrayList<ResourceInfo>();
            }
            properties = defaults;
        }
        for (Enumeration<?> iterator = properties.propertyNames(); iterator.hasMoreElements();) {
            String key = (String) iterator.nextElement();
            String method = key.substring(0, key.indexOf("/"));
            String url = key.substring(key.indexOf("/"));
            String description = properties.getProperty(key, defaults.getProperty(key));
            resources.add(new ResourceInfo(url, RequestMethod.valueOf(method), description));
        }
        return new ArrayList<ResourceInfo>(resources);
    }

    private Set<String> buildUrlsFromProperties(Properties properties) {
        Set<String> urls = new HashSet<String>();
        if (properties == null) {
            return urls;
        }
        for (Enumeration<?> iterator = properties.propertyNames(); iterator.hasMoreElements();) {
            String key = (String) iterator.nextElement();
            String url = key.substring(key.indexOf("/"));
            urls.add(url);
        }
        return urls;
    }

    /**
     * Inspect the handler mapping at the level of HTTP {@link RequestMethod}. Each
     * URI pattern that is mapped can be mapped to multiple request methods. If the
     * mapping is not explicit this method only returns GET (even though technically
     * it would respond to POST as well).
     * 
     * @param request the current servlet request (used to extract a page attribute
     *                "sevletPath")
     * @param model   {@link org.springframework.ui.ModelMap} to be used
     * 
     * @return a map of URI pattern to request methods accepted
     */
    @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
    public String getResources(HttpServletRequest request, ModelMap model) {

        String servletPath = this.servletPath;
        if (servletPath == null) {
            servletPath = new UrlPathHelper().getServletPath(request);
        }
        model.addAttribute("servletPath", servletPath);
        List<ResourceInfo> resources = new ArrayList<ResourceInfo>();
        if (!request.getRequestURI().endsWith(".json") && defaultResources != null) {
            resources.addAll(defaultResources);
        }
        if (jsonResources != null) {
            resources.addAll(jsonResources);
        }
        model.addAttribute("resources", resources);
        return "home";
    }

    /**
     * The set of unique URI patterns mapped, excluding implicit mappings. Implicit
     * mappings include all the values here plus patterns created from them by
     * appending "/" (if not already present) and ".*" (if no suffix is already
     * provided).
     * 
     * @return the set of unique URI patterns mapped
     */
    public Set<String> getUrlPatterns() {
        return urls;
    }

}
