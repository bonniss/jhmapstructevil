package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AlVueVueTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlVueVue getAlVueVueSample1() {
        return new AlVueVue()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .code("code1")
            .name("name1")
            .contentHeitiga("contentHeitiga1")
            .usageLifeTimeLimit(1)
            .usageLimitPerUser(1)
            .usageQuantity(1);
    }

    public static AlVueVue getAlVueVueSample2() {
        return new AlVueVue()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .code("code2")
            .name("name2")
            .contentHeitiga("contentHeitiga2")
            .usageLifeTimeLimit(2)
            .usageLimitPerUser(2)
            .usageQuantity(2);
    }

    public static AlVueVue getAlVueVueRandomSampleGenerator() {
        return new AlVueVue()
            .id(UUID.randomUUID())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .contentHeitiga(UUID.randomUUID().toString())
            .usageLifeTimeLimit(intCount.incrementAndGet())
            .usageLimitPerUser(intCount.incrementAndGet())
            .usageQuantity(intCount.incrementAndGet());
    }
}
