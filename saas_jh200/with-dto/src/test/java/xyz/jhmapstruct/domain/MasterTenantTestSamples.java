package xyz.jhmapstruct.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MasterTenantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MasterTenant getMasterTenantSample1() {
        return new MasterTenant().id(1L).code("code1");
    }

    public static MasterTenant getMasterTenantSample2() {
        return new MasterTenant().id(2L).code("code2");
    }

    public static MasterTenant getMasterTenantRandomSampleGenerator() {
        return new MasterTenant().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString());
    }
}
