package UI.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class gameBoardController {

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
    void ChangeColorToChoose(MouseEvent event) {
        ((Shape) (event.getSource())).setFill(Color.RED);
        ((Shape) (event.getSource())).setStroke(Color.RED);
    }

    @FXML
    void ChangeColorToNotChoose(MouseEvent event) {
        ((Shape) (event.getSource())).setFill(Color.rgb(70,70,70));
        ((Shape) (event.getSource())).setStroke(Color.rgb(70,70,70));
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
    void SetColorUnchangable(MouseEvent event){
        ((Shape) (event.getSource())).setOnMouseEntered(null);
        ((Shape) (event.getSource())).setOnMouseExited(null);
        ((Shape) (event.getSource())).setFill(Color.RED);
    }
}
