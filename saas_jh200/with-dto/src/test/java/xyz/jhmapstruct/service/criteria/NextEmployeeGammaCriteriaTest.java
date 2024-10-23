package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeGammaCriteriaTest {

    @Test
    void newNextEmployeeGammaCriteriaHasAllFiltersNullTest() {
        var nextEmployeeGammaCriteria = new NextEmployeeGammaCriteria();
        assertThat(nextEmployeeGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeGammaCriteria = new NextEmployeeGammaCriteria();

        setAllFilters(nextEmployeeGammaCriteria);

        assertThat(nextEmployeeGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeGammaCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeGammaCriteria = new NextEmployeeGammaCriteria();
        var copy = nextEmployeeGammaCriteria.copy();

        assertThat(nextEmployeeGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeGammaCriteria)
        );
    }

    @Test
    void nextEmployeeGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeGammaCriteria = new NextEmployeeGammaCriteria();
        setAllFilters(nextEmployeeGammaCriteria);

        var copy = nextEmployeeGammaCriteria.copy();

        assertThat(nextEmployeeGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeGammaCriteria = new NextEmployeeGammaCriteria();

        assertThat(nextEmployeeGammaCriteria).hasToString("NextEmployeeGammaCriteria{}");
    }

    private static void setAllFilters(NextEmployeeGammaCriteria nextEmployeeGammaCriteria) {
        nextEmployeeGammaCriteria.id();
        nextEmployeeGammaCriteria.firstName();
        nextEmployeeGammaCriteria.lastName();
        nextEmployeeGammaCriteria.email();
        nextEmployeeGammaCriteria.hireDate();
        nextEmployeeGammaCriteria.position();
        nextEmployeeGammaCriteria.tenantId();
        nextEmployeeGammaCriteria.distinct();
    }

    private static Condition<NextEmployeeGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextEmployeeGammaCriteria> copyFiltersAre(
        NextEmployeeGammaCriteria copy,
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
