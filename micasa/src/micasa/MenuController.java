package micasa;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/*
    Controls the main menu of the application, along with the appropriate actions
    Author: Adam Cook
*/

public class MenuController implements Initializable {
    
    private static boolean maximized = false; //Determine if the screen is maximized
    private FXMLLoader loader; //Loads the FXML files to change scenes
    @FXML
    public AnchorPane root; //Represents the entire menu
    @FXML
    public AnchorPane toolbar; //Represents the toolbar
    @FXML
    private Button max; //Represents the maximize button
    @FXML
    private Button min; //Represents the minimize button
    @FXML
    private Button close; //Represents the close application button
    
    Delta d = new Delta(); //Used to record and change x and y coordinates of window
    
    //Handles closing the window
    @FXML
    private void handleClose(ActionEvent event) {
        System.exit(0);
    }
    
    //Handles maximizing the window
    @FXML
    private void handleMax(ActionEvent event){
         Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
         //Only maximize the window if it is not already maximized
         if(!maximized){
             stage.setMaximized(true);
             maximized = true;
         }
         //Resets window size to normal state
         else{
             stage.setMaximized(false);
             maximized = false;
         }
    }
    
    //Handles minimizing the window
    @FXML
    private void handleMin(ActionEvent event){
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        //Minimizes the window to the taskbar
        stage.setIconified(true);
    }
    
    //Transitions to the workspace scene
    @FXML
    private void newPlan(MouseEvent event) throws Exception{
        //Retrieves the Workspace.fxml file
        loader = new FXMLLoader(getClass().getResource("Workspace.fxml"));
        //Loads the workspace
        fadeOut();
    }
    
    //Opens the file explorer to choose a saved plan
    @FXML
    private void fromFile(MouseEvent event){
        //Opens a file explorer directory (C: drive by default)
        try {
            //Uses the command prompt to retrieve the path
            Process builder = Runtime.getRuntime().exec("cmd /c start C:/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Opens the options scene
    @FXML
    private void options(MouseEvent event)throws Exception{
        //Retrieves the Options.fxml file
        loader = new FXMLLoader(getClass().getResource("Options.fxml"));
        Parent root = (Parent)loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    //Opens teh contact scene
    @FXML
    private void contact(MouseEvent event)throws Exception{
        //Retrieves the Contact.fxml file
        loader = new FXMLLoader(getClass().getResource("Contact.fxml"));
        Parent root = (Parent)loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    //Transition into the workspace
    private void fadeOut(){
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(1000));
        fade.setNode(root);
        fade.setFromValue(1);
        fade.setToValue(0);
        
        fade.setOnFinished(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                loadNext();
            }
        });
        
        fade.play();
    }
    
    //Loads the Workspace.fxml file after transition (fadeOut())
    private void loadNext(){
        try{
            
            Parent secondView;
            secondView = (AnchorPane)FXMLLoader.load(getClass().getResource("Workspace.fxml"));
            
            Scene newScene = new Scene(secondView);
            
            Stage current = (Stage) root.getScene().getWindow();
            
            current.setScene(newScene);
            
        }catch (IOException ex){
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
