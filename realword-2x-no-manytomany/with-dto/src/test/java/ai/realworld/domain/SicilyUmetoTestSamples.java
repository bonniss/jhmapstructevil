package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SicilyUmetoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SicilyUmeto getSicilyUmetoSample1() {
        return new SicilyUmeto().id(1L).content("content1");
    }

    public static SicilyUmeto getSicilyUmetoSample2() {
        return new SicilyUmeto().id(2L).content("content2");
    }

    public static SicilyUmeto getSicilyUmetoRandomSampleGenerator() {
        return new SicilyUmeto().id(longCount.incrementAndGet()).content(UUID.randomUUID().toString());
    }
}
