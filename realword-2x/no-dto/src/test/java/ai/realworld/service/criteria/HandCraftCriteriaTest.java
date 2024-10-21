package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HandCraftCriteriaTest {

    @Test
    void newHandCraftCriteriaHasAllFiltersNullTest() {
        var handCraftCriteria = new HandCraftCriteria();
        assertThat(handCraftCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void handCraftCriteriaFluentMethodsCreatesFiltersTest() {
        var handCraftCriteria = new HandCraftCriteria();

        setAllFilters(handCraftCriteria);

        assertThat(handCraftCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void handCraftCriteriaCopyCreatesNullFilterTest() {
        var handCraftCriteria = new HandCraftCriteria();
        var copy = handCraftCriteria.copy();

        assertThat(handCraftCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(handCraftCriteria)
        );
    }

    @Test
    void handCraftCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var handCraftCriteria = new HandCraftCriteria();
        setAllFilters(handCraftCriteria);

        var copy = handCraftCriteria.copy();

        assertThat(handCraftCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(handCraftCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var handCraftCriteria = new HandCraftCriteria();

        assertThat(handCraftCriteria).hasToString("HandCraftCriteria{}");
    }

    private static void setAllFilters(HandCraftCriteria handCraftCriteria) {
        handCraftCriteria.id();
        handCraftCriteria.agentId();
        handCraftCriteria.roleId();
        handCraftCriteria.applicationId();
        handCraftCriteria.distinct();
    }

    private static Condition<HandCraftCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<HandCraftCriteria> copyFiltersAre(HandCraftCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
