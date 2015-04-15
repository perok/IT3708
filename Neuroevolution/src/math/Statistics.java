package math;

import algorithms.ea.individual.Individual;

import java.util.List;

/**
 * Created by Per�yvind on 10/03/2015.
 */
public class Statistics {
    public static double populationMean(List<Individual> population) {
        return population.stream()
                .mapToDouble(Individual::getFitness)
                .sum() / population.size();
    }

    public static double standardDeviationPopulation(List<Individual> population){
        double mean = populationMean(population);
        double SD = population.stream()
                .mapToDouble(Individual::getFitness)
                .map(i -> Math.pow(i - mean, 2))
                .sum();

        SD = Math.sqrt(SD / population.size());
        return SD == 0 ? 1 : SD; // todo correct way to solve this?
    }

    public static double sigmaScaling(double iFitness, double gMean, double gSD){
        return 1 + ((iFitness - gMean) / (2 * gSD));

    }
}
