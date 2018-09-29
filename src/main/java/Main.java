import gurobi.*;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.FileNotFoundException;

public class Main {

    private static final String file = "C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-2.mis";

    public static void main(String[] args) {

        PLController plController = null;
        try {
            plController = new PLController();
            plController.calculate(file);;
        } catch (GRBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        /*try {
            GRBEnv env = new GRBEnv("mip1.log");
            GRBModel model = new GRBModel(env);

            // Create variables

            GRBVar x = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "x");
            GRBVar y = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "y");
            GRBVar s1 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "s1");
            GRBVar s2 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "s2");
            GRBVar s3 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "s3");
            GRBVar s4 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "s4");

            // Set objective: maximize x + y

            GRBLinExpr expr = new GRBLinExpr();
            expr.addTerm(1.0, x);
            expr.addTerm(1.0, y);
            model.setObjective(expr, GRB.MAXIMIZE);

            // Add constraint: -3x + 12 y +s1= 30

            expr = new GRBLinExpr();
            expr.addTerm(-3.0, x);
            expr.addTerm(12.0, y);
            expr.addTerm(1.0, s1);
            model.addConstr(expr, GRB.EQUAL, 30.0, "c0");

            // Add constraint: 6x -3y +s2 = 8

            expr = new GRBLinExpr();
            expr.addTerm(6.0, x);
            expr.addTerm(-3.0, y);
            expr.addTerm(1.0,s2);
            model.addConstr(expr, GRB.EQUAL, 8.0, "c1");

            double[][] matrixData = { {-3d,12d}, {6d,-3d}};
            double [][] matrixData2={{1d,0d},{0d,1d}};
            double [][] matrixDatacoef={{30d},{8d}};
            RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
            RealMatrix n= MatrixUtils.createRealMatrix(matrixData2);
            RealMatrix b=MatrixUtils.createRealMatrix(matrixDatacoef);
            RealMatrix p= new LUDecomposition(m).getSolver().getInverse();
            RealMatrix matrix=p.multiply(n);
            RealMatrix b1=p.multiply(b);
            System.err.println("B1");
            System.err.println(b1);
            System.err.println("base");
            System.err.println(matrix);

            // Optimize model

            model.optimize();

            System.out.println("PRIMA DEL TAGLIO");
            System.out.println("SLACK"+model.getConstrByName("c0").get(GRB.DoubleAttr.Slack));
            System.out.println("SLACK"+model.getConstrByName("c1").get(GRB.DoubleAttr.Slack));

            System.out.println(x.get(GRB.StringAttr.VarName)
                    + " " + x.get(GRB.DoubleAttr.X));
            System.out.println(y.get(GRB.StringAttr.VarName)
                    + " " + y.get(GRB.DoubleAttr.X));


            System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));

            expr = new GRBLinExpr();
            expr.addTerm(1.0,x);
            expr.addTerm(Math.floor(matrix.getEntry(0,0)),s1);
            expr.addTerm(Math.floor(matrix.getEntry(0,1)),s2);
            expr.addTerm(1.0,s3);
            model.addConstr(expr, GRB.EQUAL, Math.floor(b1.getEntry(0,0)), "c2");

            expr = new GRBLinExpr();
            expr.addTerm(1.0,y);
            expr.addTerm(Math.floor(matrix.getEntry(1,0)),s1);
            expr.addTerm(Math.floor(matrix.getEntry(1,1)),s2);
            expr.addTerm(1.0,s4);
            model.addConstr(expr, GRB.EQUAL, Math.floor(b1.getEntry(1,0)), "c3");

            System.out.println("DOPO");
            model.optimize();




            System.out.println(x.get(GRB.StringAttr.VarName)
                    + " " + x.get(GRB.DoubleAttr.X));
            System.out.println(y.get(GRB.StringAttr.VarName)
                    + " " + y.get(GRB.DoubleAttr.X));
            System.out.println(s1.get(GRB.StringAttr.VarName)
                    + " " + s1.get(GRB.DoubleAttr.X));
            System.out.println(s2.get(GRB.StringAttr.VarName)
                    + " " + s2.get(GRB.DoubleAttr.X));



            System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));


            // Dispose of model and environment

            model.dispose();
            env.dispose();
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " +
                    e.getMessage());
        }*/
    }
}




