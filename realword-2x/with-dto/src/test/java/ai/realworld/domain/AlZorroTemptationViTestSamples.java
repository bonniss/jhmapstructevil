package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlZorroTemptationViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlZorroTemptationVi getAlZorroTemptationViSample1() {
        return new AlZorroTemptationVi()
            .id(1L)
            .name("name1")
            .templateId("templateId1")
            .templateDataMapping("templateDataMapping1")
            .targetUrls("targetUrls1");
    }

    public static AlZorroTemptationVi getAlZorroTemptationViSample2() {
        return new AlZorroTemptationVi()
            .id(2L)
            .name("name2")
            .templateId("templateId2")
            .templateDataMapping("templateDataMapping2")
            .targetUrls("targetUrls2");
    }

    public static AlZorroTemptationVi getAlZorroTemptationViRandomSampleGenerator() {
        return new AlZorroTemptationVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .templateId(UUID.randomUUID().toString())
            .templateDataMapping(UUID.randomUUID().toString())
            .targetUrls(UUID.randomUUID().toString());
    }
}
