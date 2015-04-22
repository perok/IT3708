package project4;

import algorithms.ectrnn.IndividualCTRBrain;
import algorithms.ectrnn.Neuroevolution;
import gameworlds.tracker.Tracker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Per�yvind on 13/04/2015.
 */
public class AIController {

    private List<IndividualCTRBrain> population;
    private int populationSize = 100;
    private int epoch = 0;
    private Neuroevolution neuroevolution;

    private boolean noWrap = false;

    private Tracker tracker;

    // 1. Make a population
    // 2. Run population on game world
    // 3. Collect fitness underway

    public AIController() {
        reset();
    }

    public void reset() {
        epoch = 0;

        tracker = new Tracker(noWrap);
        neuroevolution = new Neuroevolution();

        // ----------------------------------
        // Create random population
        // ----------------------------------
        population = new LinkedList<>();
        IntStream.range(0, populationSize)
                .forEach(i -> population.add(new IndividualCTRBrain()));

        calculateFitnessOnPopulation();
    }

    private void calculateFitnessOnPopulation(){

        population
                .stream()
                // Make phenotypes - In case they have changed since evolution
                .peek(IndividualCTRBrain::buildPhenotypes)
                .peek(IndividualCTRBrain::rewireBrain)
                // ----------------------------------
                // Fitness function that run Tracker
                // ----------------------------------
                .peek(individualBrain -> {
                    // Setup a new Tracker
                    tracker = new Tracker(noWrap);

                    // Run Tracker with 600 iteration
                    for (int i = 0; i < 600; i++) {
                        Tracker.Movement move = helperIndividualFindMove(tracker, individualBrain);

                        // Perform the move
                        tracker.newStep(move);
                    }

                    // Record the fitness
                    individualBrain.setFitness(tracker.getStats());
                })
                .collect(Collectors.toList());
    }



    public static Tracker.Movement helperIndividualFindMove(Tracker tracker, IndividualCTRBrain individualBrain){

        // Run through brain
        List<Double> output = individualBrain.think(tracker.getSensory());

        // Find out what to do with output
        Map<Integer, Double> list = new HashMap<>();
        //System.out.println("Val1: " + output.get(0) + " Val2: " + output.get(1));
        list.put(0, output.get(0));
        list.put(1, output.get(1));

        Optional<Map.Entry<Integer, Double>> val = list.entrySet().stream()
                .sorted(byValue.reversed())
                .findFirst();

        switch (val.get().getKey()) {
            case 0:
                return Tracker.Movement.LEFT;
            case 1:
                return Tracker.Movement.RIGHT;
            default:
                System.err.println("WTF");
                return Tracker.Movement.LEFT;
        }
    }

    /**
     * Runs one epoch and returns the best individual
     */
    public IndividualCTRBrain runOneEpoch(){

        // Run population through EANN
        population = neuroevolution.evolve(population, epoch++);

        // Calculate the fitness
        calculateFitnessOnPopulation();

        return population.stream().max(IndividualCTRBrain::compareTo).get();
    }

    static Comparator<Map.Entry<Integer, Double>> byValue = (entry1, entry2) -> entry1.getValue().compareTo(
            entry2.getValue());


    public List<IndividualCTRBrain> getPopulation() {
        return population;
    }

    public int getEpoch() {
        return epoch;
    }

    public void setNoWrap(boolean noWrap) {
        this.noWrap = noWrap;

        if(this.noWrap) {
            IndividualCTRBrain.inputLayers = 7;
        } else {
            IndividualCTRBrain.inputLayers = 5;
        }
    }

    public boolean getNoWrap() {
        return noWrap;
    }

}
