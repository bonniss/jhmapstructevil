package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlPacinoAndreiRightHandViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlPacinoAndreiRightHandVi getAlPacinoAndreiRightHandViSample1() {
        return new AlPacinoAndreiRightHandVi().id(1L).name("name1");
    }

    public static AlPacinoAndreiRightHandVi getAlPacinoAndreiRightHandViSample2() {
        return new AlPacinoAndreiRightHandVi().id(2L).name("name2");
    }

    public static AlPacinoAndreiRightHandVi getAlPacinoAndreiRightHandViRandomSampleGenerator() {
        return new AlPacinoAndreiRightHandVi().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
