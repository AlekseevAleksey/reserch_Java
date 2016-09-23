import javax.swing.*;
import java.awt.*;

//реализовываем интерфейс
public class SimpleGui1 {

    JFrame frame;
    int x = 30;
    int y = 30;

    public static void main(String[] args) {
        SimpleGui1 gui1 = new SimpleGui1();
        gui1.go();
    }

    private void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Создаем панель для рисования
        MyDrawPanel drawPanel = new MyDrawPanel();

        //описание создоваемого окна и добаление в него кнопки и панели для рисования

        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(400, 300);
        frame.setVisible(true);

        //инкриментируем координаты круга
        for (int i = 0; i < 130; i++) {
            x++;
            y++;
            drawPanel.repaint();

            //замедляем
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }

    class MyDrawPanel extends JPanel {

        public void paintComponent(Graphics g) {

            //закрашиваем панель
            g.setColor(Color.white);
            g.fillRect(0,0,this.getWidth(),this.getHeight());

            //рисуем круг в новых кооринатах
            g.setColor(Color.cyan);
            g.fillOval(x,y, 100,100);
        }
    }
}