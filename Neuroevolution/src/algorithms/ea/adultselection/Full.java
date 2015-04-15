package algorithms.ea.adultselection;

import algorithms.ea.individual.Individual;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public class Full implements IParentSelection {
    /**
     * All adults from the previous generation are removed (i.e., die),
     * and all children gain free entrance to the adult pool. Thus, selection pressure on juveniles is completely
     * absent.
     * @param individuals
     * @param cGeneration
     * @return
     */
    @Override
    public List<Individual> performParentSelection(List<Individual> individuals, int cGeneration) {
        return individuals.stream()
                .filter(i -> !(i.getAge() < cGeneration))
                .sorted()
                .collect(Collectors.toList());
    }
}
