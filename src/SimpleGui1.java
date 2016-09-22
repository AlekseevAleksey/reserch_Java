import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

//реализовываем интерфейс
public class SimpleGui1 {
    JButton buttonColor;
    JButton buttonLabel;
    JFrame frame;
    JLabel label;
    int x;

    public static void main (String [] args) {
        SimpleGui1 gui1 = new SimpleGui1();
        gui1.go();
    }

    private void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Создаем кноаку и регистрируем слушатель
        buttonColor = new JButton("Изменить цвет");
        buttonColor.addActionListener(new ColorListener());

        buttonLabel = new JButton("Измениь label");
        buttonLabel.addActionListener(new LabelListener());

        //Создаем панель для рисования
        MyDrawPanel drawPanel = new MyDrawPanel();

        //
        label = new JLabel("Label 0");

        //описание создоваемого окна и добаление в него кнопки и панели для рисования
        frame.getContentPane().add(BorderLayout.SOUTH, buttonColor);
        frame.getContentPane().add(BorderLayout.EAST, buttonLabel);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.getContentPane().add(BorderLayout.WEST, label);
        frame.setSize(500,300);
        frame.setVisible(true);
    }

    class LabelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            x = (int) (Math.random() * 7);
            label.setText("new label " + x);
        }
    }

    class ColorListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.repaint();
        }
    }
}
