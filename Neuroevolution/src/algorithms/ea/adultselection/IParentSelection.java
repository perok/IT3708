package algorithms.ea.adultselection;

import algorithms.ea.individual.Individual;

import java.util.List;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public interface IParentSelection {
    List<Individual> performParentSelection(List<Individual> individuals, int cGeneration);
}
