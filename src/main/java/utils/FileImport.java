package utils;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileImport {

    private File file;
    private int numOfVar;

    public FileImport(String path, int numOfVar) {
        this.file = new File(path);
        this.numOfVar = numOfVar;
    }

    //count rows
    public int countRows() throws FileNotFoundException {

        //init dimensions array
        int rows = 0;

        //create object to scan file
        Scanner scanner = new Scanner(file);

        //jump first line
        scanner.nextLine();

        Integer firstVertex = null;
        Integer secondVertex = null;

        //jump first letter "e"
        scanner.next();

        //pair of vertices
        firstVertex = scanner.nextInt();
        secondVertex = scanner.nextInt();


        while (firstVertex <= numOfVar) {

            //skip too high vertex values
            if (secondVertex <= numOfVar) {

                //update rows counter
                rows++;
            }

            if(!scanner.hasNext()) break;

            //jump first letter "e"
            scanner.next();

            //pair of vertices
            firstVertex = scanner.nextInt();
            secondVertex = scanner.nextInt();

        }

        return rows;
    }

    //create matrix from file
    public RealMatrix populate() throws FileNotFoundException {

        //create object to scan file
        Scanner scanner = new Scanner(file);

        //create matrix object
        int rows = countRows();
        int columns = numOfVar;
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(rows, columns + rows);

        //jump first line
        scanner.nextLine();

        Integer firstVertex = null;
        Integer secondVertex = null;

        //scan file

        //while init

        //row index init
        int rowIndex = 0;

        //jump first letter "e"
        scanner.next();

        //pair of vertices
        firstVertex = scanner.nextInt();
        secondVertex = scanner.nextInt();


        while (firstVertex <= numOfVar) {

            //skip too high vertex values
            if (secondVertex <= numOfVar) {

                //populate matrix
                realMatrix.addToEntry(rowIndex, firstVertex - 1, 1D);
                realMatrix.addToEntry(rowIndex, secondVertex - 1, 1D);
                realMatrix.addToEntry(rowIndex, columns + rowIndex, -1D);

                //update row index
                rowIndex++;
            }

            if(!scanner.hasNext()) break;

            //jump first letter "e"
            scanner.next();

            //pair of vertices
            firstVertex = scanner.nextInt();
            secondVertex = scanner.nextInt();

        }

        return realMatrix;
    }

}
