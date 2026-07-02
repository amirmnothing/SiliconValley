import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logic.engine.GameEngine;
import ui.controller.GameBoardController;

import logic.engine.GameEngine;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/view/GameBoard.fxml"));
        BorderPane root = loader.load();

        GameBoardController controller = loader.getController();

        logic.engine.Map gameMap = new logic.engine.Map(5, 5);
        java.util.List<logic.models.Player> players = new java.util.ArrayList<>();

        players.add(new logic.models.TechGuruPlayer(new java.util.ArrayList<>()));
        players.add(new logic.models.HackerCEOPlayer(new java.util.ArrayList<>()));
        players.add(new logic.models.VCFundedPlayer(new java.util.ArrayList<>()));
        players.add(new logic.models.Player(new java.util.ArrayList<>()));


        logic.engine.GameEngine gameEngine = new logic.engine.GameEngine(gameMap, players);
        gameEngine.startSetupPhase();

        // پاس دادن موتور بازی به کنترلر
        controller.setGameEngine(gameEngine);
        controller.initialize(gameEngine);
        controller.resetLabel(gameEngine);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Silicon Valley: The Tech Cartel");
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);

        primaryStage.show();
    }
}