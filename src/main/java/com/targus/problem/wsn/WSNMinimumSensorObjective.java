package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import com.targus.problem.ObjectiveFunction;
import com.targus.problem.ObjectiveType;
import com.targus.represent.BitString;

import java.util.BitSet;
import java.util.HashSet;

public class WSNMinimumSensorObjective implements ObjectiveFunction {

    public final static double weightSensor = 0.1;
    public final static double weightMComm = 5;
    public final static double weightKCov = 5;

    @Override
    public double value(ProblemModel model, Representation r) {
        WSN wsn = (WSN) model;
        BitString bitString = (BitString) r;
        BitSet bitSet = bitString.getBitSet();

        HashSet<Integer> sensors = bitString.ones();

        double sensorPenValueScaled = getSensorPenValueScaled(wsn, bitSet);

        double mConnPenValueScaled = getMConnPenValueScaled(wsn, sensors);

        double kCoverPenValueScaled = getKCoverPenValueScaled(wsn, sensors);

        return sensorPenValueScaled * weightSensor + mConnPenValueScaled * weightMComm + kCoverPenValueScaled * weightKCov;
    }

    public double getSensorPenValueScaled(WSN wsn, BitSet bitSet) {
        return wsn.getSolutionSize() == 0 ?
                0 : ((double) bitSet.cardinality() / wsn.getSolutionSize());
    }

    public double getMConnPenValueScaled(WSN wsn, HashSet<Integer> sensors) {
        return sensors.size() == 0 || wsn.getM() == 0 ?
                0 : (double) mConnPenSum(wsn, sensors) / (sensors.size() * wsn.getM());
    }

    public double getKCoverPenValueScaled(WSN wsn, HashSet<Integer> sensors) {
        return wsn.targetsSize() == 0 || wsn.getK() == 0 ?
                0 : (double) kCovPenSum(wsn, sensors) / (wsn.targetsSize() * wsn.getK());
    }

    public int mConnPenSum(WSN wsn, HashSet<Integer> sensors) {
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

    public int kCovPenSum(WSN wsn, HashSet<Integer> sensors) {
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
