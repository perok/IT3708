package sample;

import algorithms.ea.Evolution;
import algorithms.ea.Helper;
import algorithms.ea.adultselection.ParentSelections;
import algorithms.ea.fitness.OneToOneFitness;
import algorithms.ea.individual.Genotype;
import algorithms.ea.individual.IPhenotype;
import algorithms.ea.individual.Phenotype;
import algorithms.ea.individual.builders.OneToOnePhenotypeBuilder;
import algorithms.ea.individual.operators.mutation.BitStringMutation;
import algorithms.ea.individual.operators.mutation.MaybeMutator;
import algorithms.ea.individual.operators.crossover.OneCrossover;
import algorithms.ea.mating.MatingTechniques;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by PerØyvind on 10/03/2015.
 */
public class ProblemCreatorHelper {
    public static Evolution problem1(){
        Evolution a = new Evolution();
        a.setPhenotypeBuilder(new OneToOnePhenotypeBuilder());
        a.setFitnessFunction(new OneToOneFitness());
        a.setMatingStrategy(MatingTechniques.SIGMA_SCALING);
        a.setAdultSelectionsStrategy(ParentSelections.FULL);
        a.setMutator(new MaybeMutator(0.1));
        a.setCrossover(new OneCrossover());
        a.setNUMBER_OF_ITERATIONS(1000);
        a.setCHIlDREN_POOL_SIZE(200);
        a.setPARENT_POOL_SIZE(110);

        Random random = new Random();
        IntStream.range(0, 40)
                .forEach(i -> {
                    int LENGTH = 40;
                    a.setQuitWhenFitnessOver(LENGTH);
                    Genotype g = new Genotype(LENGTH);
                    for (int y = 0; y < LENGTH; y++) {
                        boolean lol = random.nextBoolean();
                        g.getData().set(y, lol);
                    }

                    a.addIndividual(g);
                });

        return a;
    }

    public static Evolution problem2(){
        int z = 21;
        Evolution a = new Evolution();
        a.setPhenotypeBuilder(new OneToOnePhenotypeBuilder());
        a.setFitnessFunction((population) -> {
            int firstValue = (int)population.get(0).getValue();
            int count = 1;

            for (int i = 1; i < population.size(); i++) {
                if ((int)population.get(i).getValue() == firstValue) {
                    count++;
                } else {
                    break;
                }
            }

            if (firstValue == 0 && count > z) {
                count = z;
            }

            return count;
        });

        a.setMatingStrategy(MatingTechniques.TOURNAMENT);
        a.setAdultSelectionsStrategy(ParentSelections.FULL);
        a.setMutator(new MaybeMutator(0.1));
        a.setCrossover(new OneCrossover());
        a.setNUMBER_OF_ITERATIONS(1000);
        a.setCHIlDREN_POOL_SIZE(200);
        a.setPARENT_POOL_SIZE(110);

        Random random = new Random();

        IntStream.range(0, 40)
                .forEach(i -> {
                    int LENGTH = 40;
                    a.setQuitWhenFitnessOver(LENGTH);
                    Genotype g = new Genotype(LENGTH);
                    for (int y = 0; y < LENGTH; y++) {
                        g.getData().set(y, random.nextBoolean());
                    }

                    a.addIndividual(g);
                });

        return a;
    }

    // todo
    public static Evolution problem3(int S, int L, int best, boolean global){ //S # symbols, L = length
        int bitsPrSymbol = bitsNeeded(S);

        Evolution a = new Evolution();

        // From geno to pheno
        a.setPhenotypeBuilder((genotypes) -> {
            BitSet bits = genotypes.getData();
            List<IPhenotype> phenotypes = new LinkedList<>();

            for (int i = 0; i < genotypes.getFixedLength(); i += bitsPrSymbol) {
                int number = Helper.convertBitsetToInteger(bits.get(i, i + bitsPrSymbol));
                phenotypes.add(new Phenotype(number % S));
            }
            return phenotypes;
        });

        // From phenoes to fitness
        a.setFitnessFunction((population) -> {
            if (global)
                return isGloballySurprising(population);
            else
                return isLocallySurprising(population);
        });

        a.setMatingStrategy(MatingTechniques.SIGMA_SCALING);
        a.setAdultSelectionsStrategy(ParentSelections.MIXING);
        a.setMutator(new BitStringMutation());//new MaybeMutator(0.1));
        a.setCrossover(new OneCrossover());
        a.setNUMBER_OF_ITERATIONS(2000);
        a.setCHIlDREN_POOL_SIZE(200);
        a.setPARENT_POOL_SIZE(100);

        a.setQuitWhenFitnessOver(best);//L);
        Random random = new Random();

        // ADD PARENTS

        IntStream.range(0, 40)
                .forEach(i -> {
                    // Amount of bits needed
                    int totalAMountOfBitsNeeded = bitsPrSymbol * L;

                    Genotype g = new Genotype(totalAMountOfBitsNeeded);

                    // Parse through every symbol to be placed
                    for (int y = 0; y < totalAMountOfBitsNeeded; y+= bitsPrSymbol) {
                        // Symbol to fill in
                        int nInt = random.nextInt(S);
                        BitSet values = convert(nInt);

                        assert nInt == Helper.convertBitsetToInteger(values);

                        // Set the correct bits
                        int newIndex = values.nextSetBit(0);
                        while(newIndex != -1 ) {
                            g.getData().set(y + newIndex);

                            newIndex = values.nextSetBit(newIndex + 1);
                        }
                    }

                    a.addIndividual(g);
                });

        return a;
    }

    private static int bitsNeeded(long value) {
        int index = 0;
        int no = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                no = index;
            }
            ++index;
            value = value >>> 1;
        }
        return no + 1;
    }

    public static BitSet convert(long value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    public static double isGloballySurprising(List<IPhenotype> sequence){
        HashMap<String, Integer> seqs = new HashMap<>();

        for (int i = 0; i < sequence.size(); i++) {
            for (int j = i + 1; j < sequence.size(); j++) {
                int distance = j - i;
                String seq = String.valueOf(sequence.get(i).getValue()) + '|' + String.valueOf(distance) + '|' + String.valueOf(sequence.get(j).getValue());

                if(seqs.containsKey(seq)) {
                    seqs.put(seq, seqs.get(seq) + 1);
                } else {
                    seqs.put(seq, 1);
                }
            }

        }

        return (double)seqs.size() / (double)seqs.values().stream().mapToInt(i -> i).sum();
    }

    public static double isLocallySurprising(List<IPhenotype> sequence){
        HashMap<String, Integer> seqs = new HashMap<>();

        for (int i = 0; i < sequence.size(); i++) {
            for (int j = i + 1; j < sequence.size(); j++) {
                int distance = j - i;
                String seq = String.valueOf(sequence.get(i).getValue()) + String.valueOf(distance) + String.valueOf(sequence.get(j).getValue());

                if(seqs.containsKey(seq)) {
                    seqs.put(seq, seqs.get(seq) + 1);
                } else {
                    seqs.put(seq, 1);
                }
                break;
            }

        }

        return (double)seqs.size() / (double)seqs.values().stream().mapToInt(i -> i).sum();
    }

    public static Evolution problemSpecial(){
        int LENGTH = 40;

        Evolution a = new Evolution();

        Random random = new Random();
        Genotype geno = new Genotype(LENGTH);

        for (int y = 0; y < LENGTH; y++) {
            boolean lol = random.nextBoolean();
            geno.getData().set(y, lol);
        }
        a.setPhenotypeBuilder(new OneToOnePhenotypeBuilder());
        a.setFitnessFunction((phenotypes) -> {
            int count = 0;

            for (int i = 0; i < phenotypes.size(); i++) {
                if((int)phenotypes.get(i).getValue() == (geno.getData().get(i) ? 1 : 0)) {
                    count++;
                }
            }

            return count;
        });
        a.setMatingStrategy(MatingTechniques.FITNESS_PROPORTIONATE);
        a.setAdultSelectionsStrategy(ParentSelections.FULL);
        a.setMutator(new MaybeMutator(0.1));
        a.setCrossover(new OneCrossover());
        a.setNUMBER_OF_ITERATIONS(1000);
        a.setCHIlDREN_POOL_SIZE(200);
        a.setPARENT_POOL_SIZE(110);
        a.setQuitWhenFitnessOver(LENGTH);

        IntStream.range(0, 40)
                .forEach(i -> {
                    Genotype g = new Genotype(LENGTH);
                    for (int y = 0; y < LENGTH; y++) {
                        boolean lol = random.nextBoolean();
                        g.getData().set(y, lol);
                    }

                    a.addIndividual(g);
                });

        return a;
    }
}
