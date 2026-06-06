import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("ui/view/GameBoard.fxml")));

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Game Board");
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);

        primaryStage.show();
    }
}