package algorithms.ectrnn;

import algorithms.ea.Evolution;
import algorithms.ea.adultselection.ParentSelections;
import algorithms.ea.individual.operators.crossover.OneCrossover;
import algorithms.ea.individual.operators.mutation.MaybeMutator;
import algorithms.ea.mating.MatingTechniques;
import algorithms.eann.IndividualBrain;

import java.util.List;

/**
 * Created by Per�yvind on 03/04/2015.
 *
 * Feed forward neural net brain
 */
public class Neuroevolution {
    Evolution<IndividualCTRBrain> evolution;

    public Neuroevolution(){
        evolution = new Evolution<>(IndividualCTRBrain.class);
        evolution = evolution
                .setMatingStrategy(MatingTechniques.SIGMA_SCALING)
                .setAdultSelectionsStrategy(ParentSelections.OVER_PRODUCTION)
                .setMutation(new MaybeMutator(0.1))
                .setCrossover(new OneCrossover())
                .setNUMBER_OF_ITERATIONS(1000)
                .setCHIlDREN_POOL_SIZE(200)
                .setPARENT_POOL_SIZE(110)
                .build();
    }


    // todo Evolves a set of IndividualBrains with the evolution algorithm
    public List<IndividualCTRBrain> evolve(List<IndividualCTRBrain> population, int generation) {
        return evolution.nextEpoch(population, generation);
    }

    public void setElitism(int elites) {
        evolution.setEliteism(elites);
    }

    public int getElitism() {
        return evolution.getEliteism();
    }
}
