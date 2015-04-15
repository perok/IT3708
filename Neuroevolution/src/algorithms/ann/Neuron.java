package algorithms.ann;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by PerØyvind on 13/04/2015.
 */
public class Neuron {

    // Weights 1..N, N+1 is activation value
    private List<Double> weights;

    int numberOfInputs;

    public Neuron(int inputs){
        Random random = new Random();
        numberOfInputs = inputs;

        //inputs = new LinkedList<>();
        weights = new LinkedList<>();

        IntStream.range(0, numberOfInputs + 1)
                .forEach(i -> weights.add(random.nextDouble()));
    }

    public int getNumberOfWeights(){
        return numberOfInputs;
    }

    public Double getWeight(int index){
        return weights.get(index);
    }
}
