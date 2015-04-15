package algorithms.ea.individual;

import algorithms.ea.Evolution;
import algorithms.ea.fitness.EvaluateFitness;
import algorithms.ea.fitness.IFitness;
import algorithms.ea.individual.builders.PhenotypeBuilder;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;

/**
 * Created by Perÿyvind on 05/03/2015.
 */
public class Individual implements Comparable<Individual>{
    /**
     * The main Evolutionary algorithm controller.
     * Contains the fitness functions and other data.
     */
    private Evolution parent;

    private List<IPhenotype> phenotypes;
    private Genotype genotypes;

    private double individualFitness;

    /**
     * Generation of the individual
     */
    private int age = 0;

    public Individual(Evolution parent, Genotype data, int age){
        this(parent, data);
        this.age = age;
    }


    /**
     * @param data
     */
    public Individual(Evolution parent, Genotype data){
        this.parent = parent;

        this.genotypes = data;

        // -------------------------------------
        // Make Phenotypes
        // -------------------------------------
        phenotypes = PhenotypeBuilder.translate(parent.getPhenotypeBuilder(), genotypes);

        // Calc fitness
        IFitness fitness = parent.getFitnessFunction();
        individualFitness = EvaluateFitness.Evaluate(fitness, phenotypes);
    }

    private static Collector<IPhenotype, StringJoiner, String> phenotypeCollector =
            Collector.of(
                    () -> new StringJoiner(", "),          // supplier
                    (j, p) -> j.add(String.valueOf(p.getValue())),  // accumulator
                    (j1, j2) -> j1.merge(j2),               // combiner
                    StringJoiner::toString);                // finisher

    @Override
    public String toString() {
        return "Individual: ->Gen: " + getAge() + " Fitness: " + individualFitness
                + "\n\tGenotypes:  " + genotypes.toString()
                + "\n\tPhenotypes: " + phenotypes.stream().collect(phenotypeCollector);
    }

    public Genotype getGenotypes() {
        return genotypes;
    }

    public double getFitness() {
        return individualFitness;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int compareTo(Individual o) {
        if(getFitness() == o.getFitness()) return 0;
        else if(getFitness() > o.getFitness()) return 1;
        else return -1;
    }
}
