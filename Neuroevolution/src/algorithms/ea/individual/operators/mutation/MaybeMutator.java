package algorithms.ea.individual.operators.mutation;

import algorithms.ea.individual.Genotype;

import java.util.Random;

/**
 * Created by Perÿyvind on 09/03/2015.
 */
public class MaybeMutator implements IMutation {
    double chance;
    Random random;
    public MaybeMutator(double chance) {
        this.chance = chance;
        this.random = new Random();
    }

    @Override
    public Genotype mutate(Genotype genotypes) {
        Genotype newBitSet = genotypes.makeCopy();

        if(random.nextDouble() < chance) {
            newBitSet.getData().flip(random.nextInt(genotypes.getFixedLength()));
        }

        return newBitSet;
    }
}
