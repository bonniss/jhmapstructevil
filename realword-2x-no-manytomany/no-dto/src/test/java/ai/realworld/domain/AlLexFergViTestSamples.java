package ai.realworld.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlLexFergViTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlLexFergVi getAlLexFergViSample1() {
        return new AlLexFergVi().id(1L).title("title1").slug("slug1").summary("summary1").contentHeitiga("contentHeitiga1");
    }

    public static AlLexFergVi getAlLexFergViSample2() {
        return new AlLexFergVi().id(2L).title("title2").slug("slug2").summary("summary2").contentHeitiga("contentHeitiga2");
    }

    public static AlLexFergVi getAlLexFergViRandomSampleGenerator() {
        return new AlLexFergVi()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .summary(UUID.randomUUID().toString())
            .contentHeitiga(UUID.randomUUID().toString());
    }
}
