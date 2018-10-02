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

    private RealMatrix coefficientMatrix, constantTermsVector;
    private List<String> variables;
    private HashMap<String, Integer> varPosition;

    public PLI(RealMatrix coefficientMatrix) {
        this.varPosition = new HashMap<String, Integer>();
        this.coefficientMatrix = coefficientMatrix;
        int rows = this.coefficientMatrix.getRowDimension();
        this.constantTermsVector = MatrixUtils.createRealMatrix(rows, 1);
        this.variables = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            variables.add("x" + i);
            this.varPosition.put("x" + i, i);
        }
        for (int i = 0; i < rows; i++) {
            variables.add("s" + i);
            this.varPosition.put("s" + i, 30 + i);
            this.constantTermsVector.setEntry(i, 0, 1.0);
        }
    }


}


