package utils;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PLI {

    private RealMatrix coefficientMatrix;
    private List<String> variables;

    public PLI(RealMatrix coefficientMatrix) {
        this.coefficientMatrix = coefficientMatrix;
        this.variables = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            variables.add("x" + i);
        }
        int rows = this.coefficientMatrix.getRowDimension();
        for (int i = 0; i < rows; i++) {
            variables.add("s" + i);
        }
    }
}


