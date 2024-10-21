package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HexCharViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HexCharVi getHexCharViSample1() {
        return new HexCharVi().id(1L).phone("phone1").bioHeitiga("bioHeitiga1");
    }

    public static HexCharVi getHexCharViSample2() {
        return new HexCharVi().id(2L).phone("phone2").bioHeitiga("bioHeitiga2");
    }

    public static HexCharVi getHexCharViRandomSampleGenerator() {
        return new HexCharVi().id(longCount.incrementAndGet()).phone(UUID.randomUUID().toString()).bioHeitiga(UUID.randomUUID().toString());
    }
}
