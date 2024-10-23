package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MasterTenantCriteriaTest {

    @Test
    void newMasterTenantCriteriaHasAllFiltersNullTest() {
        var masterTenantCriteria = new MasterTenantCriteria();
        assertThat(masterTenantCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void masterTenantCriteriaFluentMethodsCreatesFiltersTest() {
        var masterTenantCriteria = new MasterTenantCriteria();

        setAllFilters(masterTenantCriteria);

        assertThat(masterTenantCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void masterTenantCriteriaCopyCreatesNullFilterTest() {
        var masterTenantCriteria = new MasterTenantCriteria();
        var copy = masterTenantCriteria.copy();

        assertThat(masterTenantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(masterTenantCriteria)
        );
    }

    @Test
    void masterTenantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var masterTenantCriteria = new MasterTenantCriteria();
        setAllFilters(masterTenantCriteria);

        var copy = masterTenantCriteria.copy();

        assertThat(masterTenantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(masterTenantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var masterTenantCriteria = new MasterTenantCriteria();

        assertThat(masterTenantCriteria).hasToString("MasterTenantCriteria{}");
    }

    private static void setAllFilters(MasterTenantCriteria masterTenantCriteria) {
        masterTenantCriteria.id();
        masterTenantCriteria.code();
        masterTenantCriteria.distinct();
    }

    private static Condition<MasterTenantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getCode()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MasterTenantCriteria> copyFiltersAre(
        MasterTenantCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
