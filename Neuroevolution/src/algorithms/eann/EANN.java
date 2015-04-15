package algorithms.eann;

import algorithms.ea.Evolution;
import algorithms.ea.adultselection.ParentSelections;
import algorithms.ea.fitness.IFitness;
import algorithms.ea.individual.operators.mutation.MaybeMutator;
import algorithms.ea.individual.operators.crossover.OneCrossover;
import algorithms.ea.mating.MatingTechniques;

/**
 * Created by Perÿyvind on 03/04/2015.
 */
public class EANN {
    Evolution evolution;

    public EANN(IFitness<Double> fitness){


        int size = 64;


        evolution = new Evolution();
        evolution = evolution
                .setPhenotypeBuilder(new DoublesPhenotypeBuilder(size))
                // todo
                //Fitness function must run ANN on the Flatland environment
                .setFitnessFunction(fitness)
                .setMatingStrategy(MatingTechniques.SIGMA_SCALING)
                .setAdultSelectionsStrategy(ParentSelections.FULL)
                .setMutator(new MaybeMutator(0.1))
                .setCrossover(new OneCrossover())
                .setNUMBER_OF_ITERATIONS(1000)
                .setCHIlDREN_POOL_SIZE(200)
                .setPARENT_POOL_SIZE(110)
                .build();



    }
}
