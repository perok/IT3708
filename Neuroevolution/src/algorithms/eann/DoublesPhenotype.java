package algorithms.eann;

import algorithms.ea.individual.IPhenotype;
import algorithms.ea.individual.builders.IPhenotypeBuilder;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public class DoublesPhenotype implements IPhenotype<Double> {


    double value;

    public DoublesPhenotype(double value){
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }
}
