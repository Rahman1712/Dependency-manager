package com.ar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/com/ar/fxmlcontroller/FXML.fxml"));
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("dependency.png"));
        stage.setTitle("Dependency Manager");
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}