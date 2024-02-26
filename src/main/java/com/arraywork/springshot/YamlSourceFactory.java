package com.arraywork.springshot;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

/**
 * YAML Property Source Factory
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/02/13
 */
public class YamlSourceFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null) {
            super.createPropertySource(name, resource);
        }
        Resource res = resource.getResource();
        List<PropertySource<?>> sources = new YamlPropertySourceLoader().load(res.getFilename(), res);
        return sources.get(0);
    }

}