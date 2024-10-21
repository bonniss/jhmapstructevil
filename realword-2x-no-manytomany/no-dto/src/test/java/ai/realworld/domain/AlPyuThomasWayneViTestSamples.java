package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlPyuThomasWayneViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlPyuThomasWayneVi getAlPyuThomasWayneViSample1() {
        return new AlPyuThomasWayneVi().id(1L).rating(1).comment("comment1");
    }

    public static AlPyuThomasWayneVi getAlPyuThomasWayneViSample2() {
        return new AlPyuThomasWayneVi().id(2L).rating(2).comment("comment2");
    }

    public static AlPyuThomasWayneVi getAlPyuThomasWayneViRandomSampleGenerator() {
        return new AlPyuThomasWayneVi()
            .id(longCount.incrementAndGet())
            .rating(intCount.incrementAndGet())
            .comment(UUID.randomUUID().toString());
    }
}
