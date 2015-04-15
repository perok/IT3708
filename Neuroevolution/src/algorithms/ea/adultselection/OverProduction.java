package algorithms.ea.adultselection;

import algorithms.ea.Helper;
import algorithms.ea.individual.Individual;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public class OverProduction implements IParentSelection {
    /**
     *         // The m adults from the previous generation do not die, so they and the n
     // children compete in a free-for-all for the m adult spots in the next generation. Here, selection pressure
     // on juveniles is extremely high, since they are competing with some of the best individuals that have
     // evolved so far, regardless of their age. This is also known as (µ + ?) selection, where the plus indicates
     // the mixing of adults and children during competition.
     * @param individuals
     * @param cGeneration
     * @return
     */
    @Override
    public List<Individual> performParentSelection(List<Individual> individuals, int cGeneration) {
        return individuals.stream()
                .sorted()
                .skip(Helper.toSkip(individuals.size(), PARENT_POOL_SIZE))
                .collect(Collectors.toList());
    }
}
