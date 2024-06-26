package jchess.ux;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Controller extends Application {
    public static int W = 1000;
    public static int H = 1000;
    public static Theme globalTheme;
    private static Stage stage;

    @Override
    public void start(Stage stage) {
        // initializing global things
        Controller.stage = stage;
        globalTheme = new Theme();

        // setting the scene to main menu
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
            case MAIN_MENU -> scene = new MainMenuScene(root, globalTheme);
            case GAME_BOARD -> scene = new GameScene(root, globalTheme, option);
            case SCENARIO_SELECT -> scene = new ScenarioSelectScene(root, globalTheme);
            default -> {
                System.out.println("[!!!] This should not have happened in " +
                        "Controller.changeScene(" + newScene + option + ")!!");
                return;
            }
        }
        // adding a resize listener to the new scene
        scene.widthProperty().addListener(
                (observableValue, oldSceneWidth, newSceneWidth) -> {
                    Controller.W = newSceneWidth.intValue();
                }
        );
        scene.heightProperty().addListener(
                (observableValue, oldSceneHeight, newSceneHeight) -> {
                    Controller.H = newSceneHeight.intValue();
                }
        );
        stage.setScene(scene);
        stage.show();
    }
    
    public static void setPopup(Popup popup) {
        popup.show(stage);
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