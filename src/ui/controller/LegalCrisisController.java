package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import javafx.scene.control.*;
import javafx.stage.Stage;
import logic.engine.GameEngine;
import logic.enums.ResourceType;
import logic.models.Player;

import java.util.Map;

public class LegalCrisisController {
    private int currentTalentCount = 0;
    private int currentPatentCount = 0;
    private int currentCloudCount = 0;
    private int currentDataCount = 0;
    private int currentCapitalCount = 0;

    Player player = null;
    int totalResourceCount = 0;
    GameEngine gameEngine;

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }


    @FXML
    private Label PlayerName;

    @FXML
    private Label TotalCardCount;

    @FXML
    private Label MaxCardCount;

    @FXML
    private Button ReturnToBankBTN;

    @FXML
    private Circle CapitalL;

    @FXML
    private Circle CapitalR;

    @FXML
    private Circle TalentL;

    @FXML
    private Circle TalentR;

    @FXML
    private Circle CloudL;

    @FXML
    private Circle CloudR;

    @FXML
    private Circle PatentL;

    @FXML
    private Circle PatentR;

    @FXML
    private Circle DataL;

    @FXML
    private Circle DataRS;

    @FXML
    private Label CapitalCount;

    @FXML
    private Label TalentCount;

    @FXML
    private Label CloudCount;

    @FXML
    private Label PatentCount;

    @FXML
    private Label DataCount;

    @FXML
    private void updateTotalCardCount() {
        int totalCardCount = totalCardCount();
        TotalCardCount.setText(String.valueOf(totalCardCount));
    }

    private int totalCardCount() {
        return currentCapitalCount + currentTalentCount
                + currentPatentCount + currentCloudCount + currentDataCount;
    }

    @FXML
    void onCapitalMinus(MouseEvent event) {
        if (currentCapitalCount > 0) currentCapitalCount--;
        CapitalCount.setText(String.valueOf(currentCapitalCount));
        updateTotalCardCount();
    }

    @FXML
    void onTalentMinus(MouseEvent event) {
        if (currentTalentCount > 0) currentTalentCount--;
        TalentCount.setText(String.valueOf(currentTalentCount));
        updateTotalCardCount();
    }

    @FXML
    void onPatentMinus(MouseEvent event) {
        if (currentPatentCount > 0) currentPatentCount--;
        PatentCount.setText(String.valueOf(currentPatentCount));
        updateTotalCardCount();
    }

    @FXML
    void onCloudMinus(MouseEvent event) {
        if (currentCloudCount > 0) currentCloudCount--;
        CloudCount.setText(String.valueOf(currentCloudCount));
        updateTotalCardCount();
    }

    @FXML
    void onDataMinus(MouseEvent event) {
        if (currentDataCount > 0) currentDataCount--;
        DataCount.setText(String.valueOf(currentDataCount));
        updateTotalCardCount();
    }

    @FXML
    void onCapitalPlus(MouseEvent event) {
        if (currentCapitalCount < player.getResources(ResourceType.CAPITAL) && totalCardCount() < totalResourceCount / 2)
            currentCapitalCount++;
        CapitalCount.setText(String.valueOf(currentCapitalCount));
        updateTotalCardCount();
    }

    @FXML
    void onTalentPlus(MouseEvent event) {
        if (currentTalentCount < player.getResources(ResourceType.TALENT) && totalCardCount() < totalResourceCount / 2)
            currentTalentCount++;

        TalentCount.setText(String.valueOf(currentTalentCount));
        updateTotalCardCount();
    }

    @FXML
    void onPatentPlus(MouseEvent event) {
        if (currentPatentCount < player.getResources(ResourceType.PATENT) && totalCardCount() < totalResourceCount / 2)
            currentPatentCount++;
        PatentCount.setText(String.valueOf(currentPatentCount));
        updateTotalCardCount();
    }

    @FXML
    void onCloudPlus(MouseEvent event) {
        if (currentCloudCount < player.getResources(ResourceType.CLOUD) && totalCardCount() < totalResourceCount / 2)
            currentCloudCount++;
        CloudCount.setText(String.valueOf(currentCloudCount));
        updateTotalCardCount();
    }

    @FXML
    void onDataPlus(MouseEvent event) {
        if (currentDataCount < player.getResources(ResourceType.DATA) && totalCardCount() < totalResourceCount / 2)
            currentDataCount++;
        DataCount.setText(String.valueOf(currentDataCount));
        updateTotalCardCount();
    }

    @FXML
    void onReturnToBank(MouseEvent event) {
        int maxCardCount = totalResourceCount / 2;
        java.util.Map<ResourceType, Integer> resourcesToDiscard = new java.util.HashMap<>();
        resourcesToDiscard.put(ResourceType.TALENT, currentTalentCount);
        resourcesToDiscard.put(ResourceType.PATENT, currentPatentCount);
        resourcesToDiscard.put(ResourceType.DATA, currentDataCount);
        resourcesToDiscard.put(ResourceType.CAPITAL, currentCapitalCount);
        resourcesToDiscard.put(ResourceType.CLOUD, currentCloudCount);
        if (totalResourcesCount(resourcesToDiscard) == maxCardCount) {
            if (gameEngine.discardSelectedResources(player, resourcesToDiscard)) {
                Button sourceButton = (Button) event.getSource();
                Stage stage = (Stage) sourceButton.getScene().getWindow();
                stage.close();
            }
        }
    }


    @FXML
    public void initialize() {
        TotalCardCount.setText("0");
        CapitalCount.setText("0");
        TalentCount.setText("0");
        CloudCount.setText("0");
        PatentCount.setText("0");
        DataCount.setText("0");
    }

    public void initData(Player player, int totalResourceCount) {
        this.player = player;
        this.totalResourceCount = totalResourceCount;
        if (player != null) {
            PlayerName.setText(player.getPlayerName());
        }
        MaxCardCount.setText(String.valueOf(totalResourceCount / 2));
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
    void ChangeReturnButtonToChoose(MouseEvent event) {
        String rgbColor = "rgb(0,100,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeReturnButtonToNotChoose(MouseEvent event) {
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    int totalResourcesCount(Map<ResourceType, Integer> resourcesToDiscard) {
        return resourcesToDiscard.getOrDefault(ResourceType.TALENT, 0) +
                resourcesToDiscard.getOrDefault(ResourceType.DATA, 0) +
                resourcesToDiscard.getOrDefault(ResourceType.PATENT, 0) +
                resourcesToDiscard.getOrDefault(ResourceType.CAPITAL, 0) +
                resourcesToDiscard.getOrDefault(ResourceType.CLOUD, 0);
    }

}
