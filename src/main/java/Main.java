import gurobi.*;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.FileNotFoundException;

public class Main {

    public static final int numOfVert = 4;

    private static final String file = "C:\\Users\\giuse\\IdeaProjects\\untitled\\frb30-15-mis\\frb30-15-1-prova.mis";

    public static void main(String[] args) {

        PLController plController = null;
        try {
            plController = new PLController(numOfVert);
            plController.calculate(file, 5);
        } catch (GRBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }




}





