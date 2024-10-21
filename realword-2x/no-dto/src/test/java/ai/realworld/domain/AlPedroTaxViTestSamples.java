package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlPedroTaxViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlPedroTaxVi getAlPedroTaxViSample1() {
        return new AlPedroTaxVi().id(1L).name("name1").description("description1").weight(1);
    }

    public static AlPedroTaxVi getAlPedroTaxViSample2() {
        return new AlPedroTaxVi().id(2L).name("name2").description("description2").weight(2);
    }

    public static AlPedroTaxVi getAlPedroTaxViRandomSampleGenerator() {
        return new AlPedroTaxVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .weight(intCount.incrementAndGet());
    }
}
