package entities;

import java.util.List;

public class Result {

    private List<Long> times;
    private String path;
    private double [][] data;
    private int optimal;

    public Result(List<Long> times, String path, double[][] data, int optimal) {
        this.times = times;
        this.path = path;
        this.data = data;
        this.optimal = optimal;
    }
}
