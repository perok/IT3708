package algorithms.ea.individual.operators.mutation;

import algorithms.ea.individual.Genotype;

/**
 * Created by Per�yvind on 06/03/2015.
 */
public interface IMutation {
    Genotype mutate(Genotype genotypes);
}
