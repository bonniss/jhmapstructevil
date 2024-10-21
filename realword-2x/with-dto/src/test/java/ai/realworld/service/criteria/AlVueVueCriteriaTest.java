package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlVueVueCriteriaTest {

    @Test
    void newAlVueVueCriteriaHasAllFiltersNullTest() {
        var alVueVueCriteria = new AlVueVueCriteria();
        assertThat(alVueVueCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alVueVueCriteriaFluentMethodsCreatesFiltersTest() {
        var alVueVueCriteria = new AlVueVueCriteria();

        setAllFilters(alVueVueCriteria);

        assertThat(alVueVueCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alVueVueCriteriaCopyCreatesNullFilterTest() {
        var alVueVueCriteria = new AlVueVueCriteria();
        var copy = alVueVueCriteria.copy();

        assertThat(alVueVueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueCriteria)
        );
    }

    @Test
    void alVueVueCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alVueVueCriteria = new AlVueVueCriteria();
        setAllFilters(alVueVueCriteria);

        var copy = alVueVueCriteria.copy();

        assertThat(alVueVueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alVueVueCriteria = new AlVueVueCriteria();

        assertThat(alVueVueCriteria).hasToString("AlVueVueCriteria{}");
    }

    private static void setAllFilters(AlVueVueCriteria alVueVueCriteria) {
        alVueVueCriteria.id();
        alVueVueCriteria.code();
        alVueVueCriteria.name();
        alVueVueCriteria.contentHeitiga();
        alVueVueCriteria.discountType();
        alVueVueCriteria.discountRate();
        alVueVueCriteria.scope();
        alVueVueCriteria.isIndividuallyUsedOnly();
        alVueVueCriteria.usageLifeTimeLimit();
        alVueVueCriteria.usageLimitPerUser();
        alVueVueCriteria.usageQuantity();
        alVueVueCriteria.minimumSpend();
        alVueVueCriteria.maximumSpend();
        alVueVueCriteria.canBeCollectedByUser();
        alVueVueCriteria.salePriceFromDate();
        alVueVueCriteria.salePriceToDate();
        alVueVueCriteria.publicationStatus();
        alVueVueCriteria.publishedDate();
        alVueVueCriteria.imageId();
        alVueVueCriteria.alVueVueUsageId();
        alVueVueCriteria.applicationId();
        alVueVueCriteria.conditionsId();
        alVueVueCriteria.distinct();
    }

    private static Condition<AlVueVueCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getContentHeitiga()) &&
                condition.apply(criteria.getDiscountType()) &&
                condition.apply(criteria.getDiscountRate()) &&
                condition.apply(criteria.getScope()) &&
                condition.apply(criteria.getIsIndividuallyUsedOnly()) &&
                condition.apply(criteria.getUsageLifeTimeLimit()) &&
                condition.apply(criteria.getUsageLimitPerUser()) &&
                condition.apply(criteria.getUsageQuantity()) &&
                condition.apply(criteria.getMinimumSpend()) &&
                condition.apply(criteria.getMaximumSpend()) &&
                condition.apply(criteria.getCanBeCollectedByUser()) &&
                condition.apply(criteria.getSalePriceFromDate()) &&
                condition.apply(criteria.getSalePriceToDate()) &&
                condition.apply(criteria.getPublicationStatus()) &&
                condition.apply(criteria.getPublishedDate()) &&
                condition.apply(criteria.getImageId()) &&
                condition.apply(criteria.getAlVueVueUsageId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getConditionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlVueVueCriteria> copyFiltersAre(AlVueVueCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getContentHeitiga(), copy.getContentHeitiga()) &&
                condition.apply(criteria.getDiscountType(), copy.getDiscountType()) &&
                condition.apply(criteria.getDiscountRate(), copy.getDiscountRate()) &&
                condition.apply(criteria.getScope(), copy.getScope()) &&
                condition.apply(criteria.getIsIndividuallyUsedOnly(), copy.getIsIndividuallyUsedOnly()) &&
                condition.apply(criteria.getUsageLifeTimeLimit(), copy.getUsageLifeTimeLimit()) &&
                condition.apply(criteria.getUsageLimitPerUser(), copy.getUsageLimitPerUser()) &&
                condition.apply(criteria.getUsageQuantity(), copy.getUsageQuantity()) &&
                condition.apply(criteria.getMinimumSpend(), copy.getMinimumSpend()) &&
                condition.apply(criteria.getMaximumSpend(), copy.getMaximumSpend()) &&
                condition.apply(criteria.getCanBeCollectedByUser(), copy.getCanBeCollectedByUser()) &&
                condition.apply(criteria.getSalePriceFromDate(), copy.getSalePriceFromDate()) &&
                condition.apply(criteria.getSalePriceToDate(), copy.getSalePriceToDate()) &&
                condition.apply(criteria.getPublicationStatus(), copy.getPublicationStatus()) &&
                condition.apply(criteria.getPublishedDate(), copy.getPublishedDate()) &&
                condition.apply(criteria.getImageId(), copy.getImageId()) &&
                condition.apply(criteria.getAlVueVueUsageId(), copy.getAlVueVueUsageId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getConditionsId(), copy.getConditionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
