package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeViViCriteriaTest {

    @Test
    void newNextEmployeeViViCriteriaHasAllFiltersNullTest() {
        var nextEmployeeViViCriteria = new NextEmployeeViViCriteria();
        assertThat(nextEmployeeViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeViViCriteria = new NextEmployeeViViCriteria();

        setAllFilters(nextEmployeeViViCriteria);

        assertThat(nextEmployeeViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeViViCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeViViCriteria = new NextEmployeeViViCriteria();
        var copy = nextEmployeeViViCriteria.copy();

        assertThat(nextEmployeeViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeViViCriteria)
        );
    }

    @Test
    void nextEmployeeViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeViViCriteria = new NextEmployeeViViCriteria();
        setAllFilters(nextEmployeeViViCriteria);

        var copy = nextEmployeeViViCriteria.copy();

        assertThat(nextEmployeeViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeViViCriteria = new NextEmployeeViViCriteria();

        assertThat(nextEmployeeViViCriteria).hasToString("NextEmployeeViViCriteria{}");
    }

    private static void setAllFilters(NextEmployeeViViCriteria nextEmployeeViViCriteria) {
        nextEmployeeViViCriteria.id();
        nextEmployeeViViCriteria.firstName();
        nextEmployeeViViCriteria.lastName();
        nextEmployeeViViCriteria.email();
        nextEmployeeViViCriteria.hireDate();
        nextEmployeeViViCriteria.position();
        nextEmployeeViViCriteria.tenantId();
        nextEmployeeViViCriteria.distinct();
    }

    private static Condition<NextEmployeeViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getHireDate()) &&
                condition.apply(criteria.getPosition()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextEmployeeViViCriteria> copyFiltersAre(
        NextEmployeeViViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getHireDate(), copy.getHireDate()) &&
                condition.apply(criteria.getPosition(), copy.getPosition()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
