package algorithms.ea.individual.builders;

import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.IPhenotype;
import algorithms.ea.individual.Phenotype;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Perÿyvind on 06/03/2015.
 */
public class OneToOnePhenotypeBuilder implements IPhenotypeBuilder {
    @Override
    public List<IPhenotype> makePhenotypes(Genotype genotypes) {
        return IntStream.range(0, genotypes.getFixedLength())
                .mapToObj(i -> new Phenotype(genotypes.getData().get(i) ? 1 : 0))
                .collect(Collectors.toList());
    }
}
