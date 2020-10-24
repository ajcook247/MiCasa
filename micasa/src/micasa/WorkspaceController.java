package micasa;

import java.text.NumberFormat;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;

/*
    Controls the workspace field, along with the appropriate actions
    Author: Adam Cook
*/

public class WorkspaceController implements Initializable {

    private static boolean maximized = false; //Determine if the screen is maximized
    private double orgSceneX, orgSceneY; //Keep track of original x and y coordinates
    private FXMLLoader loader; //Loads the FXML files to change scenes
    
    private double userX, userY;
    
    int count = 0; //Index of all points
    Circle[] points = new Circle[100];//Array to keep track of points
    Line[] lines = new Line[100]; //Array to keep track of lines
    Text[] dimList = new Text[100];// Array to keep track of dimensions
    Circle[][] anchors = new Circle[20][20]; //Array to hold grid anchors (TESTING)
    
    @FXML
    private AnchorPane toolbar; //Represents the toolar
    @FXML
    private AnchorPane left; //Represents the left side of the splitpane
    @FXML
    private AnchorPane right; //Represents the right side of the splitpane
    @FXML
    private SplitPane sp; //Represents the SplitPane
    @FXML
    private GridPane grid; //Represents the grid on the right panel
    @FXML
    private TextField tf; //Represents the textfield for the user to enter their title
    @FXML
    private Button newPoint; //Represents the new point button
    @FXML
    private Button clearAll; //Represents the button to clear the workspace of all shapes
    @FXML
    private Button save; //Represents the button to save the workspace scene as an image
    @FXML
    private Text sqfeet; //Calculation label for area in feet
    @FXML
    private Text sqmeters; //Calculation area for area in meters
    @FXML
    private TilePane tp; //Represents the tilepane of anchor points
    @FXML
    private ComboBox cb; //Represents the combo box to select furniture options
    @FXML
    private Button addIn; //Represents the button to add in furniture based on the combo box
    
    Shape furn; //Default shape
    Shape[] furniture = new Shape[49]; //Array of furniture (maximum of 50)
    int furnCount = 0; //Default count of furniture
    
    ObservableList<String> items = FXCollections.observableArrayList("Table (Long)", "Table (Short)", "Table (Round)"); //Available options to add in
    
    private Line line; //Line that connects anchor points
    private Circle c; //Represents the anchor point
    
    NumberFormat f = new DecimalFormat("#0.00"); //Decimal format for area calculations
    Delta d = new Delta(); //Used to record and change x and y coordinates of window
    AreaCalc a = new AreaCalc(); //Used to calculate the area of the floor plan
    
    //Constants for calculating floor dimensions
    final static double FOOT = 46.0; //Roughly 46 pixels per 'foot'
    final static double METER = 0.092903; //Conversion from sq. ft. to sq. m.
    
    //Closes the window
    @FXML
    private void handleClose(ActionEvent event) {
        //Gets the source window and hides it
        ((Node)(event.getSource())).getScene().getWindow().hide();
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
        //Resets window size if already maximized
        else{
            stage.setMaximized(false);
            maximized = false;
        }
        
    }
    
    //Handles minimizing the window
    @FXML
    private void handleMin(ActionEvent event){
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    
    //Handles creating a new point along with its proper connections (OPTIMIZE)
    @FXML
    private void newPoint(ActionEvent event){

        //REtrieves the user's last known click on the workspace
        right.setOnMousePressed(me ->{
            userX = me.getX();
            userY = me.getY();
        });
        
        //Creates a circle at that last point
        c = createCircle(userX, userY, 10, Color.BLACK, count);
        
        points[count] = c;
        right.getChildren().addAll(c);
        right.getChildren().remove(lines[0]);
        
        Line l = new Line();
        Line lEnd = connect(c, points[0]);
        
        if(count == 1){
            l = connect(c, points[count-1]);
            right.getChildren().addAll(l);
        }
        else if(count >= 2){
            l = connect(c, points[count-1]);
            right.getChildren().addAll(l);
        }
        
        lines[0] = lEnd;
        lines[count+1] = l;
        right.getChildren().addAll(lines[0]);
        
        //Text that denotes the length of a line (in feet)
        dimList[count] = new Text("");
        dimList[count].setText(lineLength(line));
        dimList[count].setX(c.getCenterX() + 10);
        dimList[count].setY(c.getCenterY() + 10);
        right.getChildren().addAll(dimList[count]);
        
        count++;
    }
    
    //Line connecting nodes to denote walls
    private Line connect(Circle c1, Circle c2){
        line = new Line();

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());

        line.setStrokeWidth(5);
        line.setStrokeLineCap(StrokeLineCap.ROUND);

        return line;
    }
    
    //Creates a new circle that acts as an anchor point
    private Circle createCircle(double x, double y, double radius, Color color, int index) {
        //Creates circle with given parameters
        Circle circle = new Circle(x, y, radius, color);
        circle.setOpacity(0.5);
        points[index] = circle;
        
        //Gets the circle's x and y coordinates once pressed
        circle.setOnMousePressed(me ->{
            orgSceneX = me.getSceneX();
            orgSceneY = me.getSceneY();
        });
        
        //Allows movement
        circle.setOnMouseDragged(me ->{
        
            double offsetX = me.getSceneX() - orgSceneX;
            double offsetY = me.getSceneY() - orgSceneY;
        
            Circle c = (Circle) (me.getSource());
           
            //Changes the circle's position based on mouse movement and last location
            c.setCenterX(c.getCenterX() + offsetX);
            c.setCenterY(c.getCenterY() + offsetY);
            
            //Prevents node from exiting boundaries (OPTIMIZE)
            if(c.getCenterX() < 0)
                c.setCenterX(0);
             if(c.getCenterY() < 0)
                c.setCenterY(0);
            if(!maximized){
                if(c.getCenterX() > 592)
                    c.setCenterX(592);
                if(c.getCenterY() > 560)
                    c.setCenterY(560);
            }else if(maximized){
                if(c.getCenterX() > 1192)
                    c.setCenterX(1192);
                if(c.getCenterY() > 860)
                    c.setCenterY(860);
            }
           
            //Prevents node from entering the title box
            if(c.getCenterX() >= 0 && c.getCenterX() < 230 && c.getCenterY() >= 0 && c.getCenterY() < 50){
                c.setCenterX(c.getCenterX());
                c.setCenterY(50);
            }
               
            orgSceneX = me.getSceneX();
            orgSceneY = me.getSceneY();
           
            //Move dimension text when anchor point moved
            dimList[index].setText(lineLength(line));
            dimList[index].setX(points[index].getCenterX() + 10);
            dimList[index].setY(points[index].getCenterY() + 10);
            
            //Snap to nearby anchor points
            for(int i=0; i<7; i++){
                for(int j=0; j<7; j++){
                    if((int)c.getCenterX() >= (int)anchors[i][j].getCenterX()-5 && (int)c.getCenterX() <= (int)anchors[i][j].getCenterX()+5 &&
                       (int)c.getCenterY() >= (int)anchors[i][j].getCenterY()-5 && (int)c.getCenterY() <= (int)anchors[i][j].getCenterY()+5){
                            c.setCenterX((int)anchors[i][j].getCenterX());
                            c.setCenterY((int)anchors[i][j].getCenterY());
                    }
                }
            }
        });

        return circle;
    }
    
    //Calculates the length of each wall
    private String lineLength(Line l){
        double xVal = Math.abs(line.getStartX() - line.getEndX()); //Length along x-axis
        double yVal = Math.abs(line.getStartY() - line.getEndY()); //Length along y-axis
        double h = Math.sqrt(Math.pow(xVal, 2) + Math.pow(yVal, 2)); //Pythagorean Theorem
        String hyp = String.valueOf(f.format(h/46)); //DIVIDE BY 46 FOR FEET
        return hyp;
    }
    
    //Clears the pane of all of its contents, and adds back in the grid and title space
    @FXML
    private void clearAll(ActionEvent event){
        right.getChildren().clear();
        right.getChildren().addAll(grid, tf, tp);
        for(int i=0; i<count; i++){
            points[i] = null;
            lines[i] = null;
        }
        count = 0;
        createAnchors();
    }
    
    //Opens the help menu
    @FXML
    private void help(MouseEvent event) throws Exception{
        //Retrieves the Help.fxml file
        loader = new FXMLLoader(getClass().getResource("Help.fxml"));
        Parent root = (Parent)loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    //Saves the user's plan as an image and converts to a PDF
    @FXML
    private void saveImage(ActionEvent event){
        //Ensures that there are items within the workspace to save the file
        if(count == 0)
            System.out.println("Nothing in the workspace");
        else{
            //Creates the File Explorer, default to the "Computer" page
            FileChooser fileChooser = new FileChooser();
        
            //Set extension filter to images only
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
         
            //Prompt user to select a file
            File file = fileChooser.showSaveDialog(null);
        
            if(file != null){
                try {
                    //Captures the workspace area
                    WritableImage writableImage = new WritableImage((int)right.getWidth(), (int)right.getHeight());
                    
                    //Takes a snapshot of the workspace and renders it
                    right.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                
                    //Write the snapshot to the chosen file
                    ImageIO.write(renderedImage, "png", file);

                    //Converts the rendered image file into a PDF document
                    ConvertPDF.convert(file);
                    
                } catch (IOException ex) { 
                    ex.printStackTrace(); 
                }
            }
        }
    }
    
    //Retrieves the area of the polygon
    @FXML
    private void getArea(ActionEvent action){
        double area = a.findArea(count, points);
        //System.out.println(f.format(area));
        sqfeet.setText(f.format(area) + " sq. ft.");
        sqmeters.setText(f.format(area*METER) + " sq. m.");
    }
    
    //Creates a grid of anchor points to snap to
    private void createAnchors(){
        double w = (tp.getPrefWidth()/13.0) * 2;
        double h = (tp.getPrefHeight()/13.0) * 2;
        int rows = tp.getPrefRows() / 2;
        int cols = tp.getPrefColumns() / 2;
        
        //Initializes each point in the 2D array and adds it in
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                anchors[i][j] = new Circle(10);
                anchors[i][j].setFill(Color.GREY);
                anchors[i][j].setOpacity(0.8);
                anchors[i][j].setCenterX(j * w);
                anchors[i][j].setCenterY(i * h);
                right.getChildren().add(anchors[i][j]);
            }
        }
        
        //Removes anchors that cover the title bar
        for(int i=0; i<3; i++)
            right.getChildren().remove(anchors[0][i]);
    }
    
    //Selects type of furniture to add based on the ComboBox
    @FXML
    private void addFurn(ActionEvent event){
        String furnItem = cb.getValue().toString();
        
        //Adds in a furniture shape depending on option selected
        switch(furnItem){
            case "Table (Long)":
                createFurn(100, 100, 100, 50, Color.BROWN);
                break;
            case "Table (Short)":
                createFurn(100, 100, 50, 50, Color.BROWN);
                break;
            case "Table (Round)":
                createFurn(100, 100, 25, Color.BROWN);
                break;
        }
    }
    
    //Creates a rectangular furniture object and allows movement
    private Shape createFurn(double x, double y, double w, double h, Color c){
        furn = new Rectangle(x, y, w, h);
        furn.setFill(c);
        furniture[furnCount] = furn;
        furnCount++;
        right.getChildren().add(furn);
        
        //Gets the last x and y coordinates
        furn.setOnMousePressed(me ->{
            orgSceneX = me.getSceneX();
            orgSceneY = me.getSceneY();
        });
        
        //Allows movement based on mouse movement and last location
        furn.setOnMouseDragged(me -> {
            Node n = (Node)me.getSource();
            n.setTranslateX(n.getTranslateX() + me.getX() - x);
            n.setTranslateY(n.getTranslateY() + me.getY() - y);
        });
        
        return furn;
    }
    
    //Creates a circular furniture object and allows movement
    private Shape createFurn(double x, double y, double r, Color c){
        furn = new Circle(x, y, r, c);
        furniture[furnCount] = furn;
        furnCount++;
        right.getChildren().add(furn);
        
        //Gets the last x and y coordinates
        furn.setOnMousePressed(me ->{
            orgSceneX = me.getSceneX();
            orgSceneY = me.getSceneY();
        });
        
        //Allows movement based on mouse movement and last location
        furn.setOnMouseDragged(me -> {
            Node n = (Node)me.getSource();
            n.setTranslateX(n.getTranslateX() + me.getX() - x);
            n.setTranslateY(n.getTranslateY() + me.getY() - y);
        });
        
        return furn;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Fixes the SplitPane divider
        sp.getItems();
        sp.setDividerPositions(0.25);
        left.minWidthProperty().bind(sp.widthProperty().multiply(0.25));
        left.maxWidthProperty().bind(sp.widthProperty().multiply(0.25));
        
        //Creates a grid of anchor points
        createAnchors();

        //Sets items of the combo box
        cb.setItems(items);
        
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