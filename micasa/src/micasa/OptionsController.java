package micasa;

import java.awt.Color;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/*
    Controls the options menu, along with the appropriate actions
    Author: Adam Cook
*/

public class OptionsController implements Initializable {
    
    @FXML
    private AnchorPane toolbar; //Represents the toolar
    @FXML
    private AnchorPane bg; //Represents the background
    //Radio buttons
    @FXML
    private ToggleGroup color; //Toggle group of radio buttons
    @FXML
    private RadioButton r1; //Represents the first button
    @FXML
    private RadioButton r2; //Represents the second button
    @FXML
    private RadioButton r3; //Represents the third button
    @FXML
    private RadioButton r4; //Represents the fourth button

    Delta d = new Delta(); //Used to record and change x and y coordinates of window
    MenuController m = new MenuController();
    
    //Closes the options window
    @FXML
    private void handleClose(ActionEvent event) {
        //Gets the source window and hides it
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    //Changes the color scheme
    @FXML
    private void changeColor(ActionEvent event){
        if(r1.isSelected())
            bg.setStyle("-fx-background-color: blue");
        else if(r2.isSelected())
            bg.setStyle("-fx-background-color: red");
        else if(r3.isSelected())
            bg.setStyle("-fx-background-color: green");
        else if(r4.isSelected())
            bg.setStyle("-fx-background-color: purple");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Retrieves x and y mouse coordinates when mouse pressed on toolbar
        toolbar.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent evt) {
                d.setX(evt.getX());
                d.setY(evt.getY());
            }
        });
        
        //Moves the window based on mouse x and y
        toolbar.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent evt) {
            toolbar.getScene().getWindow().setX( evt.getScreenX() - d.getX());
            toolbar.getScene().getWindow().setY( evt.getScreenY() - d.getY());
            }
            
        });
    }    
    
}
