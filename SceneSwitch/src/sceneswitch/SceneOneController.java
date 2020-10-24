package sceneswitch;

import java.io.IOException;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class SceneOneController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private void handleButtonClick(ActionEvent event){
        madeFadeOut();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    private void madeFadeOut() {
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(1000));
        fade.setNode(rootPane);
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
    
    private void loadNext(){
        try{
            Parent secondView;
            secondView = (AnchorPane)FXMLLoader.load(getClass().getResource("SceneTwo.fxml"));
            
            Scene newScene = new Scene(secondView);
            
            Stage current = (Stage) rootPane.getScene().getWindow();
            
            current.setScene(newScene);
            
        }catch (IOException ex){
            Logger.getLogger(SceneOneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
