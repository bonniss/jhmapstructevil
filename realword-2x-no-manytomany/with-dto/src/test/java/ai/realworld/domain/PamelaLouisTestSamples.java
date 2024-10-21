package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PamelaLouisTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PamelaLouis getPamelaLouisSample1() {
        return new PamelaLouis().id(1L).name("name1").configJason("configJason1");
    }

    public static PamelaLouis getPamelaLouisSample2() {
        return new PamelaLouis().id(2L).name("name2").configJason("configJason2");
    }

    public static PamelaLouis getPamelaLouisRandomSampleGenerator() {
        return new PamelaLouis()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .configJason(UUID.randomUUID().toString());
    }
}
