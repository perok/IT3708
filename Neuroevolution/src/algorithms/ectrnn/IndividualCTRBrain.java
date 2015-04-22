package algorithms.ectrnn;

import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.Individual;
import helpers.Conversion;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Per�yvind on 19/04/2015.
 *
 *
 * Each Neuron type has a different lower-upper bound. So
 * they need to get the Byte
 */
public class IndividualCTRBrain extends Individual<Byte>{

    private CTRNeuralNet brain;
    private boolean wrapAround = false;

    public IndividualCTRBrain() {
        init();
    }

    public IndividualCTRBrain(boolean wrapAround) {
        this.wrapAround = wrapAround;
        init();
    }

    public void buildPhenotypes() {
        this.phenotypes = IntStream.range(0, this.genotypes.getActualData().length)
                .mapToObj(i -> this.genotypes.getActualData()[i])
                .collect(Collectors.toList());
        System.out.println("Phenotypes built: " + this.phenotypes.size() + " " + this.genotypes.getActualData().length);
    }

    private void init() {
        // ----------------------------------
        // Artificial Neural Network
        // Inputs: 5 or 7
        // Outputs: 2
        // Hidden Layers: 1
        // Neuron in each hidden layer: 2
        // ----------------------------------
        brain = new CTRNeuralNet(wrapAround ? 5 : 7, 2, 1, 2);

        // Get brain data and make genotype of it
        // todo getWeights must return correct Byte
        List<Byte> weights = brain.getWeights(); // getfrom number of weights

        byte[] genotypeData = new byte[weights.size()]; // get all the weights
        IntStream.range(0, weights.size())
                .forEach(i -> genotypeData[i] = weights.get(i));

        this.genotypes = new Genotype(genotypeData, weights.size());

        System.out.println("Brain done: " + this.genotypes.getActualData().length + " " + weights.size());
    }

    /**
     * Inserts the phenotypes to the brain as weights
     */
    public void rewireBrain(){
        brain.setWeights(this.getPhenotypes());
    }

    public void setWrapAround(boolean wrapAround) {
        this.wrapAround = wrapAround;
        init();
    }

    public List<Double> think(List<Double> input){
        return brain.update(input);
    }
}
