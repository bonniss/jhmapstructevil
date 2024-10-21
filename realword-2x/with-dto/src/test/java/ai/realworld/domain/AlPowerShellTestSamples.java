package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlPowerShellTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlPowerShell getAlPowerShellSample1() {
        return new AlPowerShell().id(1L).value("value1");
    }

    public static AlPowerShell getAlPowerShellSample2() {
        return new AlPowerShell().id(2L).value("value2");
    }

    public static AlPowerShell getAlPowerShellRandomSampleGenerator() {
        return new AlPowerShell().id(longCount.incrementAndGet()).value(UUID.randomUUID().toString());
    }
}
