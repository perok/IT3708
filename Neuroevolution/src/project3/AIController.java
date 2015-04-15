package project3;

import algorithms.ann.NeuralNet;
import algorithms.ea.fitness.IFitness;
import algorithms.ea.individual.IPhenotype;

import java.util.List;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public class AIController {



    // 1. Make a population
    // 2. Run population on gameworld
    // 3. Collect fitness underway

    public AIController(){


        IFitness<Double> fitness = new IFitness<Double>() {
            @Override
            public double calculateFitness(List<IPhenotype<Double>> iPhenotypes) {


                NeuralNet neuralNet = new NeuralNet();

                // Insert weights

                neuralNet.createNet();



                // todo must be able to collect data from last gameloop run
                return 0;
            }
        };
    }
}
