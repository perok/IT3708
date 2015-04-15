package algorithms.ea.individual.builders;

import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.IPhenotype;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Perÿyvind on 05/03/2015.
 */
public interface IPhenotypeBuilder {
    List<IPhenotype> makePhenotypes(Genotype genotypes);
}
