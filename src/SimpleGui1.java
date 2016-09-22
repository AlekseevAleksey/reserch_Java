import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

//реализовываем интерфейс
public class SimpleGui1 implements ActionListener {
    JButton button;
    JFrame frame;

    public static void main (String [] args) {
        SimpleGui1 gui1 = new SimpleGui1();
        gui1.go();
    }

    private void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Создаем кноаку и регистрируем слушатель
        button = new JButton("Изменить цвет");
        button.addActionListener(this);

        MyDrawPanel drawPanel = new MyDrawPanel();

        //описание создоваемого окна и добаление в него кнопки
        frame.getContentPane().add(BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(300,300);
        frame.setVisible(true);
    }

    //реализация метода обработки события
    @Override
    public void actionPerformed(ActionEvent e) {

        frame.repaint();
    }
}
