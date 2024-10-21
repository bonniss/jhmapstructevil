package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AndreiRightHandViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AndreiRightHandVi getAndreiRightHandViSample1() {
        return new AndreiRightHandVi().id(1L).details("details1");
    }

    public static AndreiRightHandVi getAndreiRightHandViSample2() {
        return new AndreiRightHandVi().id(2L).details("details2");
    }

    public static AndreiRightHandVi getAndreiRightHandViRandomSampleGenerator() {
        return new AndreiRightHandVi().id(longCount.incrementAndGet()).details(UUID.randomUUID().toString());
    }
}
