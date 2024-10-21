package ai.realworld.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AlPyuDjibrilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlPyuDjibril getAlPyuDjibrilSample1() {
        return new AlPyuDjibril().id(1L);
    }

    public static AlPyuDjibril getAlPyuDjibrilSample2() {
        return new AlPyuDjibril().id(2L);
    }

    public static AlPyuDjibril getAlPyuDjibrilRandomSampleGenerator() {
        return new AlPyuDjibril().id(longCount.incrementAndGet());
    }
}
