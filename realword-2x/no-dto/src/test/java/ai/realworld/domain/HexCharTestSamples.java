package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HexCharTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HexChar getHexCharSample1() {
        return new HexChar().id(1L).phone("phone1").bioHeitiga("bioHeitiga1");
    }

    public static HexChar getHexCharSample2() {
        return new HexChar().id(2L).phone("phone2").bioHeitiga("bioHeitiga2");
    }

    public static HexChar getHexCharRandomSampleGenerator() {
        return new HexChar().id(longCount.incrementAndGet()).phone(UUID.randomUUID().toString()).bioHeitiga(UUID.randomUUID().toString());
    }
}
