package com.example.nisttestapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("test.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 680);
        stage.setTitle("Криптографиялық генераторлардың тиімділігін зерттеуге арналған бағдарлама");
        stage.setScene(scene);
        stage.getIcons().add(new Image("/icon2.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}