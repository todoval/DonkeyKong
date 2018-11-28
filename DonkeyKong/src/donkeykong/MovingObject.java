package donkeykong;

import java.util.ArrayList;

/**
 *
 * @author todov
 */

//all moving objects (Mario, Barrels) inherit from this class
public abstract class MovingObject extends StaticObject {
    
    protected float xVel; //velocity in the x plane
    protected float yVel; //velocity in the y plane
    protected float dx; //change in x plane
    protected float dy; //change in y plane

    protected boolean isClimbing = false; //for mario
    protected boolean collidingWithPeach = false; //true if there is collision with Peach, for mario
    protected StaticObject collidingWithLadder; //ladder, with which mario is colliding with
    protected float gravity = 2; //gravity of the current movingObject
    protected boolean pointAwarded = false; //there can be only one point awarded for one barrel
    protected ArrayList<StaticObject> SOList; //to check for collisions

    public MovingObject(int x, int y, int h, int w, ArrayList<StaticObject> SOList) //constructor
    {
        super(x, y, h, w);	
        this.SOList = SOList;
    }

    public void act(int time) //what moving object does in one "tick"
    {
        if (standing()) dy = 0; //when standing, don't change y coord
        if (!isClimbing) dy += gravity * time; //when climbing, change y coord
    }
    
    //directions to be implemented in subclasses
    public abstract boolean left();
    public abstract boolean right();
    public abstract boolean up();
    public abstract boolean down();
    
    public boolean standing() //check if object is standing on a platform
    {
        for(StaticObject SO : SOList)
        {
            if (!(SO instanceof Platform))
            {
                continue;
            }
            float l1 = xPos, r1 = xPos+width, b1 = yPos+height;
            float l2 = SO.xPos, r2 = SO.xPos+SO.width, t2 = SO.yPos, b2 = SO.yPos+SO.height;
            if((b1 <= b2 && b1 >= t2) && r1+40 >= l2 && l1 <= r2+40) //+40 for situations where mario might be slightly over the platform
            { 
                yPos = t2 - height; //make object stand exactly on top of the platform 
                return true; //standing on a platform
            }
        }
        return false; //not standing on a platform
    }

    public boolean checkSOCollisions(ArrayList<StaticObject> SOList) //check for a collision with static object
    {
        for(StaticObject SO : SOList)
        {
            float l1 = xPos, r1 = xPos+width, t1 = yPos, b1 = yPos+height; // get the left side, right side, top and bottom coordinates of the player
            float l2 = SO.xPos, r2 = SO.xPos+SO.width, t2 = SO.yPos, b2 = SO.yPos+SO.height; // set the left side, right side, top and bottom coordinates of the other object
            if (!(l1>=r2 || l2>=r1 || t1>=b2 || t2>=b1) && t2 < b1 && b1 > b2) //necessary condition for collision
            {
                if (SO instanceof Ladder) collidingWithLadder = SO; //colliding with ladder
                if (SO instanceof Peach) collidingWithPeach = true; //colliding with Peach - therefore we win
                return true; //return collision
            }
        }	
        // no collision (therefore none with ladder or peach)
        collidingWithLadder = null;
        collidingWithPeach = false;
        return false;
    }

    public boolean checkMOCollision(ArrayList<MovingObject> MOList) //check for a collision with Moving Object
    {
        for(int i = 1; i < MOList.size(); i++)
        {
            MovingObject MO = MOList.get(i);
            float l1 = xPos, r1 = xPos+width, t1 = yPos, b1 = yPos+height; // set the left side, right side, top and bottom coordinates of the player
            float l2 = MO.xPos, r2 = MO.xPos+MO.width, t2 = MO.yPos, b2 = MO.yPos+MO.height; // set the left side, right side, top and bottom coordinates of the other object
            if (!(l1>=r2 || l2>=r1 || t1>=b2 || t2>=b1)) return true; //condition for collision, game over
        }
        return false; //no collision
    }
    
    public void setPointAwarded()
    {
        pointAwarded = true;
    }
    
    public boolean pointAwarded()
    {
        return pointAwarded;
    }
    
} 

    
