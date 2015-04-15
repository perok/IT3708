package algorithms.ea.individual.operators;

import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.operators.crossover.ICrossover;
import algorithms.ea.individual.operators.mutation.IMutation;

import java.util.List;

/**
 * Created by Perÿyvind on 06/03/2015.
 *
 * Helper methods to perform mutation and crossover.
 *
 */
public class GeneticOperator {
    public static Genotype mutate(IMutation mutator, Genotype genotypes) {
        return mutator.mutate(genotypes);
    }

    public static List<Genotype> crossover(ICrossover crossoverer, Genotype genotypesA, Genotype genotypesB) {
        return crossoverer.crossover(genotypesA, genotypesB);
    }
}
