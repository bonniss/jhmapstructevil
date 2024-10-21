package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlPacinoPointHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlPacinoPointHistory getAlPacinoPointHistorySample1() {
        return new AlPacinoPointHistory().id(1L).associatedId("associatedId1").pointAmount(1);
    }

    public static AlPacinoPointHistory getAlPacinoPointHistorySample2() {
        return new AlPacinoPointHistory().id(2L).associatedId("associatedId2").pointAmount(2);
    }

    public static AlPacinoPointHistory getAlPacinoPointHistoryRandomSampleGenerator() {
        return new AlPacinoPointHistory()
            .id(longCount.incrementAndGet())
            .associatedId(UUID.randomUUID().toString())
            .pointAmount(intCount.incrementAndGet());
    }
}
