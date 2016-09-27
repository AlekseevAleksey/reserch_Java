import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class MiniMusicPlayer1 {

    static JFrame f = new JFrame();
    static MyDrawPanel ml;

    public static void main (String [] args) {
        MiniMusicPlayer1 mini = new MiniMusicPlayer1();
        mini.go();
    }

    //создаем зону для рисования
    public void setUpGui () {
        ml = new MyDrawPanel();
        f.setContentPane(ml);
        f.setBounds(300, 300, 500, 500);
        f.setVisible(true);
    }

    //метод создающий звук, добавляющий в трек и воспроизводящий его
    public void go() {
        setUpGui();

        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            sequencer.addControllerEventListener(ml, new int[] {127}); //setup listener

            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();

            int r;
            for (int i = 0; i < 60; i+=4) {


                r = (int) ((Math.random() * 50) + 1);
                track.add(makeEvent(144,1,r,100,i));
                track.add(makeEvent(176,1,127,0,i));  //слушатель, который ловит воспрозведенеи ноты
                track.add(makeEvent(128,1,r,100, i +2));
            }

            sequencer.setSequence(sequence);
            sequencer.start();
            sequencer.setTempoInBPM(120);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //метод который создает сообщегие и возвращает событие
    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;

        try {
            ShortMessage a  = new ShortMessage();
            a.setMessage(comd,chan,one,two);
            event = new MidiEvent(a, tick);
        } catch (Exception e) { }
        return event;
    }


    /**
     * Субкласс рисующий прямоугольники
     */
    class MyDrawPanel extends JPanel implements ControllerEventListener {

        boolean msg = false;

        //метод перерисовщик
        @Override
        public void controlChange(ShortMessage event) {

            msg = true;
            repaint();
        }

        //метод расчитывающий и рисующий прямоугольник
        public void paintComponent (Graphics g) {
            System.out.println(msg);
            if (msg) {

                Graphics2D g2 = (Graphics2D) g;

                int r = (int) (Math.random() * 255);
                int gr = (int) (Math.random() * 255);
                int b = (int) (Math.random() * 255);

                g.setColor(new Color(r,gr,b));

                int ht = (int) (Math.random() * 40);
                int wt = (int) (Math.random() * 40);

                int x = (int) ((Math.random() * 40)+10);
                int y = (int) ((Math.random() * 40)+10);

                g.fillRect(x,y, wt, ht);
                msg = false;
            }
        }
    }
}