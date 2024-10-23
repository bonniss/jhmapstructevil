package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeMiMiCriteriaTest {

    @Test
    void newNextEmployeeMiMiCriteriaHasAllFiltersNullTest() {
        var nextEmployeeMiMiCriteria = new NextEmployeeMiMiCriteria();
        assertThat(nextEmployeeMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeMiMiCriteria = new NextEmployeeMiMiCriteria();

        setAllFilters(nextEmployeeMiMiCriteria);

        assertThat(nextEmployeeMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeMiMiCriteria = new NextEmployeeMiMiCriteria();
        var copy = nextEmployeeMiMiCriteria.copy();

        assertThat(nextEmployeeMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeMiMiCriteria)
        );
    }

    @Test
    void nextEmployeeMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeMiMiCriteria = new NextEmployeeMiMiCriteria();
        setAllFilters(nextEmployeeMiMiCriteria);

        var copy = nextEmployeeMiMiCriteria.copy();

        assertThat(nextEmployeeMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeMiMiCriteria = new NextEmployeeMiMiCriteria();

        assertThat(nextEmployeeMiMiCriteria).hasToString("NextEmployeeMiMiCriteria{}");
    }

    private static void setAllFilters(NextEmployeeMiMiCriteria nextEmployeeMiMiCriteria) {
        nextEmployeeMiMiCriteria.id();
        nextEmployeeMiMiCriteria.firstName();
        nextEmployeeMiMiCriteria.lastName();
        nextEmployeeMiMiCriteria.email();
        nextEmployeeMiMiCriteria.hireDate();
        nextEmployeeMiMiCriteria.position();
        nextEmployeeMiMiCriteria.tenantId();
        nextEmployeeMiMiCriteria.distinct();
    }

    private static Condition<NextEmployeeMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextEmployeeMiMiCriteria> copyFiltersAre(
        NextEmployeeMiMiCriteria copy,
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
