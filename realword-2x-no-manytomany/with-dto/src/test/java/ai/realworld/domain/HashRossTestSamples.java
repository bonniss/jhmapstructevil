package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HashRossTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HashRoss getHashRossSample1() {
        return new HashRoss().id(1L).name("name1").slug("slug1").description("description1").permissionGridJason("permissionGridJason1");
    }

    public static HashRoss getHashRossSample2() {
        return new HashRoss().id(2L).name("name2").slug("slug2").description("description2").permissionGridJason("permissionGridJason2");
    }

    public static HashRoss getHashRossRandomSampleGenerator() {
        return new HashRoss()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .permissionGridJason(UUID.randomUUID().toString());
    }
}
