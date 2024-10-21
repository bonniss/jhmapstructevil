package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlPacinoAndreiRightHandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlPacinoAndreiRightHand getAlPacinoAndreiRightHandSample1() {
        return new AlPacinoAndreiRightHand().id(1L).name("name1");
    }

    public static AlPacinoAndreiRightHand getAlPacinoAndreiRightHandSample2() {
        return new AlPacinoAndreiRightHand().id(2L).name("name2");
    }

    public static AlPacinoAndreiRightHand getAlPacinoAndreiRightHandRandomSampleGenerator() {
        return new AlPacinoAndreiRightHand().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
