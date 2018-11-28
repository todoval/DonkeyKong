package donkeykong;

import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author todov
 */
public class MainMenu extends JFrame
{       
    public MainMenu() throws IOException //constructor for MainMenu
    {
        this.setTitle("Donkey Kong");
        this.setVisible(true);
        this.setContentPane(new MenuPanel()); //add content panel to main menu where all the components are
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
    }
}
