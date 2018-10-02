import gurobi.*;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import utils.FileImport;
import entities.PLI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PLController {

    private GRBEnv grbEnv;

    public PLController() throws GRBException {
        grbEnv = new GRBEnv("pli.log");
    }

    //create GUROBI Model given the coefficient matrix and the variables
    public GRBModel createModel(PLI pli) throws GRBException {

        //create the model
        GRBModel model = new GRBModel(grbEnv);

        //create the list of GUROBI model variables
        List<GRBVar> variables = createVariables(model, pli.getVariables());

        //create the objective function
        createObjectiveFunction(model, variables);

        //create all the constraint equations
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
            list.add(grbModel.addVar(0.0, 1.0, 0.0, GRB.CONTINUOUS, variables.get(i)));
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

    public boolean checkIntegerSolution(GRBModel model) throws GRBException {
        GRBVar[] variables = model.getVars();
        double num = 0.0, floorNum = 0.0;
        for (GRBVar v : variables) {
            num = v.get(GRB.DoubleAttr.X);
            floorNum = Math.floor(num);
            if (num != floorNum) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getBasisOrOutOfBasisIndexes(GRBModel model, RealMatrix matrix, int basis) throws GRBException {
        List<Integer> list = new ArrayList<Integer>();
        GRBVar[] variables = model.getVars();
        if (basis == 0) {
            for (int i = 0; i < variables.length; i++) {
                if (variables[i].get(GRB.DoubleAttr.RC) == 0.0) {
                    list.add(i);
                }
            }
        } else if (basis == 1) {
            for (int i = 0; i < variables.length; i++) {
                if (variables[i].get(GRB.DoubleAttr.RC) != 0.0) {
                    list.add(i);
                }
            }
        } else if (basis == 2) {
            for (int i = 0; i < variables.length; i++) {
                double value = variables[i].get(GRB.DoubleAttr.X);
                if (variables[i].get(GRB.DoubleAttr.RC) == 0.0 && Math.floor(value) != value) {
                    list.add(i);
                }
            }
        }
        return list;
    }

    public List<GRBVar> getBasisOrOutOfBasisVars(GRBModel model, List<Integer> indexes, boolean basis) throws GRBException {
        List<GRBVar> vars = new ArrayList<GRBVar>();
        if (basis) {
            for (Integer i : indexes) {
                GRBVar var = model.getVar(i);
                double value = var.get(GRB.DoubleAttr.X);
                if (Math.floor(value) != value) {
                    vars.add(var);
                }
            }
        } else {
            for (Integer i : indexes) {
                vars.add(model.getVar(i));
            }
        }
        return vars;
    }

    public RealMatrix[] getBasisAndOutOfBasisMatrix(GRBModel model, RealMatrix matrix, List<Integer> columnBasisIndexes, List<Integer> columnOutOfBasisIndexes) throws GRBException {
        //first matrix: basis matrix
        //second matrix: out of basis matrix
        RealMatrix[] matrices = new RealMatrix[2];
        int rows = matrix.getRowDimension();

        double[] column;

        matrices[0] = MatrixUtils.createRealMatrix(rows, columnBasisIndexes.size());
        for (int i = 0; i < columnBasisIndexes.size(); i++) {
            column = matrix.getColumn(columnBasisIndexes.get(i));
            matrices[0].setColumn(i, column);
        }

        matrices[1] = MatrixUtils.createRealMatrix(rows, columnOutOfBasisIndexes.size());
        for (int i = 0; i < columnOutOfBasisIndexes.size(); i++) {
            column = matrix.getColumn(columnOutOfBasisIndexes.get(i));
            matrices[1].setColumn(i, column);
        }

        return matrices;
    }

    public RealMatrix extendMatrix(RealMatrix matrix, int n) {
        RealMatrix newMatrix = null;
        double[][] oldValues = new double[matrix.getRowDimension()][matrix.getColumnDimension()];

        matrix.copySubMatrix(0, matrix.getRowDimension() - 1, 0, matrix.getColumnDimension() - 1, oldValues);

        newMatrix = MatrixUtils.createRealMatrix(matrix.getRowDimension() + n, matrix.getColumnDimension() + n);
        newMatrix.setSubMatrix(oldValues, 0, 0);

        return newMatrix;
    }

    //compute the linear programming problem given the file
    public void calculate(String file) throws FileNotFoundException, GRBException {
        FileImport fileImport = new FileImport(file);

        //A
        RealMatrix matrix = fileImport.populate();


        RealMatrix[] matrices = null;
        PLI pli = new PLI(matrix);
        GRBModel model = createModel(pli);
        model.update();
        model.write("frb30-15-1.lp");
        model.optimize();
        model.update();
        model.write("frb30-15-1.sol");

        //Gomory Cuts routine
        if (!checkIntegerSolution(model)) {

            List<Integer> columnBasisIndexes = getBasisOrOutOfBasisIndexes(model, matrix, 0);
            List<Integer> columnOutOfBasisIndexes = getBasisOrOutOfBasisIndexes(model, matrix, 1);
            List<Integer> columnFractionaryBasisIndexes = getBasisOrOutOfBasisIndexes(model, matrix, 2);

            List<GRBVar> basisFractionaryVars = getBasisOrOutOfBasisVars(model, columnBasisIndexes, true);
            List<GRBVar> outOfBasisVars = getBasisOrOutOfBasisVars(model, columnOutOfBasisIndexes, false);

            matrices = getBasisAndOutOfBasisMatrix(model, matrix, columnBasisIndexes, columnOutOfBasisIndexes);

            //B
            RealMatrix basisMatrix = matrices[0];

            //N
            RealMatrix outOfBasisMatrix = matrices[1];

            //b
            RealMatrix constantTermsVector = pli.getConstantTermsVector();

            //B^(-1)
            RealMatrix basisInverseMatrix = MatrixUtils.inverse(basisMatrix);

            //B^(-1)*N
            RealMatrix basisInverseDotOutOfBasisMatrix = basisInverseMatrix.multiply(outOfBasisMatrix);

            //B^(-1)*b
            RealMatrix basisInverseDotConstantTermsVector = basisInverseMatrix.multiply(constantTermsVector);

            GRBLinExpr expr = null;

            int lastRow = matrix.getRowDimension();

            //extend A matrix for the cuts
            matrix = extendMatrix(matrix, basisFractionaryVars.size());

            //add Gomory cuts
            for (int i = 0; i < basisFractionaryVars.size(); i++) {

                expr = new GRBLinExpr();
                expr.addTerm(1d, basisFractionaryVars.get(i));

                matrix.setEntry(lastRow + i, columnFractionaryBasisIndexes.get(i), 1d);

                for (int j = 0; j < outOfBasisVars.size(); j++) {

                    double value = Math.floor(basisInverseDotOutOfBasisMatrix.getEntry(i, j));

                    expr.addTerm(value, outOfBasisVars.get(j));

                    matrix.setEntry(lastRow + i, columnOutOfBasisIndexes.get(j), value);

                }

                GRBVar slack = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, "sg" + (lastRow + i));

                expr.addTerm(1d, slack);

                double constant = Math.floor(basisInverseDotConstantTermsVector.getEntry(i, 0));
                model.addConstr(expr, GRB.EQUAL, constant, "g" + i);


            }


            model.update();
            model.write("frb30-15-1(2).lp");
            model.optimize();
            model.update();
            model.write("frb30-15-1(2).sol");

        }


        //-------------------FOR DEBUGGING------------------------------------------------
        /*GRBModel binaryModel = createBinaryModel(pli);
        binaryModel.update();
        binaryModel.write("prova.lp");
        binaryModel.optimize();
        binaryModel.update();
        binaryModel.write("prova.sol");*/
    }

}
