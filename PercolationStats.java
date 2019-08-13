import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private static final double NCON = 1.96;
    private final double[] percAtArr;
    private final int t;
    private double mean = 0;
    private double stddev = 0;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1)
            throw new IllegalArgumentException("MIN value is 1");
        percAtArr = new double[trials];
        t = trials;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            }
            percAtArr[i] = (double) p.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean == 0)
            mean = StdStats.mean(percAtArr);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddev == 0)
            stddev = StdStats.stddev(percAtArr);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (NCON * stddev() / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (NCON * stddev() / Math.sqrt(t));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("\t\t\t= " + ps.mean() + "\rmean" + "\n\t\t\t= " + ps.stddev() + "\rstddev" + "\n\t\t\t= ["
                + ps.confidenceLo() + ", " + ps.confidenceHi() + "]" + "\r95% confidenceinterval");
    }

}
