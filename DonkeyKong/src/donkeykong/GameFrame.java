package donkeykong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author todov
 */

//frame where the game is being played, constaints a GamePanel
public class GameFrame extends JFrame { 
    GamePanel gamePanel; //panel that is used in current frame
    GameModel gameModel; //current game model that uses this frame
    MenuPanel menuPanel; //menu panel 

    public GameFrame(GameModel model, MenuPanel panel, int width, int height) throws IOException //constructor
    {
        //set properties of the frame
        JTextField title = new JTextField("Donkey Kong");
        setTitle(title.getText()); 
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //don't exit the whole game, overriden dispose function
        addWindowListener(new FrameListener()); //adding listener
        setVisible(true);
        setSize(width,height);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.gameModel = model; //get model
        this.menuPanel = panel; //get panel
        gamePanel = new GamePanel(model); //create a gamePanel from a gameModel
        gamePanel.setSize(width,height);
        this.add(gamePanel); //add panel to the frame
    }

    public GameFrame(GameModel model, MenuPanel panel) throws IOException //constructor
    {
        this(model, panel, 640, 680);
    }
    
    class FrameListener extends WindowAdapter //on window close operation, ask whether it's okay to exit
    {
       public void windowClosing(WindowEvent e)
        {
            gameModel.wait = true; //set gameModel to not update anymore - will be stuck in a loop until wait parameter set otherwise
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Your score is " + gameModel.score + ". Are you sure you want to exit?", "Exit", dialogButton);
            if(dialogResult == 0)
            {
                //yes option, therefore go back to the main menu
                gameModel.username = JOptionPane.showInputDialog("Please input your username:");
                if (gameModel.username.equals("")) gameModel.username = "unknown";
                gameModel.gameOver = true; 
                try {
                    menuPanel.updateTableWithNew(gameModel.username, gameModel.score); //update menuPanel with current username and score
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //close the window
            }
            else
            {
                //no option, therefore continue
                gameModel.wait = false; //continue playing the game
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //don't close the frame

            }
        }
    }
    
    public GamePanel getGamePanel() //return function for private gamePanel
    {
        return gamePanel;
    }
		
}