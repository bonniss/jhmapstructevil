package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RihannaViCriteriaTest {

    @Test
    void newRihannaViCriteriaHasAllFiltersNullTest() {
        var rihannaViCriteria = new RihannaViCriteria();
        assertThat(rihannaViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void rihannaViCriteriaFluentMethodsCreatesFiltersTest() {
        var rihannaViCriteria = new RihannaViCriteria();

        setAllFilters(rihannaViCriteria);

        assertThat(rihannaViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void rihannaViCriteriaCopyCreatesNullFilterTest() {
        var rihannaViCriteria = new RihannaViCriteria();
        var copy = rihannaViCriteria.copy();

        assertThat(rihannaViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(rihannaViCriteria)
        );
    }

    @Test
    void rihannaViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var rihannaViCriteria = new RihannaViCriteria();
        setAllFilters(rihannaViCriteria);

        var copy = rihannaViCriteria.copy();

        assertThat(rihannaViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(rihannaViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var rihannaViCriteria = new RihannaViCriteria();

        assertThat(rihannaViCriteria).hasToString("RihannaViCriteria{}");
    }

    private static void setAllFilters(RihannaViCriteria rihannaViCriteria) {
        rihannaViCriteria.id();
        rihannaViCriteria.name();
        rihannaViCriteria.description();
        rihannaViCriteria.permissionGridJason();
        rihannaViCriteria.applicationId();
        rihannaViCriteria.agentRolesId();
        rihannaViCriteria.distinct();
    }

    private static Condition<RihannaViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<RihannaViCriteria> copyFiltersAre(RihannaViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
