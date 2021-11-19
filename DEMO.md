A demo function looks like:

```java
package com.demo.component.context;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import com.inlabsoft.core.log.Log;
import com.inlabsoft.core.utils.Utils;

/**
 * The <code>ApplicationConfigurer</code> class is a special configuration component that reads property
 * 'spring.application.properties.locations' to get all additional configuration files that should be merged into main
 * configuration.
 * <p>
 * This class provides a transparent mechanism to split overall application's configuration into dedicated files.
 *
 * @version 1.0
 */
@Component
public class PropertiesConfigurer extends PropertySourcesPlaceholderConfigurer
    implements EnvironmentAware, InitializingBean, Log {

    /**
     * The <code>ConfigurationSource</code> is enumeration of the supported configuration resource loaders.
     *
     * @author  Andrey OCHIROV
     * @version 1.0
     */
    private static enum ConfigurationSource {

        YAML(new YamlPropertySourceLoader()), PROPERTIES(new PropertiesPropertySourceLoader());

        private final PropertySourceLoader loader;

        /**
         * Creates an instance of class <code>ConfigurationSource</code>.
         */
        private ConfigurationSource(final PropertySourceLoader loader) {
            this.loader = loader;
        }

        /**
         * Loads specified resources and returns list of {@link PropertySource} classes.
         *
         * @param   resource the resource to load.
         * @return  the list of {@link PropertySource sources}.
         * @throws  IOException throws an error in case of input/output exception.
         */
        public List<PropertySource<?>> of(final Resource resource) {
            try {
                return this.loader.load(resource.getFilename(), resource);
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

    /**
     * The name of the property, which contains list of external configuration files.
     * <pre>
     * spring:
     *   application:
     *     properties:
     *       location: classpath:components.yml, classpath:services.yml
     * </pre>
     */
    private static final String PARAM_PROPERTIES = "spring.application.properties.location";

    /**
     * The name of the property, which contains list of extensions for each supported configuration type.
     * <pre>
     * spring:
     *   application:
     *     properties:
     *       types: # Default mapping for file extensions
     *         yaml: yml, yaml, conf
     *         properties: properties, props, xml
     * </pre>
     */
    private static final String PARAM_PROPERTY_TYPES = "spring.application.properties.types";

    /**
     * The map with known configuration file types and responsible loaders. Could be extended or modified via
     * application configuration.
     */
    private final Map<String, ConfigurationSource> loaders;

    /**
     * The application environment. This environment used as placeholder for all loaded properties.
     */
    private Environment environment;

    /**
     * The array of all configuration files locations. Based on the main application's configuration.
     */
    private String[] locations;

    /**
     * The resource loader to load specified configuration files.
     */
    @Inject
    private ResourceLoader resourceLoader;

    /**
     * The list on known configuration
     */
    private final Set<String> knownFiles;

    /**
     * An array with known profiles, used during application startup.
     */
    private String[] profiles;

    /**
     * Creates an instance of class <code>ApplicationConfigurer</code>.
     */
    public PropertiesConfigurer() {
        this.knownFiles = new TreeSet<>();
        this.loaders = new HashMap<>();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // copy property sources to environment
        final MutablePropertySources envPropSources = ((ConfigurableEnvironment)this.environment).getPropertySources();

        Utils.nanoSeconds("application.configuration", this, () ->
            envPropSources.forEach(propertySource -> {
                if (propertySource.containsProperty(PARAM_PROPERTIES)) {
                    this.locations = ((String)propertySource.getProperty(PARAM_PROPERTIES)).split("[, ]+");

                    stream(this.locations).forEach(filename ->
                        loadProperties(filename.trim()).forEach(source -> envPropSources.addFirst(source)));
                }
            }));
    }

    /* (non-Javadoc)
     * @see org.springframework.context.support.PropertySourcesPlaceholderConfigurer#setEnvironment(org.springframework.core.env.Environment)
     */
    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment; // save off environment for later use

        // get the list of active profiles
        this.profiles = environment.getActiveProfiles();

        log().info("Initializing configuration for next active profiles %s",
            Arrays.toString(this.profiles));

        // let's populate mapping configuration from environment
        for (final ConfigurationSource source : ConfigurationSource.values()) {
            // lookup for available extensions for current configuration source
            final String exts = environment.getProperty(PARAM_PROPERTY_TYPES + "." + source.name().toLowerCase(),
                source.name().toLowerCase());

            // get specified extension one by one and assign to responsible loader in the mapping
            for (final String ext : exts.split("[, ]+")) {
                this.loaders.putIfAbsent(ext, source);
            }
        }
        log().info("Initializing configuration with next mappings of file types: %s", this.loaders);

        super.setEnvironment(environment);
    }

    /**
     * Loads properties from the given configuration file (YAML or java properties).
     *
     * @param   filename the name of the configuration file to load.
     * @return  the list of properties from specified configuration file.
     */
    private List<PropertySource<?>> loadProperties(final String filename) {
        if (this.knownFiles.contains(filename)) {
            return Collections.emptyList();
        }

        try {
            // getting extension from file name
            final String extension = // if filename ends with '.' lets assume we work with yaml
                (filename.endsWith(".") ? "yaml" : filename.substring(filename.lastIndexOf('.') + 1).toLowerCase())
                    .trim();

            final Set<String> names = new LinkedHashSet<>(); // we should real order of profiles

            names.add(filename);

            // get all profiles and add file names for listed profiles
            for (final String profile : this.profiles) {
                names.add(filename.substring(0, filename.length() - extension.length() - 1)
                    + "-"
                    + profile
                    + "."
                    + extension);
            }

            log().info("Looking for next configuration files %s...", names);

            final List<PropertySource<?>> properties = new LinkedList<>();
            final ConfigurationSource source = this.loaders.get(extension);

            for (final String name : names) {
                final Resource[] possiblePropertiesResources =
                    ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader).getResources(name);

                properties.addAll(stream(possiblePropertiesResources)
                    .filter(Resource::exists)
                    .map(r -> source.of(r))
                    .flatMap(l -> l.stream())
                    .collect(Collectors.toList()));
            }

            return properties;
        } catch (final Exception e) {
            log().error("An exception '%s' occurred while trying to initialize application's "
                + "configuration by specified configuration file '%s'", e, e.getMessage(), filename);

             return Collections.emptyList();
        } finally {
            this.knownFiles.add(filename);
        }
    }

}
```
