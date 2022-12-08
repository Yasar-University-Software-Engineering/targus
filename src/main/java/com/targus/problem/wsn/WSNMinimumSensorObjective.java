package com.targus.problem.wsn;

import com.targus.base.ProblemModel;
import com.targus.base.Representation;
import com.targus.problem.ObjectiveFunction;
import com.targus.problem.ObjectiveType;
import com.targus.represent.BitString;

import java.util.BitSet;
import java.util.List;

public class WSNMinimumSensorObjective implements ObjectiveFunction {

    public final static double weightSensor = 0.1;
    public final static double weightMComm = 0.45;
    public final static double weightKCov = 0.45;

    @Override
    public double value(ProblemModel model, Representation r) {
        WSN wsn = (WSN) model;
        BitString bitString = (BitString) r;
        BitSet bitSet = bitString.getBitSet();

        List<Integer> sensors = bitString.ones();

        double sensorPenValueScaled = wsn.getPopulationSize() != 0 ?
                1 - ((double) bitSet.cardinality() / wsn.getPopulationSize()) : 0;

        double mConnPenValueScaled = sensors.size() * wsn.getM() != 0 ?
                (double) mConnPenSum(wsn, sensors) / (sensors.size() * wsn.getM()) : 0;

        double kCoverPenValueScaled = wsn.targetsSize() * wsn.getK() != 0 ?
                (double) kCovPenSum(wsn, sensors) / (wsn.targetsSize() * wsn.getK()) : 1;

        return sensorPenValueScaled * weightSensor + mConnPenValueScaled * weightMComm + kCoverPenValueScaled * weightKCov;
    }

    public int mConnPenSum(WSN wsn, List<Integer> sensors) {
        int penSum = 0;
        int m = wsn.getM();

        for (Integer sensor:sensors)
        {
            int value = wsn.mConnSensors(sensor, sensors);
            if (value >= m) {
                penSum += m;
            } else {
                penSum += value;
            }
        }

        return penSum;
    }

    public int kCovPenSum(WSN wsn, List<Integer> sensors) {
        int penSum = 0;
        int k = wsn.getK();

        for (int i = 0; i < wsn.targetsSize(); i++) {
            int value = wsn.kCovTargets(i, sensors);
            if (value >= k) {
                penSum += k;
            } else {
                penSum += value;
            }
        }

        return penSum;
    }

    @Override
    public ObjectiveType type() {
        return ObjectiveType.Minimization;
    }
}
