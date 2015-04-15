package algorithms.ea;

import java.util.BitSet;

/**
 * Created by Per�yvind on 06/03/2015.
 */
public class Helper {
    public static BitSet copyBitSet(BitSet toCopy) {
        BitSet newB = new BitSet(toCopy.length());
        newB.or(toCopy);
        return newB;
    }

    public static int toSkip(int nElements, int wantToSkip) {
        return nElements < wantToSkip ? 0 : nElements - wantToSkip;
    }

    public static int convertBitsetToInteger(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return (int)value;
    }
}
