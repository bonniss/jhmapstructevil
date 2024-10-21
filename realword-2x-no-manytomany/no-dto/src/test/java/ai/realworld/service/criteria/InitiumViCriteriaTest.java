package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InitiumViCriteriaTest {

    @Test
    void newInitiumViCriteriaHasAllFiltersNullTest() {
        var initiumViCriteria = new InitiumViCriteria();
        assertThat(initiumViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void initiumViCriteriaFluentMethodsCreatesFiltersTest() {
        var initiumViCriteria = new InitiumViCriteria();

        setAllFilters(initiumViCriteria);

        assertThat(initiumViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void initiumViCriteriaCopyCreatesNullFilterTest() {
        var initiumViCriteria = new InitiumViCriteria();
        var copy = initiumViCriteria.copy();

        assertThat(initiumViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(initiumViCriteria)
        );
    }

    @Test
    void initiumViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var initiumViCriteria = new InitiumViCriteria();
        setAllFilters(initiumViCriteria);

        var copy = initiumViCriteria.copy();

        assertThat(initiumViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(initiumViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var initiumViCriteria = new InitiumViCriteria();

        assertThat(initiumViCriteria).hasToString("InitiumViCriteria{}");
    }

    private static void setAllFilters(InitiumViCriteria initiumViCriteria) {
        initiumViCriteria.id();
        initiumViCriteria.name();
        initiumViCriteria.slug();
        initiumViCriteria.description();
        initiumViCriteria.isJelloSupported();
        initiumViCriteria.distinct();
    }

    private static Condition<InitiumViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InitiumViCriteria> copyFiltersAre(InitiumViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
