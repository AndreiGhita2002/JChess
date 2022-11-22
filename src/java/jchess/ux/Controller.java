package jchess.ux;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class Controller extends Application {
    public static final int W = 1000;
    public static final int H = 1000;

    static Group root;
    static GameScene gameScene;

    @Override
    public void start(Stage stage) {
        // initializing the Controller
        root = new Group();
        gameScene = new GameScene(root);
        stage.setTitle("quirky chess");
        stage.setScene(gameScene);
        stage.show();

        // closing the application
        stage.setOnCloseRequest(event -> {
            try {
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}