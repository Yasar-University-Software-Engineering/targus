package com.targus.problem;

interface SolutionComparator{
    boolean betterThan(double  d1, double d2);
}

public enum ObjectiveType {

    Maximization((d1,d2)->d1>d2), Minimization((d1,d2)-> d1<d2);


    ObjectiveType(SolutionComparator comparator) {
        this.comparator = comparator;
    }

    SolutionComparator comparator;

    public boolean betterThan(double d1, double d2)
    {
        return comparator.betterThan(d1,d2);
    }

}