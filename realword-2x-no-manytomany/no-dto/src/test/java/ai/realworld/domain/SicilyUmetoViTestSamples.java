package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SicilyUmetoViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SicilyUmetoVi getSicilyUmetoViSample1() {
        return new SicilyUmetoVi().id(1L).content("content1");
    }

    public static SicilyUmetoVi getSicilyUmetoViSample2() {
        return new SicilyUmetoVi().id(2L).content("content2");
    }

    public static SicilyUmetoVi getSicilyUmetoViRandomSampleGenerator() {
        return new SicilyUmetoVi().id(longCount.incrementAndGet()).content(UUID.randomUUID().toString());
    }
}
