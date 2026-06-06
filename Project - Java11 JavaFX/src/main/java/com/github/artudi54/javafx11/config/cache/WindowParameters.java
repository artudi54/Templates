package com.github.artudi54.javafx11.config.cache;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class WindowParameters {
    @NotNull
    private final String title;
    private double positionX;
    private double positionY;
    private double width;
    private double height;
    private boolean maximized;
    private boolean fullScreen;
    
    
    public WindowParameters(@NotNull String title, double defaultWidth, double defaultHeight) {
        this.title = title;
        this.positionX = -1;
        this.positionY = -1;
        this.width = defaultWidth;
        this.height = defaultHeight;
        this.maximized = false;
        this.fullScreen = false;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }
    

    public void bindWithStage(@NotNull Stage stage) {
        stage.setTitle(title);
        if (!isMaximized() && !isFullScreen()) {
            if (positionX != -1) {
                stage.setX(positionX);
            }
            if (positionY != -1) {
                stage.setY(positionY);
            }
            stage.setWidth(width);
            stage.setHeight(height);
        }
        stage.setMaximized(maximized);
        stage.setFullScreen(fullScreen);
        
        stage.setOnCloseRequest(event -> {
            setPositionX(stage.getX());
            setPositionY(stage.getY());
            setWidth(stage.getWidth());
            setHeight(stage.getHeight());
            setMaximized(stage.isMaximized());
            setFullScreen(stage.isFullScreen());
        });
    }
}
