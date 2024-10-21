package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlPowerShellViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlPowerShellVi getAlPowerShellViSample1() {
        return new AlPowerShellVi().id(1L).value("value1");
    }

    public static AlPowerShellVi getAlPowerShellViSample2() {
        return new AlPowerShellVi().id(2L).value("value2");
    }

    public static AlPowerShellVi getAlPowerShellViRandomSampleGenerator() {
        return new AlPowerShellVi().id(longCount.incrementAndGet()).value(UUID.randomUUID().toString());
    }
}
