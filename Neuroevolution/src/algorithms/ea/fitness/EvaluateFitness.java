package algorithms.ea.fitness;

import algorithms.ea.individual.IPhenotype;

import java.util.List;

/**
 * Created by Perÿyvind on 06/03/2015.
 */
public class EvaluateFitness {

    public static double Evaluate(IFitness fitness, List<IPhenotype> phenotypes) {
        return fitness.calculateFitness(phenotypes);
    }
}
