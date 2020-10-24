package micasa;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/*
    Controls the tutorial menu, along with the appropriate actions
    Author: Adam Cook
*/

public class HelpController implements Initializable {

    @FXML
    private AnchorPane toolbar; //Represents the toolar
    @FXML
    private AnchorPane root; //Represents the root pane
    @FXML
    private Pagination pagination; //Pagination control between tutorial screens
    @FXML
    private TextArea text; //Represents the tutorial text on each page
    @FXML
    private Text title; //Represents the title of the current tutorial page
    
    Delta d= new Delta(); //Used to record and change x and y coordinates of window
    
    //Titles of each tutorial page
    final String[] titles = new String[]{
        "\n\tCREATING FLOORS", "\n\tSNAPPING", "\n\tMESAUREMENTS", "\n\tADDING FURNITURE", "\n\tCLEARING THE WORKSPACE", "\n\tSAVING PLANS"
    };
    
    //Descriptions of each tutorial
    final String[] tutorial = new String[]{
        "To create new anchor points, click on the 'NEW POINT' button on the left toolbar. " + 
            "When two or more are created, lines will be drawn that represent walls between two" +
            "anchor points. As you move each point around, the wall dimensions will change as well.", 
        "When an anchor points is near one of the grid cell, the point will snap to the center of it. This " +
            "allows for smooth lines connecting all points within the cells. To remove a point from the cell, " + 
            "simply click and hold teh point and drag it out. This action must be done with some speed, otherwise " + 
            "the point will remain snapped.",
        "In MiCasa, the measurements are centered around the lengths of each grid cell side. By default,"+
            "each cell measure 1 foot by 1 foot. Therefore, the area of a 1x1 square will read out as" +
            "1 square foot. Calculations from the imperial system to the metric system are also present.",
        "To add in extra furniture, you must first select an option in the choice box on the far left of the " + 
            "Workspace. Once selected, press the 'Add in' button below it, and the furniture shape will be added" + 
            "into its default location. All furniture objects can be moved freely once added in. (NOTE: As of " + 
            "Version 0.3.0, furniture objects cannot be reshaped or rotated.)",
        "To clear the workspace of all progress, click on the 'CLEAR ALL' button on the left toolbar. " + 
            "Once pressed, all points and walls in the righthand workspace will be removed. It is recommended " + 
            "that you save your progress before clearning the workspace!", 
        "To save your floor plan, click on the 'SAVE PLAN' button on the left toolbar. Once clicked, you " +
            "will be prompted to choose a name and location of your plan. Both an image and a PDF document of your plan will be created. " + 
            "(NOTE: As of Version 0.3.0, plans cannot be reaccessed once saved.)"
    };
    
    //Closes the options window
    @FXML
    private void handleClose(ActionEvent event) {
        //Gets the source window and hides it
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    public VBox createPage(int pageIndex){
        VBox pageBox = new VBox();
        
        title.setText(titles[pageIndex]);
        text.setText(tutorial[pageIndex]);
        
        return pageBox;
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
        
        //Pagination controls and styling
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
    }      
}