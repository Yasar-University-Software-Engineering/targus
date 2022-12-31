package com.targus.represent;

import com.targus.base.Representation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;

public class BitString implements Representation {
    private BitSet bitSet;
    public BitString(BitSet bitSet) {
        this.bitSet = bitSet;
    }

    public HashSet<Integer> ones() {
        HashSet<Integer> ones= new HashSet<>();
        for (int i = 0; i < bitSet.size(); i++) {
            if (bitSet.get(i))
                ones.add(i);
        }
        return ones;
    }

    public String toString() {
        return bitSet.toString();
    }

    public void flip(int bitIndex) {
        bitSet.flip(bitIndex);
    }

    public void set(int bitIndex, boolean value) {
        bitSet.set(bitIndex, value);
    }

    public int length() {
        return bitSet.length();
    }

    public boolean get(int bitIndex) {
        return bitSet.get(bitIndex);
    }

    public BitSet getBitSet() {
        return bitSet;
    }

    @Override
    public int hashCode() {
        return bitSet.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return bitSet.equals(((BitString) obj).bitSet);
    }

    @Override
    public Representation clone() {
        return new BitString((BitSet) bitSet.clone());
    }
}
