import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//реализовываем интерфейс
public class SimpleGui1 implements ActionListener {
    JButton button;

    public static void main (String [] args) {
        SimpleGui1 gui1 = new SimpleGui1();
        gui1.go();
    }

    private void go() {
        JFrame frame = new JFrame();
        button = new JButton("нажми меня");

        //Регистрируем слушатель
        button.addActionListener(this);

        //описание создоваемого окна и добаление в него кнопки
        frame.getContentPane().add(button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.setVisible(true);
    }

    //реализация метода обработки события
    @Override
    public void actionPerformed(ActionEvent e) {

        button.setText("Меня нажали!");
    }
}
