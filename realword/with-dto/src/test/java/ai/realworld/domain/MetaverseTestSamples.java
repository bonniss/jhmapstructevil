package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MetaverseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Metaverse getMetaverseSample1() {
        return new Metaverse()
            .id(1L)
            .filename("filename1")
            .contentType("contentType1")
            .fileExt("fileExt1")
            .fileSize(1L)
            .fileUrl("fileUrl1")
            .thumbnailUrl("thumbnailUrl1")
            .blurhash("blurhash1")
            .objectName("objectName1")
            .objectMetaJason("objectMetaJason1");
    }

    public static Metaverse getMetaverseSample2() {
        return new Metaverse()
            .id(2L)
            .filename("filename2")
            .contentType("contentType2")
            .fileExt("fileExt2")
            .fileSize(2L)
            .fileUrl("fileUrl2")
            .thumbnailUrl("thumbnailUrl2")
            .blurhash("blurhash2")
            .objectName("objectName2")
            .objectMetaJason("objectMetaJason2");
    }

    public static Metaverse getMetaverseRandomSampleGenerator() {
        return new Metaverse()
            .id(longCount.incrementAndGet())
            .filename(UUID.randomUUID().toString())
            .contentType(UUID.randomUUID().toString())
            .fileExt(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .fileUrl(UUID.randomUUID().toString())
            .thumbnailUrl(UUID.randomUUID().toString())
            .blurhash(UUID.randomUUID().toString())
            .objectName(UUID.randomUUID().toString())
            .objectMetaJason(UUID.randomUUID().toString());
    }
}
