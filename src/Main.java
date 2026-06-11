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

// ۳. ساخت نقشه و لیست بازیکنان (برای نمونه)
        logic.engine.Map gameMap = new logic.engine.Map(5, 5); // ابعاد فرضی متناسب با پروژه‌تان
        java.util.List<logic.models.Player> players = new java.util.ArrayList<>();

        // اضافه کردن بازیکن‌ها (مثلاً یک تک‌گورو و یک هکر سی‌ای‌او)
        players.add(new logic.models.TechGuruPlayer(new java.util.ArrayList<>()));
        players.add(new logic.models.HackerCEOPlayer(new java.util.ArrayList<>()));

        // ۴. نیو کردن انجین بازی
        logic.engine.GameEngine gameEngine = new logic.engine.GameEngine(gameMap, players);

        // ۵. پاس دادن نمونه انجین به کنترلر تا دکمه تاس و سایر اجزا کار کنند
        controller.setGameEngine(gameEngine);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Silicon Valley: The Tech Cartel");
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);

        primaryStage.show();
    }
}