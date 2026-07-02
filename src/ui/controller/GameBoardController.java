package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.engine.GameEngine;
import logic.engine.Map;
import logic.enums.BuildMode;
import logic.enums.ResourceType;
import logic.models.Edge;
import logic.models.Sector;
import logic.models.Vertex;
import logic.models.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GameBoardController {

    final static String PLAYER1COLOR = "rgb(110, 0, 0)";
    final static String PLAYER2COLOR = "rgb(7, 0, 90)";
    final static String PLAYER3COLOR = "rgb(0, 140, 100)";
    final static String PLAYER4COLOR = "rgb(255, 215, 0)";

    // متغیرهای نگهدارنده تعداد هر کارت
    private int currentTalentCount = 0;
    private int currentPatentCount = 0;
    private int currentCloudCount = 0;
    private int currentDataCount = 0;

    ArrayList<Line> lines;
    ArrayList<Circle> circles;

    // رفرنس به انجین بازی
    private GameEngine gameEngine;

    // متدی برای تزریق انجین از کلاس Main یا لودر بازی
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        if (gameEngine == null) {
            return;
        }
        changePlayerTextColor();
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
    private Line l1_0;

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
    private Label TheTechGuruColor;

    @FXML
    private Label TheHackerCEOColor;

    @FXML
    private Label TheVCFundedColor;

    @FXML
    private Label NoneColor;

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
    private Button EndTurnBTN;


    @FXML
    private GridPane mapGrid;


    @FXML
    void onEndTurnBTN(ActionEvent event) {
        if (gameEngine == null) return;

        gameEngine.endCurrentTurn();
        if (gameEngine.getCurrentPlayerIndex() == 0) {
            resetMarketPricesUI();
        }
        changePlayerTextColor();
        refreshPlayersResourcesUI();

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
    }

    public void updateSectorImages() {
        if (gameEngine == null || gameEngine.getMap() == null) return;

        Sector[][] logicSectors = gameEngine.getMap().getSectors();

        for (Node node : mapGrid.getChildren()) {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;

                Integer columnIndex = GridPane.getColumnIndex(imageView);
                Integer rowIndex = GridPane.getRowIndex(imageView);

                int c = (columnIndex != null) ? (columnIndex - 1) / 2 : 0;
                int r = (rowIndex != null) ? (rowIndex - 1) / 2 : 0;

                if (r < logicSectors.length && c < logicSectors[r].length) {
                    Sector sector = logicSectors[r][c];

                    // getting folder name
                    String folderName = switch (sector.getResourceType()) {
                        case CAPITAL -> "Fintech";
                        case CLOUD -> "Cloud";
                        case DATA -> "Data";
                        case TALENT -> "AI";
                        case PATENT -> "IP";
                        case REGULATORY -> "REGULATORY";
                    };

                    String fullPath;

                    if (folderName.equals("REGULATORY")){
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

    @FXML
    public void initialize(GameEngine gameEngine) {
        updateSectorImages();

        TalentCount.setText("0");
        PatentCount.setText("0");
        CloudCount.setText("0");
        DataCount.setText("0");
        TotalCount.setText("0");


        P1TalentCount.setText("0");
        P1PatentCount.setText("0");
        P1CloudCount.setText("0");
        P1DataCount.setText("0");
        P1CapitalCount.setText("0");


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
    }

    public void resetLabel(GameEngine gameEngine) {
//        TalentCount.setText("0");
//        PatentCount.setText("0");
//        CloudCount.setText("0");
//        DataCount.setText("0");
//        TotalCount.setText("0");
//
//
//        P1TalentCount.setText("0");
//        P1PatentCount.setText("0");
//        P1CloudCount.setText("0");
//        P1DataCount.setText("0");
//        P1CapitalCount.setText("0");

        this.gameEngine = gameEngine;
        List<Player> players = gameEngine.getPlayers();
        P1Resources.setText(Integer.toString(totalResourcesCount(players.get(0))));
        P2Resources.setText(Integer.toString(totalResourcesCount(players.get(1))));
        P3Resources.setText(Integer.toString(totalResourcesCount(players.get(2))));
        P4Resources.setText(Integer.toString(totalResourcesCount(players.get(3))));

    }

    private void updateTotalPrice() {
        int total = currentTalentCount * Integer.parseInt(TalentPrice.getText())
                + currentPatentCount * Integer.parseInt(PatentPrice.getText())
                + currentCloudCount * Integer.parseInt(CloudPrice.getText())
                + currentDataCount * Integer.parseInt(DataPrice.getText());
        TotalCount.setText(String.valueOf(total));
    }


    @FXML
    void onTalentPlus(ActionEvent event) {
        currentTalentCount++;
        TalentCount.setText(String.valueOf(currentTalentCount));
        updateTotalPrice();
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
        currentPatentCount++;
        PatentCount.setText(String.valueOf(currentPatentCount));
        updateTotalPrice();
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
        currentCloudCount++;
        CloudCount.setText(String.valueOf(currentCloudCount));
        updateTotalPrice();
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
        currentDataCount++;
        DataCount.setText(String.valueOf(currentDataCount));
        updateTotalPrice();
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
        ((Shape) (event.getSource())).setFill(color);
        ((Shape) (event.getSource())).setStroke(color);
    }

    @FXML
    void ChangeColorToNotChooseCircle(MouseEvent event) {
        ((Shape) (event.getSource())).setFill(Color.rgb(70, 70, 70));
        ((Shape) (event.getSource())).setStroke(Color.BLACK);
        ((Shape) (event.getSource())).setStrokeWidth(1);
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
                (gameEngine.getCurrentBuildMode() == BuildMode.PARTNERSHIP && !((Button) event.getSource()).getId().equals("BuildAPartnershipBTN"))
        )
            ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2;");
    }

    @FXML
    void ChangeButtonColorToNotChoose(MouseEvent event) {
        if (((Button) event.getSource()).getId().equals("RollDiceBTN"))
            ((Button) (event.getSource())).setStyle("-fx-background-color: black;" + "-fx-border-color: blue;" + "-fx-border-width: 2;");
        else if (gameEngine.getCurrentBuildMode() == BuildMode.NONE ||
                (gameEngine.getCurrentBuildMode() == BuildMode.MVP && !((Button) event.getSource()).getId().equals("BuildAMVPBTN")) ||
                (gameEngine.getCurrentBuildMode() == BuildMode.PARTNERSHIP && !((Button) event.getSource()).getId().equals("BuildAPartnershipBTN"))
        )
            ((Button) (event.getSource())).setStyle("-fx-background-color: black;" + "-fx-border-color: white;" + "-fx-border-width: 2;");
    }

    private Color getPlayerColor() {
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
                if (gameEngine.isSetupPhase()) {
                    gameEngine.notifyMVPPlaced();
                }

                circle.setOnMouseEntered(null);
                circle.setOnMouseExited(null);
                circle.setFill(color);
                circle.setStroke(color);


                resetBuildMode();

            } catch (Exception e) {
                e.printStackTrace();
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
                gameEngine.updateLongestNetwork();

                if (gameEngine.isSetupPhase()) {
                    gameEngine.notifyPartnershipPlaced();
                }

                line.setOnMouseEntered(null);
                line.setOnMouseExited(null);
                line.setFill(color);
                line.setStroke(color);
                line.setStrokeWidth(5);

                resetBuildMode();

            } catch (Exception e) {
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
    }

    @FXML
    void SetPlayerResourcesOpacityZero(MouseEvent event) {
        int currentPlayerIndex = gameEngine.getCurrentPlayerIndex();
        ((Rectangle) (event.getSource())).setOpacity(0.1);
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
        ((Rectangle) (event.getSource())).setOpacity(0.1);
    }

    @FXML
    void SetResourcesToNotChoose(MouseEvent event) {
        ((Rectangle) (event.getSource())).setOpacity(0);
    }

    @FXML
    void RollDice() {
        try {
            ArrayList<Integer> Dice = gameEngine.rollDiceForCurrentTurn();

            String D1Addr = "/assets/dice/dice_" + Dice.get(0) + ".png";
            String D2Addr = "/assets/dice/dice_" + Dice.get(1) + ".png";

            Dice1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(D1Addr))));
            Dice2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(D2Addr))));

            refreshPlayersResourcesUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //برای استخراج مختصات از نام circle و line
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/Trade.fxml"));
            Parent root = loader.load();

            TradeController tradeController = loader.getController();

            // Todo : You must send players to TRADE window to parse their resources
            tradeController.setData(new Player[]{new Player(null)});

            Stage tradeStage = new Stage();
            tradeStage.setTitle("Trade");
            tradeStage.setScene(new Scene(root));
            tradeStage.setResizable(false);
            tradeStage.initModality(Modality.APPLICATION_MODAL);
            tradeStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            tradeStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int totalResourcesCount(Player player) {
        return player.getResourceCount().getOrDefault(ResourceType.TALENT, 0) +
                player.getResourceCount().getOrDefault(ResourceType.DATA, 0) +
                player.getResourceCount().getOrDefault(ResourceType.PATENT, 0) +
                player.getResourceCount().getOrDefault(ResourceType.CAPITAL, 0) +
                player.getResourceCount().getOrDefault(ResourceType.CLOUD, 0);
    }

    void refreshPlayersResourcesUI() {
        Player p = gameEngine.getCurrentPlayer();
        int playerIndex = gameEngine.getCurrentPlayerIndex();
        List<Label> labels = new ArrayList<>();
        labels.add(P1CapitalCount);
        labels.add(P1PatentCount);
        labels.add(P1CloudCount);
        labels.add(P1DataCount);
        labels.add(P1TalentCount);

        if (playerIndex == 0) {
            labels.add(P1Resources);
            setPlayerResourcesUIText(p, labels);
        } else if (playerIndex == 1) {
            labels.add(P2Resources);
            setPlayerResourcesUIText(p, labels);
        } else if (playerIndex == 2) {
            labels.add(P3Resources);
            setPlayerResourcesUIText(p, labels);
        } else if (playerIndex == 3) {
            labels.add(P4Resources);
            setPlayerResourcesUIText(p, labels);
        }


    }

    void setPlayerResourcesUIText(Player p, List<Label> label) {
        label.get(0).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.CAPITAL, 0)));
        label.get(1).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.PATENT, 0)));
        label.get(2).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.CLOUD, 0)));
        label.get(3).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.DATA, 0)));
        label.get(4).setText(String.valueOf(p.getResourceCount().getOrDefault(ResourceType.TALENT, 0)));

        label.get(5).setText(String.valueOf(totalResourcesCount(p)));

    }

    void changePlayerTextColor() {
        int playerIndex = gameEngine.getCurrentPlayerIndex();
        if (playerIndex == 0) {
            resetLabelColor();
            Label[] labels = {Player1Color, TheTechGuruColor, P1PointColor, P1Resources};
            setLabelColor(PLAYER1COLOR, labels);
        } else if (playerIndex == 1) {
            resetLabelColor();
            Label[] labels = {Player2Color, TheHackerCEOColor, P2PointColor, P2Resources};
            setLabelColor(PLAYER2COLOR, labels);
        } else if (playerIndex == 2) {
            resetLabelColor();
            Label[] labels = {Player3Color, TheVCFundedColor, P3PointColor, P3Resources};
            setLabelColor(PLAYER3COLOR, labels);
        } else if (playerIndex == 3) {
            resetLabelColor();
            Label[] labels = {Player4Color, NoneColor, P4PointColor, P4Resources};
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
        TheTechGuruColor.setTextFill(c);
        TheVCFundedColor.setTextFill(c);
        TheHackerCEOColor.setTextFill(c);
        NoneColor.setTextFill(c);
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

    private void resetMarketPricesUI() {
        TalentPrice.setText(String.valueOf(gameEngine.getMarket().getPrice(ResourceType.TALENT)));
        PatentPrice.setText(String.valueOf(gameEngine.getMarket().getPrice(ResourceType.PATENT)));
        CloudPrice.setText(String.valueOf(gameEngine.getMarket().getPrice(ResourceType.CLOUD)));
        DataPrice.setText(String.valueOf(gameEngine.getMarket().getPrice(ResourceType.DATA)));
    }
}
