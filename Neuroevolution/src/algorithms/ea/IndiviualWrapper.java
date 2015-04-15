package algorithms.ea;

import algorithms.ea.individual.Individual;

import java.util.List;

/**
 * Created by Perÿyvind on 09/03/2015.
 */
public class IndiviualWrapper implements Comparable<IndiviualWrapper>{
    public Individual individual;
    public double fitness;


    public IndiviualWrapper(Individual individual, double fitness) {
        this.individual = individual;
        this.fitness = fitness;
    }

    public static Individual findWheelSpinn(List<IndiviualWrapper> indiviualWrappers, double wheelSpin) {
        double fitnessAccumulator = 0;

        for(int i = 0; i < indiviualWrappers.size(); i++) {
            fitnessAccumulator += indiviualWrappers.get(i).fitness;
            if(wheelSpin < fitnessAccumulator) {
                return indiviualWrappers.get(i).individual;
            }
        }

        return null;
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(IndiviualWrapper o) {
        if(getFitness() == o.getFitness()) return 0;
        else if(getFitness() > o.getFitness()) return 1;
        return 0;
    }
}
