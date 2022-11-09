package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import com.targus.problem.ObjectiveFunction;
import com.targus.problem.ObjectiveType;
import com.targus.represent.BitString;

import java.util.BitSet;

public class WSNMinimumSensorObjective implements ObjectiveFunction {
    @Override
    public double value(ProblemModel model, Representation r) {
        WSN wsn = (WSN) model;
        BitString bitString = (BitString) r;
        BitSet bitSet = bitString.bitSet;

        double sensorPenValueScaled = (double) bitSet.cardinality() / bitSet.length();
        double mConnPenValueScaled = (double) wsn.mConnPenSum(r) / (wsn.potentialPositionSet.length * wsn.m);
        double kCoverPenValueScaled = (double) wsn.kCoverPenSum(r) / (wsn.targetSet.length * wsn.k);

        return sensorPenValueScaled + mConnPenValueScaled + kCoverPenValueScaled;
    }

    @Override
    public ObjectiveType type() {
        return null;
    }
}
