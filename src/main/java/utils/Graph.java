package utils;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Graph extends JFrame {

    public Graph() throws FileNotFoundException {
        super("Hello, World!");

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {

            //create object to scan file
            Scanner scanner = new Scanner(new File("C:\\Users\\bino\\IdeaProjects\\amod\\frb30-15-mis\\frb30-15-1.mis"));

            //jump first line
            scanner.nextLine();

            Integer firstVertex = null;
            Integer secondVertex = null;

            //scan file

            //while init

            //jump first letter "e"
            scanner.next();

            //pair of vertices
            firstVertex = scanner.nextInt();
            secondVertex = scanner.nextInt();

            HashMap<Integer, Object> map = new HashMap<Integer, Object>();

            while (firstVertex <= 30) {

                //skip too high vertex values
                if (secondVertex <= 30) {

                    Object v1, v2;
                    if (!map.containsKey(firstVertex)) {
                       /* v1 = graph.insertVertex(parent, String.valueOf(firstVertex), firstVertex, 20 + (firstVertex % 6) * 200, 20 + (firstVertex % 5) * 200, 20,
                                20); */
                        v1 = graph.insertVertex(parent, String.valueOf(firstVertex), firstVertex, 20 , 20, 20,
                                20);
                        map.put(firstVertex, v1);
                    } else {
                        v1 = map.get(firstVertex);
                    }
                    if (!map.containsKey(secondVertex)) {
                       /* v2 = graph.insertVertex(parent, String.valueOf(secondVertex), secondVertex, 20 + (secondVertex % 6) * 200, 20 + (secondVertex % 5) * 200,
                                20, 20);*/
                        v2 = graph.insertVertex(parent, String.valueOf(secondVertex), secondVertex, 20 , 20,
                                20, 20);
                        map.put(secondVertex, v2);
                    } else {
                        v2 = map.get(secondVertex);
                    }
                    graph.insertEdge(parent, null, "", v1, v2, "endArrow=none;");

                }

                //jump first letter "e"
                scanner.next();

                //pair of vertices
                firstVertex = scanner.nextInt();
                secondVertex = scanner.nextInt();

            }


        } finally {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        new mxCircleLayout(graph, 600).execute(graph.getDefaultParent());
        new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());

        getContentPane().add(graphComponent);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Graph frame = new Graph();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
    }

}
