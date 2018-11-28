package donkeykong;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Observer;
import java.util.Observable;

public class GamePanel extends JPanel implements Observer {
    private final GameModel model; //model that uses the panel
    
    //images that will be painted
    Image mario = new ImageIcon(getClass().getResource("images/mario.png")).getImage();
    Image kong = new ImageIcon(getClass().getResource("images/konky_dong.gif")).getImage();
    Image peach = new ImageIcon(getClass().getResource("images/peach.png")).getImage();
    Image barrel = new ImageIcon(getClass().getResource("images/barrel.png")).getImage();
    Image platform = new ImageIcon(getClass().getResource("images/platform.png")).getImage();
    Image ladder = new ImageIcon(getClass().getResource("images/ladder.png")).getImage();

    public GamePanel(GameModel model) throws IOException //constructor
    {
        setOpaque(true);
        setBackground(Color.BLACK);
        this.model = model;
        this.model.addObserver(this);
    }

    @Override
    public void paintComponent(Graphics g) //overriden paintComponent function
    {
        super.paintComponent(g); 
        g.drawImage(kong, 60, 120, 100, 100, this); //draw kong
        for (StaticObject object : model.getSOList()) //paint every static object - ladders, platforms, peach
        {
            if(object instanceof Platform) g.drawImage(platform,(int)object.getXPos(), (int)object.getYPos(), (int)object.getWidth(), (int)object.getHeight(), null);
            if(object instanceof Ladder) g.drawImage(ladder,(int)object.getXPos(), (int)object.getYPos(), (int)object.getWidth(), (int)object.getHeight(), null);
            if(object instanceof Peach) g.drawImage(peach,(int)object.getXPos(), (int)object.getYPos(), (int)object.getWidth(), (int)object.getHeight(), null);
        }
        
        //write the score on the top of the screen
        g.setColor(Color.WHITE);
        g.drawString("Score: " + model.getScore(), 500, 50); 

        for (MovingObject object : model.getMOList()) //paint every moving object - mario and barrel
        {
            if(object instanceof Barrel) g.drawImage(barrel,(int)object.getXPos(), (int)object.getYPos(), (int)object.getWidth(), (int)object.getHeight(), null);
            if(object instanceof Mario) g.drawImage(mario, (int)object.getXPos(),(int)object.getYPos(),(int)object.getWidth(),(int)object.getHeight(), null);
        } 	
    }

    public void update(Observable caller, Object data)
    {
        repaint();
    }

}

