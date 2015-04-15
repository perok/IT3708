package algorithms.eann;

import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.IPhenotype;
import algorithms.ea.individual.builders.IPhenotypeBuilder;

import java.util.List;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public class DoublesPhenotypeBuilder implements IPhenotypeBuilder {


    private int bitSize;

    public DoublesPhenotypeBuilder(int bitSize){
        this.bitSize = bitSize;
    }

    @Override
    public List<IPhenotype> makePhenotypes(Genotype genotypes) {

        //todo
        return null;
    }
}
