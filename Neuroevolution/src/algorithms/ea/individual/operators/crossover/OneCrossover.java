package algorithms.ea.individual.operators.crossover;

import algorithms.ea.Constants;
import algorithms.ea.individual.Genotype;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Perÿyvind on 09/03/2015.
 */
public class OneCrossover implements ICrossover {
    Random random = new Random();

    @Override
    public List<Genotype> crossover(Genotype genotypesA, Genotype genotypesB) {
        int crossoverPoint = random.nextInt(genotypesA.getFixedLength());

        Genotype n1 = new Genotype(genotypesA.getFixedLength());
        Genotype n2 = new Genotype(genotypesA.getFixedLength());

        for(int i = 0; i < genotypesA.getFixedLength(); i++) {
            if(i > crossoverPoint) {
                n1.getData().set(i, genotypesB.getData().get(i));
                n2.getData().set(i, genotypesA.getData().get(i));
            } else {
                n1.getData().set(i, genotypesA.getData().get(i));
                n2.getData().set(i, genotypesB.getData().get(i));
            }
        }

        List<Genotype> genotypes = new LinkedList<>();
        if(random.nextDouble() < Constants.CROSSOVER_CHANCE)
            genotypes.add(n1);
        if(random.nextDouble() < Constants.CROSSOVER_CHANCE)
            genotypes.add(n2);

        return genotypes;
    }
}
