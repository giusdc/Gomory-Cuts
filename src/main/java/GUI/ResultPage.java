package GUI;

import entities.Result;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.omg.CORBA.ValueMember;

import javax.swing.*;
import java.awt.*;

public class ResultPage {
    private JPanel resultPageView;
    private JPanel chartPanel;

    public static void main(String[] args) {
        new ResultPage(new Result());
    }

    public ResultPage(Result result) {

        JFrame frame = new JFrame("Results");

        frame.setContentPane(resultPageView);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        frame.setVisible(true);

        //DefaultXYDataset dataset = new DefaultXYDataset();
        //dataset.addSeries(Object::hashCode, new double[][]{{0.1, 0.2, 0.3}, {1,2,3}});

        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        dataset1.addValue(5, "1", "1");
        dataset1.addValue(9, "2", "2");
        dataset1.addValue(3, "3", "3");

        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("key", new double[][]{{0.1, 0.2, 0.3}, {6, 2, 3}});

        ValueMarker marker = new ValueMarker(6);
        marker.setPaint(Color.BLACK);

        JFreeChart jFreeChart = ChartFactory.createXYLineChart("title", "x", "y", dataset);

        XYPlot plot = (XYPlot) jFreeChart.getPlot();
        plot.addRangeMarker(marker);

        chartPanel.setLayout(new java.awt.BorderLayout());
        ChartPanel CP = new ChartPanel(jFreeChart);
        chartPanel.add(CP, BorderLayout.CENTER);
        chartPanel.validate();
    }
}
