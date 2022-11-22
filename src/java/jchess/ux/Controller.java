package jchess.ux;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller extends Application {
    public static final int W = 1000;
    public static final int H = 1000;
    private static Stage stage;

    @Override
    public void start(Stage stage) {
        Controller.stage = stage;
        changeScene(GUIStates.MAIN_MENU);

        // closing the application
        stage.setOnCloseRequest(event -> {
            try {
                stop();
            } catch (Exception e) {
                System.out.println("\n\nCouldn't close application:");
                e.printStackTrace();
            }
        });
    }

    static void changeScene(GUIStates newScene) {
        changeScene(newScene, "");

    }

    static void changeScene(GUIStates newScene, String option) {
        Group root = new Group();
        Scene scene;
        switch (newScene) {
            case MAIN_MENU -> scene = new MainMenuScene(root);
            case GAME_BOARD -> scene = new GameScene(root, option);
            default -> {
                System.out.println("[!!!] Something bad in Controller.changeScene(" +
                        newScene +
                        option +
                        ")!!");
                return;
            }
        }
        stage.setScene(scene);
        stage.show();
    }

    static void setTitle(String title) {
        stage.setTitle(title);
    }

    static void quitApplication() {
        stage.close();
    }

    public static void main(String[] args) {
        launch();
    }
}