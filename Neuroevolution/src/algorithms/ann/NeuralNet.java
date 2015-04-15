package algorithms.ann;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public class NeuralNet {
    int numInputs;
    int numOutputs;
    int numHiddenLayers;
    int neuronsPerHiddenLayer;

    List<NeuronLayer> neuronLayers;

    public NeuralNet(){
    }

    public void createNet() {

        neuronLayers = new LinkedList<>();


        if(numHiddenLayers > 0) {
            // Add first hidden layer connected from input layer
            neuronLayers.add(new NeuronLayer(neuronsPerHiddenLayer, numInputs));

            // Add other layers
            for (int i = 0; i < numHiddenLayers - 1; i++) {
                neuronLayers.add(new NeuronLayer(neuronsPerHiddenLayer, neuronsPerHiddenLayer));
            }

            // Create output layer
            neuronLayers.add(new NeuronLayer(numOutputs, neuronsPerHiddenLayer));
        } else {
            // Create output layer
            neuronLayers.add(new NeuronLayer(numOutputs, numInputs));
        }
    }


    // getWe

    // set weights


    /**
     * Basicly execute
     * @param inputs
     * @return
     */
    public List<Double> update(List<Double> inputs) {
        // The results
        List<Double> outputs = new LinkedList<>();


        int cWeight;

        // Incorrect amount if inputs. Return empty
        if(inputs.size() != numInputs) {
            return new LinkedList<>();
        }

        // Now we walk through each layer
        for (int i = 0; i < numHiddenLayers + 1; i++) {
            // When input layer has been traversed we update to the next layer
            if(i > 0)
                inputs = outputs;

            outputs = new LinkedList<>();//.clear();

            cWeight = 0;


            for (int j = 0; j < neuronLayers.get(i).getNeurons().size(); j++) {

                double collectedInput = 0;

                int numInputs = neuronLayers.get(i).getNeurons().get(j).numberOfInputs;

                // Each weight
                for (int k = 0; k < numInputs - 1; k++) {
                    collectedInput += neuronLayers.get(i).getNeurons().get(j).getWeight(k) * inputs.get(cWeight++);
                }

                // todo +1?
                // todo BIAS? always -1
                collectedInput += neuronLayers.get(i).getNeurons().get(j).getWeight(numInputs - 1) * -1;

                // todo 1 should be static settings value
                outputs.add(activationFunction(collectedInput, 1));

                cWeight = 0;
            }
            
            /*
            collectedWeight = neuronLayers.stream()
                    .mapToDouble((neuronLayer ->
                        IntStream.range(0, neuronLayer.neurons.size())
                                .mapToDouble(y -> neuronLayer.neurons.get(y) * inputs.get(y))
                                .sum()
                    ));
                    */
        }

        return outputs;


    }

    private double layerweight(final NeuronLayer neuronLayer, final List<Double> inputs) {
        return IntStream.range(0, neuronLayer.neurons.size())
                .mapToDouble(y ->
                        IntStream.range(0, neuronLayer.neurons.get(y).getNumberOfWeights())
                                .mapToDouble(x -> neuronLayer.neurons.get(y).getWeight(x) * inputs.get(x))
                                .sum()
                )
                .sum();
    }


    private double activationFunction(Double netInput, double p) {
        return 1 / (1 + Math.exp( -netInput/p));


    }

}
