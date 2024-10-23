package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextEmployeeThetaCriteriaTest {

    @Test
    void newNextEmployeeThetaCriteriaHasAllFiltersNullTest() {
        var nextEmployeeThetaCriteria = new NextEmployeeThetaCriteria();
        assertThat(nextEmployeeThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextEmployeeThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextEmployeeThetaCriteria = new NextEmployeeThetaCriteria();

        setAllFilters(nextEmployeeThetaCriteria);

        assertThat(nextEmployeeThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextEmployeeThetaCriteriaCopyCreatesNullFilterTest() {
        var nextEmployeeThetaCriteria = new NextEmployeeThetaCriteria();
        var copy = nextEmployeeThetaCriteria.copy();

        assertThat(nextEmployeeThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeThetaCriteria)
        );
    }

    @Test
    void nextEmployeeThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextEmployeeThetaCriteria = new NextEmployeeThetaCriteria();
        setAllFilters(nextEmployeeThetaCriteria);

        var copy = nextEmployeeThetaCriteria.copy();

        assertThat(nextEmployeeThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextEmployeeThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextEmployeeThetaCriteria = new NextEmployeeThetaCriteria();

        assertThat(nextEmployeeThetaCriteria).hasToString("NextEmployeeThetaCriteria{}");
    }

    private static void setAllFilters(NextEmployeeThetaCriteria nextEmployeeThetaCriteria) {
        nextEmployeeThetaCriteria.id();
        nextEmployeeThetaCriteria.firstName();
        nextEmployeeThetaCriteria.lastName();
        nextEmployeeThetaCriteria.email();
        nextEmployeeThetaCriteria.hireDate();
        nextEmployeeThetaCriteria.position();
        nextEmployeeThetaCriteria.tenantId();
        nextEmployeeThetaCriteria.distinct();
    }

    private static Condition<NextEmployeeThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextEmployeeThetaCriteria> copyFiltersAre(
        NextEmployeeThetaCriteria copy,
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
