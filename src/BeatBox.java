import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class BeatBox {
    JPanel mainPanel;
    ArrayList<JCheckBox> checkBoxList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;

    String[] instrumentName = {"Bass Drum", "Closed Hi-Hat","Open Hi-Hat", "Acoustic Snare",
                "Crash Cymbal", "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
                "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo"};

    int[] insruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public static void main (String[] args) {
        new BeatBox().buildGUI();
    }

    public void buildGUI() {
        theFrame = new JFrame("BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); //граница для создания промежутка между компонентами

        checkBoxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyTempoUpListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        JButton serializelt = new JButton("serializelt");
        serializelt.addActionListener(new MySendListener());
        buttonBox.add(serializelt);

        JButton restore = new JButton("restore");
        restore.addActionListener(new MyReadInListener());
        buttonBox.add(restore);

        //create label with the name of the instrumental
        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i<15; i++) {
            nameBox.add(new Label(instrumentName [i]));
        }

        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);
        theFrame.getContentPane().add(background);

        //installation of the grid for checkbox
        GridLayout grid = new GridLayout(16,16);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel =new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);

        //create checkBox and add in the array
        for (int i=0;i<256;i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkBoxList.add(c);
            mainPanel.add(c);
        }

        setUpMidi();

        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    //gaining access to the synthesizer, sequenser and track
    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Big magic method
    public void buildTrackAndStart() {
        int[] trackList = null;     //array for value instrumental

        //delete and create track
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        //cycle for each instrument
        for (int i = 0;i<16;i++) {
            trackList = new int[16];

            int key = insruments[i]; //type instrumental

            System.out.println(insruments[i]);

            //cycle for each measure the current instrumental
            for (int j=0;j<16;j++) {
                JCheckBox jc = checkBoxList.get(j + (16*i));


                //check the checkbox is enabled, if selected then add in the cell array
                if (jc.isSelected()) {
                    System.out.println("true tacts " + j);
                    trackList[j] = key;
                } else {
                    System.out.println("false tacts " + j);
                    trackList[j] = 0;
                }
            }

            //for this instrument and for all 16 cycles, create an event and add track
            makeTracks(trackList);
            track.add(makeEvent(176,1,127,0,16));

        }

        track.add(makeEvent(192,9,1,0,15));
        try {
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            buildTrackAndStart();
        }
    }

    private class MyStopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.stop();
        }
    }

    //Temp up 3%
    private class MyTempoUpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            float TempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (TempoFactor * 1.03));
        }
    }

    //Temp down 3%
    private class MyDownTempoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            float TempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (TempoFactor * 0.97));
        }
    }

    //Event for one instrument, comprising value or key or 0
    public void makeTracks (int[] list) {
        for (int i =0;i<16;i++) {
            int key = list[i];

            //create event on/off and add to the track
            if (key !=0) {
                System.out.println(instrumentName);
                track.add(makeEvent(144,9,key,100,i));
                track.add(makeEvent(144,9,key,100,i+1));
            }
        }
    }

    //create message and return MidiEvent
    public MidiEvent makeEvent (int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;

        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd,chan,one,two);
            event = new MidiEvent(a, tick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    //listener open save window
    public class MySendListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(theFrame);
            saveFile(fileSave.getSelectedFile());
        }
    }

    /**
     * save status checkbox in file
     * @param file it contains the path to the file
     */
    public void saveFile (File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            boolean [] checkboxState = new boolean[256];

            for (int i=0;i<256;i++) {
                JCheckBox checkBox = (JCheckBox) checkBoxList.get(i);
                if (checkBox.isSelected()) {
                    checkboxState[i] = true;
                    writer.write(checkboxState[i] + "\n");
                } else {
                    checkboxState[i] = false;
                    writer.write(checkboxState[i] + "\n");
                }

            }
            writer.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    //listener open dialog window for load file
    public class MyReadInListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(theFrame);
            loadFile(fileOpen.getSelectedFile());
        }
    }

    /**
     * the status of the uploaded file recovery
     * @param file it contains the path to the file
     */
    public void loadFile (File file) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int i = 0;
            String line = null;
            while ((line = reader.readLine()) != null) {
                boolean checkboxState = Boolean.parseBoolean(line);
                JCheckBox check = (JCheckBox) checkBoxList.get(i);

                if (checkboxState){
                    check.setSelected(true);
                } else {
                    check.setSelected(false);
                }
                i++;
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sequencer.stop();
        buildTrackAndStart();
    }
}