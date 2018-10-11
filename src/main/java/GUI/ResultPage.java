package GUI;

import entities.Result;
import javafx.scene.chart.NumberAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.omg.CORBA.ValueMember;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class ResultPage {
    private JPanel resultPageView;
    private JPanel chartPanel;
    private JList timeList;
    private JList pathList;
    private JLabel timeLabel;
    private JLabel pathLabel;

    public ResultPage(Result result, boolean singleCut) {

        JFrame frame = new JFrame("Results");

        frame.setContentPane(resultPageView);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.pack();

        frame.setVisible(true);

        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("relaxed objective function", result.getData());
        dataset.addSeries("optimal binary solution", result.getOptimal());

        JFreeChart jFreeChart = ChartFactory.createXYLineChart("objective function plot", "n-th gomory cut(s) iteration", "objective function", dataset);

        XYPlot plot = (XYPlot) jFreeChart.getPlot();
        if(!singleCut) {
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            renderer.setSeriesPaint(1, Color.BLACK);
            renderer.setSeriesStroke(1, new BasicStroke(2.0f));
            plot.setRenderer(renderer);
        }
        ValueAxis domainAxis = plot.getDomainAxis();
        TickUnits tickUnits = new TickUnits();
        for (double tick: result.getData()[0]){
            tickUnits.add(new NumberTickUnit(tick));
        }
        domainAxis.setStandardTickUnits(tickUnits);
        ValueAxis rangeAxis = plot.getRangeAxis();
        double margin = String.valueOf((int) result.getOptimal()[1][0]).length();
        if (margin > 2)
            margin = Math.pow(10, margin - 2);
        else if (margin == 1)
            margin = 1;
        else if (margin == 2)
            margin = 5;

        rangeAxis.setRange(result.getData()[1][0] - margin, result.getOptimal()[1][0] + margin);

        plot.setBackgroundPaint(Color.white);

        chartPanel.setLayout(new java.awt.BorderLayout());
        ChartPanel CP = new ChartPanel(jFreeChart);
        chartPanel.add(CP, BorderLayout.CENTER);
        chartPanel.validate();

        timeList.setListData(result.getTimes().toArray());

        File folder = new File(result.getPath());
        File[] listOfFiles = folder.listFiles();

        Vector<File> vector = new Vector<>();

        for (File f : listOfFiles) {
            if (f.isFile()) {
                vector.add(f);
            }
        }

        pathList.setListData(vector);

        pathList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    File selectedFile = (File) pathList.getSelectedValue();
                    try {
                        Desktop.getDesktop().open(selectedFile);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
