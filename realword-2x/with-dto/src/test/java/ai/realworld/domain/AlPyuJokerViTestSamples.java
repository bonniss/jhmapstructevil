package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AlPyuJokerViTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlPyuJokerVi getAlPyuJokerViSample1() {
        return new AlPyuJokerVi()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .bookingNo("bookingNo1")
            .noteHeitiga("noteHeitiga1")
            .numberOfAdults(1)
            .numberOfPreschoolers(1)
            .numberOfChildren(1)
            .historyRefJason("historyRefJason1");
    }

    public static AlPyuJokerVi getAlPyuJokerViSample2() {
        return new AlPyuJokerVi()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .bookingNo("bookingNo2")
            .noteHeitiga("noteHeitiga2")
            .numberOfAdults(2)
            .numberOfPreschoolers(2)
            .numberOfChildren(2)
            .historyRefJason("historyRefJason2");
    }

    public static AlPyuJokerVi getAlPyuJokerViRandomSampleGenerator() {
        return new AlPyuJokerVi()
            .id(UUID.randomUUID())
            .bookingNo(UUID.randomUUID().toString())
            .noteHeitiga(UUID.randomUUID().toString())
            .numberOfAdults(intCount.incrementAndGet())
            .numberOfPreschoolers(intCount.incrementAndGet())
            .numberOfChildren(intCount.incrementAndGet())
            .historyRefJason(UUID.randomUUID().toString());
    }
}
