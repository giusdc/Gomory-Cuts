package GUI;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class GraphGUI {
    private JPanel panel;

    public GraphGUI(String path, int vertex) {

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {

            //create object to scan file
            Scanner scanner = new Scanner(new File(path));

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

            while (firstVertex <= vertex) {

                //skip too high vertex values
                if (secondVertex <= vertex) {

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

                if(!scanner.hasNext()) break;

                //jump first letter "e"
                scanner.next();

                //pair of vertices
                firstVertex = scanner.nextInt();
                secondVertex = scanner.nextInt();

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        new mxCircleLayout(graph, 500).execute(graph.getDefaultParent());
        new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());


        JFrame frame = new JFrame("Graph");
        panel = new JPanel();
        frame.setContentPane(panel);
        panel.add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
