package utils;

import gurobi.GRB;
import gurobi.GRBException;
import gurobi.GRBVar;
import org.apache.commons.math3.linear.RealMatrix;

public class Debug {

    public static void printMatrix(RealMatrix matrix) {

        int rows = matrix.getRowDimension();
        int columns = matrix.getColumnDimension();

        System.err.println("-----------------------------------BEGIN MATRIX--------------------------------------");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.err.print(matrix.getEntry(i, j) + " ");
            }
            System.err.println("\n");
        }

        System.err.println("-----------------------------------END MATRIX--------------------------------------");

    }

    public static void printVars(GRBVar[] vars) throws GRBException {

        System.err.println("-----------------------------------BEGIN VARS--------------------------------------");

        for (GRBVar var : vars) {
            if (var != null)
                System.err.print(var.get(GRB.StringAttr.VarName) + " ");
        }
        System.err.println("\n");
        System.err.println("-----------------------------------END VARS--------------------------------------");

    }

}
