import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.FileImport;
import utils.PLI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TestClass {

    @Test
    public void testUtilis() {
        FileImport fileImport = new FileImport("C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-1.mis");
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
        FileImport fileImport = new FileImport("C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-1.mis");
        RealMatrix realMatrix = null;
        try {
            realMatrix = fileImport.populate();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PLI pli = new PLI(realMatrix);
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

}
