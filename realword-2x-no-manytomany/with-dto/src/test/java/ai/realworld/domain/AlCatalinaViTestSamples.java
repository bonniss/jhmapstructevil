package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlCatalinaViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlCatalinaVi getAlCatalinaViSample1() {
        return new AlCatalinaVi().id(1L).name("name1").description("description1").treeDepth(1);
    }

    public static AlCatalinaVi getAlCatalinaViSample2() {
        return new AlCatalinaVi().id(2L).name("name2").description("description2").treeDepth(2);
    }

    public static AlCatalinaVi getAlCatalinaViRandomSampleGenerator() {
        return new AlCatalinaVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .treeDepth(intCount.incrementAndGet());
    }
}
