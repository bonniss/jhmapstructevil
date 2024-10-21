package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlVueVueViCriteriaTest {

    @Test
    void newAlVueVueViCriteriaHasAllFiltersNullTest() {
        var alVueVueViCriteria = new AlVueVueViCriteria();
        assertThat(alVueVueViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alVueVueViCriteriaFluentMethodsCreatesFiltersTest() {
        var alVueVueViCriteria = new AlVueVueViCriteria();

        setAllFilters(alVueVueViCriteria);

        assertThat(alVueVueViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alVueVueViCriteriaCopyCreatesNullFilterTest() {
        var alVueVueViCriteria = new AlVueVueViCriteria();
        var copy = alVueVueViCriteria.copy();

        assertThat(alVueVueViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueViCriteria)
        );
    }

    @Test
    void alVueVueViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alVueVueViCriteria = new AlVueVueViCriteria();
        setAllFilters(alVueVueViCriteria);

        var copy = alVueVueViCriteria.copy();

        assertThat(alVueVueViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alVueVueViCriteria = new AlVueVueViCriteria();

        assertThat(alVueVueViCriteria).hasToString("AlVueVueViCriteria{}");
    }

    private static void setAllFilters(AlVueVueViCriteria alVueVueViCriteria) {
        alVueVueViCriteria.id();
        alVueVueViCriteria.code();
        alVueVueViCriteria.name();
        alVueVueViCriteria.contentHeitiga();
        alVueVueViCriteria.discountType();
        alVueVueViCriteria.discountRate();
        alVueVueViCriteria.scope();
        alVueVueViCriteria.isIndividuallyUsedOnly();
        alVueVueViCriteria.usageLifeTimeLimit();
        alVueVueViCriteria.usageLimitPerUser();
        alVueVueViCriteria.usageQuantity();
        alVueVueViCriteria.minimumSpend();
        alVueVueViCriteria.maximumSpend();
        alVueVueViCriteria.canBeCollectedByUser();
        alVueVueViCriteria.salePriceFromDate();
        alVueVueViCriteria.salePriceToDate();
        alVueVueViCriteria.publicationStatus();
        alVueVueViCriteria.publishedDate();
        alVueVueViCriteria.imageId();
        alVueVueViCriteria.alVueVueViUsageId();
        alVueVueViCriteria.applicationId();
        alVueVueViCriteria.conditionsId();
        alVueVueViCriteria.distinct();
    }

    private static Condition<AlVueVueViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getAlVueVueViUsageId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getConditionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlVueVueViCriteria> copyFiltersAre(AlVueVueViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
                condition.apply(criteria.getAlVueVueViUsageId(), copy.getAlVueVueViUsageId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getConditionsId(), copy.getConditionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
