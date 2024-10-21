package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HashRossViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HashRossVi getHashRossViSample1() {
        return new HashRossVi().id(1L).name("name1").slug("slug1").description("description1").permissionGridJason("permissionGridJason1");
    }

    public static HashRossVi getHashRossViSample2() {
        return new HashRossVi().id(2L).name("name2").slug("slug2").description("description2").permissionGridJason("permissionGridJason2");
    }

    public static HashRossVi getHashRossViRandomSampleGenerator() {
        return new HashRossVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .permissionGridJason(UUID.randomUUID().toString());
    }
}
