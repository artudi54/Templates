package com.github.artudi54.javafx11.config;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public class ApplicationConfigurationException extends IOException {
    public enum ErrorType {
        CREATE,
        READ,
        WRITE
    }
    
    public ApplicationConfigurationException(@NotNull ErrorType errorType, @NotNull Path path) {
        super(makeErrorMessage(errorType) + " '" + path.toString() + "'");
    }

    public ApplicationConfigurationException(@NotNull ErrorType errorType, @NotNull Path path, @NotNull Throwable cause) {
        super(makeErrorMessage(errorType) + " '" + path.toString() + "'", cause);
    }
    
    private static String makeErrorMessage(@NotNull ErrorType errorType) {
        String message;
        switch (errorType) {
            case CREATE:
                message = "Failed to create configuration file";
                break;
            case READ:
                message = "Failed to read configuration from file";
                break;
            case WRITE:
                message = "Failed to write configuration to file";
                break;
            default:
                throw new IllegalArgumentException("Unknown ErrorType for ConfigurationException");
        }
        return message;
    }
}
