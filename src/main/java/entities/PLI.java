package entities;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PLI {

    //A
    private RealMatrix coefficientMatrix,
    //b
            constantTermsVector;
    //variable names
    private List<String> variables;

    public PLI(RealMatrix coefficientMatrix, int numOfVert) {
        this.coefficientMatrix = coefficientMatrix;
        int rows = this.coefficientMatrix.getRowDimension();
        this.constantTermsVector = MatrixUtils.createRealMatrix(rows, 1);
        this.variables = new ArrayList<String>();
        for (int i = 0; i < numOfVert; i++) {
            variables.add("x" + i);
        }
        for (int i = 0; i < rows; i++) {
            variables.add("s" + i);
            this.constantTermsVector.setEntry(i, 0, 1.0);
        }
    }


}


