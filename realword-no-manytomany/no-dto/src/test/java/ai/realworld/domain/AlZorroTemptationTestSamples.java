package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlZorroTemptationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlZorroTemptation getAlZorroTemptationSample1() {
        return new AlZorroTemptation()
            .id(1L)
            .name("name1")
            .templateId("templateId1")
            .templateDataMapping("templateDataMapping1")
            .targetUrls("targetUrls1");
    }

    public static AlZorroTemptation getAlZorroTemptationSample2() {
        return new AlZorroTemptation()
            .id(2L)
            .name("name2")
            .templateId("templateId2")
            .templateDataMapping("templateDataMapping2")
            .targetUrls("targetUrls2");
    }

    public static AlZorroTemptation getAlZorroTemptationRandomSampleGenerator() {
        return new AlZorroTemptation()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .templateId(UUID.randomUUID().toString())
            .templateDataMapping(UUID.randomUUID().toString())
            .targetUrls(UUID.randomUUID().toString());
    }
}
