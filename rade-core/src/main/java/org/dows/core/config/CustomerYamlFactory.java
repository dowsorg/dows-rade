package org.dows.core.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author lait.zhang@gmail.com
 * @description: 加载自定义yml文件
 * @weixin SH330786
 * @date 2/22/2022
 */
public class CustomerYamlFactory extends DefaultPropertySourceFactory {

/*    @Override
    public PropertySource<?> createPropertySource(String s, EncodedResource encodedResource) throws IOException {
        Properties propertiesFromYaml = loadYamlIntoProperties(encodedResource);
        String sourceName = s != null ? s : encodedResource.getResource().getFilename();
        return new PropertiesPropertySource(sourceName, propertiesFromYaml);
    }*/
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {

        String sourceName = name != null ? name : resource.getResource().getFilename();
        if (!resource.getResource().exists()) {
            return new PropertiesPropertySource(sourceName, new Properties());
        } else if (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml")) {
            Properties propertiesFromYaml = loadYamlIntoProperties(resource);
            return new PropertiesPropertySource(sourceName, propertiesFromYaml);
        } else {
            return super.createPropertySource(name, resource);
        }
    }
    /**
     * 加载yml文件
     * @param resource
     * @return
     * @throws FileNotFoundException
     */
    private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (IllegalStateException e) {
            // for ignoreResourceNotFound
            Throwable cause = e.getCause();
            if (cause instanceof FileNotFoundException) {
                throw (FileNotFoundException) e.getCause();
            }
            throw e;
        }
    }



}
