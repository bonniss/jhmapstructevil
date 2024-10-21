package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AllMassageThaiTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AllMassageThai getAllMassageThaiSample1() {
        return new AllMassageThai()
            .id(1L)
            .title("title1")
            .topContent("topContent1")
            .content("content1")
            .bottomContent("bottomContent1")
            .propTitleMappingJason("propTitleMappingJason1")
            .targetUrls("targetUrls1");
    }

    public static AllMassageThai getAllMassageThaiSample2() {
        return new AllMassageThai()
            .id(2L)
            .title("title2")
            .topContent("topContent2")
            .content("content2")
            .bottomContent("bottomContent2")
            .propTitleMappingJason("propTitleMappingJason2")
            .targetUrls("targetUrls2");
    }

    public static AllMassageThai getAllMassageThaiRandomSampleGenerator() {
        return new AllMassageThai()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .topContent(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .bottomContent(UUID.randomUUID().toString())
            .propTitleMappingJason(UUID.randomUUID().toString())
            .targetUrls(UUID.randomUUID().toString());
    }
}
