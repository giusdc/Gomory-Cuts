import gurobi.*;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.FileNotFoundException;

public class Main {

    public static final int numOfVert = 30;
    public static final boolean verbose = false;

    private static final String file = "C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-3.mis";
    private static final String path = "C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-3-files\\";

    public static void main(String[] args) {

        PLController plController = null;
        try {
            plController = new PLController(numOfVert, verbose);
            plController.calculate(file, path, 10);
        } catch (GRBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }




}





