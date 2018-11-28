package donkeykong;

import java.util.ArrayList;

/**
 *
 * @author todov
 */
public class Mario extends MovingObject {
   
    //constants
    private final float jumpHeight = 3.6f; //height of mario's jump
    
    //booleans that say which keys were pressed
    private boolean goLeft;
    private boolean goRight;
    private boolean goUp;
    private boolean goDown;
    private boolean jump; //the enter key was pressed
    
    private boolean keysDown[] = new boolean[255]; //array of boolean telling us which key is pressed
    public boolean jumping; //whether or not is mario currently jumping
    private boolean hasWon = false; //default initialization

    public Mario(int x, int y, int h, int w, ArrayList<StaticObject> SOList) //constructor
    {
        super(x, y, h, w, SOList);
        xVel = 5f;
        yVel = 5f;
    }
    
    public void setToDefault() //set all mario's parameters to default
    {
        this.goLeft = false;
        this.goRight = false;
        this.goUp = false;
        this.goDown = false;
        this.jump = false;
        for (int i = 0; i < 255;i++) this.keysDown[i] = false; 
        this.jumping = false;
        this.hasWon = false;
    }

    @Override
    public void act(int time)
    {
        //initialize changes
        dx = 0;
        dy = 0;

        //call the super act function for gravity and standing on platform
        super.act(time);
        readInput();
        if(jump && !jumping && standing()) jumping = true; //start jumping
        if (standing() || collidingWithLadder == null) isClimbing = false;  //don't climb unless colliding with ladder
        if((collidingWithLadder != null)) isClimbing = true; //if mario is colliding with ladder - set climbing to true
        dx += (goRight ? xVel : 0) - (goLeft ? xVel : 0); //change the change of x coordinate
        if (isClimbing) dy += (goDown ? yVel : 0) - (goUp ? yVel : 0); //if mario's climbing, change the change of the y coordinate
        if(jumping) dy += -jumpHeight; //if mario is jumping, change the y coordinate
        xPos += dx; 
        yPos += dy;
        if (collidingWithLadder != null && !standing()) xPos -= dx; //disable falling of the ladder
        if (checkSOCollisions(SOList)) // there was a collision
        {
            if (collidingWithPeach) hasWon = true; //if mario has reached peach, game is over
            if (collidingWithLadder == null) yPos -= dy; //disable moving if the movement on y coord would result in a collision
        }
        if (this.xPos >= 230 && this.xPos <=270 && this.yPos <= 130 && this.yPos >= 90) hasWon = true; //check whether he's not near peach - so he doesn't have to be exactly at the same position, peach is at 250 , 105
    }

    public void readInput()
    {
        goLeft = keysDown[37]; //left key
        goRight = keysDown[39]; //right key
        goUp = keysDown[38]; //up key
        goDown = keysDown[40]; //down key
        jump = keysDown[32]; //space key
    }

    public void setKeysDown(boolean[] down) //gets pressed keys from gameModel
    {
        keysDown = down;
    }
    
    //return functions for parameters
    
    @Override
    public boolean left() 
    {
        return goLeft;
    }

    @Override
    public boolean right() 
    {
        return goRight;
    }

    @Override
    public boolean up() 
    {
        return goUp;
    }

    @Override
    public boolean down() 
    {
        return goDown;
    }

    public void setJump(boolean b)
    {
        jumping = b;
    }
    
    public boolean hasWon()
    {
        return hasWon;
    }

}

