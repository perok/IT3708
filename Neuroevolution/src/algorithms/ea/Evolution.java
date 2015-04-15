package algorithms.ea;

import algorithms.ea.adultselection.*;
import algorithms.ea.fitness.IFitness;
import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.Individual;
import algorithms.ea.individual.builders.IPhenotypeBuilder;
import algorithms.ea.individual.operators.GeneticOperator;
import algorithms.ea.individual.operators.crossover.ICrossover;
import algorithms.ea.individual.operators.mutation.IMutation;
import algorithms.ea.statistics.GenerationStatistics;
import algorithms.ea.mating.MatingTechniques;
import math.Statistics;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by Perÿyvind on 05/03/2015.
 *
 1. A population of potential solutions (i.e. population) all represented in some low-level
 genotypic form such as binary vectors.

 2. A developmental method for converting the genotypes into phenotypes. For this general
 EA, a simple routine for converting a binary genotype into a list of integers will suffice.
 This can be extended for future homework modules.

 3. A fitness evaluation method that can be applied to all phenotypes. You will want to translate
 this a very modular component of your EA such that a wide variety of fitness functions
 can be experimented with

 4. All 3 of the basic protocols for adult selection described in the ea-appendices.pdf chapter
 of the lecture notes. (Full, over-production and mixing)

 5. todo A set (of at least 4) mechanisms (also described in ea-appendices.pdf) for parent/mate
 selection. These 4 must include fitness-proportionate, sigma-scaling, and tournament
 selection.

 6. The genetic operators of mutation and crossover. For this project, you only need to
 define them for binary genomes (i.e. bit vectors). But your system should be able to
 handle different representations for later problems.

 7. A basic evolutionary loop for running the EA through many generations of evolution

 8. A logging routine that shows various data while the EA is running. This can be as simple
 as printing to stdout/console. For each generation, this data should be logged: generation
 number, best fitness, mean fitness, standard deviation (SD) of fitness, phenotype of the
 best individual.

 9. A plotting routine that gives a visualization of an EA run. The plot should show how
 the data from the logging changes from generation to generation: Generations on the X
 axis, and then plot the best, mean and SD of the fitness. For this, you can either use an
 existing plotting library for your language, or just copy the logging data to a spreadsheet
 program like Excel and create the plots there.
 1
 *
 */
public class Evolution implements Runnable{

    private BlockingQueue<GenerationStatistics> dataQueue;
    private LinkedList<Individual> population;

    private IFitness fitness = null;
    private IPhenotypeBuilder phenotypeBuilder = null;
    private IMutation mutator = null;
    private ICrossover crossover = null;

    private IParentSelection selectionsStrategy = null;
    private MatingTechniques matingStrategy = MatingTechniques.FITNESS_PROPORTIONATE;


    int POOL_SIZE = 40;

    int PARENT_POOL_SIZE = 35;
    int CHIlDREN_POOL_SIZE = 40;
    int NUMBER_OF_ITERATIONS = 100;


    private Random random;

    private boolean quitWhenFitnessOver = false;
    private double quitWhenFitnessOverThis;


    private boolean ready = false;

    public Evolution(){
        random = new Random();
       // population = new LinkedList<>();
    }

    @Deprecated
    public void addIndividual(Genotype genotype){
        population.add(new Individual(this, genotype.makeCopy()));
    }

    public void setPopulation(List<Individual> population) {
        this.population = new LinkedList<>(population);
    }

    double T = 10;

    private List<Individual> performMating(List<Individual> matingPool){
        List<Individual> childrenPool = new LinkedList<>();
        List<IndiviualWrapper> popSorted;

        switch (matingStrategy) {
            case FITNESS_PROPORTIONATE:
                double populationFitness = matingPool.stream()
                        .mapToDouble(Individual::getFitness)
                        .sum();

                popSorted = matingPool.stream()
                        .map(i -> new IndiviualWrapper(i, i.getFitness() / populationFitness))
                        .sorted()
                        .collect(Collectors.toList());

                do {
                    Individual found1 = IndiviualWrapper.findWheelSpinn(popSorted, random.nextDouble());
                    Individual found2 = IndiviualWrapper.findWheelSpinn(popSorted, random.nextDouble());

                    if(found1 == null || found2 == null || found1 == found2) {
                        continue;
                    }

                    childrenPool.addAll(reproduce(found1, found2));
                    // todo Produce two or more children?
                    // todo Empty after, so can't be choosen again?
                } while(childrenPool.size() < CHIlDREN_POOL_SIZE);

                break;
            case SIGMA_SCALING:

                double gMean = Statistics.populationMean(matingPool);
                double gSD = Statistics.standardDeviationPopulation(matingPool);
                popSorted = matingPool.stream()
                        .map(i -> new IndiviualWrapper(i, Statistics.sigmaScaling(i.getFitness(), gMean, gSD)))
                        .sorted()
                        .collect(Collectors.toList());

                double scaleFactor = popSorted.stream()
                        .mapToDouble(IndiviualWrapper::getFitness)
                        .sum();

                popSorted.stream()
                        .forEach(i -> i.fitness /= scaleFactor);

                do {
                    Individual found1 = IndiviualWrapper.findWheelSpinn(popSorted, random.nextDouble());
                    Individual found2 = IndiviualWrapper.findWheelSpinn(popSorted, random.nextDouble());

                    if(found1 == null || found2 == null || found1 == found2) {
                        continue;
                    }

                    childrenPool.addAll(reproduce(found1, found2));
                    // todo Produce two or more children?
                    // todo Empty after, so can't be choosen again?
                } while(childrenPool.size() < CHIlDREN_POOL_SIZE);
                break;
            case TOURNAMENT:
                double TOURNAMENT_EPSILON = 0.3;
                int TOURNAMENT_K = 10;

                if(matingPool.size() < TOURNAMENT_K) TOURNAMENT_K = matingPool.size() - 1;

                do {
                    List<Individual> pool1 = new LinkedList<>();
                    List<Individual> pool2 = new LinkedList<>();

                    do{
                        Individual choosenOne = matingPool.get(random.nextInt(matingPool.size()));
                        if(!pool1.contains(choosenOne)) pool1.add(choosenOne);
                    }while(pool1.size() < TOURNAMENT_K);

                    do{
                        Individual choosenOne = matingPool.get(random.nextInt(matingPool.size()));
                        if(!pool2.contains(choosenOne)) pool2.add(choosenOne);
                    }while(pool2.size() < TOURNAMENT_K);

                    pool1 = pool1.stream().sorted().collect(Collectors.toList());
                    pool2 = pool2.stream().sorted().collect(Collectors.toList());

                    Individual found1 = null;
                    Individual found2 = null;

                    for(int i = pool1.size() - 1; i >= 0; i--) {
                        if(i == 0) {
                            found1 = pool1.get(i);
                        } else if(random.nextDouble() < 1 - TOURNAMENT_EPSILON){
                            found1 = pool1.get(i);
                            break;
                        }
                    }
                    for(int i = pool2.size() - 1; i >= 0; i--) {
                        if(i == pool2.size() - 1) {
                            found2 = pool2.get(i);
                        } else if(random.nextDouble() < 1 - TOURNAMENT_EPSILON){
                            found2 = pool2.get(i);
                            break;
                        }
                    }
                    if(found1 == null || found2 == null || found1 == found2) {
                        continue;
                    }

                    childrenPool.addAll(reproduce(found1, found2));
                    // todo Produce two or more children?
                    // todo Empty after, so can't be choosen again?
                } while(childrenPool.size() < CHIlDREN_POOL_SIZE);
                break;
            case BOLTZMAN:

                popSorted = matingPool.stream()
                        .map(i -> new IndiviualWrapper(i, Math.exp(i.getFitness() / T)))
                        .collect(Collectors.toList());
                final double generationTotal = popSorted.stream().mapToDouble(IndiviualWrapper::getFitness).sum();

                popSorted = popSorted.stream()
                        .map(i -> {
                            i.fitness = i.fitness / generationTotal;
                            return i;
                        })
                        .collect(Collectors.toList());

                double scaleF = popSorted.stream()
                        .mapToDouble(IndiviualWrapper::getFitness)
                        .sum();

                popSorted.stream()
                        .forEach(i -> i.fitness /= scaleF);

                do {
                    Individual found1 = IndiviualWrapper.findWheelSpinn(popSorted, random.nextDouble());
                    Individual found2 = IndiviualWrapper.findWheelSpinn(popSorted, random.nextDouble());

                    if(found1 == null || found2 == null || found1 == found2) {
                        continue;
                    }

                    childrenPool.addAll(reproduce(found1, found2));
                    // todo Produce two or more children?
                    // todo Empty after, so can't be choosen again?
                } while(childrenPool.size() < CHIlDREN_POOL_SIZE);

                T -= Constants.EPSILON_OR_ERROR;
                break;
        }

        return childrenPool;

    }

    /**
     * TODO write
     * @param individual1
     * @param individual2
     * @return
     */
    private List<Individual> reproduce(Individual individual1, Individual individual2) {
        Genotype genotype1 = individual1.getGenotypes();
        Genotype genotype2 = individual2.getGenotypes();

        return GeneticOperator.crossover(crossover, genotype1, genotype2)
                .stream()
                .map(g -> new Individual(this,
                        g,
                        1 + ((individual1.getAge() > individual2.getAge()) ? individual1.getAge() : individual2.getAge())))
                .collect(Collectors.toList());
    }

    private List<Individual> nextEpoch(List<Individual> population, int generation) {
        // 1. Adult selection - The fight for a place! Survival
        List<Individual> newPopulation = this.selectionsStrategy.performParentSelection(population, generation);

        System.out.println("\tAdult selection performed. New population to carry on: " + newPopulation.size());

        // 2. Parent selection - Who get to mate of the survivors
        List<Individual> newChildren = performMating(newPopulation).stream()
                .map(i -> new Individual(
                        this,
                        GeneticOperator.mutate(mutator, i.getGenotypes()),
                        i.getAge()
                ))
                .collect(Collectors.toList());

        System.out.println("\tMating pool created. Containing: " + newChildren.size());

        newPopulation.addAll(newChildren);

        // Perform mutations on whole population
        // todo mutate for all, or only children
        return newPopulation;
    }

    @Deprecated // todo Should be class over that handles this
    public void loop(){

        int generation = 0;
        List<Individual> population = this.population;

        do {
            double totalFitness = population.stream()
                    .mapToDouble(Individual::getFitness)
                    .sum();

            System.out.println(
                    "Generation: " + generation +
                            ". Total fitness: " + totalFitness +
                            ". Individuals: " + population.size());

            // Run
            population = nextEpoch(population, generation);

            // Send to GUI
            GenerationStatistics gs = new GenerationStatistics(population, generation);
            System.out.println("\t\tBest: " + gs.bestIndividual);
            System.out.println("\t\tMean: " + gs.mean + "\tSD: " + gs.SD + "\tLength: ");

            if(dataQueue != null) {
                try {
                    dataQueue.put(gs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(quitWhenFitnessOver && gs.bestIndividual.getFitness() >= quitWhenFitnessOverThis) {
                System.out.println("Success!");
                System.out.println("Best is: " + gs.bestIndividual);
                break;
            }

            generation++;
        } while (generation < NUMBER_OF_ITERATIONS);

        System.out.println("EA loop finished.");
    }

    /**
     *
     * @return null if error
     */
    public Evolution build(){
        if(fitness == null) {
            System.err.println("Build abort: Missing fitness function");
            return null;
        }
        else if(phenotypeBuilder == null) {
            System.err.println("Build abort: Missing phenotype builder");
            return null;
        }
        else if(mutator == null) {
            System.err.println("Build abort: Missing mutator function");
            return null;
        }
        else if(crossover == null) {
            System.err.println("Build abort: Missing crossover function");
            return null;
        }

        if(dataQueue == null) {
            System.out.println("Build: Running without thread safe queue to report too.");
        }

        System.out.println("==== Evolution algorithm ready ====");
        System.out.println("Fitness function: " + fitness);
        System.out.println("Phenotype builder: " + phenotypeBuilder);
        System.out.println("Mutator: " + mutator);
        System.out.println("Crossover: " + crossover);
        System.out.println("Parent pool size: " + PARENT_POOL_SIZE);
        System.out.println("Children pool size: " + CHIlDREN_POOL_SIZE);
        System.out.println("# of iterations: " + NUMBER_OF_ITERATIONS);
        System.out.println("Selection strategy: " + selectionsStrategy); //.name());
        System.out.println("Mating strategy: " + matingStrategy.name());

        if(quitWhenFitnessOver)
            System.out.println("Quitting when fitness over: " + quitWhenFitnessOver);

        ready = true;
        return this;
    }

    @Override
    public void run() {
        if(ready)
            loop();
        else
            System.err.println("Error: Evolution algorithm is not built.");
    }

    // -------------------------------------
    // Getters and setters
    // -------------------------------------

    public Evolution setDataListener(BlockingQueue<GenerationStatistics> dataListener) {
        this.dataQueue = dataListener;

        return this;
    }

    public Evolution setQuitWhenFitnessOver(double quitWhenFitnessOver) {
        this.quitWhenFitnessOver = true;
        this.quitWhenFitnessOverThis = quitWhenFitnessOver;

        return this;
    }

    public Evolution setFitnessFunction(IFitness fitness) {
        this.fitness = fitness;
        return this;
    }

    public IFitness getFitnessFunction() {
        return fitness;
    }

    public Evolution setPhenotypeBuilder(IPhenotypeBuilder IPhenotypeBuilder) {
        this.phenotypeBuilder = IPhenotypeBuilder;

        return this;
    }

    public IPhenotypeBuilder getPhenotypeBuilder() {
        return phenotypeBuilder;
    }

    public Evolution setAdultSelectionsStrategy(ParentSelections selectionsStrategy) {
        switch (selectionsStrategy) {
            // All adults from the previous generation are removed (i.e., die),
            // and all children gain free entrance to the adult pool. Thus, selection pressure on juveniles is completely
            // absent.
            case FULL:
                this.selectionsStrategy = new Full();
                break;
            case MIXING:
                this.selectionsStrategy = new Mixing();
                break;
            case OVER_PRODUCTION:
                this.selectionsStrategy = new OverProduction();
                break;
            default:
                System.err.println("Parent selection strategy invalid");
                this.selectionsStrategy = null;
        }

        return this;
    }

    public Evolution setMatingStrategy(MatingTechniques matingStrategy) {
        this.matingStrategy = matingStrategy;
        return this;
    }

    public Evolution setMutator(IMutation mutator) {
        this.mutator = mutator;
        return this;
    }

    public Evolution setCrossover(ICrossover crossover) {
        this.crossover = crossover;
        return this;
    }

    public Evolution setPARENT_POOL_SIZE(int PARENT_POOL_SIZE) {
        this.PARENT_POOL_SIZE = PARENT_POOL_SIZE;
        return this;
    }

    public Evolution setCHIlDREN_POOL_SIZE(int CHIlDREN_POOL_SIZE) {
        this.CHIlDREN_POOL_SIZE = CHIlDREN_POOL_SIZE;
        return this;

    }

    public Evolution setNUMBER_OF_ITERATIONS(int NUMBER_OF_ITERATIONS) {
        this.NUMBER_OF_ITERATIONS = NUMBER_OF_ITERATIONS;
        return this;
    }
}
