package controller;

import GUI.MainGUI;

public class Main {

    public static final int numOfVert = 30;
    public static final boolean verbose = false;

    private static final String filePath = "C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-1.mis";
    private static final String fileName = "frb30-15-1.mis";
    private static final String path = "C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-1-files\\";

    //only integer cuts 0
    //only fractional cuts 1
    //both integer and fractional cuts 2
    //single integer cut 3
    //single fractional cut 4
    //both single integer and single fractional cut 5

    public static void main(String[] args) {

        MainGUI mainGUI = new MainGUI();

        /*controller.PLController plController = null;
        try {
            plController = new controller.PLController(numOfVert, verbose);
            plController.calculate(filePath, fileName, path, 10,  2, false);
        } catch (GRBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }




}





