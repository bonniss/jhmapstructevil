package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InitiumCriteriaTest {

    @Test
    void newInitiumCriteriaHasAllFiltersNullTest() {
        var initiumCriteria = new InitiumCriteria();
        assertThat(initiumCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void initiumCriteriaFluentMethodsCreatesFiltersTest() {
        var initiumCriteria = new InitiumCriteria();

        setAllFilters(initiumCriteria);

        assertThat(initiumCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void initiumCriteriaCopyCreatesNullFilterTest() {
        var initiumCriteria = new InitiumCriteria();
        var copy = initiumCriteria.copy();

        assertThat(initiumCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(initiumCriteria)
        );
    }

    @Test
    void initiumCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var initiumCriteria = new InitiumCriteria();
        setAllFilters(initiumCriteria);

        var copy = initiumCriteria.copy();

        assertThat(initiumCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(initiumCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var initiumCriteria = new InitiumCriteria();

        assertThat(initiumCriteria).hasToString("InitiumCriteria{}");
    }

    private static void setAllFilters(InitiumCriteria initiumCriteria) {
        initiumCriteria.id();
        initiumCriteria.name();
        initiumCriteria.slug();
        initiumCriteria.description();
        initiumCriteria.isJelloSupported();
        initiumCriteria.distinct();
    }

    private static Condition<InitiumCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getIsJelloSupported()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InitiumCriteria> copyFiltersAre(InitiumCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getIsJelloSupported(), copy.getIsJelloSupported()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
