import org.junit.jupiter.api.Test;
import utils.FileImport;

import java.io.FileNotFoundException;

public class TestClass {

    @Test
    public void testUtilis() {
        FileImport fileImport = new FileImport("C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-1.mis");
        try {
            fileImport.populate();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
