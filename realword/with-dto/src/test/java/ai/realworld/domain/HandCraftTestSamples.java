package ai.realworld.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class HandCraftTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HandCraft getHandCraftSample1() {
        return new HandCraft().id(1L);
    }

    public static HandCraft getHandCraftSample2() {
        return new HandCraft().id(2L);
    }

    public static HandCraft getHandCraftRandomSampleGenerator() {
        return new HandCraft().id(longCount.incrementAndGet());
    }
}
