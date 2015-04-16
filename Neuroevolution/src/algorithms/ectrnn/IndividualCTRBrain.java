package algorithms.ectrnn;

import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.Individual;
import helpers.Conversion;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Perÿyvind on 19/04/2015.
 *
 *
 * Each Neuron type has a different lower-upper bound. So
 * they need to get the Byte
 */
public class IndividualCTRBrain extends Individual<Byte>{
    private CTRNeuralNet brain;

    public IndividualCTRBrain() {
        // ----------------------------------
        // Artificial Neural Network
        // Inputs: 5
        // Outputs: 2
        // Hidden Layers: 1
        // Neuron in each hidden layer: 2
        // ----------------------------------
        brain = new CTRNeuralNet(5, 2, 1, 2);

        // Get brain data and make genotype of it
        // todo getWeights must return correct Byte
        List<Byte> weights = brain.getWeights(); // getfrom number of weights

        byte[] genotypeData = new byte[weights.size()]; // get all the weights
        IntStream.range(0, weights.size())
                .forEach(i -> genotypeData[i] = new Byte(weights.get(i)));

        this.genotypes = new Genotype(genotypeData, weights.size());
    }

    public void buildPhenotypes() {
        this.phenotypes = IntStream.range(0, this.genotypes.getActualData().length)
                .mapToObj(i -> this.genotypes.getActualData()[i])
                .collect(Collectors.toList());
    }

    /**
     * Inserts the phenotypes to the brain as weights
     */
    public void rewireBrain(){
        brain.setWeights(this.getPhenotypes());
    }


    public List<Double> think(List<Double> input){
        return brain.update(input);
    }
}
