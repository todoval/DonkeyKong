package donkeykong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JOptionPane;

/**
 *
 * @author todov
 */
public final class GameModel extends Observable {
    
    //constants
    final int SCREEN_X = 640; //width of screen
    final int SCREEN_Y = 680; //height of screen
    final int platform_HEIGHT = 20; //height of platform
    final int platform_WIDTH = 30; //width of platform
    final private int gravityTime = 10; //handle gravity every 10 miliseconds
    final private int barrelSpawnTime = 150; //barrel spawns every 150 miliseconds
    
    public boolean wait = false; //whether this model should be paused
    public String username; //username that will be asked at the end of the game
    public int score; //current score 
    public boolean gameOver; //true if game is over
    private ArrayList<StaticObject> SOList; //all static objects
    public ArrayList<MovingObject> MOList; //all moving objects
    private int timer = 0; //current time, start at 0

    private int epochs; //a variable used only for iteration and ending the game if it takes too long
    public ArrayList<Integer> gravityTimes; //gravity time for every moving object - every object with index i in MO has gravityTime in this list on index i 
    private Mario mario; //player
    private Peach peach; //princess
    
    GameModel() //constructor, initialize game
    {
        initGame();	
    }
    
    public boolean startGame(GameFrame frame) throws InterruptedException, IOException
    {
        while(epochs < 10000) //after epochs read 10000, the game will end
        {
            while (wait == true) //loop for JFrame - when wait is true, game is "paused"
            {
                Thread.sleep(1);
            }
            epochs++;
            timer++;   
            if(timer % gravityTime == 0) incrementTime(); //handle gravity every 10 milliseconds
            if(timer % barrelSpawnTime == 0) spawnBarrel(); //spawn a barrel every 150 milliseconds         
            if(timer > 1500) timer = 0; //reset timer eventually, to avoid overflow
            for(int i = 0; i < MOList.size(); i++)
            {
                //make all moving objects act/move
                MOList.get(i).act(gravityTimes.get(i));
                if(mario.hasWon()) //mario has won
                {
                    if (!continueAfterWin()) return false;
                }
                if(mario.checkMOCollision(MOList)) //mario is hit by a barrel
                {
                    //mario is dead
                    endLostGame(); //lost game function
                    if (gameOver) //player has chosen not to play again
                    {
                        frame.dispose(); //close the frame -> returning to main menu
                        return false; //return statements for menuPanel function
                    }
                    else return true;
                }
                else if(MOList.get(i).getYPos() >= SCREEN_Y) //delete object if it's out of screen
                {
                    if (mario.equals(MOList.get(i))) //mario fell out of screen
                    { //game lost
                        endLostGame(); //lost game function
                        if (gameOver) //player has chosen not to play again
                        {
                            frame.dispose(); //close the frame -> returning to main menu
                            return false; //return statements for menuPanel function
                        }
                        else return true;
                    }
                    else
                    {
                        //the object falling out of the screen is a barrel, therefore remove barrel and it's gravity time out of screen
                        MOList.remove(i);
                        gravityTimes.remove(i);
                    }
                }
                //increase score when jumping over barrel
                else checkForPoints(MOList.get(i));
            }
            GamePanel tempPanel = frame.getGamePanel(); //get panel from the frame
            tempPanel.repaint(); //repaint the panel
            frame.add(tempPanel); //show panel on the frame
            Thread.sleep(15);
        }
        return true;
    }
    
    private void checkForPoints(MovingObject tempMov) //check whether mario is jumping over a barrel and whether to add points
    {
        if(tempMov.getYPos() >= mario.getYPos() && tempMov.getYPos() <= mario.getYPos() + 100 && 
            mario.getXPos() >= tempMov.getXPos()	&&
            mario.getXPos() <= tempMov.getXPos()+tempMov.getWidth() &&
            !(tempMov.pointAwarded) && mario.jumping && !mario.equals(tempMov))
        {
            tempMov.setPointAwarded();
            score += 100;
        }
    }
 
    private boolean continueAfterWin() throws InterruptedException //what to do after player has won
    {
        score+=1000; //add points for winning the game
        JOptionPane.showMessageDialog(null, "You have won! Your score is " + score);
        //no option, therefore end and save score
        this.username = JOptionPane.showInputDialog("Please input your username:");
        if (username.equals("")) username = "unknown";
        gameOver = true;
        Thread.sleep(50); //wait for a while
        return false;
    }
    
    private void endLostGame() throws InterruptedException //what to do after player has lost
    {
        //mario is dead
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Your score is " + score + ". Do you want to try again?", "Game over", dialogButton);
        if(dialogResult == 0)
        {
            //yes option, therefore init again and start over
            MOList.clear();
            gravityTimes.clear();
            score = 0;
            initGame();
            Thread.sleep(30);
        }
        else
        {
            //no option, therefore end and save score
            this.username = JOptionPane.showInputDialog("Please input your username:");
            if (username.equals("")) username = "unknown";
            gameOver = true;  
            Thread.sleep(50);
        }
    }
    
    public void initGame() //initialize the beginning of the game
    {
        initStaticObjects();
        initMovingObjects();
        timer = 0;
        gameOver = false;
        mario.setToDefault();
    }

    private void initStaticObjects() //initialize all static objects - add static objects to SOList
    {
        //initialize list
        SOList = new ArrayList<>();

        //create platforms		

        //bottom layer first half
        for(int i = 0; i < SCREEN_X/2; i = i + platform_WIDTH)
        {
            SOList.add(new Platform(50 + i,SCREEN_Y - 50, platform_HEIGHT, platform_WIDTH));
        }

        //bottom layer second half 
        int y = SCREEN_Y - 50;
        for(int i = SCREEN_X/2; i < SCREEN_X - 50; i = i + platform_WIDTH)
        {
            SOList.add(new Platform(i,y,platform_HEIGHT,platform_WIDTH));
            y = y - 1;
        }

        //second layer
        int x = SCREEN_X - 100;
        y = SCREEN_Y - 150;
        for(int i = x - 20; i > 35; i = i - platform_WIDTH)
        {
            SOList.add(new Platform(i,y,platform_HEIGHT,platform_WIDTH));
            y = y - 1;
        }

        //third layer
        y = SCREEN_Y - 300;
        for(int i = 100; i < SCREEN_X - 50; i = i + platform_WIDTH)
        {
            SOList.add(new Platform(i,y,platform_HEIGHT,platform_WIDTH));
            y = y - 1;
        }

        //upper layer        
        x = SCREEN_X - 100;
        y = SCREEN_Y - 450;
        for(int i = x - 20; i > 50; i = i - 30)
        {
            SOList.add(new Platform(i,y,20,30));
            y = y - 1;
        }
        
        //platform with Peach
        x = 230;
        y = 140;
        SOList.add(new Platform(x, y, 20, 30));
        for (int i = x; i < x + 180; i = i + 30)
        {
            SOList.add(new Platform(i, y, 20, 30));
        }

        //create ladders
        
        //ladder from first
        for(int i = 0; i < 9*10; i += 10)
        {
            SOList.add(new Ladder(500,612-i,10,15));
        }
        
        //first ladder from second
        for(int i = 0; i < 12*10; i += 10)
        {
            SOList.add(new Ladder(120,510-i,10,15));
        }
        
        //second ladder from second
        for(int i = 0; i < 12*10; i += 10)
        {
            SOList.add(new Ladder(200,505-i,10,15));
        }
        
        //ladder from third
        for(int i = 0; i < 12*10; i += 10)
        {
            SOList.add(new Ladder(510,360-i,10,15));
        }
        
        //laddder to peach
        for(int i = 0; i < 6*10; i += 10)
        {
            SOList.add(new Ladder(320,210-i,10,15));
        }
        
        //first ladder next to kong
        for(int i = 0; i < 11*10; i += 10)
        {
            SOList.add(new Ladder(180,210-i,10,15));
        }
        
        //second ladder next to kong
        for(int i = 0; i < 11*10; i += 10)
        {
            SOList.add(new Ladder(220,210-i,10,15));
        }
        
        //add Peach
        peach = new Peach(250,105,35,20);
        SOList.add(peach);
    }

    private void initMovingObjects() //initialize all moving objects - add moving objects to MOList
    {   
        //initialize list
        MOList = new ArrayList<>();
        
        mario = new Mario(60,600,35,20, SOList);
        MOList.add(mario);
        
        //initalize the gravity timers of the moving objects
        gravityTimes = new ArrayList<>();
        for (int i = 0; i < MOList.size(); i++)
        {
            gravityTimes.add(0);
        }
    }
    
    public void spawnBarrel() //adds a new barrel to the game every 150 miliseconds, spawn place is next to kong
    {
        gravityTimes.add(0);
        MOList.add(new Barrel(120,200,20,25, SOList, true));
    }
    
    public void incrementTime() //increments gravity times for moving objects from MOList
    {
        for(int i = 0; i < MOList.size(); i++)
        {
            MovingObject MO = MOList.get(i);
            if(MO.standing())
            {
                gravityTimes.set(i, 0);
                if(MO instanceof Mario) //if object is mario and is standing, set jumping to false
                {
                    ((Mario) MOList.get(i)).setJump(false);
                }			
            }
            else
            {
                gravityTimes.set(i, (gravityTimes.get(i)) + 1) ;
            }		
        }	
    }
    
    public void passKeysDownToPlayer(boolean[] down) //function used to get pressed keys on the keyboard, used in menuPanel
    {
        mario.setKeysDown(down);
    }
    
    //return functions for elements in the class
    public ArrayList<StaticObject> getSOList()
    {
        return SOList;
    }

    public ArrayList<MovingObject> getMOList()
    {
        return MOList;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }
    public int getScore()
    {
        return score;
    }
	    
}   

