package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HandCraftViCriteriaTest {

    @Test
    void newHandCraftViCriteriaHasAllFiltersNullTest() {
        var handCraftViCriteria = new HandCraftViCriteria();
        assertThat(handCraftViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void handCraftViCriteriaFluentMethodsCreatesFiltersTest() {
        var handCraftViCriteria = new HandCraftViCriteria();

        setAllFilters(handCraftViCriteria);

        assertThat(handCraftViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void handCraftViCriteriaCopyCreatesNullFilterTest() {
        var handCraftViCriteria = new HandCraftViCriteria();
        var copy = handCraftViCriteria.copy();

        assertThat(handCraftViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(handCraftViCriteria)
        );
    }

    @Test
    void handCraftViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var handCraftViCriteria = new HandCraftViCriteria();
        setAllFilters(handCraftViCriteria);

        var copy = handCraftViCriteria.copy();

        assertThat(handCraftViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(handCraftViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var handCraftViCriteria = new HandCraftViCriteria();

        assertThat(handCraftViCriteria).hasToString("HandCraftViCriteria{}");
    }

    private static void setAllFilters(HandCraftViCriteria handCraftViCriteria) {
        handCraftViCriteria.id();
        handCraftViCriteria.agentId();
        handCraftViCriteria.roleId();
        handCraftViCriteria.applicationId();
        handCraftViCriteria.distinct();
    }

    private static Condition<HandCraftViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAgentId()) &&
                condition.apply(criteria.getRoleId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HandCraftViCriteria> copyFiltersAre(HandCraftViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAgentId(), copy.getAgentId()) &&
                condition.apply(criteria.getRoleId(), copy.getRoleId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
