package ai.realworld.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class HandCraftViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HandCraftVi getHandCraftViSample1() {
        return new HandCraftVi().id(1L);
    }

    public static HandCraftVi getHandCraftViSample2() {
        return new HandCraftVi().id(2L);
    }

    public static HandCraftVi getHandCraftViRandomSampleGenerator() {
        return new HandCraftVi().id(longCount.incrementAndGet());
    }
}
