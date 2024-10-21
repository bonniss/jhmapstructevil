package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HashRossViCriteriaTest {

    @Test
    void newHashRossViCriteriaHasAllFiltersNullTest() {
        var hashRossViCriteria = new HashRossViCriteria();
        assertThat(hashRossViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hashRossViCriteriaFluentMethodsCreatesFiltersTest() {
        var hashRossViCriteria = new HashRossViCriteria();

        setAllFilters(hashRossViCriteria);

        assertThat(hashRossViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hashRossViCriteriaCopyCreatesNullFilterTest() {
        var hashRossViCriteria = new HashRossViCriteria();
        var copy = hashRossViCriteria.copy();

        assertThat(hashRossViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hashRossViCriteria)
        );
    }

    @Test
    void hashRossViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hashRossViCriteria = new HashRossViCriteria();
        setAllFilters(hashRossViCriteria);

        var copy = hashRossViCriteria.copy();

        assertThat(hashRossViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hashRossViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hashRossViCriteria = new HashRossViCriteria();

        assertThat(hashRossViCriteria).hasToString("HashRossViCriteria{}");
    }

    private static void setAllFilters(HashRossViCriteria hashRossViCriteria) {
        hashRossViCriteria.id();
        hashRossViCriteria.name();
        hashRossViCriteria.slug();
        hashRossViCriteria.description();
        hashRossViCriteria.permissionGridJason();
        hashRossViCriteria.distinct();
    }

    private static Condition<HashRossViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPermissionGridJason()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HashRossViCriteria> copyFiltersAre(HashRossViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPermissionGridJason(), copy.getPermissionGridJason()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
