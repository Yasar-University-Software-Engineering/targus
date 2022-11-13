package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import com.targus.problem.ObjectiveFunction;
import com.targus.problem.ObjectiveType;
import com.targus.represent.BitString;
import javafx.geometry.Point2D;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class WSNMinimumSensorObjective implements ObjectiveFunction {
    @Override
    public double value(ProblemModel model, Representation r) {
        WSN wsn = (WSN) model;
        BitString bitString = (BitString) r;
        BitSet bitSet = bitString.bitSet;

        List<Integer> sensors = bitString.ones();

        double sensorPenValueScaled = (double) bitSet.cardinality() / bitSet.length();
        double mConnPenValueScaled = (double) mConnPenSum(wsn,sensors) / (wsn.potentialPositionSet.length * wsn.m);
        double kCoverPenValueScaled = (double) wsn.kCoverPenSum(r) / (wsn.targetSet.length * wsn.k);

        return sensorPenValueScaled + mConnPenValueScaled + kCoverPenValueScaled;
    }

    public int mConnPenSum(WSN wsn, List<Integer> sensors) { // Ignore bitset for now
        int penSum = 0;
        int m = wsn.getM();

        for (Integer sensor:sensors)
        {
            penSum += m-Math.min(wsn.mConnSensors(sensor,sensors),m);
        }

        return penSum;
    }

    @Override
    public ObjectiveType type() {
        return null;
    }
}
