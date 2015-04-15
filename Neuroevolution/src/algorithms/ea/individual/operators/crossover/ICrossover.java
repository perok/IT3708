package algorithms.ea.individual.operators.crossover;

import algorithms.ea.individual.Genotype;

import java.util.List;

/**
 * Created by Perÿyvind on 06/03/2015.
 */
public interface ICrossover {

    List<Genotype> crossover(Genotype genotypesA, Genotype genotypesB);
}
