import gurobi.*;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.FileImport;
import entities.PLI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TestClass {

    @Test
    public void testUtilis() {
        FileImport fileImport = new FileImport("C:\\Users\\giuse\\IdeaProjects\\untitled\\frb30-15-mis\\frb30-15-1.mis", 30);
        RealMatrix realMatrix = null;
        int rows = 0;
        try {
            realMatrix = fileImport.populate();
            rows = realMatrix.getRowDimension();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int countPos = 0;
        int countNeg = 0;
        Boolean result = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 30 + rows; j++) {
                if (realMatrix.getEntry(i, j) == 1d) countPos++;
                if (realMatrix.getEntry(i, j) == -1d) countNeg++;
                if (realMatrix.getEntry(i, j) != 1d && realMatrix.getEntry(i, j) != 0d && realMatrix.getEntry(i, j) != -1d)
                    result = false;
            }
            if (countPos != 2) result = false;
            if (countNeg != 1) result = false;
            countPos = 0;
            countNeg = 0;
            if (!result) break;
        }
        Assertions.assertTrue(result);

    }

    @Test
    public void createPLI() {
        FileImport fileImport = new FileImport("C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-1.mis", 30);
        RealMatrix realMatrix = null;
        try {
            realMatrix = fileImport.populate();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PLI pli = new PLI(realMatrix, 30);
        Boolean flag = true;
        for (int i = 0; i < 30; i++) {
            if (!pli.getVariables().get(i).equals("x" + i))
                flag = false;
        }
        if (!flag) Assertions.assertTrue(flag);
        else {
            for (int i = 30; i < pli.getVariables().size(); i++) {
                if (!pli.getVariables().get(i).equals("s" + (i - 30)))
                    flag = false;
                if (!flag)
                    break;
            }
            Assertions.assertTrue(flag);
        }
    }

    @Test
    public void realBasisTest() throws GRBException, FileNotFoundException {
        PLController plController = new PLController(30);
        FileImport fileImport = new FileImport("C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-1.mis", 30);
        RealMatrix matrix = fileImport.populate();
        PLI pli = new PLI(matrix, 30);
        GRBModel model = plController.createModel(pli);
        model.optimize();
        RealMatrix realMatrix = null;
        if (!plController.checkIntegerSolution(model)) {
            //realMatrix = plController.getBasisMatrix(model, matrix);
        }
        boolean bool = false;
        if(realMatrix.getColumnDimension() == 30) bool = true;
        Assertions.assertTrue(bool);
    }

    @Test
    public void test() {
        List<String> list = new ArrayList<String>();
        list.add("x1");
        list.add("s1");
        Boolean b = false;
        for (String s : list) {
            if (s.contains("x")) {
                b = true;
            }
        }
        Assertions.assertTrue(b);
    }

    @Test
    public void RC() throws GRBException {

        GRBEnv env   = new GRBEnv("mip1.log");
        GRBModel  model = new GRBModel(env);

        // Create variables

        GRBVar x0 = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x0");
        GRBVar x1 = model.addVar(0.0, 1.0, 1.0, GRB.BINARY, "x1");
        GRBVar x2 = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x2");
        GRBVar x3 = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x3");

        model.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);

        GRBVar s0 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "ArtN_c0");
        GRBVar s1 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "ArtN_c1");
        GRBVar s2 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "ArtN_c2");
        GRBVar s3 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "ArtN_c3");
        GRBVar s4 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "ArtN_c4");
        //GRBVar s5 = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "s5");

        // Set objective: minimize x0 + 2 x1 + 3 x2 + 4 x3

        GRBLinExpr expr;

        GRBLinExpr expr2 = new GRBLinExpr();
        expr2.addTerm(1.0, x0); expr2.addTerm(2.0, x1); expr2.addTerm(3.0, x2); expr2.addTerm(4.0, x3);
        model.setObjective(expr2, GRB.MINIMIZE);

        // Add constraint: x0 + x1 >= 1

        expr = new GRBLinExpr();
        expr.addTerm(1.0, x0); expr.addTerm(1.0, x1); expr.addTerm(-1.0, s0);
        model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "c0");

        // Add constraint: x0 + x3 >= 1

        expr = new GRBLinExpr();
        expr.addTerm(1.0, x0); expr.addTerm(1.0, x3); expr.addTerm(-1.0, s1);
        model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "c1");

        // Add constraint: x1 + x3 >= 1

        expr = new GRBLinExpr();
        expr.addTerm(1.0, x1); expr.addTerm(1.0, x3); expr.addTerm(-1.0, s2);
        model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "c2");

        // Add constraint: x2 + x3 >= 1

        expr = new GRBLinExpr();
        expr.addTerm(1.0, x2); expr.addTerm(1.0, x3); expr.addTerm(-1.0, s3);
        model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "c3");

        // Add constraint: x1 + x2 >= 1

        expr = new GRBLinExpr();
        expr.addTerm(1.0, x1); expr.addTerm(1.0, x2); expr.addTerm(-1.0, s4);
        model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, "c4");

        // Add constraint: x0 + x2 >= 1

        /*expr = new GRBLinExpr();
        expr.addTerm(1.0, x0); expr.addTerm(1.0, x2); expr.addTerm(-1.0, s5);
        model.addConstr(expr, GRB.EQUAL, 1.0, "c5");*/

        // Optimize model

        model.write("a.lp");
        model.feasRelax(GRB.FEASRELAX_LINEAR,true,false,true);
        model.optimize();
        model.write("a.sol");

        System.out.println("----------------------------------------------------------------------");

        for (GRBVar v: model.getVars()) {
            System.out.println(v.get(GRB.StringAttr.VarName));
            if (v.get(GRB.IntAttr.VBasis) == 0) {
                System.out.println(v.get(GRB.StringAttr.VarName));
                System.out.println(v.get(GRB.DoubleAttr.RC));
            }
        }

        System.out.println("----------------------------------------------------------------------");

        // Dispose of model and environment

        model.dispose();
        env.dispose();

    }

}
