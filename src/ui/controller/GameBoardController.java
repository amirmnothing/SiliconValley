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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.engine.GameEngine;
import logic.models.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GameBoardController {

    final static String PLAYER1COLOR = "rgb(110, 0, 0)";
    final static String PLAYER2COLOR = "rgb(7, 0, 90)";
    final static String PLAYER3COLOR = "rgb(0, 140, 100)";
    final static String PLAYER4COLOR = "rgb(255, 215, 0)";

    // رفرنس به انجین بازی
    private GameEngine gameEngine;

    // متدی برای تزریق انجین از کلاس Main یا لودر بازی
    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @FXML
    private Circle c00;

    @FXML
    private Circle c010;

    @FXML
    private Circle c02;

    @FXML
    private Circle c04;

    @FXML
    private Circle c06;

    @FXML
    private Circle c08;

    @FXML
    private Circle c100;

    @FXML
    private Circle c1010;

    @FXML
    private Circle c102;

    @FXML
    private Circle c104;

    @FXML
    private Circle c106;

    @FXML
    private Circle c108;

    @FXML
    private Circle c20;

    @FXML
    private Circle c210;

    @FXML
    private Circle c22;

    @FXML
    private Circle c24;

    @FXML
    private Circle c26;

    @FXML
    private Circle c28;

    @FXML
    private Circle c40;

    @FXML
    private Circle c410;

    @FXML
    private Circle c42;

    @FXML
    private Circle c44;

    @FXML
    private Circle c46;

    @FXML
    private Circle c48;

    @FXML
    private Circle c60;

    @FXML
    private Circle c610;

    @FXML
    private Circle c62;

    @FXML
    private Circle c64;

    @FXML
    private Circle c66;

    @FXML
    private Circle c68;

    @FXML
    private Circle c80;

    @FXML
    private Circle c810;

    @FXML
    private Circle c82;

    @FXML
    private Circle c84;

    @FXML
    private Circle c86;

    @FXML
    private Circle c88;

    @FXML
    private Line l01;

    @FXML
    private Line l03;

    @FXML
    private Line l05;

    @FXML
    private Line l07;

    @FXML
    private Line l09;

    @FXML
    private Line l10;

    @FXML
    private Line l101;

    @FXML
    private Line l103;

    @FXML
    private Line l105;

    @FXML
    private Line l107;

    @FXML
    private Line l109;

    @FXML
    private Line l110;

    @FXML
    private Line l12;

    @FXML
    private Line l14;

    @FXML
    private Line l16;

    @FXML
    private Line l18;

    @FXML
    private Line l21;

    @FXML
    private Line l23;

    @FXML
    private Line l25;

    @FXML
    private Line l27;

    @FXML
    private Line l29;

    @FXML
    private Line l30;

    @FXML
    private Line l310;

    @FXML
    private Line l32;

    @FXML
    private Line l34;

    @FXML
    private Line l36;

    @FXML
    private Line l38;

    @FXML
    private Line l41;

    @FXML
    private Line l43;

    @FXML
    private Line l45;

    @FXML
    private Line l47;

    @FXML
    private Line l49;

    @FXML
    private Line l50;

    @FXML
    private Line l510;

    @FXML
    private Line l52;

    @FXML
    private Line l54;

    @FXML
    private Line l56;

    @FXML
    private Line l58;

    @FXML
    private Line l61;

    @FXML
    private Line l63;

    @FXML
    private Line l65;

    @FXML
    private Line l67;

    @FXML
    private Line l69;

    @FXML
    private Line l70;

    @FXML
    private Line l710;

    @FXML
    private Line l72;

    @FXML
    private Line l74;

    @FXML
    private Line l76;

    @FXML
    private Line l78;

    @FXML
    private Line l81;

    @FXML
    private Line l83;

    @FXML
    private Line l85;

    @FXML
    private Line l87;

    @FXML
    private Line l89;

    @FXML
    private Line l90;

    @FXML
    private Line l910;

    @FXML
    private Line l92;

    @FXML
    private Line l94;

    @FXML
    private Line l96;

    @FXML
    private Line l98;

    @FXML
    private Group PlayerResources;

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

    // متغیرهای نگهدارنده تعداد هر کارت
    private int currentTalentCount = 0;
    private int currentPatentCount = 0;
    private int currentCloudCount = 0;
    private int currentDataCount = 0;

    @FXML
    public void initialize() {

        TalentCount.setText("0");
        PatentCount.setText("0");
        CloudCount.setText("0");
        DataCount.setText("0");
        TotalCount.setText("0");
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
    void ChangeShopButtonsToChoose(MouseEvent event){
        String rgbColor = "rgb(33, 33, 33)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";");
    }

    @FXML
    void ChangeShopButtonsToNotChoose(MouseEvent event){
        String rgbColor = "rgb(18, 18, 18)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";");
    }

    @FXML
    void ChangeShopBuyButtonToChoose(MouseEvent event){
        String rgbColor = "rgb(00, 100, 18)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";");
    }

    @FXML
    void ChangeShopBuyButtonToNotChoose(MouseEvent event){
        String rgbColor = "rgb(00, 60, 18)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";");
    }

    @FXML
    void ChangeColorToChooseCircle(MouseEvent event) {
        ((Shape) (event.getSource())).setFill(Color.RED);
        ((Shape) (event.getSource())).setStroke(Color.BLACK);
        ((Shape) (event.getSource())).setStrokeWidth(1);
    }

    @FXML
    void ChangeColorToChooseLine(MouseEvent event) {
        ((Shape) (event.getSource())).setStroke(Color.RED);
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
        else
            ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 2;");
    }

    @FXML
    void ChangeButtonColorToNotChoose(MouseEvent event) {
        if (((Button) event.getSource()).getId().equals("RollDiceBTN"))
            ((Button) (event.getSource())).setStyle("-fx-background-color: black;" + "-fx-border-color: blue;" + "-fx-border-width: 2;");
        else
            ((Button) (event.getSource())).setStyle("-fx-background-color: black;" + "-fx-border-color: white;" + "-fx-border-width: 2;");
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
    void SetColorUnchangable(MouseEvent event) {
        ((Shape) (event.getSource())).setOnMouseEntered(null);
        ((Shape) (event.getSource())).setOnMouseExited(null);
        ((Shape) (event.getSource())).setFill(Color.RED);
    }

    @FXML
    void SetPlayerResourcesOpacityZero(MouseEvent event) {
        PlayerResources.setOpacity(0);
        ((Rectangle) (event.getSource())).setOpacity(0.1);
        if (((Rectangle) (event.getSource())).getId().equals("P1ResourceRectangle")) {
            P1RLines.setOpacity(0);
        }
        if (((Rectangle) (event.getSource())).getId().equals("P2ResourceRectangle")) {
            P2RLines.setOpacity(0);
        }
        if (((Rectangle) (event.getSource())).getId().equals("P3ResourceRectangle")) {
            P3RLines.setOpacity(0);
        }
        if (((Rectangle) (event.getSource())).getId().equals("P4ResourceRectangle")) {
            P4RLines.setOpacity(0);
        }
    }

    @FXML
    void SetPlayerResourcesOpacityOne(MouseEvent event) {
        PlayerResources.setOpacity(1);
        ((Rectangle) (event.getSource())).setOpacity(0.2);
        if (((Rectangle) (event.getSource())).getId().equals("P1ResourceRectangle")) {
            P1RLines.setOpacity(1);
        } else if (((Rectangle) (event.getSource())).getId().equals("P2ResourceRectangle")) {
            P2RLines.setOpacity(1);
        } else if (((Rectangle) (event.getSource())).getId().equals("P3ResourceRectangle")) {
            P3RLines.setOpacity(1);
        } else if (((Rectangle) (event.getSource())).getId().equals("P4ResourceRectangle")) {
            P4RLines.setOpacity(1);
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
        ArrayList<Integer> Dice = gameEngine.rollDice();

        String D1Addr = "/assets/dice/dice_"+ Dice.get(0) + ".png";
        String D2Addr = "/assets/dice/dice_"+ Dice.get(1) + ".png";

        Dice1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(D1Addr))));
        Dice2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(D2Addr))));
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

//            tradeStage.setAlwaysOnTop(true);
            tradeStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
