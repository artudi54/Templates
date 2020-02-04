package com.github.artudi54.javafx11.config;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ApplicationConfiguration {
    private Path configurationDirectory;
    private Path configurationPath;
    
    public ApplicationConfiguration(
        @Value("${application.name}") String applicationName,
        @Value("${application.config}") String configurationFile) throws ApplicationConfigurationException {
        AppDirs appDirs = AppDirsFactory.getInstance();
        configurationDirectory = Path.of(appDirs.getUserConfigDir(applicationName, "", ""));
        configurationPath = configurationDirectory.resolve(configurationFile);
        createConfigurationFile();
        reloadSettings();
    }
    
    private void createConfigurationFile() throws ApplicationConfigurationException {
        try {
            if (!Files.exists(configurationDirectory))
                Files.createDirectories(configurationDirectory);
            if (!Files.exists(configurationPath))
                Files.createFile(configurationPath);
        }
        catch (IOException exc) {
            throw new ApplicationConfigurationException(ApplicationConfigurationException.ErrorType.CREATE, configurationPath);
        }
    }
    
    public void reloadSettings() throws ApplicationConfigurationException {
    }
    
    public void saveSettings() throws ApplicationConfigurationException {
    }

    public Path getConfigurationPath() {
        return configurationPath;
    }
}
