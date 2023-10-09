package jchess.ux;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenuScene extends Scene {
    static Group root;

    public MainMenuScene(Parent parent, Theme theme) {
        super(parent, Controller.H, Controller.W);
        Controller.setTitle("JChess - Main Menu");

        // initializing the menu:
        root = (Group) parent;
        VBox vBox = new VBox(8);
        root.getChildren().add(vBox);
        vBox.setLayoutX(Controller.W / 2.0);
        vBox.setLayoutY(Controller.H / 2.0);

        // Play Classic Button
        Button playClassicButton = new Button("Play Classic");
        playClassicButton.setOnAction((e) -> Controller.changeScene(GUIStates.GAME_BOARD, "classic"));
        vBox.getChildren().add(playClassicButton);
        
        // Pre-made Scenarios Button
        Button preMadeButton = new Button("Load Scenario");
        preMadeButton.setOnAction((e) -> Controller.changeScene(GUIStates.SCENARIO_SELECT));
        vBox.getChildren().add(preMadeButton);
        
        // Import Scenario Button
        //TODO import scenarios feature
        Button importButton = new Button("Import Button");
        importButton.setOnAction((e) -> System.out.println("!! import button was pressed"));
        vBox.getChildren().add(importButton);

        // Quit Button
        Button quitButton = new Button("Quit");
        quitButton.setOnAction((e) -> Controller.quitApplication());
        vBox.getChildren().add(quitButton);

        // drawing the background
        this.setFill(theme.background_colour);
    }
}
