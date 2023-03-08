package com.targus.algorithm.aco.problem.tsp;

import core.algorithm.aco.problem.tsp.tsplib.datamodel.tsp.Tsp;
import core.base.ProblemModel;
import core.base.Representation;
import core.representation.Permutation;

public class TSP implements ProblemModel {

    int n;
    double distances[][];

    public TSP(int n, double[][] distances) {
        this.n = n;
        this.distances = distances;
    }

    public static TSP fromTsp(Tsp tsp)
    {
        double distances[][] = new double[tsp.getDimension()][tsp.getDimension()];

        for (int r = 0; r < distances.length; r++) {
            for (int c = 0; c < distances.length; c++) {
                if (r==c) {
                    distances[r][c]=0;
                    continue;
                }
                double x1= tsp.getNodes().get().get(r).getX();
                double y1 = tsp.getNodes().get().get(r).getY();
                double x2= tsp.getNodes().get().get(c).getX();
                double y2 = tsp.getNodes().get().get(c).getY();

                distances[r][c] = Math.sqrt( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
            }
        }
        TSP tsp1 = new TSP(tsp.getDimension(),distances);
        return tsp1;
    }

    @Override
    public boolean isFeasible(Representation r) {
        assert r instanceof Permutation : "TSP only accepts Permutation";

        Permutation p = (Permutation) r;

        return p.size()==n;

    }

    public int getN() {
        return n;
    }

    public double getDistance(Integer first, Integer second) {
        return distances[first][second];
    }
}
