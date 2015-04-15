package algorithms.ea.fitness;

import algorithms.ea.individual.IPhenotype;

import java.util.List;

/**
 * Created by Perÿyvind on 06/03/2015.
 */
public interface IFitness<T> {
    double calculateFitness(List<IPhenotype<T>> phenotypes);
}
