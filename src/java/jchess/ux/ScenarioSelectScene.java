package jchess.ux;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import jchess.game.Scenario;

import java.io.File;
import java.util.ArrayList;

public class ScenarioSelectScene extends Scene {
    static String scenarioPath = "/Users/andrei/IdeaProjects/JChess/src/resources/scenarios";
    static String scenarioExtension = ".json";
    static ArrayList<String> scenarioList = new ArrayList<>();
    
    static Group root;
    
    public ScenarioSelectScene(Parent parent, Theme theme) {
        super(parent, Controller.W, Controller.H);
        makeScenarioList();
    
        // initializing the menu:
        root = (Group) parent;
        VBox vBox = new VBox(8);
        root.getChildren().add(vBox);
        vBox.setLayoutX(Controller.W / 2.0);
        vBox.setLayoutY(Controller.H / 2.0);
        
        // drawing the scenario load buttons:
        for (String scenario : scenarioList) {
            Button b = new Button(scenario);
            b.setOnAction((e) -> loadScenario(scenario));
            vBox.getChildren().add(b);
        }
    
        // drawing the background
        this.setFill(theme.background_colour);
    }
    
    private void loadScenario(String scenario) {
        String path = scenarioPath + "/" + scenario;
        if (!Scenario.isValidScenario(path)) {
            System.out.println("Invalid scenario passed to ScenarioSelectScreen.loadScenario()");
            System.out.println(" ~ scenario passed: \"" + path + "\"");
            return;
        }
        Controller.changeScene(GUIStates.GAME_BOARD, scenario);
    }
    
    private void makeScenarioList() {
        // from: https://stackoverflow.com/questions/5751335/using-file-listfiles-with-filenameextensionfilter
        File dir = new File(scenarioPath);
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(scenarioExtension));
        if (files == null) return;
        scenarioList = new ArrayList<>();
        for (File file : files) {
            scenarioList.add(file.getName().replaceFirst(".json", ""));
        }
    }
}
