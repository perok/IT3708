package algorithms.ann;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by PerØyvind on 13/04/2015.
 */
public class NeuronLayer {

    List<Neuron> neurons;

    public NeuronLayer(int numNeurons, int inputsPerNeuron){

        IntStream.range(0, numNeurons)
                .forEach((i) ->
                    neurons.add(new Neuron(inputsPerNeuron))
                );
    }


    public List<Neuron> getNeurons(){
        return neurons;
    }
}
