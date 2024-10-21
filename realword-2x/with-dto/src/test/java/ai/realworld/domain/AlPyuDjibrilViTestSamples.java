package ai.realworld.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AlPyuDjibrilViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlPyuDjibrilVi getAlPyuDjibrilViSample1() {
        return new AlPyuDjibrilVi().id(1L);
    }

    public static AlPyuDjibrilVi getAlPyuDjibrilViSample2() {
        return new AlPyuDjibrilVi().id(2L);
    }

    public static AlPyuDjibrilVi getAlPyuDjibrilViRandomSampleGenerator() {
        return new AlPyuDjibrilVi().id(longCount.incrementAndGet());
    }
}
