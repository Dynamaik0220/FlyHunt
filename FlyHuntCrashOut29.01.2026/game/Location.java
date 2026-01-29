package game;

class Location {          //class: lcoation
    double xLocation;
    double yLocation;
    boolean beyondPlain = false;        //

    public Location(double xStartLocation, double yStartLocation) {  //object constuctor
        xLocation = xStartLocation;
        yLocation = yStartLocation;
    }

    public void moveBy(double xMoveBy, double yMoveBy) {     //method: add coordinates to locations coordinates
        xLocation += xMoveBy;
        yLocation += yMoveBy;
    }

    /*
        public void moveTo(int xMoveTo, int yMoveTo){      //method: change location to ... depreciated
            if(xMoveTo >= 0){
                xLocation = xMoveTo;
            }
            if(yMoveTo )
            yLocation = yMoveTo;
        }
    */
    public void setX(int xSet) {         //method: set x coordinates as int
        xLocation = xSet;
    }

    public void setY(int ySet) {         //method: set coordinates as int
        yLocation = ySet;
    }

    public int getX() {          //method: get x coordinate as int
        return (int) Math.round(xLocation);
    }

    public int getY() {           //method: get y coordinate as int
        return (int) Math.round(yLocation);
    }

}
