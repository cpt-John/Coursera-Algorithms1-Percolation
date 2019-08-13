
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[] gridStO;
    private final int source;
    private final int sink;
    private final int len;
    private final int elms;
    private int openSites = 0;
    private final WeightedQuickUnionUF wquf;
    private final WeightedQuickUnionUF wqufR;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException("MIN value is 1 ");
        len = n;
        elms = n * n;
        source = elms;
        sink = elms + 1;
        wquf = new WeightedQuickUnionUF(elms + 2);
        wqufR = new WeightedQuickUnionUF(elms + 1);
        gridStO = new boolean[elms];
        for (int i = 0; i < len; i++) {
            gridStO[i] = false;
        }
    }

    // check for invald input
    private void validate(int row, int col, int length) {
        if (row < 1 || row > length || col < 1 || col > length)
            throw new IllegalArgumentException("Cell not available inside grid limits");
    }

    // covert to 1d
    private int to1D(int row, int col, int length) {
        int site = ((row - 1) * length) + (col - 1);
        return site;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col, len);
        int site = to1D(row, col, len);
        int[] adjSites = { site - len, site + len, site - 1, site + 1 };
        if (!gridStO[site]) {
            gridStO[site] = true;
            // connect to adj sites
            if (adjSites[0] >= 0 && gridStO[adjSites[0]]) {
                wquf.union(site, adjSites[0]);
                wqufR.union(site, adjSites[0]);
            }
            if (adjSites[1] < len * len && gridStO[adjSites[1]]) {
                wquf.union(site, adjSites[1]);
                wqufR.union(site, adjSites[1]);
            }
            if (col > 1 && gridStO[adjSites[2]]) {
                wquf.union(site, adjSites[2]);
                wqufR.union(site, adjSites[2]);
            }
            if (col < len && gridStO[adjSites[3]]) {
                wquf.union(site, adjSites[3]);
                wqufR.union(site, adjSites[3]);
            }
            openSites++;
            // connect to source and sink
            if (site >= 0 && site < len) {
                wquf.union(site, source);
                wqufR.union(site, source);
            }
            if (site >= elms - len && site < elms)
                wquf.union(site, sink);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col, len);
        int site = to1D(row, col, len);
        return gridStO[site];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col, len);
        int site = to1D(row, col, len);
        return wqufR.connected(site, source);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return wquf.connected(source, sink);
    }

}
