package algorithms.ea.individual.operators.mutation;

import algorithms.ea.individual.Genotype;

/**
 * Created by Perÿyvind on 06/03/2015.
 * todo will not work well now..
 */
public interface IMutation {
    Genotype mutate(Genotype genotypes);
}
