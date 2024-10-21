package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlMenityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlMenity getAlMenitySample1() {
        return new AlMenity().id(1L).name("name1").iconSvg("iconSvg1");
    }

    public static AlMenity getAlMenitySample2() {
        return new AlMenity().id(2L).name("name2").iconSvg("iconSvg2");
    }

    public static AlMenity getAlMenityRandomSampleGenerator() {
        return new AlMenity().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).iconSvg(UUID.randomUUID().toString());
    }
}
