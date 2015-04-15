package project3;

import algorithms.ea.individual.Genotype;

import java.util.BitSet;
import java.util.Random;

/**
 * Created by PerØyvind on 15/04/2015.
 */
public class Helper {

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

}
