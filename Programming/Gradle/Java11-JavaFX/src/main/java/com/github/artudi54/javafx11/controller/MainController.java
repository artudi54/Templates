package com.github.artudi54.javafx11.controller;

import com.github.artudi54.javafx11.config.ApplicationConfiguration;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    @Autowired
    ApplicationConfiguration configuration;
    
    @FXML
    private void initialize() {
    }
}
