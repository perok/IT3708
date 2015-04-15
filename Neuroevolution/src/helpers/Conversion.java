package helpers;

import java.nio.ByteBuffer;

/**
 * Created by Perÿyvind on 15/04/2015.
 */
public class Conversion {
    public static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}
