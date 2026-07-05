package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.engine.GameEngine;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Label Player1Label;

    @FXML
    private Label Player2Label;

    @FXML
    private Label Player3Label;

    @FXML
    private Label Player4Label;

    @FXML
    private Group GameLobby;

    @FXML
    private Group MainMenu;

    @FXML
    public void initialize(){
        Player1Label.setTextFill(Color.web(GameEngine.PLAYER1COLOR));
        Player2Label.setTextFill(Color.web(GameEngine.PLAYER2COLOR));
        Player3Label.setTextFill(Color.web(GameEngine.PLAYER3COLOR));
        Player4Label.setTextFill(Color.web(GameEngine.PLAYER4COLOR));
    }

    @FXML
    void OnStartANewGame(ActionEvent event){
        GameLobby.setOpacity(1);
        GameLobby.setMouseTransparent(false);
        MainMenu.setOpacity(0);
        MainMenu.setMouseTransparent(true);
    }

    @FXML
    void ButtonsMouseEnter(MouseEvent event){
        ((Button) (event.getSource())).setStyle("-fx-background-color: black; -fx-border-color: yellow ;-fx-border-width: 5;");
    }

    @FXML
    void ButtonsMouseExit(MouseEvent event){
        ((Button) (event.getSource())).setStyle("-fx-background-color: black; -fx-border-color: white ;-fx-border-width: 5;");
    }

    @FXML
    void onExitButton(ActionEvent event){
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void RoleMouseEnter(MouseEvent event){
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        lighting.setSpecularConstant(0.25);
        lighting.setSpecularExponent(0);
        lighting.setSurfaceScale(0);

        ((ImageView) event.getSource()).setEffect(lighting);
    }

    @FXML
    void ChangeRoleToChoose(MouseEvent event){
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        lighting.setSpecularConstant(0.4);
        lighting.setSpecularExponent(0);
        lighting.setSurfaceScale(0);

        ((ImageView) event.getSource()).setEffect(lighting);
    }

    @FXML
    void ResetRole(MouseEvent event){
        ((ImageView) event.getSource()).setEffect(null);
    }

    @FXML
    void NextPlayerButtonMouseEnter(MouseEvent event){
        String rgbColor = "rgb(0,0,225)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2;");
    }

    @FXML
    void NextPlayerButtonMouseExit(MouseEvent event){
        ((Button) (event.getSource())).setStyle("-fx-background-color: black ;-fx-border-color: white ;-fx-border-width: 2;");
    }

    @FXML
    void ResetAllButtonMouseEnter(MouseEvent event){
        String rgbColor = "rgb(220,0,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2;");
    }

    @FXML
    void ResetAllButtonMouseExit(MouseEvent event){
        ((Button) (event.getSource())).setStyle("-fx-background-color: black ;-fx-border-color: white ;-fx-border-width: 2;");
    }

    @FXML
    void StartButtonMouseEnter(MouseEvent event){
        String rgbColor = "rgb(0,110,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2;");
    }

    @FXML
    void StartButtonMouseExit(MouseEvent event){
        ((Button) (event.getSource())).setStyle("-fx-background-color: black ;-fx-border-color: white ;-fx-border-width: 2;");
    }

    @FXML
    void onStartGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/GameBoard.fxml"));
        BorderPane root = loader.load();

        GameBoardController controller = loader.getController();

        // Todo: Start game based on entered settings

        logic.engine.Map gameMap = new logic.engine.Map(5, 5);
        java.util.List<logic.models.Player> players = new java.util.ArrayList<>();

        players.add(new logic.models.TechGuruPlayer("Player 1",new java.util.ArrayList<>()));
        players.add(new logic.models.HackerCEOPlayer("Player 2",new java.util.ArrayList<>()));
        players.add(new logic.models.VCFundedPlayer("Player 3",new java.util.ArrayList<>()));
        players.add(new logic.models.Player("Player 4",new java.util.ArrayList<>()));


        logic.engine.GameEngine gameEngine = new logic.engine.GameEngine(gameMap, players);
        gameEngine.startSetupPhase();

        // پاس دادن موتور بازی به کنترلر
        controller.setGameEngine(gameEngine);
        controller.initialize(gameEngine);

        Stage gameBoardStage = new Stage();
        gameBoardStage.setScene(new Scene(root));
        gameBoardStage.setTitle("Silicon Valley: The Tech Cartel");
        gameBoardStage.setResizable(false);

        gameBoardStage.show();
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
