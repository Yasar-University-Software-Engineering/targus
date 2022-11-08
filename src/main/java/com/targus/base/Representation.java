package com.targus.base;

public interface Representation {
    int hashCode();
    boolean equals(Object obj);
    Representation clone();
}
