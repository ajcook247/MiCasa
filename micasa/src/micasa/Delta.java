package micasa;

/*
    Records the x and y coordinates of the window when moving
    Author: Adam Cook
*/

public class Delta {
    
    //Default x and y
    double x = 0;
    double y = 0;
        
    //Returns the x coordinate
    public double getX(){
        return x;
    }
        
    //Returns the y coordinate
    public double getY(){
        return y;
    }
        
    //Sets the x coordinate to the parameter x
    public void setX(double x){
        this.x = x;
    }
        
    //Sets the y coordinate to the parameter y
    public void setY(double y){
        this.y = y;
    }
}
