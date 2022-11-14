package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import com.targus.problem.ObjectiveFunction;
import com.targus.problem.ObjectiveType;
import com.targus.represent.BitString;

import java.util.BitSet;
import java.util.List;

public class WSNMinimumSensorObjective implements ObjectiveFunction {
    @Override
    public double value(ProblemModel model, Representation r) {
        WSN wsn = (WSN) model;
        BitString bitString = (BitString) r;
        BitSet bitSet = bitString.bitSet;

        List<Integer> sensors = bitString.ones();

        double sensorPenValueScaled = (double) bitSet.cardinality() / bitSet.length();
        double mConnPenValueScaled = (double) mConnPenSum(wsn, sensors) / bitSet.length() * wsn.getM();
        double kCoverPenValueScaled = (double) kCovPenSum(wsn, sensors) / wsn.targetsSize() * wsn.getK();

        return sensorPenValueScaled + mConnPenValueScaled + kCoverPenValueScaled;
    }

    private int mConnPenSum(WSN wsn, List<Integer> sensors) {
        int penSum = 0;
        int m = wsn.getM();

        for (Integer sensor:sensors)
        {
            int value = wsn.mConnSensors(sensor, sensors);
            if (value < m) {
                penSum += m - value;
            }
        }

        return penSum;
    }

    private int kCovPenSum(WSN wsn, List<Integer> sensors) {
        int penSum = 0;
        int k = wsn.getK();

        for (int i = 0; i < wsn.targetsSize(); i++) {
            int value = wsn.kCovTargets(i, sensors);
            if (value < k) {
                penSum += k - value;
            }
        }

        return penSum;
    }

    @Override
    public ObjectiveType type() {
        return ObjectiveType.Minimization;
    }
}
