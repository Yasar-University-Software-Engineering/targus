package com.targus.represent;

import com.targus.base.Representation;

import java.util.BitSet;

public class BitString implements Representation {
    public BitSet bitSet;
    public BitString(BitSet bitSet) {
        this.bitSet = bitSet;
    }

    @Override
    public Representation clone() {
        return new BitString(bitSet);
    }
}
