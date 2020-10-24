package micasa;

import javafx.scene.shape.Circle;

/*
    Determines the area of the floor plan based on its side lengths
    Author: Adam Cook
*/

public class AreaCalc {
    public int numVertices;
    public double area;
    private double[] xCoord;
    private double[] yCoord;
    
    /*
        Area for irregular polygons with n vertices (NOT SCALED):
        A = [(x1y2 - y1x2) + (x2y3 - y2x3) + ... + (xny1 - ynx1))] / 2
    */
    
    public double findArea(int count, Circle[] points){
        numVertices = count;
        area = 0.0;
        xCoord = new double[count];
        yCoord = new double[count];
        
        if(count >= 1) {
            //Fills x and y coordinate of each vertex
            for(int i=0; i<count; i++){
                xCoord[i] = points[i].getCenterX() / WorkspaceController.FOOT;
                yCoord[i] = points[i].getCenterY() / WorkspaceController.FOOT;
            }
        
            //Computes the area
            for(int j=0; j<count-1; j++){
                area += ((xCoord[j]*yCoord[j+1]) - (yCoord[j]*xCoord[j+1]));
            }
        
            area += ((xCoord[count-1]*yCoord[0])-(yCoord[count-1]*xCoord[0]));
            area /= 2;
        
            //Ensure that the area remains positive
            if(area < 0)
                area *= -1;       
        }
        
        return area;
    }
}