package utils;

import org.apache.commons.math3.linear.RealMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileImport {

    private File file;
    public FileImport(String path) {
        this.file = new File(path);
    }

    public RealMatrix populate() throws FileNotFoundException {

        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        scanner.next();
        int firstVertex = scanner.nextInt();
        int secondVertex= scanner.nextInt();

        return null;
    }

}
