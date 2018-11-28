package donkeykong;

import java.util.ArrayList;

/**
 *
 * @author todov
 */

public class Barrel extends MovingObject {
    
    private boolean direction;     //false - left, true - right
    private boolean moveDownLadder; //need to be ihnerited from moving object
    boolean pointAwarded = false; //whether the point was awarded for this barrel, default wasn't
    private int distanceFallen = 0; //keep track of the distance fallen in order to change direction 

    public Barrel(int x, int y, int h, int w, ArrayList<StaticObject> SOList, boolean d) //constructor for barrel
    {
        super(x, y, h, w, SOList); //super constructor
        direction = true; //set direction to right - starting from kong to the right
        xVel = 4;
    }

    public void act(int time)
    {
        super.act(time);
        dy = gravity * time;
        if(distanceFallen > 50 && standing()) direction = !direction; //if falling for longer than 2 time units, change direction
        if(standing())
        {
            distanceFallen = 0; //reset distance fallen
            if(direction) dx = xVel;
            else dx = -xVel;
        }
        else
        {
            if(distanceFallen > 0) dx = 0f; //only let a barrel pause in its horizontal movement if it falls a long distance
        }
        //update
        xPos += dx;
        yPos += dy;
        distanceFallen += dy;
    }

    //implementing all abstract functions for barrel
    public boolean left() 
    {
        return !direction;
    }

    public boolean right()
    {
        return direction;
    }

    public boolean up()
    {
        return false;
    }

    public boolean down()
    {
        return moveDownLadder;
    }
    
}
