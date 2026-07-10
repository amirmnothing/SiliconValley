import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import ui.controller.GameBoardController;
import ui.controller.MainMenuController;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/view/MainMenu.fxml"));
//        StackPane root = loader.load();
//        MainMenuController controller = loader.getController();
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setTitle("Silicon Valley: The Tech Cartel");
//        primaryStage.setResizable(false);
//        primaryStage.show();

//
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/view/GameBoard.fxml"));
        BorderPane root = loader.load();

        GameBoardController controller = loader.getController();

        logic.engine.Map gameMap = new logic.engine.Map(5, 5);
        java.util.List<logic.models.Player> players = new java.util.ArrayList<>();

        players.add(new logic.models.TechGuruPlayer("Player 1",new java.util.ArrayList<>()));
//        players.add(new logic.models.HackerCEOPlayer("Player 2",new java.util.ArrayList<>()));
//        players.add(new logic.models.VCFundedPlayer("Player 3",new java.util.ArrayList<>()));
//        players.add(new logic.models.Player("Player 4",new java.util.ArrayList<>()));
        players.add(new logic.models.AIHackerCEOPlayer(2,controller));
        players.add(new logic.models.AITechGuruPlayer(2,controller));
        players.add(new logic.models.AIVCFundedPlayer(2,controller));


        logic.engine.GameEngine gameEngine = new logic.engine.GameEngine(gameMap, players);
        gameEngine.startSetupPhase();

        // پاس دادن موتور بازی به کنترلر
        controller.setGameEngine(gameEngine);
        controller.initialize(gameEngine);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Silicon Valley: The Tech Cartel");
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);

        primaryStage.show();
    }
}