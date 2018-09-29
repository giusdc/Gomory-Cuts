import gurobi.*;
import org.apache.commons.math3.linear.RealMatrix;
import utils.FileImport;
import utils.PLI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PLController {

    private GRBEnv grbEnv;

    public PLController() throws GRBException {
        grbEnv = new GRBEnv("pli.log");
    }

    public GRBModel createModel(PLI pli) throws GRBException {
        GRBModel model = new GRBModel(grbEnv);
        List<GRBVar> variables = createVariables(model, pli.getVariables());
        createObjectiveFunction(model, variables);
        createConstraints(model, pli.getCoefficientMatrix(), variables);
        return model;
    }

    public GRBModel createBinaryModel(PLI pli) throws GRBException {
        GRBModel model = new GRBModel(grbEnv);
        List<GRBVar> variables = createBinaryVariables(model, pli.getVariables());
        createObjectiveFunction(model, variables);
        createBinaryConstraints(model, pli.getCoefficientMatrix(), variables);
        return model;
    }

    public List<GRBVar> createVariables(GRBModel grbModel, List<String> variables) throws GRBException {
        List<GRBVar> list = new ArrayList<GRBVar>();
        for (String v : variables) {
            list.add(grbModel.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, v));
        }
        return list;
    }

    public List<GRBVar> createBinaryVariables(GRBModel grbModel, List<String> variables) throws GRBException {
        List<GRBVar> list = new ArrayList<GRBVar>();
        for (int i = 0; i < 30; i++) {
            list.add(grbModel.addVar(0.0, 1.0, 0.0, GRB.BINARY, variables.get(i)));
        }
        return list;
    }

    public void createObjectiveFunction(GRBModel grbModel, List<GRBVar> variables) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        double weight = 0;
        for (int i = 0; i < 30; i++) {
            weight++;
            expr.addTerm(weight, variables.get(i));
        }
        grbModel.setObjective(expr, GRB.MINIMIZE);
    }

    public void createBinaryConstraints(GRBModel grbModel, RealMatrix matrix, List<GRBVar> variables) throws GRBException {

        int rows = matrix.getRowDimension();
        int columns = matrix.getColumnDimension();

        GRBLinExpr expr = null;

        for (int i = 0; i < rows; i++) {
            expr = new GRBLinExpr();
            for (int j = 0; j < 30; j++) {
                if (matrix.getEntry(i, j) != 0d)
                    expr.addTerm(matrix.getEntry(i, j), variables.get(j));
            }
            grbModel.addConstr(expr, GRB.GREATER_EQUAL, 1d, "c" + i);
        }
    }

    public void createConstraints(GRBModel grbModel, RealMatrix matrix, List<GRBVar> variables) throws GRBException {

        int rows = matrix.getRowDimension();
        int columns = matrix.getColumnDimension();

        GRBLinExpr expr = null;

        for (int i = 0; i < rows; i++) {
            expr = new GRBLinExpr();
            for (int j = 0; j < columns; j++) {
                if (matrix.getEntry(i, j) != 0d)
                    expr.addTerm(matrix.getEntry(i, j), variables.get(j));
            }
            grbModel.addConstr(expr, GRB.EQUAL, 1d, "c" + i);
        }
    }

    public void calculate(String file) throws FileNotFoundException, GRBException {
        FileImport fileImport = new FileImport(file);
        RealMatrix realMatrix = fileImport.populate();
        PLI pli = new PLI(realMatrix);
        GRBModel model = createModel(pli);
        model.update();
        model.write("debug.lp");
        model.optimize();
        model.update();
        model.write("debug.sol");

        GRBModel binaryModel = createBinaryModel(pli);
        binaryModel.update();
        binaryModel.write("binary_debug.lp");
        binaryModel.optimize();
        binaryModel.update();
        binaryModel.write("binary_debug.sol");
    }

}
