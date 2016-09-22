import javax.swing.*;
import java.awt.*;

public class MyDrawPanel extends JPanel{

    public void paintComponent (Graphics g) {
        g.fillRect(0,0, this.getWidth(),this.getHeight());

        int Red = (int) (Math.random() * 255);
        int Blue= (int) (Math.random() * 255);
        int Green = (int) (Math.random() * 255);

        Color randomColor = new Color(Red,Blue,Green);
        g.setColor(randomColor);
        g.fillOval(70, 70, 100, 100);
    }

}
