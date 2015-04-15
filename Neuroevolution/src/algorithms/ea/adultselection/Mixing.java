package algorithms.ea.adultselection;

import algorithms.ea.Helper;
import algorithms.ea.individual.Individual;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public class Mixing implements IParentSelection{
    /**
     *         // All previous adults die, but m (the maximum size of the adult pool) is smaller
     // than n (the number of children). Hence, the children must compete among themselves for the m adult
     // spots, so selection pressure is significant. This is also known as (µ, ?) selection, where µ and ? are sizes
     // of the adult and child pools, respectively.
     * @param individuals
     * @param cGeneration
     * @return
     */
    @Override
    public List<Individual> performParentSelection(List<Individual> individuals, int cGeneration) {

        List<Individual> rofl = individuals.stream()
                .filter(i -> !(i.getAge() < cGeneration))
                .collect(Collectors.toList());
        rofl = rofl.stream()
                .sorted().collect(Collectors.toList());
        rofl = rofl.stream()
                .skip(Helper.toSkip(rofl.size(), PARENT_POOL_SIZE))
                .collect(Collectors.toList());

        return rofl;
    }
}
