package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPacinoVoucherViCriteriaTest {

    @Test
    void newAlPacinoVoucherViCriteriaHasAllFiltersNullTest() {
        var alPacinoVoucherViCriteria = new AlPacinoVoucherViCriteria();
        assertThat(alPacinoVoucherViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPacinoVoucherViCriteriaFluentMethodsCreatesFiltersTest() {
        var alPacinoVoucherViCriteria = new AlPacinoVoucherViCriteria();

        setAllFilters(alPacinoVoucherViCriteria);

        assertThat(alPacinoVoucherViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPacinoVoucherViCriteriaCopyCreatesNullFilterTest() {
        var alPacinoVoucherViCriteria = new AlPacinoVoucherViCriteria();
        var copy = alPacinoVoucherViCriteria.copy();

        assertThat(alPacinoVoucherViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoVoucherViCriteria)
        );
    }

    @Test
    void alPacinoVoucherViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPacinoVoucherViCriteria = new AlPacinoVoucherViCriteria();
        setAllFilters(alPacinoVoucherViCriteria);

        var copy = alPacinoVoucherViCriteria.copy();

        assertThat(alPacinoVoucherViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoVoucherViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPacinoVoucherViCriteria = new AlPacinoVoucherViCriteria();

        assertThat(alPacinoVoucherViCriteria).hasToString("AlPacinoVoucherViCriteria{}");
    }

    private static void setAllFilters(AlPacinoVoucherViCriteria alPacinoVoucherViCriteria) {
        alPacinoVoucherViCriteria.id();
        alPacinoVoucherViCriteria.sourceTitle();
        alPacinoVoucherViCriteria.sourceUrl();
        alPacinoVoucherViCriteria.collectedDate();
        alPacinoVoucherViCriteria.userId();
        alPacinoVoucherViCriteria.voucherId();
        alPacinoVoucherViCriteria.applicationId();
        alPacinoVoucherViCriteria.distinct();
    }

    private static Condition<AlPacinoVoucherViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSourceTitle()) &&
                condition.apply(criteria.getSourceUrl()) &&
                condition.apply(criteria.getCollectedDate()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getVoucherId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPacinoVoucherViCriteria> copyFiltersAre(
        AlPacinoVoucherViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSourceTitle(), copy.getSourceTitle()) &&
                condition.apply(criteria.getSourceUrl(), copy.getSourceUrl()) &&
                condition.apply(criteria.getCollectedDate(), copy.getCollectedDate()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getVoucherId(), copy.getVoucherId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
