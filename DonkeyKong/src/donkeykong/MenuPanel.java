package donkeykong;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author todov
 */

public class MenuPanel extends JPanel
{
    class pair //a structure used to store high score table elements
    {
        String name;
        int score;
        
        pair(String _name, int _score)
        {
            this.name = _name;
            this.score = _score;
        }
    }
    
    private GameModel model; //model of the game
    private GameFrame frame; //frame where the game is being played
    private InputController inputController = new InputController(); //controller that keeps control of which keys are pressed 
    private final pair[] scoreTable = new pair[3]; //array where the score is being kept
    
    //all components in the menu
    JButton play;
    JButton reset;
    JLabel highScore;
    JLabel one;
    JLabel two;
    JLabel three;
    JLabel firstPlace;
    JLabel secondPlace;
    JLabel thirdPlace;
    JLabel firstScore;
    JLabel secondScore;
    JLabel thirdScore;
 
    public MenuPanel() throws UnsupportedEncodingException, IOException
    {      
        getScore(); //get score table
        
        //colors
        this.setBackground(Color.black);
        Font thisFont = new Font("Arial", Font.PLAIN, 36);

        //using a grid bag layout in the panel with constraints
        GridBagLayout gblPanel = new GridBagLayout();
        this.setLayout(gblPanel);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,0,0);
        
        //add elements to the panel 
        
        //add play button
        this.play = new JButton("Play");
        this.play.setBackground(Color.red);
        this.play.setFont(new Font("Arial", Font.BOLD, 36));
        this.play.setForeground(Color.black);
        this.play.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(64, 0, 0), 5), 
        BorderFactory.createEmptyBorder(5, 5, 10, 10)));
        setGridBagInsetsParameters(c, 0, 0, 100, 50);
        setGridBagConstraintsParameters(c, 2, 4, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL);
        gblPanel.setConstraints(this.play, c);
        this.play.setVisible(true);
        this.play.setEnabled(true);
        this.add(this.play);
        
        //add reset button
        this.reset = new JButton("Reset");
        this.reset.setBackground(Color.red);
        this.reset.setFont(new Font("Arial", Font.BOLD, 36));
        this.reset.setForeground(Color.black);
        this.reset.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(64, 0, 0), 5), 
        BorderFactory.createEmptyBorder(5, 5, 10, 10)));
        setGridBagInsetsParameters(c, 0, 0, 100, 50);
        setGridBagInsetsParameters(c, 0, 50, 100, 0);
        setGridBagConstraintsParameters(c, 0, 4, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL);
        gblPanel.setConstraints(this.reset, c);
        this.reset.setVisible(true);
        this.reset.setEnabled(true);
        this.add(this.reset);
        
        //add high score label
        this.highScore = new JLabel("High Score");
        this.highScore.setForeground(Color.red);
        this.highScore.setFont(new Font("Arial", Font.BOLD, 56));
        setGridBagInsetsParameters(c, 50, 0,0, 0);
        setGridBagConstraintsParameters(c, 1, 0, 1, 1, 1, 1, GridBagConstraints.NONE);
        gblPanel.setConstraints(this.highScore, c);
        this.highScore.setVisible(true);
        this.add(this.highScore);
        
        //adding labels, won't be used later
        this.one = new JLabel("1.");
        this.one.setForeground(Color.red);
        this.one.setFont(thisFont);
        setGridBagInsetsParameters(c, 100, 100,0, 0);
        setGridBagConstraintsParameters(c, 0, 1, 1, 1, 1, 1, GridBagConstraints.EAST);
        gblPanel.setConstraints(this.one, c);
        this.one.setVisible(true);
        this.add(this.one);
        
        this.two = new JLabel("2.");
        this.two.setForeground(Color.red);
        this.two.setFont(thisFont);
        setGridBagInsetsParameters(c, 0, 100,0, 0);
        setGridBagConstraintsParameters(c, 0, 2, 1, 1, 1, 1, GridBagConstraints.EAST);
        gblPanel.setConstraints(this.two , c);
        this.two.setVisible(true);
        this.two.setEnabled(true);
        this.add(this.two );
        
        this.three = new JLabel("3.");
        this.three.setForeground(Color.red);
        this.three.setFont(thisFont);
        setGridBagInsetsParameters(c, 0, 100, 100, 0);
        setGridBagConstraintsParameters(c, 0, 3, 1, 1, 1, 1, GridBagConstraints.EAST);
        gblPanel.setConstraints(this.three, c);
        this.three.setVisible(true);
        this.three.setEnabled(true);
        this.add(this.three);

        //add labels for the high score table, can be overwriten later
        this.firstPlace = new JLabel("unknown");
        this.firstPlace.setForeground(Color.red);
        this.firstPlace.setFont(thisFont);
        setGridBagInsetsParameters(c, 100, 0, 0, 0);
        setGridBagConstraintsParameters(c, 1, 1, 1, 1, 1, 1, GridBagConstraints.NONE);
        gblPanel.setConstraints(this.firstPlace, c);
        this.firstPlace.setVisible(true);
        this.firstPlace.setEnabled(true);
        this.add(this.firstPlace);
         
        this.secondPlace = new JLabel("unknown");
        this.secondPlace.setForeground(Color.red);
        this.secondPlace.setFont(thisFont);
        setGridBagInsetsParameters(c, 0, 0, 0, 0);
        setGridBagConstraintsParameters(c, 1, 2, 1, 1, 1, 1, GridBagConstraints.NONE);
        gblPanel.setConstraints(this.secondPlace, c);
        this.secondPlace.setVisible(true);
        this.secondPlace.setEnabled(true);
        this.add(this.secondPlace);
        
        this.thirdPlace = new JLabel("unknown");
        this.thirdPlace.setForeground(Color.red);
        this.thirdPlace.setFont(thisFont);     
        setGridBagInsetsParameters(c, 0, 0, 100, 0);
        setGridBagConstraintsParameters(c, 1, 3, 1, 1, 1, 1, GridBagConstraints.NONE);
        gblPanel.setConstraints(this.thirdPlace, c);
        this.thirdPlace.setVisible(true);
        this.thirdPlace.setEnabled(true);
        this.add(this.thirdPlace);
        
        this.firstScore = new JLabel("0");
        this.firstScore.setForeground(Color.red);
        this.firstScore.setFont(thisFont);
        setGridBagInsetsParameters(c, 100, 0, 0, 200);
        setGridBagConstraintsParameters(c, 2, 1, 1, 1, 1, 1, GridBagConstraints.NONE);
        gblPanel.setConstraints(this.firstScore, c);
        this.firstScore.setVisible(true);
        this.firstScore.setEnabled(true);
        this.add(this.firstScore);
         
        this.secondScore = new JLabel("0");
        this.secondScore.setForeground(Color.red);
        this.secondScore.setFont(thisFont);
        setGridBagInsetsParameters(c, 0, 0, 0, 200);
        setGridBagConstraintsParameters(c, 2, 2, 1, 1, 1, 1, GridBagConstraints.NONE);
        gblPanel.setConstraints(this.secondScore, c);
        this.secondScore.setVisible(true);
        this.secondScore.setEnabled(true);
        this.add(this.secondScore);
        
        this.thirdScore = new JLabel("0");
        this.thirdScore.setForeground(Color.red);
        this.thirdScore.setFont(thisFont);     
        setGridBagInsetsParameters(c, 0, 0, 100, 200);
        setGridBagConstraintsParameters(c, 2, 3, 1, 1, 1, 1, GridBagConstraints.NONE);
        gblPanel.setConstraints(this.thirdScore, c);
        this.thirdScore.setVisible(true);
        this.thirdScore.setEnabled(true);
        this.add(this.thirdScore);
        
        updateScoreInMenu(); //show current high score table using labels
        
        //actions
        
        //reset high score button
        this.reset.addActionListener((ActionEvent e) -> {
            int willReset = 0;
            willReset = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset high score table?", "Reset high score table", JOptionPane.YES_NO_OPTION);
            if (willReset == JOptionPane.YES_OPTION) {
                try {
                    resetHighScore();
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        //play the game button
        this.play.addActionListener((ActionEvent e) -> {
            try
            {
                PlayGame();
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    class InputController implements KeyListener //class used for getting the keys pressed from the keyboard
    {
        private final boolean[] down = new boolean[255];
        private final boolean[] pressed = new boolean[255];

        @Override
        public void keyPressed(KeyEvent e)
        {
            down[e.getKeyCode()] = true;
            pressed[e.getKeyCode()] = true;
            model.passKeysDownToPlayer(down); //pass the key to the model
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            down[e.getKeyCode()]= false;
            model.passKeysDownToPlayer(down); //pass the key to the model
        }

        @Override
        public void keyTyped(KeyEvent e)
        {
            // to do - all methods need to be overriden
        }
    }
    
    private void PlayGame() throws InterruptedException, IOException
    {
        model = new GameModel(); //create new game model
        MenuPanel tempPan = this;
        frame = new GameFrame(model, tempPan); //create frame from the current model
        frame.addKeyListener(inputController);
        
        //make a thread that controls game model logic
        Thread thread = new Thread(){
            @Override
            public void run()
            {	
                try
                {
                    boolean endGame = false;
                    while (!endGame)
                    {
                        if (!model.startGame(frame))
                        {
                            //the game has ended, get username and score and try to update the high score table
                            frame.dispose();
                            updateTableWithNew(model.username, model.score);
                            endGame = true;
                        }
                        else
                        {
                            //player wants to play again, create a new controller for keys - if not, mario keeps jumping/running after new game is initialized
                            frame.dispose();
                            model = new GameModel();
                            frame = new GameFrame(model, tempPan);
                            inputController = new InputController();
                            frame.addKeyListener(inputController);
                        }
                    }
                } catch (InterruptedException e) {
                } catch (IOException ex) {
                    Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }    
            }
        };
        thread.start(); //start the thread
        AbstractAction FPSTimer = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                frame.gamePanel.repaint(); //repainting the frame					
            }			
        };
        new Timer(15, FPSTimer).start();
    }
    
    public void updateTableWithNew(String toSaveName, int toSaveScore) throws FileNotFoundException, UnsupportedEncodingException //updates table with new username and score
    {
        //check whether the pair is in top 3
        for (int i = 0; i < 3; i++)
        {
            if (scoreTable[i].score <= toSaveScore)
            {
                //put the new score and username in scoreTable[i]
                pair tempPair = new pair(toSaveName, toSaveScore);
                //make all other users move down in the high score table
                for (int j = 2; j > i; j--)
                {
                    scoreTable[j] = scoreTable[j-1];
                }
                scoreTable[i] = tempPair;
                break;
            }
        }
        saveScoreFromArrToFile(); //save score to file
        updateScoreInMenu(); //update the view of high score table in the main menu   
    }
    
    private void saveScoreFromArrToFile() throws FileNotFoundException, UnsupportedEncodingException
    {
        //save the elements from scoreTable array to a txt file
        //array is already initialized when using this function
        PrintWriter writer = new PrintWriter("score.txt", "UTF-8"); 
        for (int i = 0; i < 3; i++)
        {
            writer.println(scoreTable[i].name);
            writer.println(scoreTable[i].score);
        }
        writer.close();
    }
    
    private void updateScoreInMenu() //update view of high score table in the main menu
    {
        this.firstPlace.setText(scoreTable[0].name);
        this.secondPlace.setText(scoreTable[1].name);
        this.thirdPlace.setText(scoreTable[2].name);
        this.firstScore.setText(Integer.toString(scoreTable[0].score));
        this.secondScore.setText(Integer.toString(scoreTable[1].score));
        this.thirdScore.setText(Integer.toString(scoreTable[2].score));
    }
    
    private void resetHighScore() throws FileNotFoundException, UnsupportedEncodingException //reset high score, update it in the file and main menu
    {
        for (int i = 0; i < 3; i++)
        {
            scoreTable[i].name = "unknown";
            scoreTable[i].score = 0;
        }
        saveScoreFromArrToFile();
        updateScoreInMenu();
    }
    
    //get score from the score file and save it to the array
    //if no file exists, create one with the default options
    private void getScore() throws FileNotFoundException, UnsupportedEncodingException, IOException 
    {
        File f = new File("score.txt");
        if (!f.exists())
        {
            PrintWriter writer = new PrintWriter("score.txt", "UTF-8");
            for (int i = 0; i < 3; i++)
            {
                writer.println("unknown");
                writer.println("0");
            }
            writer.close();
        }
        //now the file exists and we can read from it
        FileReader in = new FileReader("score.txt");
        BufferedReader br = new BufferedReader(in);
        String line = br.readLine();
        int i = 0;
        while (line!=null)
        {
            String tempName = line;
            line = br.readLine();
            int tempScore = Integer.parseInt(line);
            scoreTable[i] = new pair(tempName, tempScore);
            line = br.readLine();
            i++;
        }
        in.close();
    }
    
    //fuctions used for the layout
    private static void setGridBagConstraintsParameters(GridBagConstraints gbc, int gridx, int gridy, double weightx,
                                                        double weighty, int gridwidth, int gridheight, int fill)
    {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = fill;
        gbc.anchor = GridBagConstraints.CENTER;
    }

    private static void setGridBagInsetsParameters(GridBagConstraints gbc, int top, int left, int bottom, int right)
    {
        gbc.insets = new Insets(top, left, bottom, right);
    }
    
}
