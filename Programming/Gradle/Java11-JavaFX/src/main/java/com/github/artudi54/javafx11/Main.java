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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Objects;


@SpringBootApplication
public class Main extends Application {
    @Nullable
    private ConfigurableApplicationContext springContext;
    @Nullable
    private ApplicationConfiguration configuration;
    @Nullable
    private Exception creationException;
    
    public static void main(@NotNull String[] args) {
        launch(args);
    }
    
    @Override
    public void init() {
        try {
            springContext = SpringApplication.run(Main.class);
            configuration = springContext.getBean(ApplicationConfiguration.class);
        } catch (Exception exc) {
            exc.printStackTrace();
            creationException = exc;
        }
    }
    
    @Override
    public void start(@NotNull Stage primaryStage) throws Exception {
        if (creationException != null) {
            showErrorAndQuit();
        }
        else {
            showWindow(primaryStage);
        }
    }

    private void showErrorAndQuit() {
        String fullMessage = "Critical error occurred. Program will now exit";
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Critical error");
        alert.setHeaderText("Critical application error occurred");
        alert.setContentText(fullMessage);
        alert.showAndWait();
        
        Platform.exit();
        System.exit(1);
    }

    private void showWindow(@NotNull Stage primaryStage) throws Exception {
        Objects.requireNonNull(springContext);
        Objects.requireNonNull(configuration);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        
        Parent rootNode = fxmlLoader.load();
        Scene scene = new Scene(rootNode);

        primaryStage.setScene(scene);
        configuration.getWindowParameters().bindWithStage(primaryStage);
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
