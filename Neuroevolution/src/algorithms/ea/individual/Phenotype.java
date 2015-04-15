package algorithms.ea.individual;

/**
 * Created by Perÿyvind on 05/03/2015.
 */
public class Phenotype implements IPhenotype<Integer> {

    private int phenotypeValue;

    public Phenotype(int phenotypeValue){
        this.phenotypeValue = phenotypeValue;
    }

    @Override
    public Integer getValue() {
        return phenotypeValue;
    }
}
