package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.models.Player;

public class IncomingTradeController {

    @FXML
    void ChangeAcceptButtonColorToChoose (MouseEvent event){
        String rgbColor = "rgb(0,100,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeAcceptButtonColorToNotChoose (MouseEvent event){
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeRejectButtonColorToChoose (MouseEvent event){
        String rgbColor = "rgb(120,0,0)";
        ((Button) (event.getSource())).setStyle("-fx-background-color: " + rgbColor + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void ChangeRejectButtonColorToNotChoose (MouseEvent event){

        ((Button) (event.getSource())).setStyle("-fx-background-color: " + "black" + ";" + "-fx-border-color: white;" + "-fx-border-width: 3;");
    }

    @FXML
    void onAcceptButton (ActionEvent event){
        // Todo: Accept and do the trade
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void onRejectButton (ActionEvent event){
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

}
