package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlBestToothViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlBestToothVi getAlBestToothViSample1() {
        return new AlBestToothVi().id(1L).name("name1");
    }

    public static AlBestToothVi getAlBestToothViSample2() {
        return new AlBestToothVi().id(2L).name("name2");
    }

    public static AlBestToothVi getAlBestToothViRandomSampleGenerator() {
        return new AlBestToothVi().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
