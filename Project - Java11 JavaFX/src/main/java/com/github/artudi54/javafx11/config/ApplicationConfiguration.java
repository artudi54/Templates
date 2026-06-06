package com.github.artudi54.javafx11.config;

import com.github.artudi54.javafx11.config.cache.WindowParameters;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ApplicationConfiguration {
    @NotNull
    private final Path configurationDirectory;
    @NotNull
    private final Path cacheDirectory;
    
    @NotNull
    private final Path configurationPath;
    @NotNull
    private final Path cachePath;
    
    @NotNull
    private final WindowParameters windowParameters;
    
    
    @Autowired
    public ApplicationConfiguration(@NotNull Environment environment) throws ApplicationConfigurationException {
        String applicationName = environment.getRequiredProperty("application.name");
        String configurationFile = environment.getRequiredProperty("application.config");
        String cacheFile = environment.getRequiredProperty("application.cache");
        
        AppDirs appDirs = AppDirsFactory.getInstance();
        configurationDirectory = Path.of(appDirs.getUserConfigDir(applicationName, "", ""));
        cacheDirectory = Path.of(appDirs.getUserCacheDir(applicationName, "", ""));
        configurationPath = configurationDirectory.resolve(configurationFile);
        cachePath = cacheDirectory.resolve(cacheFile);
        
        windowParameters = new WindowParameters(
            environment.getRequiredProperty("application.window.title"),
            environment.getRequiredProperty("application.window.defaultWidth", Double.class),
            environment.getRequiredProperty("application.window.defaultHeight", Double.class)
        );
        
        createFiles();
        reloadSettings();
    }
    
    private void createFiles() throws ApplicationConfigurationException {
        try {
            if (!Files.exists(configurationDirectory))
                Files.createDirectories(configurationDirectory);
            if (!Files.exists(configurationPath))
                Files.createFile(configurationPath);
        }
        catch (IOException exc) {
            throw new ApplicationConfigurationException(ApplicationConfigurationException.ErrorType.CREATE, configurationPath, exc);
        }
        try {
            if (!Files.exists(cacheDirectory))
                Files.createDirectories(cacheDirectory);
            if (!Files.exists(cachePath))
                Files.createFile(cachePath);
        }
        catch (IOException exc) {
            throw new ApplicationConfigurationException(ApplicationConfigurationException.ErrorType.CREATE, cachePath, exc);
        }
    }
    
    
    public void reloadSettings() throws ApplicationConfigurationException {
        Configurations configs = new Configurations();
        try {
            Configuration configuration = configs.properties(cachePath.toFile());
            windowParameters.setPositionX(configuration.getDouble("windowParameters.positionX", windowParameters.getPositionX()));
            windowParameters.setPositionY(configuration.getDouble("windowParameters.positionY", windowParameters.getPositionY()));
            windowParameters.setWidth(configuration.getDouble("windowParameters.width", windowParameters.getWidth()));
            windowParameters.setHeight(configuration.getDouble("windowParameters.height", windowParameters.getHeight()));
            windowParameters.setMaximized(configuration.getBoolean("windowParameters.maximized", windowParameters.isMaximized()));
            windowParameters.setFullScreen(configuration.getBoolean("windowParameters.fullScreen", windowParameters.isFullScreen()));
        }
        catch (ConfigurationException exc) {
            throw new ApplicationConfigurationException(ApplicationConfigurationException.ErrorType.READ, cachePath, exc);
        }

    }
    
    public void saveSettings() throws ApplicationConfigurationException {
        Configurations configs = new Configurations();
        try {
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder(cachePath.toFile());
            Configuration configuration = builder.getConfiguration();
            configuration.setProperty("windowParameters.positionX", windowParameters.getPositionX());
            configuration.setProperty("windowParameters.positionY", windowParameters.getPositionY());
            configuration.setProperty("windowParameters.width", windowParameters.getWidth());
            configuration.setProperty("windowParameters.height", windowParameters.getHeight());
            configuration.setProperty("windowParameters.maximized", windowParameters.isMaximized());
            configuration.setProperty("windowParameters.fullScreen", windowParameters.isFullScreen());
            builder.save();
        }
        catch (ConfigurationException exc) {
            throw new ApplicationConfigurationException(ApplicationConfigurationException.ErrorType.WRITE, cachePath, exc);
        }
    }

    
    @NotNull
    public Path getConfigurationPath() {
        return configurationPath;
    }
    
    @NotNull
    public Path getCachePath() {
        return cachePath;
    }
    
    
    @Bean
    @NotNull
    public WindowParameters getWindowParameters() {
        return windowParameters;
    }
}
