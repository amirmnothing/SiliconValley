package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import logic.models.Player;

import javax.swing.text.html.ImageView;
import java.util.ArrayList;

public class TradeController {


    @FXML
    public void setData(Player[] players) {
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
    void ChangeRequestButtonColorToChoose(MouseEvent event){
        String rgbColor = "rgb(0,100,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeRequestButtonColorToNotChoose(MouseEvent event){

        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeResetButtonColorToChoose(MouseEvent event){
        String rgbColor = "rgb(120,0,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeResetButtonColorToNotChoose(MouseEvent event){

        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }
}
