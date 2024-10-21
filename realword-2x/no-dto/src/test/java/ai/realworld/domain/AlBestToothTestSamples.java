package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlBestToothTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlBestTooth getAlBestToothSample1() {
        return new AlBestTooth().id(1L).name("name1");
    }

    public static AlBestTooth getAlBestToothSample2() {
        return new AlBestTooth().id(2L).name("name2");
    }

    public static AlBestTooth getAlBestToothRandomSampleGenerator() {
        return new AlBestTooth().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
