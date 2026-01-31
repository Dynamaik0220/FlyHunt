package game;

public class MovingThing {                             //every physical item existant and moveable in game world

    Location thingLocation;             // thing attributes: has location, has velocity, maybe accel.
    int typeOfMovement = 0;
    boolean presentInGame = true;
    double xVelocityMovement = 0;
    double yVelocityMovement = 0;
    double xAccelerationVelocity = 0;
    double yAccelerationVelocity = 0;
    double aSinusAmplitude;
    double bSinusPeriodFactor;

    double ySinusMovement = 0; //store even outside method to try to avoid conflicts on boundaryActions


    //ArrayList<Thing> listOfThings = new ArrayList<Thing>();     //not active  list of things realized in gameEngineDemo.java

    public MovingThing(int xStart, int yStart, double xMovement, double yMovement, double xAccel, double yAccel, double aSinusAmplitude, double bSinusPeriodFactor, int typeOfMovement ){      //constuctor
        this.typeOfMovement = typeOfMovement;
        thingLocation = new Location(xStart, yStart);
        xVelocityMovement = xMovement;
        yVelocityMovement = yMovement;
        xAccelerationVelocity = xAccel;
        yAccelerationVelocity = yAccel;
        this.bSinusPeriodFactor = bSinusPeriodFactor;
        this.aSinusAmplitude = aSinusAmplitude;
    }



    //public  void addToList(Thing thing){            //method: add to list (not active)
     //   listOfThings.add(thing);
    //}

    public int[] getLocation(){                        //method: return location as 2x2 Array
        int[] locationArray = new int[] {thingLocation.getX(), thingLocation.getY()};
        return locationArray;
    }

    //following methods could all be merged into one, lets see

    public void performAction(int xMinusBoundary, int xPlusBoundary, int yMinusBoundary, int yPlusBoundary, int cycleCount, int maxCycleCount){
        performReflectionOnBoundary(xMinusBoundary, xPlusBoundary, yMinusBoundary, yPlusBoundary);
        switch(typeOfMovement){
            case 1:
                performMovement();
            break;
            case 2:
                performTeleportOnBoundary(xMinusBoundary, xPlusBoundary, yMinusBoundary, yPlusBoundary );
                performMovement();
            break;
            case 3:
                performReflectionOnBoundary(xMinusBoundary, xPlusBoundary, yMinusBoundary, yPlusBoundary);
                performMovement();
                //System.out.println("===detected 3===");
            break;
            case 4:
                performMovement();
                performSinusMovement(cycleCount, maxCycleCount);
                performTeleportOnBoundary(xMinusBoundary, xPlusBoundary, yMinusBoundary, yPlusBoundary);
                break;
            case 5:
                performReflectionOnBoundary(xMinusBoundary, xPlusBoundary, yMinusBoundary, yPlusBoundary);
                performMovement();
                performSinusMovement(cycleCount, maxCycleCount);
                constrainToBoundaries(xMinusBoundary, xPlusBoundary, yMinusBoundary, yPlusBoundary);
                break;
            default:
                performMovement();
                System.out.println("performAction effed up");
                break;
        }
    }


    public void performMovement(){                            //method: move Thing by velocityMovement and add accelerationVelocity to velocityMovement
        thingLocation.moveBy(xVelocityMovement, yVelocityMovement);

        if(xVelocityMovement < 30 && yVelocityMovement < 30){   //when velocity gets greater than resolution, game will break
            xVelocityMovement += xAccelerationVelocity;
            yVelocityMovement += yAccelerationVelocity;
        }
    }

    public void performSinusMovement(int cycleCount, int maxCycleCount){     //to work: movement(velocity) in y direction should(neednt) be zero
        //some result could be stored instead of being calced every time
        double bPeriod = 6.28319;       //2*pi
        double factor = bPeriod / maxCycleCount;        // factor is x size of parts into wich curve is segmented
        double xSinusNow = cycleCount * factor;
        double ySinusMovement = aSinusAmplitude * (Math.sin(bSinusPeriodFactor * xSinusNow) - Math.sin(bSinusPeriodFactor * (xSinusNow - factor)));
        thingLocation.moveBy(0, ySinusMovement);
    }

    public void performReflectionOnBoundary(int xMinusBoundary, int xPlusBoundary, int yMinusBoundary, int yPlusBoundary){          //method: if thing is near to boundary reverse direction for dimension
                                                                                                   //int: define boundaries
        if(thingLocation.getX() <= xMinusBoundary){
            xVelocityMovement = Math.abs(xVelocityMovement);
            thingLocation.setX(xMinusBoundary + 1);
        } else if(thingLocation.getX() >= xPlusBoundary){
            xVelocityMovement = -Math.abs(xVelocityMovement); 
            thingLocation.setX(xPlusBoundary - 1);
        }

        // Y-Achse abprallen
        if(thingLocation.getY() <= yMinusBoundary){
            yVelocityMovement = Math.abs(yVelocityMovement);
            thingLocation.setY(yMinusBoundary + 1);
        } else if(thingLocation.getY() >= yPlusBoundary){
            yVelocityMovement = -Math.abs(yVelocityMovement);
            thingLocation.setY(yPlusBoundary - 1);
        }
    }

    public void performTeleportOnBoundary(int xMinusBoundary, int xPlusBoundary, int yMinusBoundary, int yPlusBoundary){
        if(thingLocation.getX() <= xMinusBoundary) thingLocation.setX(xPlusBoundary);
        if(thingLocation.getX() >= xPlusBoundary) thingLocation.setX(xMinusBoundary);
    }
    
    public void constrainToBoundaries(int xMinusBoundary, int xPlusBoundary,
                                      int yMinusBoundary, int yPlusBoundary){
        if(thingLocation.getX() < xMinusBoundary) thingLocation.setX(xMinusBoundary);
        if(thingLocation.getX() > xPlusBoundary) thingLocation.setX(xPlusBoundary);
        if(thingLocation.getY() < yMinusBoundary) thingLocation.setY(yMinusBoundary);
        if(thingLocation.getY() > yPlusBoundary) thingLocation.setY(yPlusBoundary);
    }


} //public clas end     //possibly sub class
/*
class Enemy extends Thing{       //enemy; ie fly or parent to fly (ignore)




int health = 10;

//public

}
*/

