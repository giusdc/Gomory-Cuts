import gurobi.*;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import utils.Debug;
import utils.FileImport;
import entities.PLI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PLController {

    private GRBEnv grbEnv;
    private int numOfVert;
    private boolean verbose;

    public PLController(int numOfVert, boolean verbose) throws GRBException {
        grbEnv = new GRBEnv("pli.log");
        this.numOfVert = numOfVert;
        this.verbose = verbose;
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
        for (int i = 0; i < Main.numOfVert; i++) {
            list.add(grbModel.addVar(0.0, 1.0, 0.0, GRB.BINARY, variables.get(i)));
        }
        return list;
    }

    public void createObjectiveFunction(GRBModel grbModel, List<GRBVar> variables) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        double weight = 0;
        for (int i = 0; i < Main.numOfVert; i++) {
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
            for (int j = 0; j < Main.numOfVert; j++) {
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
        GRBVar var = null;
        double num = 0.0, floorNum = 0.0;
        for (int i = 0; i < numOfVert; i++) {
            var = variables[i];
            num = var.get(GRB.DoubleAttr.X);
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
        GRBVar[] varToPrint = new GRBVar[model.getVars().length];
        GRBConstr[] constraints = model.getConstrs();
        if (basis == 0) {
            for (int i = 0; i < variables.length; i++) {
                if (variables[i].get(GRB.IntAttr.VBasis) == 0) {
                    list.add(i);
                    varToPrint[i] = variables[i];
                }
                if (i >= numOfVert) {
                    if (constraints[i - numOfVert].get(GRB.IntAttr.CBasis) == 0) {
                        list.add(i);
                        varToPrint[i] = variables[i];
                    }
                }
            }
        } else if (basis == 1) {
            for (int i = 0; i < variables.length; i++) {
                if (variables[i].get(GRB.IntAttr.VBasis) != 0) {
                    list.add(i);
                    varToPrint[i] = variables[i];
                }
            }
        } else if (basis == 2) {
            for (int i = 0; i < variables.length; i++) {
                double value = variables[i].get(GRB.DoubleAttr.X);
                if (variables[i].get(GRB.IntAttr.VBasis) == 0 && Math.floor(value) != value) {
                    list.add(i);
                    varToPrint[i] = variables[i];
                } else if (variables[i].get(GRB.IntAttr.VBasis) == 0 && Math.floor(value) == value) {
                    list.add(-1);
                }
                if (i >= numOfVert) {
                    if (constraints[i - numOfVert].get(GRB.IntAttr.CBasis) == 0 && Math.floor(value) != value) {
                        list.add(i);
                        varToPrint[i] = variables[i];
                    } else if (constraints[i - numOfVert].get(GRB.IntAttr.CBasis) == 0 && Math.floor(value) == value) {
                        list.add(-1);
                    }
                }
            }
        }
        if (verbose) {
            System.err.println("type: " + basis);
            System.err.println("list: " + list);
            Debug.printVars(varToPrint);
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

    public RealMatrix extendMatrix(RealMatrix matrix, int n, boolean vector) {
        RealMatrix newMatrix = null;
        double[][] oldValues = new double[matrix.getRowDimension()][matrix.getColumnDimension()];

        matrix.copySubMatrix(0, matrix.getRowDimension() - 1, 0, matrix.getColumnDimension() - 1, oldValues);

        if (vector) {
            newMatrix = MatrixUtils.createRealMatrix(matrix.getRowDimension() + n, matrix.getColumnDimension());
        } else {
            newMatrix = MatrixUtils.createRealMatrix(matrix.getRowDimension() + n, matrix.getColumnDimension() + n);
        }
        newMatrix.setSubMatrix(oldValues, 0, 0);

        return newMatrix;
    }

    //compute the linear programming problem given the file
    public void calculate(String filePath, String fileName, String path, int iterations, boolean integerCut, boolean binary) throws FileNotFoundException, GRBException {
        FileImport fileImport = new FileImport(filePath, numOfVert);

        //A
        RealMatrix matrix = fileImport.populate();

        RealMatrix[] matrices = null;
        PLI pli = new PLI(matrix, numOfVert);
        if (!binary) {
            GRBModel model = createModel(pli);
            model.set(GRB.IntParam.Method, 0);
            //model.update();
            model.write(path + fileName + ".lp");
            //  model.relax();
            model.optimize();
            //model.update();
            model.write(path + fileName + ".sol");


            int counter = 0;

            //Gomory Cuts routine
            while (!checkIntegerSolution(model) && counter < iterations) {

                if (verbose) {
                    for (GRBConstr c : model.getConstrs()) {
                        System.err.println(c.get(GRB.StringAttr.ConstrName));
                        System.err.println(c.get(GRB.IntAttr.CBasis));
                    }
                }

                System.err.println("-----------------------> " + counter);

                if (verbose) {
                    System.err.println("A");
                    Debug.printMatrix(matrix);
                }

                List<Integer> columnBasisIndexes = getBasisOrOutOfBasisIndexes(model, matrix, 0);
                List<Integer> columnOutOfBasisIndexes = getBasisOrOutOfBasisIndexes(model, matrix, 1);
                List<Integer> columnFractionaryBasisIndexes = getBasisOrOutOfBasisIndexes(model, matrix, 2);

                List<GRBVar> basisFractionaryVars = getBasisOrOutOfBasisVars(model, columnBasisIndexes, true);
                List<GRBVar> outOfBasisVars = getBasisOrOutOfBasisVars(model, columnOutOfBasisIndexes, false);

                matrices = getBasisAndOutOfBasisMatrix(model, matrix, columnBasisIndexes, columnOutOfBasisIndexes);

                //B
                RealMatrix basisMatrix = matrices[0];
                if (verbose) {
                    System.err.println("B");
                    Debug.printMatrix(basisMatrix);
                }

                //N
                RealMatrix outOfBasisMatrix = matrices[1];
                if (verbose) {
                    System.err.println("N");
                    Debug.printMatrix(outOfBasisMatrix);
                }

                //b
                RealMatrix constantTermsVector = pli.getConstantTermsVector();
                if (verbose) {
                    System.err.println("b");
                    Debug.printMatrix(constantTermsVector);
                }

                //B^(-1)
                //RealMatrix basisInverseMatrix = MatrixUtils.inverse(basisMatrix);
                RealMatrix basisInverseMatrix = new LUDecomposition(basisMatrix).getSolver().getInverse();
                if (verbose) {
                    System.err.println("B^(-1)");
                    Debug.printMatrix(basisInverseMatrix);
                }

                //B^(-1)*N
                RealMatrix basisInverseDotOutOfBasisMatrix = basisInverseMatrix.multiply(outOfBasisMatrix);
                if (verbose) {
                    System.err.println("B^(-1)*N");
                    Debug.printMatrix(basisInverseDotOutOfBasisMatrix);
                }

                //B^(-1)*b
                RealMatrix basisInverseDotConstantTermsVector = basisInverseMatrix.multiply(constantTermsVector);
                if (verbose) {
                    System.err.println("B^(-1)*b");
                    Debug.printMatrix(basisInverseDotConstantTermsVector);
                }

                GRBLinExpr expr = null;

                int lastRow = matrix.getRowDimension();
                int lastColumn = matrix.getColumnDimension();

                //extend A matrix for the cuts
                matrix = extendMatrix(matrix, basisFractionaryVars.size(), false);

                //extend vector b
                constantTermsVector = extendMatrix(constantTermsVector, basisFractionaryVars.size(), true);

                int count = 0;

                //add Gomory cuts
                for (int i = 0; i < columnFractionaryBasisIndexes.size(); i++) {

                    if (columnFractionaryBasisIndexes.get(i) != -1) {

                        expr = new GRBLinExpr();

                        if (integerCut) {

                            expr.addTerm(1d, basisFractionaryVars.get(count));

                            matrix.setEntry(lastRow + count, columnFractionaryBasisIndexes.get(i), 1d);

                        }

                        //out of basis variables terms

                        for (int j = 0; j < outOfBasisVars.size(); j++) {

                            double value = Math.floor(basisInverseDotOutOfBasisMatrix.getEntry(i, j));

                            if (!integerCut) {
                                value = basisInverseDotOutOfBasisMatrix.getEntry(i, j) - value;
                            }

                            expr.addTerm(value, outOfBasisVars.get(j));

                            matrix.setEntry(lastRow + count, columnOutOfBasisIndexes.get(j), value);

                        }

                        String slackName = "sg" + (lastRow + count);

                        GRBVar slack = model.addVar(0.0, GRB.INFINITY, 0.0, GRB.CONTINUOUS, slackName);

                        pli.getVariables().add(slackName);

                        if(integerCut) {
                            expr.addTerm(1d, slack);

                            matrix.setEntry(lastRow + count, lastColumn + count, 1.0);
                        } else {
                            expr.addTerm(-1d, slack);

                            matrix.setEntry(lastRow + count, lastColumn + count, -1.0);
                        }

                        //constant term

                        double constant = Math.floor(basisInverseDotConstantTermsVector.getEntry(i, 0));

                        if(!integerCut) {
                            constant = basisInverseDotConstantTermsVector.getEntry(i, 0) - constant;
                        }

                        model.addConstr(expr, GRB.EQUAL, constant, "g" + (lastRow + count));

                        constantTermsVector.setEntry(lastRow + count, 0, constant);

                        count++;
                    }

                }

                pli.setCoefficientMatrix(matrix);
                pli.setConstantTermsVector(constantTermsVector);
                model.set(GRB.IntParam.Method, 0);
                //model.update();
                model.write(path + fileName + "(" + counter + ").lp");
                model.optimize();
                //model.update();
                model.write(path + fileName + "(" + counter + ").sol");


                counter++;
            }

        } else {

            //-------------------FOR DEBUGGING------------------------------------------------
            GRBModel binaryModel = createBinaryModel(pli);
            binaryModel.update();
            binaryModel.write(fileName + "-BINARY.lp");
            binaryModel.optimize();
            binaryModel.update();
            binaryModel.write(fileName + "-BINARY.sol");
        }
    }

}
