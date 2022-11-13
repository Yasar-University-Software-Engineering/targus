package com.targus.represent;

import com.targus.base.Representation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitString implements Representation {
    public BitSet bitSet;
    public BitString(BitSet bitSet) {
        this.bitSet = bitSet;
    }

    @Override
    public Representation clone() {
        return new BitString(bitSet);
    }

    public List<Integer> ones() {
        List<Integer> ones= new ArrayList<>();
        for (int i = 0; i < bitSet.size(); i++) {
            if (bitSet.get(i))
                ones.add(i);
        }
        return ones;
    }
}
