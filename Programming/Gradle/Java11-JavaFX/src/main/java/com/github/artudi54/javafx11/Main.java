package com.github.artudi54.javafx11;

import com.github.artudi54.javafx11.config.ApplicationConfiguration;
import com.github.artudi54.javafx11.config.ApplicationConfigurationException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Main extends Application {
    private ConfigurableApplicationContext springContext;
    private Environment environment;
    private FXMLLoader fxmlLoader;
    private ApplicationConfiguration configuration;
    private String initializeError;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void init() {
        try {
            springContext = SpringApplication.run(Main.class);
            environment = springContext.getEnvironment();
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(springContext::getBean);
            configuration = springContext.getBean(ApplicationConfiguration.class);
        }
        catch (BeanCreationException exc) {
            Throwable rootCause = exc.getRootCause();
            if (rootCause != null)
                initializeError = rootCause.getMessage();
            else
                initializeError = "Critical error occurred";
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        if (initializeError != null)
            showErrorAndQuit(initializeError);
        else
            showWindow(primaryStage);
    }

    private void showErrorAndQuit(String message) {
        String fullMessage = message + ". Program will now exit";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Critical error");
        alert.setHeaderText("Critical application error occurred");
        alert.setContentText(fullMessage);
        alert.showAndWait();
        Platform.exit();
        System.exit(1);
    }

    private void showWindow(Stage primaryStage) throws Exception {
        fxmlLoader.setLocation(getClass().getResource("/fxml/main.fxml"));
        Parent rootNode = fxmlLoader.load();
        Scene scene = new Scene(rootNode);

        primaryStage.setTitle(environment.getProperty("application.descriptiveName"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws ApplicationConfigurationException {
        if (springContext != null)
            springContext.stop();
        if (configuration != null) {
            configuration.saveSettings();
        }
    }
}
