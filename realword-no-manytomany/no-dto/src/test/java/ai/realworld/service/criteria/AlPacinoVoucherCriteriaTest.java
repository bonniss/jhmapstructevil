package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPacinoVoucherCriteriaTest {

    @Test
    void newAlPacinoVoucherCriteriaHasAllFiltersNullTest() {
        var alPacinoVoucherCriteria = new AlPacinoVoucherCriteria();
        assertThat(alPacinoVoucherCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPacinoVoucherCriteriaFluentMethodsCreatesFiltersTest() {
        var alPacinoVoucherCriteria = new AlPacinoVoucherCriteria();

        setAllFilters(alPacinoVoucherCriteria);

        assertThat(alPacinoVoucherCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPacinoVoucherCriteriaCopyCreatesNullFilterTest() {
        var alPacinoVoucherCriteria = new AlPacinoVoucherCriteria();
        var copy = alPacinoVoucherCriteria.copy();

        assertThat(alPacinoVoucherCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoVoucherCriteria)
        );
    }

    @Test
    void alPacinoVoucherCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPacinoVoucherCriteria = new AlPacinoVoucherCriteria();
        setAllFilters(alPacinoVoucherCriteria);

        var copy = alPacinoVoucherCriteria.copy();

        assertThat(alPacinoVoucherCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoVoucherCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPacinoVoucherCriteria = new AlPacinoVoucherCriteria();

        assertThat(alPacinoVoucherCriteria).hasToString("AlPacinoVoucherCriteria{}");
    }

    private static void setAllFilters(AlPacinoVoucherCriteria alPacinoVoucherCriteria) {
        alPacinoVoucherCriteria.id();
        alPacinoVoucherCriteria.sourceTitle();
        alPacinoVoucherCriteria.sourceUrl();
        alPacinoVoucherCriteria.collectedDate();
        alPacinoVoucherCriteria.userId();
        alPacinoVoucherCriteria.voucherId();
        alPacinoVoucherCriteria.applicationId();
        alPacinoVoucherCriteria.distinct();
    }

    private static Condition<AlPacinoVoucherCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlPacinoVoucherCriteria> copyFiltersAre(
        AlPacinoVoucherCriteria copy,
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
