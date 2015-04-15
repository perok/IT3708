package algorithms.ea.individual.builders;

import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.IPhenotype;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Perÿyvind on 06/03/2015.
 */
public class PhenotypeBuilder {

    public static List<IPhenotype> translate(IPhenotypeBuilder builder, Genotype genotypes) {
        return builder.makePhenotypes(genotypes);
    }
}
