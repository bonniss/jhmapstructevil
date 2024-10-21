package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AlProProViTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlProProVi getAlProProViSample1() {
        return new AlProProVi()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .descriptionHeitiga("descriptionHeitiga1")
            .numberOfAdults(1)
            .numberOfPreschoolers(1)
            .numberOfChildren(1)
            .numberOfRooms(1)
            .numberOfFloors(1);
    }

    public static AlProProVi getAlProProViSample2() {
        return new AlProProVi()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .descriptionHeitiga("descriptionHeitiga2")
            .numberOfAdults(2)
            .numberOfPreschoolers(2)
            .numberOfChildren(2)
            .numberOfRooms(2)
            .numberOfFloors(2);
    }

    public static AlProProVi getAlProProViRandomSampleGenerator() {
        return new AlProProVi()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .descriptionHeitiga(UUID.randomUUID().toString())
            .numberOfAdults(intCount.incrementAndGet())
            .numberOfPreschoolers(intCount.incrementAndGet())
            .numberOfChildren(intCount.incrementAndGet())
            .numberOfRooms(intCount.incrementAndGet())
            .numberOfFloors(intCount.incrementAndGet());
    }
}
