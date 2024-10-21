package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlLexFergTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlLexFerg getAlLexFergSample1() {
        return new AlLexFerg().id(1L).title("title1").slug("slug1").summary("summary1").contentHeitiga("contentHeitiga1");
    }

    public static AlLexFerg getAlLexFergSample2() {
        return new AlLexFerg().id(2L).title("title2").slug("slug2").summary("summary2").contentHeitiga("contentHeitiga2");
    }

    public static AlLexFerg getAlLexFergRandomSampleGenerator() {
        return new AlLexFerg()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .summary(UUID.randomUUID().toString())
            .contentHeitiga(UUID.randomUUID().toString());
    }
}
