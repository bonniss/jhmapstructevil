package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RihannaViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RihannaVi getRihannaViSample1() {
        return new RihannaVi().id(1L).name("name1").description("description1").permissionGridJason("permissionGridJason1");
    }

    public static RihannaVi getRihannaViSample2() {
        return new RihannaVi().id(2L).name("name2").description("description2").permissionGridJason("permissionGridJason2");
    }

    public static RihannaVi getRihannaViRandomSampleGenerator() {
        return new RihannaVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .permissionGridJason(UUID.randomUUID().toString());
    }
}
