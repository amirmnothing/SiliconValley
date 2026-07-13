package ui.controller;

import exception.InsufficientResourcesException;
import exception.InvalidPlacementException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.engine.GameEngine;
import logic.enums.BuildMode;
import logic.enums.MessageMode;
import logic.enums.PlayerRole;
import logic.enums.ResourceType;
import logic.models.*;
import logic.save.SaveManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static logic.engine.GameEngine.*;

public class GameBoardController {

    private int currentTalentCount = 0;
    private int currentPatentCount = 0;
    private int currentCloudCount = 0;
    private int currentDataCount = 0;

    private int mouseOnBtn = 0;

    private boolean isDiceRolled = false;


    ArrayList<Line> lines;
    ArrayList<Circle> circles;
    ArrayList<SVGPath> hexagons;
    ArrayList<StackPane> sectors;

    private GameEngine gameEngine;

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        if (gameEngine == null) {
            // Todo: Show error
            return;
        }
        refreshPlayersResourcesUI();
    }

    @FXML
    private Circle c0_0;

    @FXML
    private Circle c0_10;

    @FXML
    private Circle c0_2;

    @FXML
    private Circle c0_4;

    @FXML
    private Circle c0_6;

    @FXML
    private Circle c0_8;

    @FXML
    private Circle c10_0;

    @FXML
    private Circle c10_10;

    @FXML
    private Circle c10_2;

    @FXML
    private Circle c10_4;

    @FXML
    private Circle c10_6;

    @FXML
    private Circle c10_8;

    @FXML
    private Circle c2_0;

    @FXML
    private Circle c2_10;

    @FXML
    private Circle c2_2;

    @FXML
    private Circle c2_4;

    @FXML
    private Circle c2_6;

    @FXML
    private Circle c2_8;

    @FXML
    private Circle c4_0;

    @FXML
    private Circle c4_10;

    @FXML
    private Circle c4_2;

    @FXML
    private Circle c4_4;

    @FXML
    private Circle c4_6;

    @FXML
    private Circle c4_8;

    @FXML
    private Circle c6_0;

    @FXML
    private Circle c6_10;

    @FXML
    private Circle c6_2;

    @FXML
    private Circle c6_4;

    @FXML
    private Circle c6_6;

    @FXML
    private Circle c6_8;

    @FXML
    private Circle c8_0;

    @FXML
    private Circle c8_10;

    @FXML
    private Circle c8_2;

    @FXML
    private Circle c8_4;

    @FXML
    private Circle c8_6;

    @FXML
    private Circle c8_8;

    @FXML
    private SVGPath h0_0;

    @FXML
    private SVGPath h0_10;

    @FXML
    private SVGPath h0_2;

    @FXML
    private SVGPath h0_4;

    @FXML
    private SVGPath h0_6;

    @FXML
    private SVGPath h0_8;

    @FXML
    private SVGPath h10_0;

    @FXML
    private SVGPath h10_10;

    @FXML
    private SVGPath h10_2;

    @FXML
    private SVGPath h10_4;

    @FXML
    private SVGPath h10_6;

    @FXML
    private SVGPath h10_8;

    @FXML
    private SVGPath h2_0;

    @FXML
    private SVGPath h2_10;

    @FXML
    private SVGPath h2_2;

    @FXML
    private SVGPath h2_4;

    @FXML
    private SVGPath h2_6;

    @FXML
    private SVGPath h2_8;

    @FXML
    private SVGPath h4_0;

    @FXML
    private SVGPath h4_10;

    @FXML
    private SVGPath h4_2;

    @FXML
    private SVGPath h4_4;

    @FXML
    private SVGPath h4_6;

    @FXML
    private SVGPath h4_8;

    @FXML
    private SVGPath h6_0;

    @FXML
    private SVGPath h6_10;

    @FXML
    private SVGPath h6_2;

    @FXML
    private SVGPath h6_4;

    @FXML
    private SVGPath h6_6;

    @FXML
    private SVGPath h6_8;

    @FXML
    private SVGPath h8_0;

    @FXML
    private SVGPath h8_10;

    @FXML
    private SVGPath h8_2;

    @FXML
    private SVGPath h8_4;

    @FXML
    private SVGPath h8_6;

    @FXML
    private SVGPath h8_8;

    @FXML
    private Line l0_1;

    @FXML
    private Line l0_3;

    @FXML
    private Line l0_5;

    @FXML
    private Line l0_7;

    @FXML
    private Line l0_9;

    @FXML
    private Line l10_1;

    @FXML
    private Line l10_3;

    @FXML
    private Line l10_5;

    @FXML
    private Line l10_7;

    @FXML
    private Line l10_9;

    @FXML
    private Line l1_0;

    @FXML
    private Line l1_10;

    @FXML
    private Line l1_2;

    @FXML
    private Line l1_4;

    @FXML
    private Line l1_6;

    @FXML
    private Line l1_8;

    @FXML
    private Line l2_1;

    @FXML
    private Line l2_3;

    @FXML
    private Line l2_5;

    @FXML
    private Line l2_7;

    @FXML
    private Line l2_9;

    @FXML
    private Line l3_0;

    @FXML
    private Line l3_10;

    @FXML
    private Line l3_2;

    @FXML
    private Line l3_4;

    @FXML
    private Line l3_6;

    @FXML
    private Line l3_8;

    @FXML
    private Line l4_1;

    @FXML
    private Line l4_3;

    @FXML
    private Line l4_5;

    @FXML
    private Line l4_7;

    @FXML
    private Line l4_9;

    @FXML
    private Line l5_0;

    @FXML
    private Line l5_10;

    @FXML
    private Line l5_2;

    @FXML
    private Line l5_4;

    @FXML
    private Line l5_6;

    @FXML
    private Line l5_8;

    @FXML
    private Line l6_1;

    @FXML
    private Line l6_3;

    @FXML
    private Line l6_5;

    @FXML
    private Line l6_7;

    @FXML
    private Line l6_9;

    @FXML
    private Line l7_0;

    @FXML
    private Line l7_10;

    @FXML
    private Line l7_2;

    @FXML
    private Line l7_4;

    @FXML
    private Line l7_6;

    @FXML
    private Line l7_8;

    @FXML
    private Line l8_1;

    @FXML
    private Line l8_3;

    @FXML
    private Line l8_5;

    @FXML
    private Line l8_7;

    @FXML
    private Line l8_9;

    @FXML
    private Line l9_0;

    @FXML
    private Line l9_10;

    @FXML
    private Line l9_2;

    @FXML
    private Line l9_4;

    @FXML
    private Line l9_6;

    @FXML
    private Line l9_8;

    @FXML
    private StackPane S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15, S16, S17, S18, S19, S20, S21, S22, S23, S24;

    // ========================== Table ==========================

    @FXML
    private Group FourPlayerTable;

    @FXML
    private Group ThreePlayerTable;

    @FXML
    private Group TwoPlayerTable;

    @FXML
    private Label P1T2, P2T2, P1RT2, P2RT2, P1PT2, P2PT2, P1RET2, P2RET2;

    @FXML
    private Label P1T3, P2T3, P3T3, P1RT3, P2RT3, P3RT3, P1PT3, P2PT3, P3PT3, P1RET3, P2RET3, P3RET3;

    @FXML
    private Group PlayerResources;

    @FXML
    private Label P1TalentCount;

    @FXML
    private Label P1PatentCount;

    @FXML
    private Label P1CloudCount;

    @FXML
    private Label P1DataCount;

    @FXML
    private Label P1CapitalCount;

    @FXML
    private Label Player1Color;

    @FXML
    private Label Player2Color;

    @FXML
    private Label Player3Color;

    @FXML
    private Label Player4Color;

    @FXML
    private Label Player1Role;

    @FXML
    private Label Player2Role;

    @FXML
    private Label Player3Role;

    @FXML
    private Label Player4Role;

    @FXML
    private Label P1PointColor;

    @FXML
    private Label P2PointColor;

    @FXML
    private Label P3PointColor;

    @FXML
    private Label P4PointColor;

    @FXML
    private Label P1Resources;

    @FXML
    private Label P2Resources;

    @FXML
    private Label P3Resources;

    @FXML
    private Label P4Resources;

    @FXML
    private Group P1RLines;

    @FXML
    private Group P2RLines;

    @FXML
    private Group P3RLines;

    @FXML
    private Group P4RLines;

    @FXML
    private Label TalentPrice;

    @FXML
    private Label CloudPrice;

    @FXML
    private Label PatentPrice;

    @FXML
    private Label DataPrice;

    @FXML
    private Button TalentL;

    @FXML
    private Button TalentR;

    @FXML
    private Label TalentCount;

    @FXML
    private Button PatentL;

    @FXML
    private Button PatentR;

    @FXML
    private Label PatentCount;

    @FXML
    private Button CloudL;

    @FXML
    private Button CloudR;

    @FXML
    private Label CloudCount;

    @FXML
    private Button DataL;

    @FXML
    private Button DataR;

    @FXML
    private Label DataCount;

    @FXML
    private Label TotalCount;

    @FXML
    private Label TotalCount11;

    @FXML
    private Button Buy;

    @FXML
    private ImageView Dice1;

    @FXML
    private ImageView Dice2;

    @FXML
    private Button BuildAMVPBTN;

    @FXML
    private Button BuildAPartnershipBTN;

    @FXML
    private Button UpgradeToUnicornBTN;

    @FXML
    private Tab Shop;

    @FXML
    private Button EndTurnBTN;

    @FXML
    private Button trade1;

    @FXML
    private Button trade2;

    @FXML
    private Button trade3;

    @FXML
    private Label name1;

    @FXML
    private Label name2;

    @FXML
    private Label name3;

    @FXML
    private Tab Trade;

    @FXML
    private Label T1CapitalCnt;

    @FXML
    private Label T1TalentCnt;

    @FXML
    private Label T1CloudCnt;

    @FXML
    private Label T1PatentCnt;

    @FXML
    private Label T1DataCnt;

    @FXML
    private Label T2CapitalCnt;

    @FXML
    private Label T2TalentCnt;

    @FXML
    private Label T2CloudCnt;

    @FXML
    private Label T2PatentCnt;

    @FXML
    private Label T2DataCnt;

    @FXML
    private Label T3CapitalCnt;

    @FXML
    private Label T3TalentCnt;

    @FXML
    private Label T3CloudCnt;

    @FXML
    private Label T3PatentCnt;

    @FXML
    private Label T3DataCnt;

    @FXML
    private Label yourCapital;

    @FXML
    private Label yourTalent;

    @FXML
    private Label yourCloud;

    @FXML
    private Label yourPatent;

    @FXML
    private Label yourData;

    @FXML
    private Button RollDiceBTN;


    @FXML
    private GridPane mapGrid;

    @FXML
    private VBox TradeP1Box, TradeP2Box, TradeP3Box;

    @FXML
    private Rectangle TradeP1Rectangle, TradeP2Rectangle, TradeP3Rectangle;

    private boolean isActiveEndTurn = false;
    private StackPane previousAuditorLocation = null;


    @FXML
    void onEndTurnBTN(ActionEvent event) {
        if (gameEngine == null) return;

        gameEngine.endCurrentTurn();
        if (gameEngine.getCurrentPlayerIndex() == 0) {
            resetMarketPricesUI();
        }
        changePlayerTextColor();
        refreshPlayersResourcesUI();

        setDiceRolled(false);
        enableButtonsAfterDiceRoll();

        setActiveEndTurn(false);
        endTurnDisable();
        if(!(gameEngine.getCurrentPlayer() instanceof PlayableAI)){
            showMessage("Dice","Please roll the dice.",MessageMode.NORMAL);
        }

        startNextTurn();
    }

    @FXML
    void onBuildAMVPBTN(ActionEvent event) {
        if (gameEngine.getCurrentBuildMode() != BuildMode.MVP) {
            gameEngine.setBuildMode(BuildMode.MVP);
            for (Line l : lines) l.setDisable(true);
            for (Circle c : circles) c.setDisable(false);
        } else {
            resetBuildMode();
            return;
        }
        BuildAMVPBTN.setStyle("-fx-border-color: yellow; -fx-background-color: #333; -fx-border-width: 2");
        BuildAPartnershipBTN.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-border-width: 2");
        UpgradeToUnicornBTN.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-border-width: 2");
    }

    @FXML
    void onBuildAPartnershipBTN(ActionEvent event) {
        if (gameEngine.getCurrentBuildMode() != BuildMode.PARTNERSHIP) {
            for (Circle c : circles) c.setDisable(true);
            for (Line l : lines) l.setDisable(false);
            gameEngine.setBuildMode(BuildMode.PARTNERSHIP);
        } else {
            resetBuildMode();
            return;
        }
        BuildAPartnershipBTN.setStyle("-fx-border-color: yellow; -fx-background-color: #333; -fx-border-width: 2");
        BuildAMVPBTN.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-border-width: 2");
        UpgradeToUnicornBTN.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-border-width: 2");
    }

    @FXML
    void onUpgradeToUnicorn(ActionEvent event) {
        if (gameEngine.getCurrentBuildMode() != BuildMode.UNICORN) {
            gameEngine.setBuildMode(BuildMode.UNICORN);
            for (Line l : lines) l.setDisable(true);
            for (Circle c : circles) c.setDisable(false);
        } else {
            resetBuildMode();
            return;
        }
        UpgradeToUnicornBTN.setStyle("-fx-border-color: yellow; -fx-background-color: #333; -fx-border-width: 2");
        BuildAMVPBTN.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-border-width: 2");
        BuildAPartnershipBTN.setStyle("-fx-border-color: white; -fx-background-color: black; -fx-border-width: 2");
    }

    public void updateSectorImages() {
        if (gameEngine == null || gameEngine.getMap() == null) return;

        Sector[][] logicSectors = gameEngine.getMap().getSectors();

        for (Node node : mapGrid.getChildren()) {
            if (node instanceof StackPane && node.getId() != null) {
                ImageView imageView = (ImageView) ((StackPane) node).getChildren().getFirst();

                Integer columnIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);

                int c = (columnIndex != null) ? (columnIndex - 1) / 2 : 0;
                int r = (rowIndex != null) ? (rowIndex - 1) / 2 : 0;

                if (r < logicSectors.length && c < logicSectors[r].length) {
                    Sector sector = logicSectors[r][c];

                    String folderName = switch (sector.getResourceType()) {
                        case CAPITAL -> "Fintech";
                        case CLOUD -> "Cloud";
                        case DATA -> "Data";
                        case TALENT -> "AI";
                        case PATENT -> "IP";
                        case REGULATORY -> "REGULATORY";
                    };

                    String fullPath;

                    if (folderName.equals("REGULATORY")) {
                        fullPath = "/assets/Sectors/Regulatory.png";
                    } else {
                        fullPath = "/assets/Sectors/" + folderName + "/" + sector.getactivationNumber() + ".png";
                    }

                    try (var stream = getClass().getResourceAsStream(fullPath)) {
                        if (stream != null) {
                            Image sectorImage = new Image(stream);
                            imageView.setImage(sectorImage);
                        } else {
                            // Todo : Show error : Image not found... (print image path)
                        }
                    } catch (Exception e) {
                        // Todo : Show error with a messagebox
                    }
                }
            }
        }
    }

    public void setAuditorOnSector(StackPane stackPane) {
        Node sectorImage = stackPane.getChildren().get(0);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.3);
        colorAdjust.setContrast(0);
        colorAdjust.setHue(0);
        colorAdjust.setSaturation(-1.0);

        ((ImageView) sectorImage).setEffect(colorAdjust);

        Node auditorSector = stackPane.getChildren().get(1);
        String auditorImagePath = "/assets/Sectors/Auditor.png";
        try (var stream = getClass().getResourceAsStream(auditorImagePath)) {
            if (stream != null) {
                Image auditorImage = new Image(stream);
                ((ImageView) auditorSector).setImage(auditorImage);
            } else {
                // Todo : Show error : Image not found... (print image path)
            }
        } catch (Exception e) {
            // Todo : Show error with a messagebox
        }
    }

    public void setAuditorNotOnSector(StackPane stackPane) {
        Node sectorImage = stackPane.getChildren().get(0);
        ((ImageView) sectorImage).setEffect(null);

        Node auditorSector = stackPane.getChildren().get(1);
        ((ImageView) auditorSector).setImage(null);
    }


    @FXML
    void choiceAuditor(MouseEvent event) {
        if (!(event.getSource() instanceof StackPane stackPane)) {
            return;
        }
        if (previousAuditorLocation != null && stackPane == previousAuditorLocation) {
            return;
        }
        if (gameEngine.getCurrentPlayer().isCanPlaceAuditor()) {
            StackPane temp = previousAuditorLocation;
            int row = gameEngine.getMap().getRows();
            int col = gameEngine.getMap().getCols();
            boolean moveSuccessful = false;
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    if (mapGrid.getChildren().get(r * 5 + c) == stackPane) {
                        if (gameEngine.moveAuditor(gameEngine.getMap().getSectors()[r][c])) {
                            setAuditorOnSector(stackPane);
                            showMessage("Auditor deployment", "The auditor was successfully placed in your desired location", MessageMode.SUCCESS);
                            temp = stackPane;
                            gameEngine.getCurrentPlayer().setCanPlaceAuditor(false);
                            moveSuccessful = true;
                        } else {
                            if (previousAuditorLocation != null) setAuditorOnSector(previousAuditorLocation);
                            gameEngine.getMap().getSectors()[r][c].setAuditor(true);
                            showMessage("Invalid Place", "This is an inappropriate location for the Auditor", MessageMode.ERROR);
                        }
                    } else if (previousAuditorLocation != null && mapGrid.getChildren().get(r * 5 + c) == previousAuditorLocation) {
                        setAuditorNotOnSector(previousAuditorLocation);
                        gameEngine.getMap().getSectors()[r][c].setAuditor(false);
                    }
                }
            }
            if (moveSuccessful) {
                previousAuditorLocation = temp;
                setActiveEndTurn(true);
                endTurnDisable();
            }
        }
    }

    public void performAIAuditorMove(int targetRow, int targetCol) {
        Sector targetSector = gameEngine.getMap().getSectors()[targetRow][targetCol];

        if (gameEngine.moveAuditor(targetSector)) {


            if (previousAuditorLocation != null) {
                setAuditorNotOnSector(previousAuditorLocation);
            }

            StackPane newAuditorPane = (StackPane) mapGrid.getChildren().get(targetRow * 5 + targetCol);
            setAuditorOnSector(newAuditorPane);

            previousAuditorLocation = newAuditorPane;

            gameEngine.getCurrentPlayer().setCanPlaceAuditor(false);
        }
    }

    public boolean getIsDiceRolled() {
        return !isDiceRolled;
    }

    public void setDiceRolled(boolean diceRolled) {
        isDiceRolled = diceRolled;
    }

    public boolean getIsActiveEndTurn() {
        return isActiveEndTurn;
    }

    public void setActiveEndTurn(boolean activeEndTurn) {
        isActiveEndTurn = activeEndTurn;
    }

    @FXML
    public void initialize(GameEngine gameEngine) {
        updateSectorImages();


        TalentCount.setText("0");
        PatentCount.setText("0");
        CloudCount.setText("0");
        DataCount.setText("0");
        TotalCount.setText("0");
        List<Player> players = gameEngine.getPlayers();

        switch (players.size()) {
            case 2:
                TwoPlayerTable.setDisable(false);
                TwoPlayerTable.setMouseTransparent(false);
                TwoPlayerTable.setVisible(true);
                Player1Color = P1T2;
                Player2Color = P2T2;
                Player1Role = P1RT2;
                Player2Role = P2RT2;
                P1PointColor = P1PT2;
                P2PointColor = P2PT2;
                P1Resources = P1RET2;
                P2Resources = P2RET2;
                TradeP2Rectangle.setVisible(false);
                TradeP2Rectangle.setMouseTransparent(true);
                TradeP2Box.setVisible(false);
                TradeP2Box.setMouseTransparent(true);
                TradeP3Rectangle.setVisible(false);
                TradeP3Rectangle.setMouseTransparent(true);
                TradeP3Box.setVisible(false);
                TradeP3Box.setMouseTransparent(true);
                break;
            case 3:
                ThreePlayerTable.setDisable(false);
                ThreePlayerTable.setMouseTransparent(false);
                ThreePlayerTable.setVisible(true);
                Player1Color = P1T3;
                Player2Color = P2T3;
                Player3Color = P3T3;
                Player1Role = P1RT3;
                Player2Role = P2RT3;
                Player3Role = P3RT3;
                P1PointColor = P1PT3;
                P2PointColor = P2PT3;
                P3PointColor = P3PT3;
                P1Resources = P1RET3;
                P2Resources = P2RET3;
                P3Resources = P3RET3;
                TradeP3Rectangle.setVisible(false);
                TradeP3Rectangle.setMouseTransparent(true);
                TradeP3Box.setVisible(false);
                TradeP3Box.setMouseTransparent(true);
                break;
            case 4:
                FourPlayerTable.setDisable(false);
                FourPlayerTable.setMouseTransparent(false);
                FourPlayerTable.setVisible(true);
                break;
        }

        switch (players.size()) {
            case 4:
                Player4Color.setText(players.get(3).getPlayerName());
                Player4Role.setText(role(players.get(3).getRole()));
                P4Resources.setText(Integer.toString(totalResourcesCount(players.get(3))));
            case 3:
                Player3Color.setText(players.get(2).getPlayerName());
                Player3Role.setText(role(players.get(2).getRole()));
                P3Resources.setText(Integer.toString(totalResourcesCount(players.get(2))));
            case 2:
                Player2Color.setText(players.get(1).getPlayerName());
                Player2Role.setText(role(players.get(1).getRole()));
                P2Resources.setText(Integer.toString(totalResourcesCount(players.get(1))));
            case 1:
                Player1Color.setText(players.get(0).getPlayerName());
                Player1Role.setText(role(players.get(0).getRole()));
                P1Resources.setText(Integer.toString(totalResourcesCount(players.get(0))));
        }

        changePlayerTextColor();

        P1TalentCount.setText(Integer.toString(gameEngine.getCurrentPlayer().getResourceCount().getOrDefault(ResourceType.TALENT, 0)));
        P1PatentCount.setText(Integer.toString(gameEngine.getCurrentPlayer().getResourceCount().getOrDefault(ResourceType.PATENT, 0)));
        P1CloudCount.setText(Integer.toString(gameEngine.getCurrentPlayer().getResourceCount().getOrDefault(ResourceType.CLOUD, 0)));
        P1DataCount.setText(Integer.toString(gameEngine.getCurrentPlayer().getResourceCount().getOrDefault(ResourceType.DATA, 0)));
        P1CapitalCount.setText(Integer.toString(gameEngine.getCurrentPlayer().getResourceCount().getOrDefault(ResourceType.CAPITAL, 0)));

        updatePlayersPoints();

        enableButtonsAfterDiceRoll();
        startNextTurn();
        updateTurnControls();
        endTurnDisable();
        saveGameTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        saveGameNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        saveGameDateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        saveGameNameCol.setReorderable(false);
        saveGameDateCol.setReorderable(false);
        saveGameTable.setItems(saveList);
        saveGameTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                EditSaveNameField.setText(newSelection.getName());
            }
        });


        lines = new ArrayList<>(Arrays.asList(
                l0_1, l0_3, l0_5, l0_7, l0_9,
                l1_0, l1_2, l1_4, l1_6, l1_8, l1_10,
                l2_1, l2_3, l2_5, l2_7, l2_9,
                l3_0, l3_2, l3_4, l3_6, l3_8, l3_10,
                l4_1, l4_3, l4_5, l4_7, l4_9,
                l5_0, l5_2, l5_4, l5_6, l5_8, l5_10,
                l6_1, l6_3, l6_5, l6_7, l6_9,
                l7_0, l7_2, l7_4, l7_6, l7_8, l7_10,
                l8_1, l8_3, l8_5, l8_7, l8_9,
                l9_0, l9_2, l9_4, l9_6, l9_8, l9_10,
                l10_1, l10_3, l10_5, l10_7, l10_9
        ));
        circles = new ArrayList<>(Arrays.asList(
                c0_0, c0_2, c0_4, c0_6, c0_8, c0_10,
                c2_0, c2_2, c2_4, c2_6, c2_8, c2_10,
                c4_0, c4_2, c4_4, c4_6, c4_8, c4_10,
                c6_0, c6_2, c6_4, c6_6, c6_8, c6_10,
                c8_0, c8_2, c8_4, c8_6, c8_8, c8_10,
                c10_0, c10_2, c10_4, c10_6, c10_8, c10_10
        ));
        hexagons = new ArrayList<>(Arrays.asList(
                h0_0, h0_2, h0_4, h0_6, h0_8, h0_10,
                h2_0, h2_2, h2_4, h2_6, h2_8, h2_10,
                h4_0, h4_2, h4_4, h4_6, h4_8, h4_10,
                h6_0, h6_2, h6_4, h6_6, h6_8, h6_10,
                h8_0, h8_2, h8_4, h8_6, h8_8, h8_10,
                h10_0, h10_2, h10_4, h10_6, h10_8, h10_10
        ));
        sectors = new ArrayList<>(Arrays.asList(
                S0, S1, S2, S3, S4,
                S5, S6, S7, S8, S9,
                S10, S11, S12, S13, S14,
                S15, S16, S17, S18, S19,
                S20, S21, S22, S23, S24
        ));

        LoadCompaniesAndPartnerships();
    }

    public String role(PlayerRole playerRole) {
        return switch (playerRole) {
            case THE_HACKER_CEO -> "The Hacker CEO";
            case THE_TECH_GURU_CTO -> "The Tech Guru";
            case THE_VC_FUNDED -> "The VC-Funded";
            case NONE -> "None";
        };
    }

    public void endTurnDisable() {
        if (gameEngine != null && gameEngine.isSetupPhase()) {
            EndTurnBTN.setDisable(true);
            return;
        }
        EndTurnBTN.setDisable(!getIsActiveEndTurn());
    }

    private void updateTotalPrice() {
        int total = currentTalentCount * Integer.parseInt(TalentPrice.getText())
                + currentPatentCount * Integer.parseInt(PatentPrice.getText())
                + currentCloudCount * Integer.parseInt(CloudPrice.getText())
                + currentDataCount * Integer.parseInt(DataPrice.getText());
        TotalCount.setText(String.valueOf(total));
    }

    private int getTotalPriceAfterAdd(ResourceType RT) {
        int total = currentTalentCount * Integer.parseInt(TalentPrice.getText())
                + currentPatentCount * Integer.parseInt(PatentPrice.getText())
                + currentCloudCount * Integer.parseInt(CloudPrice.getText())
                + currentDataCount * Integer.parseInt(DataPrice.getText());

        if (RT == ResourceType.TALENT) return total + Integer.parseInt(TalentPrice.getText());
        else if (RT == ResourceType.PATENT) return total + Integer.parseInt(PatentPrice.getText());
        else if (RT == ResourceType.CLOUD) return total + Integer.parseInt(CloudPrice.getText());
        else if (RT == ResourceType.DATA) return total + Integer.parseInt(DataPrice.getText());
        else return -1;
    }

    @FXML
    void onShopClick() {
        TotalCount11.setText(Integer.toString(gameEngine.getCurrentPlayer().getResources(ResourceType.CAPITAL)));
    }

    @FXML
    void onTalentPlus(ActionEvent event) {
        if (getTotalPriceAfterAdd(ResourceType.TALENT) <= gameEngine.getCurrentPlayer().getResources(ResourceType.CAPITAL)) {
            currentTalentCount++;
            TalentCount.setText(String.valueOf(currentTalentCount));
            updateTotalPrice();
        }
    }

    @FXML
    void onTalentMinus(ActionEvent event) {
        if (currentTalentCount > 0) {
            currentTalentCount--;
            TalentCount.setText(String.valueOf(currentTalentCount));
            updateTotalPrice();
        }
    }


    @FXML
    void onPatentPlus(ActionEvent event) {
        if (getTotalPriceAfterAdd(ResourceType.PATENT) <= gameEngine.getCurrentPlayer().getResources(ResourceType.CAPITAL)) {
            currentPatentCount++;
            PatentCount.setText(String.valueOf(currentPatentCount));
            updateTotalPrice();
        }
    }

    @FXML
    void onPatentMinus(ActionEvent event) {
        if (currentPatentCount > 0) {
            currentPatentCount--;
            PatentCount.setText(String.valueOf(currentPatentCount));
            updateTotalPrice();
        }
    }


    @FXML
    void onCloudPlus(ActionEvent event) {
        if (getTotalPriceAfterAdd(ResourceType.CLOUD) <= gameEngine.getCurrentPlayer().getResources(ResourceType.CAPITAL)) {
            currentCloudCount++;
            CloudCount.setText(String.valueOf(currentCloudCount));
            updateTotalPrice();
        }
    }

    @FXML
    void onCloudMinus(ActionEvent event) {
        if (currentCloudCount > 0) {
            currentCloudCount--;
            CloudCount.setText(String.valueOf(currentCloudCount));
            updateTotalPrice();
        }
    }


    @FXML
    void onDataPlus(ActionEvent event) {
        if (getTotalPriceAfterAdd(ResourceType.DATA) <= gameEngine.getCurrentPlayer().getResources(ResourceType.CAPITAL)) {
            currentDataCount++;
            DataCount.setText(String.valueOf(currentDataCount));
            updateTotalPrice();
        }
    }

    @FXML
    void onDataMinus(ActionEvent event) {
        if (currentDataCount > 0) {
            currentDataCount--;
            DataCount.setText(String.valueOf(currentDataCount));
            updateTotalPrice();
        }
    }

    @FXML
    void ChangeShopButtonsToChoose(MouseEvent event) {
        String rgbColor = "rgb(33, 33, 33)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";");
    }

    @FXML
    void ChangeShopButtonsToNotChoose(MouseEvent event) {
        String rgbColor = "rgb(18, 18, 18)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";");
    }

    @FXML
    void ChangeShopBuyButtonToChoose(MouseEvent event) {
        String rgbColor = "rgb(00, 100, 18)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";");
    }

    @FXML
    void ChangeShopBuyButtonToNotChoose(MouseEvent event) {
        String rgbColor = "rgb(00, 60, 18)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";");
    }

    @FXML
    void ChangeColorToChoose(MouseEvent event) {
        Color color = getPlayerColor();
        Player owner = null;

        if (gameEngine.getCurrentBuildMode() == BuildMode.UNICORN &&
                event.getSource() instanceof Circle circle) {
            int[] Coordinates = parseCoordinates(circle.getId());
            if (gameEngine.getMap().getVertices()[Coordinates[0] / 2][Coordinates[1] / 2].getCompanyStructure() != null) {
                owner = gameEngine.getMap().getVertices()[Coordinates[0] / 2][Coordinates[1] / 2].getCompanyStructure().getOwner();
            }
            if (owner != null && owner == gameEngine.getCurrentPlayer()) {
                for (Node n : ((StackPane) (circle.getParent())).getChildren()) {
                    if (n instanceof SVGPath) {
                        SVGPath hexagon = (SVGPath) n;
                        hexagon.setFill(color);
                        hexagon.setOpacity(1);
                        hexagon.setMouseTransparent(false);
                        circle.setOpacity(0);
                        circle.setMouseTransparent(true);
                    }
                }
            }
        } else {
            if (event.getSource() instanceof Circle circle) {
                int[] Coordinates = parseCoordinates(circle.getId());
                if (gameEngine.getMap().getVertices()[Coordinates[0] / 2][Coordinates[1] / 2].getCompanyStructure() != null) {
                    owner = gameEngine.getMap().getVertices()[Coordinates[0] / 2][Coordinates[1] / 2].getCompanyStructure().getOwner();
                }
                if (owner == null) ((Shape) (event.getSource())).setFill(color);
            } else if (event.getSource() instanceof Line) ((Shape) (event.getSource())).setStroke(color);
        }
    }

    @FXML
    void ChangeColorToNotChooseCircle(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        Player owner = null;
        int[] Coordinates = parseCoordinates(circle.getId());
        if (gameEngine.getMap().getVertices()[Coordinates[0] / 2][Coordinates[1] / 2].getCompanyStructure() != null) {
            owner = gameEngine.getMap().getVertices()[Coordinates[0] / 2][Coordinates[1] / 2].getCompanyStructure().getOwner();
        }
        if (owner == null) {
            ((Shape) (event.getSource())).setFill(Color.rgb(70, 70, 70));
            ((Shape) (event.getSource())).setStroke(Color.BLACK);
            ((Shape) (event.getSource())).setStrokeWidth(1);
        }
    }

    @FXML
    void ChangeHexagonToNotChoose(MouseEvent event) {
        Player owner = null;

        if (event.getSource() instanceof SVGPath hexagon) {
            for (Node n : ((StackPane) (hexagon.getParent())).getChildren()) {
                if (n instanceof Circle) {
                    Circle circle = (Circle) n;
                    int[] Coordinates = parseCoordinates(circle.getId());
                    if (gameEngine.getMap().getVertices()[Coordinates[0] / 2][Coordinates[1] / 2].getCompanyStructure() != null) {
                        owner = gameEngine.getMap().getVertices()[Coordinates[0] / 2][Coordinates[1] / 2].getCompanyStructure().getOwner();
                    }

                    hexagon.setOpacity(0);
                    hexagon.setMouseTransparent(true);

                    circle.setOpacity(1);
                    circle.setMouseTransparent(false);
                    if (owner == null) {
                        circle.setFill(Color.rgb(70, 70, 70));
                        circle.setStroke(Color.BLACK);
                        circle.setStrokeWidth(1);
                    } else {
                        List<Player> playersList = gameEngine.getPlayers();
                        Color color = null;
                        color = getPlayerColor(owner, playersList);
                        circle.setFill(color);
                        circle.setStroke(Color.BLACK);
                        circle.setStrokeWidth(1);
                    }
                }
            }
        }
    }

    @FXML
    void ChangeColorToNotChooseLine(MouseEvent event) {
        ((Shape) (event.getSource())).setStroke(Color.rgb(70, 70, 70));
    }

    @FXML
    void ChangeButtonColorToChoose(MouseEvent event) {
        String rgbColor = "rgb(37,37,37)";
        if (((Button) event.getSource()).getId().equals("RollDiceBTN"))
            ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: blue;" + "-fx-border-width: 2;");
        else if (gameEngine.getCurrentBuildMode() == BuildMode.NONE ||
                (gameEngine.getCurrentBuildMode() == BuildMode.MVP && !((Button) event.getSource()).getId().equals("BuildAMVPBTN")) ||
                (gameEngine.getCurrentBuildMode() == BuildMode.PARTNERSHIP && !((Button) event.getSource()).getId().equals("BuildAPartnershipBTN")) ||
                (gameEngine.getCurrentBuildMode() == BuildMode.UNICORN && !((Button) event.getSource()).getId().equals("UpgradeToUnicornBTN"))
        )
            ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2;");
    }

    @FXML
    void ChangeButtonColorToNotChoose(MouseEvent event) {
        if (((Button) event.getSource()).getId().equals("RollDiceBTN"))
            ((Button) (event.getSource())).setStyle("-fx-background-color: black;" + "-fx-border-color: blue;" + "-fx-border-width: 2;");
        else if (gameEngine.getCurrentBuildMode() == BuildMode.NONE ||
                (gameEngine.getCurrentBuildMode() == BuildMode.MVP && !((Button) event.getSource()).getId().equals("BuildAMVPBTN")) ||
                (gameEngine.getCurrentBuildMode() == BuildMode.PARTNERSHIP && !((Button) event.getSource()).getId().equals("BuildAPartnershipBTN")) ||
                (gameEngine.getCurrentBuildMode() == BuildMode.UNICORN && !((Button) event.getSource()).getId().equals("UpgradeToUnicornBTN"))
        )
            ((Button) (event.getSource())).setStyle("-fx-background-color: black;" + "-fx-border-color: white;" + "-fx-border-width: 2;");
    }

    public Color getPlayerColor() {
        int index = gameEngine.getCurrentPlayerIndex();

        switch (index) {
            case 0:
                return Color.web(PLAYER1COLOR);
            case 1:
                return Color.web(PLAYER2COLOR);
            case 2:
                return Color.web(PLAYER3COLOR);
            case 3:
                return Color.web(PLAYER4COLOR);
            default:
                return Color.BLACK;
        }
    }

    public Color getPlayerColor(Player player, List<Player> playersList) {
        for (int i = 0; i < playersList.size(); i++) {
            if (playersList.get(i) == player) {
                switch (i) {
                    case 0:
                        return (Color.web(PLAYER1COLOR));
                    case 1:
                        return (Color.web(PLAYER2COLOR));
                    case 2:
                        return (Color.web(PLAYER3COLOR));
                    case 3:
                        return (Color.web(PLAYER4COLOR));
                }
            }
        }
        return Color.BLACK;
    }

    @FXML
    void ChangeTradeButtonColorToChoose(MouseEvent event) {
        String rgbColor = "rgb(37,37,37)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 4;");
    }

    @FXML
    void ChangeTradeButtonColorToNotChoose(MouseEvent event) {
        ((Button) (event.getSource())).setStyle("-fx-background-color: black;" + "-fx-border-color: white;" + "-fx-border-width: 4;");
    }

    @FXML
    void ChangeTradeButtonColorToPressed(MouseEvent event) {
        String rgbColor = "rgb(78,78,78)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 4;");
    }


    public void updateVertexUI(Vertex vertex, Color playerColor) {
        Circle circle = findCircleForVertex(vertex);

        if (circle != null) {
            circle.setFill(playerColor);
            circle.setStroke(Color.BLACK);
        }
    }


    public void updateEdgeUI(Edge edge, Color playerColor) {
        Line line = findLineForEdge(edge);

        if (line != null) {
            line.setOnMouseEntered(null);
            line.setOnMouseExited(null);
            line.setOnMouseClicked(null);
            line.setStroke(playerColor);
            line.setStrokeWidth(5);
        }
    }

    public void updateUnicornUI(Vertex vertex, Color playerColor) {
        SVGPath hexagon = findSVGPathForVertex(vertex);
        hexagon.setOnMouseExited(null);
        hexagon.setOnMouseClicked(null);

        hexagon.setFill(playerColor);
        hexagon.setStroke(Color.BLACK);
        hexagon.setOpacity(1.0);
        hexagon.setVisible(true);
    }

    @FXML
    void SetColorUnchangable(MouseEvent event) {
        if (gameEngine.getCurrentBuildMode() == BuildMode.NONE) {
            return;
        }


        if (event.getSource() instanceof Circle circle && gameEngine.getCurrentBuildMode() == BuildMode.MVP) {
            if (gameEngine.isSetupPhase() && gameEngine.isSetupPlacedMVP()) {
                return;
                //TODO میتوان اکسپشن زد
            }
            Vertex vertex = getVertexFromCircle(circle);
            int[] coordinates = parseCoordinates(circle.getId());

            if (!gameEngine.canBuildMVP(coordinates[0] / 2, coordinates[1] / 2)) {
                return;
            }
            try {
                Color color = getPlayerColor();
                gameEngine.buildMVP(vertex, gameEngine.getCurrentPlayer());
                showMessage("Build MVP", "Congratulations! " + gameEngine.getCurrentPlayer().getPlayerName() + " have successfully built the MVP", MessageMode.SUCCESS);
                checkTurnAdvancement();
                if (gameEngine.isSetupPhase()) {
                    gameEngine.notifyMVPPlaced();
                }

                updateVertexUI(vertex, color);
                resetBuildMode();
            } catch (InvalidPlacementException e) {
                showMessage("Invalid Place", gameEngine.getCurrentPlayer().getPlayerName() + " wanted to build the MVP in an inappropriate location", MessageMode.ERROR);
                checkTurnAdvancement();
            } catch (InsufficientResourcesException e) {
                showMessage("Insufficient resources", "You don't have enough resources to build an MVP.", MessageMode.ERROR);
                checkTurnAdvancement();
            }
        } else if (event.getSource() instanceof Line line && gameEngine.getCurrentBuildMode() == BuildMode.PARTNERSHIP) {

            if (gameEngine.isSetupPhase() && gameEngine.isSetupPlacedPartnership()) {
                return;
                //TODO میتوان اکسپشن زد
            }

            Edge edge = getEdgeFromLine(line);

            if (edge == null || !gameEngine.canBuildPartnership(gameEngine.getCurrentPlayer(), edge)) {
                return;
            }
            try {
                Color color = getPlayerColor();
                gameEngine.buildPartnership(gameEngine.getCurrentPlayer(), edge);
                showMessage("Build Partnership", "Congratulations! " + gameEngine.getCurrentPlayer().getPlayerName() + " have successfully built the Partnership.", MessageMode.SUCCESS);
                checkTurnAdvancement();
                gameEngine.updateLongestNetwork();

                if (gameEngine.isSetupPhase()) {
                    gameEngine.notifyPartnershipPlaced();
                }

                updateEdgeUI(edge, color);
                resetBuildMode();

            } catch (InvalidPlacementException e) {
                showMessage("Invalid Place", gameEngine.getCurrentPlayer().getPlayerName() + " wanted to build the Partnership in an inappropriate location", MessageMode.ERROR);
                checkTurnAdvancement();
            } catch (InsufficientResourcesException e) {
                showMessage("Insufficient resources", "You don't have enough resources to build a Partnership.", MessageMode.ERROR);
                checkTurnAdvancement();
            }
        } else if (event.getSource() instanceof SVGPath hexagon && gameEngine.getCurrentBuildMode() == BuildMode.UNICORN) {
            if (gameEngine.isSetupPhase()) {
                return;
                //TODO میتوان اکسپشن زد
            }
            Vertex vertex = getVertexFromCircle(hexagon);
            int[] coordinates = parseCoordinates(hexagon.getId());

            if (!gameEngine.canUpgradeToUnicorn(coordinates[0] / 2, coordinates[1] / 2)) {
                return;
            }

            try {
                Color color = getPlayerColor();
                gameEngine.upgradeToUnicorn(vertex, gameEngine.getCurrentPlayer());
                showMessage("Upgrade to unicorn", "Congratulations!  " + gameEngine.getCurrentPlayer().getPlayerName() + " have successfully built the Unicorn.", MessageMode.SUCCESS);

                checkTurnAdvancement();
                updateUnicornUI(vertex, color);
                resetBuildMode();

            } catch (InvalidPlacementException e) {
                showMessage("Invalid Place", gameEngine.getCurrentPlayer().getPlayerName() + ", you can’t build a Unicorn here.", MessageMode.ERROR);
                checkTurnAdvancement();
            } catch (InsufficientResourcesException e) {
                showMessage("Insufficient resources", "You don't have enough resources to build a Unicorn.", MessageMode.ERROR);
                checkTurnAdvancement();
            }

        }


        changePlayerTextColor();
        refreshPlayersResourcesUI();
        enableButtonsAfterDiceRoll();
        endTurnDisable();
        updatePlayersPoints();
        Player winner = gameEngine.winnerPlayer();
        if (winner != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("🏆 Game Over 🏆");
            alert.setHeaderText("🎉 CHAMPION OF SILICON VALLEY 🎉");
            alert.setContentText("Player <<" + winner.getPlayerName() + ">> won with " + winner.calculateVictoryPoints() + " Points!");
            DialogPane dialogPane = alert.getDialogPane();
            if (getClass().getResource("/ui/view/style.css") != null) {
                String cssPath = getClass().getResource("/ui/view/style.css").toExternalForm();
                dialogPane.getStylesheets().add(cssPath);
                dialogPane.getStyleClass().add("game-over-alert");
            }
            alert.showAndWait();
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/MainMenu.fxml"));
                StackPane root = loader.load();
                Scene scene = new Scene(root);
                currentStage.setScene(scene);
                currentStage.setTitle("Silicon Valley: The Tech Cartel");
                currentStage.setResizable(false);
                currentStage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void resetBuildMode() {
        gameEngine.setBuildMode(BuildMode.NONE);
        for (Line l : lines) l.setDisable(false);
        for (Circle c : circles) c.setDisable(false);
        BuildAMVPBTN.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 2");
        BuildAPartnershipBTN.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 2");
        UpgradeToUnicornBTN.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 2");
    }

    @FXML
    void SetPlayerResourcesOpacityZero(MouseEvent event) {
        int currentPlayerIndex = gameEngine.getCurrentPlayerIndex();
        if (mouseOnBtn == 1) ((Rectangle) (event.getSource())).setOpacity(0.1);
        else if (mouseOnBtn == 0) ((Rectangle) (event.getSource())).setOpacity(0);

        if (((Rectangle) (event.getSource())).getId().equals("P1ResourceRectangle") && currentPlayerIndex == 0) {
            P1RLines.setOpacity(0);
            PlayerResources.setOpacity(0);
        }
        if (((Rectangle) (event.getSource())).getId().equals("P2ResourceRectangle") && currentPlayerIndex == 1) {
            P2RLines.setOpacity(0);
            PlayerResources.setOpacity(0);
        }
        if (((Rectangle) (event.getSource())).getId().equals("P3ResourceRectangle") && currentPlayerIndex == 2) {
            P3RLines.setOpacity(0);
            PlayerResources.setOpacity(0);
        }
        if (((Rectangle) (event.getSource())).getId().equals("P4ResourceRectangle") && currentPlayerIndex == 3) {
            P4RLines.setOpacity(0);
            PlayerResources.setOpacity(0);
        }
    }

    @FXML
    void SetPlayerResourcesOpacityOne(MouseEvent event) {
        int currentPlayerIndex = gameEngine.getCurrentPlayerIndex();
        ((Rectangle) (event.getSource())).setOpacity(0.2);
        if (((Rectangle) (event.getSource())).getId().equals("P1ResourceRectangle") && currentPlayerIndex == 0) {
            P1RLines.setOpacity(1);
            PlayerResources.setOpacity(1);
        } else if (((Rectangle) (event.getSource())).getId().equals("P2ResourceRectangle") && currentPlayerIndex == 1) {
            P2RLines.setOpacity(1);
            PlayerResources.setOpacity(1);
        } else if (((Rectangle) (event.getSource())).getId().equals("P3ResourceRectangle") && currentPlayerIndex == 2) {
            P3RLines.setOpacity(1);
            PlayerResources.setOpacity(1);
        } else if (((Rectangle) (event.getSource())).getId().equals("P4ResourceRectangle") && currentPlayerIndex == 3) {
            P4RLines.setOpacity(1);
            PlayerResources.setOpacity(1);
        }
    }

    @FXML
    void SetResourcesToChoose(MouseEvent event) {
        if (!event.isPrimaryButtonDown() && !event.isSecondaryButtonDown())
            ((Rectangle) (event.getSource())).setOpacity(0.1);
        mouseOnBtn = 1;
    }

    @FXML
    void SetResourcesToNotChoose(MouseEvent event) {
        if (!event.isPrimaryButtonDown() && !event.isSecondaryButtonDown())
            ((Rectangle) (event.getSource())).setOpacity(0);
        mouseOnBtn = 0;
    }

    @FXML
    void RollDice(ActionEvent event) {
        try {
            ArrayList<Integer> dice = gameEngine.rollDiceForCurrentTurn();
            showDiceResultsUI(dice, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTurnControls();
    }

    public void showDiceResultsUI(ArrayList<Integer> Dice, Runnable onDiceProcessed) {

        String D1Addr = "/assets/dice/dice_" + Dice.get(0) + ".png";
        String D2Addr = "/assets/dice/dice_" + Dice.get(1) + ".png";

        Dice1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(D1Addr))));
        Dice2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(D2Addr))));
        setDiceRolled(true);
        enableButtonsAfterDiceRoll();

        setActiveEndTurn(true);
        endTurnDisable();

        refreshPlayersResourcesUI();

        if (Dice.get(0) + Dice.get(1) == 7) {
            gameEngine.processCrisisForAIOnly();
            openLegalCrisisWindow();
            if (!(gameEngine.getCurrentPlayer() instanceof PlayableAI)) {
                showMessage("Auditor deployment", "Please place the auditor at the place of your choice", MessageMode.NORMAL);
            }
            setActiveEndTurn(false);
            endTurnDisable();
        }
        if (onDiceProcessed != null) {
            onDiceProcessed.run();
        }
    }

    public void enableButtonsAfterDiceRoll() {
        if (!gameEngine.isSetupPhase()) {
            BuildAPartnershipBTN.setDisable(getIsDiceRolled());
            BuildAMVPBTN.setDisable(getIsDiceRolled());
            UpgradeToUnicornBTN.setDisable(getIsDiceRolled());
        }
        Shop.setDisable(getIsDiceRolled());
        Trade.setDisable(getIsDiceRolled());
    }

    private int[] parseCoordinates(String id) {
        id = id.substring(1);
        String[] parts = id.split("_");

        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        return new int[]{row, col};
    }

    private Vertex getVertexFromCircle(Circle circle) {
        int[] coordinates = parseCoordinates(circle.getId());
        int mapRow = coordinates[0] / 2;
        int mapCol = coordinates[1] / 2;

        return gameEngine.getMap().getVertices()[mapRow][mapCol];
    }

    private Vertex getVertexFromCircle(SVGPath hexagon) {
        int[] coordinates = parseCoordinates(hexagon.getId());
        int mapRow = coordinates[0] / 2;
        int mapCol = coordinates[1] / 2;

        return gameEngine.getMap().getVertices()[mapRow][mapCol];
    }

    private Edge findEdge(Vertex v1, Vertex v2) {
        for (Edge e : gameEngine.getMap().getEdges()) {
            if ((e.getStart() == v1 && e.getEnd() == v2) || (e.getStart() == v2 && e.getEnd() == v1)) {
                return e;
            }
        }
        return null;
    }

    private Edge getEdgeFromLine(Line line) {
        int[] coordinates = parseCoordinates(line.getId());
        int row = coordinates[0];
        int col = coordinates[1];
        if (row % 2 == 1) {
            int r = (row - 1) / 2;
            int c = col / 2;

            Vertex v1 = gameEngine.getMap().getVertices()[r][c];
            Vertex v2 = gameEngine.getMap().getVertices()[r + 1][c];

            return findEdge(v1, v2);
        } else {
            int r = row / 2;
            int c = (col - 1) / 2;

            Vertex v1 = gameEngine.getMap().getVertices()[r][c];
            Vertex v2 = gameEngine.getMap().getVertices()[r][c + 1];

            return findEdge(v1, v2);
        }
    }


    @FXML
    private void openTradeWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/TradeRequest.fxml"));
            Parent root = loader.load();
            Player player = null;

            for (Player p : gameEngine.getPlayers()) {
                if (p == gameEngine.getCurrentPlayer()) {
                    continue;
                }
                if (trade1 == (Button) event.getSource()) {
                    if (name1.getText().equals(p.getPlayerName())) {
                        player = p;
                    }
                }
                if (trade2 == (Button) event.getSource()) {
                    if (name2.getText().equals(p.getPlayerName())) {
                        player = p;
                    }
                }
                if (trade3 == (Button) event.getSource()) {
                    if (name3.getText().equals(p.getPlayerName())) {
                        player = p;
                    }
                }
            }
            if (player == null || player instanceof logic.models.PlayableAI) {
                return;
            }
            TradeRequestController tradeRequestController = loader.getController();

            // Todo : You must send players to TRADE window to parse their resources
            tradeRequestController.setData(gameEngine, new Player[]{gameEngine.getCurrentPlayer(), player});
            tradeRequestController.setLabel();
            tradeRequestController.setGameBoardController(this);

            Stage tradeStage = new Stage();
            tradeStage.setTitle("Trade Request");
            tradeStage.setScene(new Scene(root));
            tradeStage.setResizable(false);
            tradeStage.initModality(Modality.APPLICATION_MODAL);
            tradeStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            tradeStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openLegalCrisisWindow() {
        for (Player p : gameEngine.getPlayers()) {
            if (p instanceof PlayableAI) continue;

            if (!gameEngine.isResourceBelowCrisisThreshold(p)) continue;


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/LegalCrisis.fxml"));
                Parent root = loader.load();
                int totalResourceCount = totalResourcesCount(p);
                LegalCrisisController legalCrisisController = loader.getController();
                legalCrisisController.setGameEngine(gameEngine);
                legalCrisisController.initData(p, totalResourceCount);


                Stage legalCrisisStage = new Stage();
                legalCrisisStage.setTitle("Legal Crisis");
                legalCrisisStage.setScene(new Scene(root));
                legalCrisisStage.setResizable(false);
                legalCrisisStage.initModality(Modality.APPLICATION_MODAL);
                legalCrisisStage.setOnCloseRequest(e -> {
                    e.consume();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(legalCrisisStage);
                    alert.setTitle("Hold On!");
                    alert.setHeaderText(null);
                    alert.setContentText("You can't close the window until you return requested resources to the bank");
                    alert.showAndWait();
                });

                legalCrisisStage.showAndWait();

                refreshPlayersResourcesUI();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    int totalResourcesCount(Player player) {
        return player.getResourceCount().getOrDefault(ResourceType.TALENT, 0) +
                player.getResourceCount().getOrDefault(ResourceType.DATA, 0) +
                player.getResourceCount().getOrDefault(ResourceType.PATENT, 0) +
                player.getResourceCount().getOrDefault(ResourceType.CAPITAL, 0) +
                player.getResourceCount().getOrDefault(ResourceType.CLOUD, 0);
    }

    public void refreshPlayersResourcesUI() {
        List<Player> players = gameEngine.getPlayers();
        Player p = gameEngine.getCurrentPlayer();
        int playerIndex = gameEngine.getCurrentPlayerIndex();
        List<Label> labels = new ArrayList<>();
        labels.add(P1CapitalCount);
        labels.add(P1PatentCount);
        labels.add(P1CloudCount);
        labels.add(P1DataCount);
        labels.add(P1TalentCount);

        if (playerIndex == 0) {
            setPlayerResourcesUIText(p, labels);
        } else if (playerIndex == 1) {
            setPlayerResourcesUIText(p, labels);
        } else if (playerIndex == 2) {
            setPlayerResourcesUIText(p, labels);
        } else if (playerIndex == 3) {
            setPlayerResourcesUIText(p, labels);
        }
        switch (players.size()) {
            case 4:
                P4Resources.setText(String.valueOf(totalResourcesCount(players.get(3))));
            case 3:
                P3Resources.setText(String.valueOf(totalResourcesCount(players.get(2))));
            case 2:
                P2Resources.setText(String.valueOf(totalResourcesCount(players.get(1))));
            case 1:
                P1Resources.setText(String.valueOf(totalResourcesCount(players.get(0))));
                break;
        }
        updateDynamicTradeButtons();
    }

    void setPlayerResourcesUIText(Player p, List<Label> label) {
        label.get(0).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.CAPITAL, 0)));
        label.get(1).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.PATENT, 0)));
        label.get(2).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.CLOUD, 0)));
        label.get(3).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.DATA, 0)));
        label.get(4).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.TALENT, 0)));

    }

    public void changePlayerTextColor() {
        int playerIndex = gameEngine.getCurrentPlayerIndex();
        if (playerIndex == 0) {
            resetLabelColor();
            Label[] labels = {Player1Color, Player1Role, P1PointColor, P1Resources};
            setLabelColor(PLAYER1COLOR, labels);
        } else if (playerIndex == 1) {
            resetLabelColor();
            Label[] labels = {Player2Color, Player2Role, P2PointColor, P2Resources};
            setLabelColor(PLAYER2COLOR, labels);
        } else if (playerIndex == 2) {
            resetLabelColor();
            Label[] labels = {Player3Color, Player3Role, P3PointColor, P3Resources};
            setLabelColor(PLAYER3COLOR, labels);
        } else if (playerIndex == 3) {
            resetLabelColor();
            Label[] labels = {Player4Color, Player4Role, P4PointColor, P4Resources};
            setLabelColor(PLAYER4COLOR, labels);
        }
    }

    void setLabelColor(String playerColor, Label[] labels) {
        Color color = Color.web(playerColor);
        for (Label label : labels) {
            label.setTextFill(color);
        }
    }

    void resetLabelColor() {
        Color c = Color.WHITE;

        Player1Color.setTextFill(c);
        Player2Color.setTextFill(c);
        Player3Color.setTextFill(c);
        Player4Color.setTextFill(c);
        Player1Role.setTextFill(c);
        Player3Role.setTextFill(c);
        Player2Role.setTextFill(c);
        Player4Role.setTextFill(c);
        P1PointColor.setTextFill(c);
        P2PointColor.setTextFill(c);
        P3PointColor.setTextFill(c);
        P4PointColor.setTextFill(c);
        P1Resources.setTextFill(c);
        P2Resources.setTextFill(c);
        P3Resources.setTextFill(c);
        P4Resources.setTextFill(c);
    }

    @FXML
    void onBuyFromMarket(ActionEvent event) {
        Player p = gameEngine.getCurrentPlayer();
        try {
            if (currentTalentCount > 0)
                gameEngine.getMarket().buyFromMarket(gameEngine, p, ResourceType.TALENT, currentTalentCount);

            if (currentPatentCount > 0)
                gameEngine.getMarket().buyFromMarket(gameEngine, p, ResourceType.PATENT, currentPatentCount);

            if (currentCloudCount > 0)
                gameEngine.getMarket().buyFromMarket(gameEngine, p, ResourceType.CLOUD, currentCloudCount);

            if (currentDataCount > 0)
                gameEngine.getMarket().buyFromMarket(gameEngine, p, ResourceType.DATA, currentDataCount);

            resetMarketSelectionUI();
            resetMarketPricesUI();
            refreshPlayersResourcesUI();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resetMarketSelectionUI() {
        currentPatentCount = 0;
        currentTalentCount = 0;
        currentCloudCount = 0;
        currentDataCount = 0;

        TalentCount.setText("0");
        PatentCount.setText("0");
        CloudCount.setText("0");
        DataCount.setText("0");
        TotalCount.setText("0");
    }

    public void resetMarketPricesUI() {
        TalentPrice.setText(String.valueOf(gameEngine.getMarket().getPrice(ResourceType.TALENT)));
        PatentPrice.setText(String.valueOf(gameEngine.getMarket().getPrice(ResourceType.PATENT)));
        CloudPrice.setText(String.valueOf(gameEngine.getMarket().getPrice(ResourceType.CLOUD)));
        DataPrice.setText(String.valueOf(gameEngine.getMarket().getPrice(ResourceType.DATA)));
    }

    public void updatePlayersPoints() {
        List<Integer> totalPoints = gameEngine.calculatePlayerPoints();
        switch (totalPoints.size()) {
            case 4:
                P4PointColor.setText(Integer.toString(totalPoints.get(3)));
            case 3:
                P3PointColor.setText(Integer.toString(totalPoints.get(2)));
            case 2:
                P2PointColor.setText(Integer.toString(totalPoints.get(1)));
            case 1:
                P1PointColor.setText(Integer.toString(totalPoints.get(0)));
        }
    }

    @FXML
    void onTabChanged(Event event) {
        if (Trade.isSelected()) {
            updateResourceFields();
        }
        refreshPlayersResourcesUI();
    }


    public void updateResourceFields() {
        List<Player> players = gameEngine.getPlayers();
        Player p = gameEngine.getCurrentPlayer();
        yourCapital.setText(String.valueOf(p.getResources(ResourceType.CAPITAL)));
        yourTalent.setText(String.valueOf(p.getResources(ResourceType.TALENT)));
        yourCloud.setText(String.valueOf(p.getResources(ResourceType.CLOUD)));
        yourData.setText(String.valueOf(p.getResources(ResourceType.DATA)));
        yourPatent.setText(String.valueOf(p.getResources(ResourceType.PATENT)));

        boolean table1 = false, table2 = false, table3 = false;

        for (Player player : players) {
            if (player == p) {
                continue;
            }
            if (!table1) {
                name1.setText(player.getPlayerName());
                T1CapitalCnt.setText(String.valueOf(player.getResources(ResourceType.CAPITAL)));
                T1TalentCnt.setText(String.valueOf(player.getResources(ResourceType.TALENT)));
                T1CloudCnt.setText(String.valueOf(player.getResources(ResourceType.CLOUD)));
                T1DataCnt.setText(String.valueOf(player.getResources(ResourceType.DATA)));
                T1PatentCnt.setText(String.valueOf(player.getResources(ResourceType.PATENT)));
                table1 = true;
            } else if (!table2) {
                name2.setText(player.getPlayerName());
                T2CapitalCnt.setText(String.valueOf(player.getResources(ResourceType.CAPITAL)));
                T2TalentCnt.setText(String.valueOf(player.getResources(ResourceType.TALENT)));
                T2CloudCnt.setText(String.valueOf(player.getResources(ResourceType.CLOUD)));
                T2DataCnt.setText(String.valueOf(player.getResources(ResourceType.DATA)));
                T2PatentCnt.setText(String.valueOf(player.getResources(ResourceType.PATENT)));
                table2 = true;
            } else if (!table3) {
                name3.setText(player.getPlayerName());
                T3CapitalCnt.setText(String.valueOf(player.getResources(ResourceType.CAPITAL)));
                T3TalentCnt.setText(String.valueOf(player.getResources(ResourceType.TALENT)));
                T3CloudCnt.setText(String.valueOf(player.getResources(ResourceType.CLOUD)));
                T3DataCnt.setText(String.valueOf(player.getResources(ResourceType.DATA)));
                T3PatentCnt.setText(String.valueOf(player.getResources(ResourceType.PATENT)));
                table3 = true;
            }
        }
    }

    // ========================== Save Tab ==========================

    int NameFieldChoose = 0;
    int CreateBtnChoose = 0;
    int EditBtnChoose = 0;

    private final ObservableList<FileItem> saveList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<FileItem, String> saveGameDateCol;

    @FXML
    private TableColumn<FileItem, String> saveGameNameCol;

    @FXML
    private TableView<FileItem> saveGameTable;

    @FXML
    private TextField CreateSaveNameField;

    @FXML
    private TextField EditSaveNameField;

    @FXML
    private HBox EditSaveFileBox;

    @FXML
    private HBox CreateSaveFileBox;

    @FXML
    private Button EditSaveBtn;

    @FXML
    private Button CreateANewSaveBtn;

    @FXML
    void FileNameEnterFieldMouseEnter(MouseEvent event) {
        if (NameFieldChoose == 0)
            ((TextField) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-color:  #d4af37; -fx-text-fill:  #d4af37");
    }

    @FXML
    void FileNameEnterFieldMouseExit(MouseEvent event) {
        if (NameFieldChoose == 0)
            ((TextField) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-width: 2; -fx-text-fill:  #d4af37");
    }

    @FXML
    void onFileNameEnterField(MouseEvent event) {
        NameFieldChoose = 1;
        ((TextField) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-color:  #d4af37; -fx-border-width: 3; -fx-text-fill:  #d4af37");
    }

    @FXML
    void unchooseFileNameEnterField(MouseEvent event) {
        NameFieldChoose = 0;
        CreateSaveNameField.setStyle("-fx-background-color:  #1a1a1a; -fx-text-fill:  #d4af37");
        EditSaveNameField.setStyle("-fx-background-color:  #1a1a1a; -fx-text-fill:  #d4af37");
    }

    @FXML
    void SaveTabButtonsMouseEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-color: #d4af37; -fx-border-width: 2");
    }

    @FXML
    void SaveTabButtonsMouseExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a");
    }

    @FXML
    void SaveTabCreateButtonMouseEnter(MouseEvent event) {
        if (CreateBtnChoose == 0)
            ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-color: #d4af37; -fx-border-width: 2");
    }

    @FXML
    void SaveTabCreateButtonMouseExit(MouseEvent event) {
        if (CreateBtnChoose == 0) ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a");
    }

    @FXML
    void SaveTabEditButtonMouseEnter(MouseEvent event) {
        if (EditBtnChoose == 0)
            ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-color: #d4af37; -fx-border-width: 2");
    }

    @FXML
    void SaveTabEditButtonMouseExit(MouseEvent event) {
        if (EditBtnChoose == 0) ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a");
    }

    @FXML
    void openSaveGameTab() {
        loadSaveFiles();
    }

    @FXML
    void onCreateANewSave(ActionEvent event) {
        if (CreateBtnChoose == 1) {
            closeCreateEditBoxes(event, CreateBtnChoose);
            CreateBtnChoose = 0;
        } else {
            CreateBtnChoose = 1;
            EditBtnChoose = 0;
            EditSaveBtn.setStyle("-fx-background-color:  #1a1a1a");
            ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-color: #d4af37; -fx-border-width: 3");
            CreateSaveFileBox.setOpacity(1);
            EditSaveFileBox.setOpacity(0);
            CreateSaveFileBox.setMouseTransparent(false);
            EditSaveFileBox.setMouseTransparent(true);
        }
    }

    @FXML
    void onEditASave(ActionEvent event) {
        if (EditBtnChoose == 1) {
            closeCreateEditBoxes(event, EditBtnChoose);
            EditBtnChoose = 0;
        } else {
            FileItem chosenFile = saveGameTable.getSelectionModel().getSelectedItem();
            if (chosenFile == null) {
                FileNotChosenAlert();
            } else {
                EditSaveNameField.setText(chosenFile.getName());
                EditBtnChoose = 1;
                CreateBtnChoose = 0;
                CreateANewSaveBtn.setStyle("-fx-background-color:  #1a1a1a");
                ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-color: #d4af37; -fx-border-width: 3");
                CreateSaveFileBox.setOpacity(0);
                EditSaveFileBox.setOpacity(1);
                CreateSaveFileBox.setMouseTransparent(true);
                EditSaveFileBox.setMouseTransparent(false);
            }
        }
    }

    private void closeCreateEditBoxes(ActionEvent event, int editBtnChoose) {
        ((Button) event.getSource()).setStyle("-fx-background-color:  #1a1a1a; -fx-border-color: #d4af37; -fx-border-width: 2");
        CreateSaveFileBox.setOpacity(0);
        EditSaveFileBox.setOpacity(0);
        CreateSaveFileBox.setMouseTransparent(true);
        EditSaveFileBox.setMouseTransparent(true);
    }

    private void loadSaveFiles() {
        saveList.clear();

        File saveFolder = new File("saves");

        if (!saveFolder.exists()) {
            saveFolder.mkdir();
        }

        File[] files = saveFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".sv")) {
                    saveList.add(new FileItem(file));
                }
            }
        }
    }

    @FXML
    void onCreateBtnSaveGameTab(ActionEvent event) {
        try {

            File newFile = new File("saves/" + CreateSaveNameField.getText() + ".sv");
            if (newFile.createNewFile()) {
                try {
                    SaveManager.save(gameEngine, newFile);
                } catch (IOException e) {
                    FileSaveFailedAlert(e.getMessage());
                }
                loadSaveFiles();
                FileCreatedSuccessfullyAlert();
            } else {
                FileAlreadyExistsErrorAlert();
            }
        } catch (IOException e) {
            FailedToCreateFileErrorAlert();
        }
    }

    void FailedToCreateFileErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to create save file");
        alert.setContentText("Maybe you entered an unsupported character in file name");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    void FileCreatedSuccessfullyAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("File created successfully");
        alert.setContentText("You can see it in saves' list now!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    void FileAlreadyExistsErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("File already exists");
        alert.setContentText("You can not create a file which is existed!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    @FXML
    void onEditBtnSaveGameTab(ActionEvent event) {
        FileItem chosenFile = saveGameTable.getSelectionModel().getSelectedItem();
        if (chosenFile != null) {
            File file = chosenFile.getFile();
            if (file.renameTo(new File("saves/" + EditSaveNameField.getText() + ".sv"))) {
                loadSaveFiles();
                FileRenameSuccessfullyAlert();
            } else {
                FileRenameFailedfullyAlert();
            }

        } else FileNotChosenAlert();
    }

    void FileRenameSuccessfullyAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Save file renamed successfully");
        alert.setContentText("You can see renamed file in saves' list");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    void FileRenameFailedfullyAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to rename save file");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    @FXML
    void onDeleteSave(ActionEvent event) {
        FileItem chosenFile = saveGameTable.getSelectionModel().getSelectedItem();
        if (chosenFile != null) {
            if (DeleteAFileConfirmation(chosenFile.getName())) {
                if (chosenFile.getFile().delete()) {
                    saveList.remove(chosenFile);
                    FileDeletedSuccessfullyAlert();
                } else FileDeleteFailedAlert();
            }
        } else FileNotChosenAlert();
    }

    void FileNotChosenAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No file has chosen");
        alert.setContentText("You should choose a file from table");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    boolean DeleteAFileConfirmation(String FileName) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete save file");
        confirmationAlert.setContentText("Are you sure you want to delete \"" + FileName + "\" save file?");
        DialogPane dialogPane = confirmationAlert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.get() == ButtonType.OK) return true;
        else return false;
    }

    void FileDeletedSuccessfullyAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Save file deleted successfully");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    void FileDeleteFailedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to delete save file");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    @FXML
    void onSaveGame(ActionEvent event) {
        FileItem chosenFile = saveGameTable.getSelectionModel().getSelectedItem();
        try {
            SaveManager.save(gameEngine, chosenFile.getFile());
            FileSavedSuccessfullyAlert();
        } catch (IOException e) {
            FileSaveFailedAlert(e.getMessage());
        }
    }

    void FileSaveFailedAlert(String Message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to save file");
        alert.setContentText(Message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    void FileSavedSuccessfullyAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Game saved successfully");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/view/style.css")).toExternalForm());
        alert.showAndWait();
    }

    //  -------------------متد های مربوط به AIPlayer-------------------
    //----------------------------------------------------------------
    public void startNextTurn() {
        Player currentPlayer = gameEngine.getCurrentPlayer();

        if (currentPlayer instanceof PlayableAI aiPlayer) {
            updateTurnControls();
            PauseTransition aiDelay = getPauseTransition(aiPlayer);
            aiDelay.play();

        } else {
            updateTurnControls();
        }
    }

    private PauseTransition getPauseTransition(PlayableAI aiPlayer) {
        PauseTransition aiDelay = new PauseTransition(Duration.seconds(2.0));
        aiDelay.setOnFinished(event -> {
            aiPlayer.playTurn(gameEngine, () -> {

                Platform.runLater(() -> {
                    refreshPlayersResourcesUI();
                    changePlayerTextColor();

                    startNextTurn();
                });
            });
        });
        return aiDelay;
    }

    public void setHumanControlsDisabled(boolean disable) {
        Platform.runLater(() -> {

            EndTurnBTN.setDisable(disable);
            BuildAMVPBTN.setDisable(disable);
            BuildAPartnershipBTN.setDisable(disable);
            UpgradeToUnicornBTN.setDisable(disable);
            Shop.setDisable(disable);
            Trade.setDisable(disable);
            RollDiceBTN.setDisable(disable);
        });
    }

    public Circle findCircleForVertex(Vertex vertex) {
        Vertex[][] vertices = gameEngine.getMap().getVertices();
        int mapRow = -1, mapCol = -1;


        for (int r = 0; r < vertices.length; r++) {
            for (int c = 0; c < vertices[r].length; c++) {
                if (vertices[r][c] == vertex) {
                    mapRow = r;
                    mapCol = c;
                    break;
                }
            }
        }
        if (mapRow == -1) return null;


        int uiRow = mapRow * 2;
        int uiCol = mapCol * 2;

        String targetId = "#c" + uiRow + "_" + uiCol;

        return (Circle) mapGrid.lookup(targetId);
    }

    public Line findLineForEdge(Edge edge) {
        Vertex v1 = edge.getStart();
        Vertex v2 = edge.getEnd();

        Vertex[][] vertices = gameEngine.getMap().getVertices();
        int r1 = -1, c1 = -1, r2 = -1, c2 = -1;

        for (int r = 0; r < vertices.length; r++) {
            for (int c = 0; c < vertices[r].length; c++) {
                if (vertices[r][c] == v1) {
                    r1 = r;
                    c1 = c;
                }
                if (vertices[r][c] == v2) {
                    r2 = r;
                    c2 = c;
                }
            }
        }
        if (r1 == -1 || r2 == -1) return null;

        int uiRow, uiCol;

        if (r1 == r2) {

            uiRow = r1 * 2;
            uiCol = Math.min(c1, c2) * 2 + 1;
        } else {

            uiRow = Math.min(r1, r2) * 2 + 1;
            uiCol = c1 * 2;
        }

        String targetId = "#l" + uiRow + "_" + uiCol;

        return (Line) mapGrid.lookup(targetId);
    }

    public SVGPath findSVGPathForVertex(Vertex vertex) {
        Vertex[][] vertices = gameEngine.getMap().getVertices();
        int mapRow = -1, mapCol = -1;


        for (int r = 0; r < vertices.length; r++) {
            for (int c = 0; c < vertices[r].length; c++) {
                if (vertices[r][c] == vertex) {
                    mapRow = r;
                    mapCol = c;
                    break;
                }
            }
        }
        if (mapRow == -1) return null;


        int uiRow = mapRow * 2;
        int uiCol = mapCol * 2;

        String targetId = "#h" + uiRow + "_" + uiCol;

        return (SVGPath) mapGrid.lookup(targetId);
    }

    public void updateDynamicTradeButtons() {
        Platform.runLater(() -> {
            if (gameEngine == null || gameEngine.getPlayers() == null) return;
            trade1.setDisable(false);
            trade2.setDisable(false);
            trade3.setDisable(false);
            for (Player p : gameEngine.getPlayers()) {
                if (p == gameEngine.getCurrentPlayer()) continue;
                if (p instanceof logic.models.PlayableAI) {
                    if (name1 != null && name1.getText().contains(p.getPlayerName())) {
                        trade1.setDisable(true);
                    } else if (name2 != null && name2.getText().contains(p.getPlayerName())) {
                        trade2.setDisable(true);
                    } else if (name3 != null && name3.getText().contains(p.getPlayerName())) {
                        trade3.setDisable(true);
                    }
                }
            }
        });
    }

    public void updateTurnControls() {
        Platform.runLater(() -> {
            if (gameEngine.getCurrentPlayer() instanceof logic.models.PlayableAI) {
                setHumanControlsDisabled(true);
                return;
            }
            if (gameEngine.isSetupPhase()) {
                UpgradeToUnicornBTN.setDisable(true);
                Shop.setDisable(true);
                Trade.setDisable(true);
                RollDiceBTN.setDisable(true);
                BuildAMVPBTN.setDisable(false);
                BuildAPartnershipBTN.setDisable(false);
                EndTurnBTN.setDisable(true);
                return;
            }
            boolean isDiceRolled = gameEngine.isDiceRolled();

            RollDiceBTN.setDisable(isDiceRolled);

            boolean canPerformActions = isDiceRolled;

            UpgradeToUnicornBTN.setDisable(!canPerformActions);
            Shop.setDisable(!canPerformActions);
            BuildAMVPBTN.setDisable(!canPerformActions);
            BuildAPartnershipBTN.setDisable(!canPerformActions);
            if (gameEngine.LastDice.getFirst() + gameEngine.LastDice.getLast() != 7) EndTurnBTN.setDisable(!canPerformActions);

            if (canPerformActions) {
                Trade.setDisable(false);
                updateDynamicTradeButtons();
            } else {
                Trade.setDisable(true);
            }
        });
    }

    public void checkTurnAdvancement() {
        Player previousPlayer = gameEngine.getCurrentPlayer();
        boolean wasSetupPhase = gameEngine.isSetupPhase();


        gameEngine.checkAndMoveToNextSetupTurn();

        Player newPlayer = gameEngine.getCurrentPlayer();
        boolean isNowMainPhase = !gameEngine.isSetupPhase();

        // برای تغییر فاز از ستاپ به اصلی
        if (previousPlayer != newPlayer || (wasSetupPhase && isNowMainPhase)) {
            resetBuildMode();
            startNextTurn();
        } else {
            updateTurnControls();
        }
    }
    // ========================== Message Box ==========================

    private final Image SuccessImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Icons/Success.png")));
    private final Image ErrorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Icons/Error.png")));
    private final Image NormalImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Icons/Normal.png")));

    @FXML
    private ImageView MessageIcon;

    @FXML
    private VBox MessageVBox;

    @FXML
    private Label MessageHeader;

    @FXML
    private Line MessageLine;

    @FXML
    private Label MessageBody;

    @FXML
    private Group MessageBox;

    @FXML
    void showMessage(String messageHeader, String messageBody, MessageMode mode) {

        if (mode == MessageMode.ERROR) {
            MessageIcon.setImage(ErrorImage);
            MessageVBox.setStyle("-fx-border-color: red; -fx-border-width: 2");
            MessageLine.setStroke(Color.RED);
            MessageHeader.setTextFill(Color.RED);
            MessageBody.setTextFill(Color.RED);
        } else if (mode == MessageMode.SUCCESS) {
            MessageIcon.setImage(SuccessImage);
            MessageVBox.setStyle("-fx-border-color: green; -fx-border-width: 2");
            MessageLine.setStroke(Color.GREEN);
            MessageHeader.setTextFill(Color.GREEN);
            MessageBody.setTextFill(Color.GREEN);
        } else if (mode == MessageMode.NORMAL) {
            MessageIcon.setImage(NormalImage);
            MessageVBox.setStyle("-fx-border-color: white; -fx-border-width: 2");
            MessageLine.setStroke(Color.WHITE);
            MessageHeader.setTextFill(Color.WHITE);
            MessageBody.setTextFill(Color.WHITE);
        }
        MessageLine.setEndX(20 * messageHeader.length());
        MessageHeader.setText(messageHeader);
        MessageBody.setText(messageBody);
        MessageBox.setVisible(true);
    }

    // ======================= Load UI =======================

    void LoadCompaniesAndPartnerships() {
        Player owner;
        for (Circle circle : circles) {
            if (getVertexFromCircle(circle).getCompanyStructure() != null) {
                owner = getVertexFromCircle(circle).getCompanyStructure().getOwner();
                if (owner != null) {
                    if (getVertexFromCircle(circle).getCompanyStructure() instanceof MVP) {
                        circle.setFill(getPlayerColor(owner, gameEngine.getPlayers()));
                    } else if (getVertexFromCircle(circle).getCompanyStructure() instanceof Unicorn) {
                        SVGPath hexagon = hexagons.get(circles.indexOf(circle));
                        circle.setOpacity(0);
                        circle.setMouseTransparent(true);
                        hexagon.setOnMouseExited(null);
                        hexagon.setOnMouseClicked(null);
                        hexagon.setFill(getPlayerColor(owner, gameEngine.getPlayers()));
                        hexagon.setOpacity(1);
                        hexagon.setMouseTransparent(false);
                    }
                }
            }
        }
        for (Line line : lines) {
            if (getEdgeFromLine(line).getPartnership() != null) {
                owner = getEdgeFromLine(line).getPartnership().getOwner();
                if (owner != null) {
                    line.setStroke(getPlayerColor(owner, gameEngine.getPlayers()));
                    line.setOnMouseClicked(null);
                    line.setOnMouseEntered(null);
                    line.setOnMouseExited(null);
                }
            }
        }
        Sector[][] logicSectors = gameEngine.getMap().getSectors();
        for (int i = 0; i < logicSectors.length; i++) {
            for (int j = 0; j < logicSectors[0].length; j++) {
                if (logicSectors[i][j].isAuditor()) {
                    StackPane stackPane = sectors.get(i + 5 * j);
                    previousAuditorLocation = stackPane;

                    Node sectorImage = stackPane.getChildren().get(0);
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(-0.3);
                    colorAdjust.setContrast(0);
                    colorAdjust.setHue(0);
                    colorAdjust.setSaturation(-1.0);

                    ((ImageView) sectorImage).setEffect(colorAdjust);

                    Node auditorSector = stackPane.getChildren().get(1);
                    String auditorImagePath = "/assets/Sectors/Auditor.png";
                    try (var stream = getClass().getResourceAsStream(auditorImagePath)) {
                        if (stream != null) {
                            Image auditorImage = new Image(stream);
                            ((ImageView) auditorSector).setImage(auditorImage);
                        } else {
                            // Todo : Show error : Image not found... (print image path)
                        }
                    } catch (Exception e) {
                        // Todo : Show error with a messagebox
                    }
                    return;
                }
            }

        }
    }
}