package algorithms.ectrnn;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by Per�yvind on 13/04/2015.
 */
public class Neuron {

    // ================================================
    // Bounds
    // ================================================
    private int lowerBoundWeight = -5;
    private int higherBoundWeight = 5;

    private int lowerBoundGain = 1;
    private int higherBoundGain = 5;

    private int lowerBoundTime = 1;
    private int higherBoundTime = 2;

    private int lowerBoundBias = -5;
    private int higherBoundBias = 5;

    // ================================================
    // Variables
    // ================================================
    int numberOfInputs;
    int numberOfEvolvableWeights;
    int numberOfOtherOnLayerInputs;

    // 1..N = Weights, N+1 = bias, N+2 = gain, N+3 = time
    private List<Double> weights;
    private List<Byte> weightsByte;

    // Internal state
    private double y = 0;

    // Last output
    private double lastOutput = 0;

    public Neuron(int inputs, int layerSize){
        Random random = new Random();
        numberOfInputs = inputs + 1; // +1 = bias input
        numberOfOtherOnLayerInputs = layerSize;
        //Feed forward inputs + bias | Other layers including self | Gain | time
        // 1..N                      | N -> - 3                    | -2   |  -1

        numberOfEvolvableWeights = numberOfInputs + (numberOfOtherOnLayerInputs) + 2;
        weights = new LinkedList<>();
        weightsByte = new LinkedList<>();

        IntStream.range(0, numberOfEvolvableWeights)
                .forEach(i -> {
                    byte[] onebyte = new byte[1];

                    random.nextBytes(onebyte);
                    setWeight(i, onebyte[0]);
                });
    }

    /**
     * Normal feedforward weights
     *
     * @return
     */
    public int getNumberOfWeights(){
        return numberOfInputs;
    }

    public Double getWeight(int index){
        return weights.get(index);
    }

    public Byte getByteWeight(int index){
        return weightsByte.get(index);
    }

    public Double setWeight(int index, Byte value){
        weightsByte.set(index, value);

        double v;

        if(index == numberOfInputs - 1) // bias
            v = convert(value, lowerBoundBias, higherBoundBias);
        else if(index == numberOfInputs) // gain
            v = convert(value, lowerBoundGain, higherBoundGain);
        else if(index == numberOfInputs + 1) // time
            v = convert(value, lowerBoundTime, higherBoundTime);
        else // weight
            v = convert(value, lowerBoundWeight, higherBoundWeight);

        return weights.set(index, v);
    }

    public double convert(byte value, int lower, int higher){
        return value * ((lower + higher) / 256.0) - lower;
    }


    public double getY() {
        return y;
    }

    public void addToY(double y) {
        this.y += y;
    }


    public double getBias() {
        return weights.get(numberOfInputs - 1);
    }

    public double getOtherLayersLength() {
        return numberOfOtherOnLayerInputs;
    }

    public double getOtherLayerWeight(int index) {
        assert (index <= numberOfOtherOnLayerInputs);

        return weights.get(numberOfInputs - 1 + index);
    }

    public double getLastOutput(){
        return lastOutput;
    }

    public void setLastOutput(double lastOutput){
        this.lastOutput = lastOutput;
    }

    public double getGain() {
        return weights.get(numberOfEvolvableWeights - 2);
    }

    public double getTime() {
        return weights.get(numberOfEvolvableWeights - 1);
    }


}
