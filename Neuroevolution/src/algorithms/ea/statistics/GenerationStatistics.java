package algorithms.ea.statistics;

import algorithms.ea.individual.Individual;
import math.Statistics;

import java.util.List;

/**
 * Created by Per�yvind on 09/03/2015.
 */
public class GenerationStatistics {

    public int generation;

    private double SSE;
    public double SD; // Standard deviation
    public double mean;
    private double total;


    private double bestFitness;
    private double worstFitness;

    public Individual bestIndividual;



    public GenerationStatistics(List<Individual> population, int generation) {
        this.generation = generation;

        bestIndividual = population.stream()
                .max((a, b) -> Double.compare(a.getFitness(), b.getFitness()))
                .get();

        worstFitness = population.stream()
                .min((a, b) -> Double.compare(a.getFitness(), b.getFitness()))
                .get()
                .getFitness();

        bestFitness = bestIndividual.getFitness();
        total = population.stream().mapToDouble(Individual::getFitness).sum();
        mean =  Statistics.populationMean(population);

        SD = Statistics.standardDeviationPopulation(population);

    }
}
