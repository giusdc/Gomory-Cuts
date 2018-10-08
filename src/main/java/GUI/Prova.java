package GUI;

import controller.PLController;
import entities.Mode;
import gurobi.GRBException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class Prova {
    private JButton button1;
    private JPanel provaView;
    private JButton computeButton;
    private JComboBox<Mode> comboBox1;
    private JSpinner spinner1;

    private String filePath = null;
    private String path = "C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-1-files\\";
    private String fileName = "frb30-15-1.mis";
    PLController plController = null;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Minimum weighted vertex cover with Gomory cuts");

        frame.setContentPane(new Prova().provaView);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        frame.setVisible(true);


    }


    public Prova() {

        comboBox1.setModel(new DefaultComboBoxModel<>(Mode.values()));
        comboBox1.setSelectedIndex(0);

        spinner1.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                }
            }
        });


        computeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                if (filePath == null) return;
                Mode mode = (Mode) comboBox1.getSelectedItem();
                if (mode == null) return;
                int iterations = (int) spinner1.getValue();
                try {
                    plController = new PLController(30, false);
                    plController.calculate(filePath, fileName, path, iterations, mode, false);
                } catch (GRBException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
