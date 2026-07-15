package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.engine.GameEngine;
import logic.enums.ResourceType;
import logic.models.Player;
import logic.sound.SFXManager;

import java.util.Map;

public class IncomingTradeController {
    GameEngine gameEngine;
    Player[] players;
    Map<ResourceType, Integer> giveResources;
    Map<ResourceType, Integer> getResources;
    GameBoardController gameBoardController;

    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

    @FXML
    private Label recCapital;

    @FXML
    private Label recCloud;

    @FXML
    private Label recData;

    @FXML
    private Label recTalent;

    @FXML
    private Label recPatent;

    @FXML
    private Label givCapital;

    @FXML
    private Label givCloud;

    @FXML
    private Label givData;

    @FXML
    private Label givTalent;

    @FXML
    private Label givPatent;

    @FXML
    private Label sender;

    @FXML
    private Label receiver;


    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }


    @FXML
    void ChangeAcceptButtonColorToChoose(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        String rgbColor = "rgb(0,100,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeAcceptButtonColorToNotChoose(MouseEvent event) {
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeRejectButtonColorToChoose(MouseEvent event) {
        SFXManager.play("MouseEnter.mp3");
        String rgbColor = "rgb(120,0,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeRejectButtonColorToNotChoose(MouseEvent event) {

        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void onAcceptButton(ActionEvent event) {
        gameEngine.trade(getResources, giveResources, players[0], players[1]);
        gameBoardController.updateResourceFields();

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onRejectButton(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    public void setIncomingTradeLabels() {
        updateResourceFields(getResources, recCapital, recCloud, recData, recTalent, recPatent);
        updateResourceFields(giveResources, givCapital, givCloud, givData, givTalent, givPatent);
        sender.setText(players[0].getPlayerName());
        receiver.setText(players[1].getPlayerName());
    }


    private void updateResourceFields(java.util.Map<ResourceType, Integer> resources, Label capitalFld, Label cloudFld, Label dataFld, Label talentFld, Label patentFld) {

        capitalFld.setText(String.valueOf(resources.getOrDefault(ResourceType.CAPITAL, 0)));
        cloudFld.setText(String.valueOf(resources.getOrDefault(ResourceType.CLOUD, 0)));
        dataFld.setText(String.valueOf(resources.getOrDefault(ResourceType.DATA, 0)));
        talentFld.setText(String.valueOf(resources.getOrDefault(ResourceType.TALENT, 0)));
        patentFld.setText(String.valueOf(resources.getOrDefault(ResourceType.PATENT, 0)));
    }

    public void setGiveResources(Map<ResourceType, Integer> giveResources) {
        this.giveResources = giveResources;
    }

    public void setGetResources(Map<ResourceType, Integer> getResources) {
        this.getResources = getResources;
    }
}
