package ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import logic.engine.GameEngine;
import logic.enums.ResourceType;
import logic.models.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TradeRequestController {
    GameBoardController gameBoardController;

    private int toGiveTalentCount = 0;
    private int toGiveCloudCount = 0;
    private int toGiveDataCount = 0;
    private int toGiveCapitalCount = 0;
    private int toGivePatentCount = 0;

    private int toGetTalentCount = 0;
    private int toGetCloudCount = 0;
    private int toGetDataCount = 0;
    private int toGetCapitalCount = 0;
    private int toGetPatentCount = 0;
    GameEngine gameEngine;
    Player[] players;


    @FXML
    private AnchorPane mainRootPane;

    @FXML
    private Label CapitalGetLabel;
    @FXML
    private Label CapitalGiveLabel;
    @FXML
    private Label CloudGetLabel;
    @FXML
    private Label CloudGiveLabel;
    @FXML
    private Label DataGetLabel;
    @FXML
    private Label DataGiveLabel;
    @FXML
    private Label PatentGetLabel;
    @FXML
    private Label PatentGiveLabel;
    @FXML
    private Label TalentGetLabel;
    @FXML
    private Label TalentGiveLabel;

    public void setGameBoardController(GameBoardController gameBoardController) {
        this.gameBoardController = gameBoardController;
    }

    @FXML
    void onGiveTalentPlus(MouseEvent event) {
        if (toGiveTalentCount < players[1].getResources(ResourceType.TALENT)) {
            toGiveTalentCount++;
        }
        TalentGiveLabel.setText(String.valueOf(toGiveTalentCount));
    }

    @FXML
    void onGiveTalentMinus(MouseEvent event) {
        if (toGiveTalentCount > 0) {
            toGiveTalentCount--;
            TalentGiveLabel.setText(String.valueOf(toGiveTalentCount));
        }
    }

    @FXML
    void onGiveCloudPlus(MouseEvent event) {
        if (toGiveCloudCount < players[1].getResources(ResourceType.CLOUD)) {
            toGiveCloudCount++;
        }
        CloudGiveLabel.setText(String.valueOf(toGiveCloudCount));
    }

    @FXML
    void onGiveCloudMinus(MouseEvent event) {
        if (toGiveCloudCount > 0) {
            toGiveCloudCount--;
            CloudGiveLabel.setText(String.valueOf(toGiveCloudCount));
        }
    }

    @FXML
    void onGiveDataPlus(MouseEvent event) {
        if (toGiveDataCount < players[1].getResources(ResourceType.DATA)) {
            toGiveDataCount++;
        }
        DataGiveLabel.setText(String.valueOf(toGiveDataCount));
    }

    @FXML
    void onGiveDataMinus(MouseEvent event) {
        if (toGiveDataCount > 0) {
            toGiveDataCount--;
            DataGiveLabel.setText(String.valueOf(toGiveDataCount));
        }
    }

    @FXML
    void onGiveCapitalPlus(MouseEvent event) {
        if (toGiveCapitalCount < players[1].getResources(ResourceType.CAPITAL)) {
            toGiveCapitalCount++;
        }
        CapitalGiveLabel.setText(String.valueOf(toGiveCapitalCount));
    }

    @FXML
    void onGiveCapitalMinus(MouseEvent event) {
        if (toGiveCapitalCount > 0) {
            toGiveCapitalCount--;
            CapitalGiveLabel.setText(String.valueOf(toGiveCapitalCount));
        }
    }

    @FXML
    void onGivePatentPlus(MouseEvent event) {
        if (toGivePatentCount < players[1].getResources(ResourceType.PATENT)) {
            toGivePatentCount++;
        }
        PatentGiveLabel.setText(String.valueOf(toGivePatentCount));
    }

    @FXML
    void onGivePatentMinus(MouseEvent event) {
        if (toGivePatentCount > 0) {
            toGivePatentCount--;
            PatentGiveLabel.setText(String.valueOf(toGivePatentCount));
        }
    }

    @FXML
    void onGetTalentPlus(MouseEvent event) {
        if (toGetTalentCount < players[0].getResources(ResourceType.TALENT)) {
            toGetTalentCount++;
        }
        TalentGetLabel.setText(String.valueOf(toGetTalentCount));
    }

    @FXML
    void onGetTalentMinus(MouseEvent event) {
        if (toGetTalentCount > 0) {
            toGetTalentCount--;
            TalentGetLabel.setText(String.valueOf(toGetTalentCount));
        }
    }

    @FXML
    void onGetCloudPlus(MouseEvent event) {
        if (toGetCloudCount < players[0].getResources(ResourceType.CLOUD)) {
            toGetCloudCount++;
        }
        CloudGetLabel.setText(String.valueOf(toGetCloudCount));
    }

    @FXML
    void onGetCloudMinus(MouseEvent event) {
        if (toGetCloudCount > 0) {
            toGetCloudCount--;
            CloudGetLabel.setText(String.valueOf(toGetCloudCount));
        }
    }

    @FXML
    void onGetDataPlus(MouseEvent event) {
        if (toGetDataCount < players[0].getResources(ResourceType.DATA)) {
            toGetDataCount++;
        }
        DataGetLabel.setText(String.valueOf(toGetDataCount));
    }

    @FXML
    void onGetDataMinus(MouseEvent event) {
        if (toGetDataCount > 0) {
            toGetDataCount--;
            DataGetLabel.setText(String.valueOf(toGetDataCount));
        }
    }

    @FXML
    void onGetCapitalPlus(MouseEvent event) {
        if (toGetCapitalCount < players[0].getResources(ResourceType.CAPITAL)) {
            toGetCapitalCount++;
        }
        CapitalGetLabel.setText(String.valueOf(toGetCapitalCount));
    }

    @FXML
    void onGetCapitalMinus(MouseEvent event) {
        if (toGetCapitalCount > 0) {
            toGetCapitalCount--;
            CapitalGetLabel.setText(String.valueOf(toGetCapitalCount));
        }
    }

    @FXML
    void onGetPatentPlus(MouseEvent event) {
        if (toGetPatentCount < players[0].getResources(ResourceType.PATENT)) {
            toGetPatentCount++;
        }
        PatentGetLabel.setText(String.valueOf(toGetPatentCount));
    }

    @FXML
    void onGetPatentMinus(MouseEvent event) {
        if (toGetPatentCount > 0) {
            toGetPatentCount--;
            PatentGetLabel.setText(String.valueOf(toGetPatentCount));
        }
    }

    @FXML
    public void initialize() {
        resetAll();
    }

    @FXML
    void onResetAll(ActionEvent event) {
        resetAll();
    }

    @FXML
    public void setData(GameEngine gameEngine, Player[] players) {
        this.gameEngine = gameEngine;
        this.players = players;
    }


    @FXML
    void ChangeBorderWidthToChoose(MouseEvent event) {
        ((Circle) event.getSource()).setStrokeWidth(3);
    }

    @FXML
    void ChangeBorderWidthToNotChoose(MouseEvent event) {
        ((Circle) event.getSource()).setStrokeWidth(0);
    }

    @FXML
    void ChangeRequestButtonColorToChoose(MouseEvent event) {
        String rgbColor = "rgb(0,100,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeRequestButtonColorToNotChoose(MouseEvent event) {
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeResetButtonColorToChoose(MouseEvent event) {
        String rgbColor = "rgb(120,0,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeResetButtonColorToNotChoose(MouseEvent event) {

        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }


    private void resetAll() {
        toGiveTalentCount = 0;
        toGiveCloudCount = 0;
        toGiveDataCount = 0;
        toGiveCapitalCount = 0;
        toGivePatentCount = 0;
        toGetTalentCount = 0;
        toGetCloudCount = 0;
        toGetDataCount = 0;
        toGetCapitalCount = 0;
        toGetPatentCount = 0;

        TalentGiveLabel.setText("0");
        CloudGiveLabel.setText("0");
        DataGiveLabel.setText("0");
        CapitalGiveLabel.setText("0");
        PatentGiveLabel.setText("0");
        TalentGetLabel.setText("0");
        CloudGetLabel.setText("0");
        DataGetLabel.setText("0");
        CapitalGetLabel.setText("0");
        PatentGetLabel.setText("0");
    }

    Map<ResourceType, Integer> putResources(int talent, int cloud, int data, int capital, int patent) {
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.TALENT, talent);
        resources.put(ResourceType.CLOUD, cloud);
        resources.put(ResourceType.DATA, data);
        resources.put(ResourceType.CAPITAL, capital);
        resources.put(ResourceType.PATENT, patent);
        return resources;

    }

    @FXML
    private void onRequestButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/IncomingTrade.fxml"));
            Parent root = loader.load();

            IncomingTradeController incomingTradeController = loader.getController();
            incomingTradeController.setPlayers(players);
            incomingTradeController.setGameEngine(gameEngine);
            incomingTradeController.setGiveResources(putResources(toGetTalentCount, toGetCloudCount, toGetDataCount, toGetCapitalCount, toGetPatentCount));
            incomingTradeController.setGetResources(putResources(toGiveTalentCount, toGiveCloudCount, toGiveDataCount, toGiveCapitalCount, toGivePatentCount));
            incomingTradeController.setIncomingTradeLabels();
            incomingTradeController.setGameBoardController(gameBoardController);

            Stage tradeIncomingStage = new Stage();
            tradeIncomingStage.setTitle("Incoming Trade");
            tradeIncomingStage.setScene(new Scene(root));
            tradeIncomingStage.setResizable(false);

            tradeIncomingStage.setAlwaysOnTop(true);
            tradeIncomingStage.initModality(Modality.APPLICATION_MODAL);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Window mainStage = currentStage.getOwner();
            if (mainStage != null) {
                tradeIncomingStage.initOwner(mainStage);
            }

            currentStage.close();
            tradeIncomingStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
