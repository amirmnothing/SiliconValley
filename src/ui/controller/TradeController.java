package ui.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
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
    void ChangeColorToChoose(MouseEvent event) {
        ((Circle) event.getSource()).setStrokeWidth(3);
    }

    @FXML
    void ChangeColorToNotChoose(MouseEvent event) {
        ((Circle) event.getSource()).setStrokeWidth(0);
    }

}
