package algorithms.ea.individual;


import algorithms.ea.Helper;

import java.util.BitSet;
import java.util.stream.IntStream;

/**
 * Created by PerØyvind on 05/03/2015.
 *
 * GenoType is one property
 * todo BitSet, then this class can be removed..
 */
public class Genotype {

    private BitSet data;
    private int length = 0;

    public Genotype(String genotype) {
        this.data = new BitSet();
        IntStream.range(0, genotype.length())
                .filter(i -> genotype.charAt(i) == '1')
                .forEach(i -> this.data.set(i));

        length = genotype.length();
    }

    public Genotype(BitSet genotype, int length) {
        this.length = length;
        this.data = Helper.copyBitSet(genotype);
    }

    public Genotype(int length) {
        this.length = length;
        this.data = new BitSet();
    }

    public Genotype makeCopy() {
        return new Genotype(data, length);
    }

    public void copyFrom(Genotype g){
        data = Helper.copyBitSet(g.data);
        length = g.getFixedLength();
    }


    public boolean getValue(int pos) {
        return data.get(pos);
    }


    public BitSet getData() {
        return data;
    }


    public int getFixedLength() {
        return length;
    }

    @Override
    public String toString(){

        String print = "";

        for(int i = 0; i < getFixedLength(); i++) {
            print += data.get(i) ? "1" : "0";
        }
        return print;
    }
}
