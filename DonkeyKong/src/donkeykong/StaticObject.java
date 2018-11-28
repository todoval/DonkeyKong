package donkeykong;

/**
 *
 * @author todov
 */

//abstract game object class - all static objects inherit from this class
public abstract class StaticObject
{
    protected float xPos; //position on x coordinate
    protected float yPos; //position on y coordinate
    protected float height; //height of object
    protected float width; //width of object

    public StaticObject(int x, int y, int h, int w) //constructor
    {
        xPos = x;
        yPos = y;
        height = h;
        width = w;
    }
    
    //return functions for parameters

    public float getXPos()
    {
        return xPos;
    }
    
    public void setXPos(float x)
    {
        xPos = x;
    }
    
    public float getYPos()
    {
        return yPos;
    }
    
    public void setYPos(float y)
    {
        yPos = y;
    }
    
    public float getHeight()
    {
        return height;
    }
    
    public float getWidth()
    {
        return width;
    }   
}