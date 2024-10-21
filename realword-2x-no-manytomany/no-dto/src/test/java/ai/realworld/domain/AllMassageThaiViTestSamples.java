package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AllMassageThaiViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AllMassageThaiVi getAllMassageThaiViSample1() {
        return new AllMassageThaiVi()
            .id(1L)
            .title("title1")
            .topContent("topContent1")
            .content("content1")
            .bottomContent("bottomContent1")
            .propTitleMappingJason("propTitleMappingJason1")
            .targetUrls("targetUrls1");
    }

    public static AllMassageThaiVi getAllMassageThaiViSample2() {
        return new AllMassageThaiVi()
            .id(2L)
            .title("title2")
            .topContent("topContent2")
            .content("content2")
            .bottomContent("bottomContent2")
            .propTitleMappingJason("propTitleMappingJason2")
            .targetUrls("targetUrls2");
    }

    public static AllMassageThaiVi getAllMassageThaiViRandomSampleGenerator() {
        return new AllMassageThaiVi()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .topContent(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .bottomContent(UUID.randomUUID().toString())
            .propTitleMappingJason(UUID.randomUUID().toString())
            .targetUrls(UUID.randomUUID().toString());
    }
}
