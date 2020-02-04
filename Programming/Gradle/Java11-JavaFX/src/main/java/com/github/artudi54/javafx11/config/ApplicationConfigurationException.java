package com.github.artudi54.javafx11.config;

import java.nio.file.Path;

public class ApplicationConfigurationException extends Exception {
    public enum ErrorType {
        CREATE,
        READ,
        WRITE
    }
    
    public ApplicationConfigurationException(ErrorType errorType, Path path) {
        super(makeErrorMessage(errorType) + " '" + path.toString() + "'");
    }
    
    private static String makeErrorMessage(ErrorType errorType) {
        String message;
        switch (errorType) {
            case CREATE:
                message = "Failed to create configuration file";
                break;
            case READ:
                message = "Failed to read configuration from file";
            case WRITE:
                message = "Failed to write configuration to file";
            default:
                throw new IllegalArgumentException("Unknown ErrorType for ConfigurationException");
        }
        return message;
    }
}
