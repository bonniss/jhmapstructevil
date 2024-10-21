package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HashRossCriteriaTest {

    @Test
    void newHashRossCriteriaHasAllFiltersNullTest() {
        var hashRossCriteria = new HashRossCriteria();
        assertThat(hashRossCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hashRossCriteriaFluentMethodsCreatesFiltersTest() {
        var hashRossCriteria = new HashRossCriteria();

        setAllFilters(hashRossCriteria);

        assertThat(hashRossCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hashRossCriteriaCopyCreatesNullFilterTest() {
        var hashRossCriteria = new HashRossCriteria();
        var copy = hashRossCriteria.copy();

        assertThat(hashRossCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hashRossCriteria)
        );
    }

    @Test
    void hashRossCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hashRossCriteria = new HashRossCriteria();
        setAllFilters(hashRossCriteria);

        var copy = hashRossCriteria.copy();

        assertThat(hashRossCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hashRossCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hashRossCriteria = new HashRossCriteria();

        assertThat(hashRossCriteria).hasToString("HashRossCriteria{}");
    }

    private static void setAllFilters(HashRossCriteria hashRossCriteria) {
        hashRossCriteria.id();
        hashRossCriteria.name();
        hashRossCriteria.slug();
        hashRossCriteria.description();
        hashRossCriteria.permissionGridJason();
        hashRossCriteria.distinct();
    }

    private static Condition<HashRossCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<HashRossCriteria> copyFiltersAre(HashRossCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
