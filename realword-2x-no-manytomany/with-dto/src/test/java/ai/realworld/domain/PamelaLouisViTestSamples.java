package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PamelaLouisViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PamelaLouisVi getPamelaLouisViSample1() {
        return new PamelaLouisVi().id(1L).name("name1").configJason("configJason1");
    }

    public static PamelaLouisVi getPamelaLouisViSample2() {
        return new PamelaLouisVi().id(2L).name("name2").configJason("configJason2");
    }

    public static PamelaLouisVi getPamelaLouisViRandomSampleGenerator() {
        return new PamelaLouisVi()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .configJason(UUID.randomUUID().toString());
    }
}
