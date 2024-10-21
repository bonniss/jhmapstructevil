package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppZnsTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppZnsTemplate getAppZnsTemplateSample1() {
        return new AppZnsTemplate()
            .id(1L)
            .name("name1")
            .templateId("templateId1")
            .templateDataMapping("templateDataMapping1")
            .targetUrls("targetUrls1");
    }

    public static AppZnsTemplate getAppZnsTemplateSample2() {
        return new AppZnsTemplate()
            .id(2L)
            .name("name2")
            .templateId("templateId2")
            .templateDataMapping("templateDataMapping2")
            .targetUrls("targetUrls2");
    }

    public static AppZnsTemplate getAppZnsTemplateRandomSampleGenerator() {
        return new AppZnsTemplate()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .templateId(UUID.randomUUID().toString())
            .templateDataMapping(UUID.randomUUID().toString())
            .targetUrls(UUID.randomUUID().toString());
    }
}
