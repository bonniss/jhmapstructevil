package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlMenityViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlMenityVi getAlMenityViSample1() {
        return new AlMenityVi().id(1L).name("name1").iconSvg("iconSvg1");
    }

    public static AlMenityVi getAlMenityViSample2() {
        return new AlMenityVi().id(2L).name("name2").iconSvg("iconSvg2");
    }

    public static AlMenityVi getAlMenityViRandomSampleGenerator() {
        return new AlMenityVi().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).iconSvg(UUID.randomUUID().toString());
    }
}
