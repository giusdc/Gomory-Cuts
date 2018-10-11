package entities;

import lombok.Getter;

import java.util.List;

@Getter
public class Result {

    private List<String> times;
    private String path;
    private double [][] data, optimal;

    public Result(List<String> times, String path, double[][] data, double[][] optimal) {
        this.times = times;
        this.path = path;
        this.data = data;
        this.optimal = optimal;
    }
}
