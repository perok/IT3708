package algorithms.ea.fitness;

import algorithms.ea.individual.IPhenotype;

import java.util.List;

/**
 * Created by Perÿyvind on 06/03/2015.
 */
public class OneToOneFitness implements IFitness<Integer> {


    @Override
    public double calculateFitness(List<IPhenotype<Integer>> phenotypes) {
        return phenotypes.stream()
                .mapToDouble(IPhenotype::getValue)
                .sum();
    }
}
