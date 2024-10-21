package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RihannaCriteriaTest {

    @Test
    void newRihannaCriteriaHasAllFiltersNullTest() {
        var rihannaCriteria = new RihannaCriteria();
        assertThat(rihannaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void rihannaCriteriaFluentMethodsCreatesFiltersTest() {
        var rihannaCriteria = new RihannaCriteria();

        setAllFilters(rihannaCriteria);

        assertThat(rihannaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void rihannaCriteriaCopyCreatesNullFilterTest() {
        var rihannaCriteria = new RihannaCriteria();
        var copy = rihannaCriteria.copy();

        assertThat(rihannaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(rihannaCriteria)
        );
    }

    @Test
    void rihannaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var rihannaCriteria = new RihannaCriteria();
        setAllFilters(rihannaCriteria);

        var copy = rihannaCriteria.copy();

        assertThat(rihannaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(rihannaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var rihannaCriteria = new RihannaCriteria();

        assertThat(rihannaCriteria).hasToString("RihannaCriteria{}");
    }

    private static void setAllFilters(RihannaCriteria rihannaCriteria) {
        rihannaCriteria.id();
        rihannaCriteria.name();
        rihannaCriteria.description();
        rihannaCriteria.permissionGridJason();
        rihannaCriteria.applicationId();
        rihannaCriteria.agentRolesId();
        rihannaCriteria.distinct();
    }

    private static Condition<RihannaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPermissionGridJason()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getAgentRolesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RihannaCriteria> copyFiltersAre(RihannaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPermissionGridJason(), copy.getPermissionGridJason()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getAgentRolesId(), copy.getAgentRolesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
