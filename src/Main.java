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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/view/MainMenu.fxml"));
        StackPane root = loader.load();
        MainMenuController controller = loader.getController();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Silicon Valley: The Tech Cartel");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}