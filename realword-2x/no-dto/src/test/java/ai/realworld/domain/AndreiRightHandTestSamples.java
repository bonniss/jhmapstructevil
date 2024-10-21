package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AndreiRightHandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AndreiRightHand getAndreiRightHandSample1() {
        return new AndreiRightHand().id(1L).details("details1");
    }

    public static AndreiRightHand getAndreiRightHandSample2() {
        return new AndreiRightHand().id(2L).details("details2");
    }

    public static AndreiRightHand getAndreiRightHandRandomSampleGenerator() {
        return new AndreiRightHand().id(longCount.incrementAndGet()).details(UUID.randomUUID().toString());
    }
}
