package GUI;

import controller.PLController;
import entities.Mode;
import entities.Result;
import gurobi.GRBException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class MainGUI {
    private JButton button1;
    private JPanel provaView;
    private JButton computeButton;
    private JComboBox<Mode> comboBox1;
    private JSpinner spinner1;
    private JLabel fileNameLabel;
    private JButton StoreFileButton;
    private JLabel DebugDirectoryLebel;
    private JButton graphButton;

    private String filePath = null;
    private String directoryPath = null;
    private String fileName = null;
    private PLController plController = null;
    private int numOfVert = 30;

    public MainGUI() {

        JFrame frame = new JFrame("Minimum weighted vertex cover with Gomory cuts");
        frame.setResizable(false);
        frame.setContentPane(provaView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        comboBox1.setModel(new DefaultComboBoxModel<>(Mode.values()));
        comboBox1.setSelectedIndex(0);

        spinner1.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    fileNameLabel.setText(filePath);
                    fileName = selectedFile.getName();
                }
            }
        });


        computeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                if (filePath == null) return;
                Mode mode = (Mode) comboBox1.getSelectedItem();
                if (mode == null) return;
                if(directoryPath == null) return;
                Result result = null;
                int iterations = (int) spinner1.getValue();
                try {
                    plController = new PLController(numOfVert, false);
                    result = plController.calculate(filePath, fileName, directoryPath, iterations, mode, false);
                } catch (GRBException e) {
                    System.err.println("a");
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (result != null) {
                    if (mode == Mode.singleFractional || mode == Mode.singleInteger || mode == Mode.singleIntegerAndFractional)
                        new ResultPage(result, true);
                    else
                        new ResultPage(result, false);
                }
            }
        });

        StoreFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    directoryPath = selectedFile.getAbsolutePath() + System.getProperty("file.separator");
                    DebugDirectoryLebel.setText(directoryPath);
                }
            }
        });

        graphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filePath != null) new GraphGUI(filePath, numOfVert);
            }
        });
    }
}
