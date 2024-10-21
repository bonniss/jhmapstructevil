package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlZorroTemptationViCriteriaTest {

    @Test
    void newAlZorroTemptationViCriteriaHasAllFiltersNullTest() {
        var alZorroTemptationViCriteria = new AlZorroTemptationViCriteria();
        assertThat(alZorroTemptationViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alZorroTemptationViCriteriaFluentMethodsCreatesFiltersTest() {
        var alZorroTemptationViCriteria = new AlZorroTemptationViCriteria();

        setAllFilters(alZorroTemptationViCriteria);

        assertThat(alZorroTemptationViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alZorroTemptationViCriteriaCopyCreatesNullFilterTest() {
        var alZorroTemptationViCriteria = new AlZorroTemptationViCriteria();
        var copy = alZorroTemptationViCriteria.copy();

        assertThat(alZorroTemptationViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alZorroTemptationViCriteria)
        );
    }

    @Test
    void alZorroTemptationViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alZorroTemptationViCriteria = new AlZorroTemptationViCriteria();
        setAllFilters(alZorroTemptationViCriteria);

        var copy = alZorroTemptationViCriteria.copy();

        assertThat(alZorroTemptationViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alZorroTemptationViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alZorroTemptationViCriteria = new AlZorroTemptationViCriteria();

        assertThat(alZorroTemptationViCriteria).hasToString("AlZorroTemptationViCriteria{}");
    }

    private static void setAllFilters(AlZorroTemptationViCriteria alZorroTemptationViCriteria) {
        alZorroTemptationViCriteria.id();
        alZorroTemptationViCriteria.zipAction();
        alZorroTemptationViCriteria.name();
        alZorroTemptationViCriteria.templateId();
        alZorroTemptationViCriteria.dataSourceMappingType();
        alZorroTemptationViCriteria.templateDataMapping();
        alZorroTemptationViCriteria.targetUrls();
        alZorroTemptationViCriteria.thumbnailId();
        alZorroTemptationViCriteria.applicationId();
        alZorroTemptationViCriteria.distinct();
    }

    private static Condition<AlZorroTemptationViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getZipAction()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getTemplateId()) &&
                condition.apply(criteria.getDataSourceMappingType()) &&
                condition.apply(criteria.getTemplateDataMapping()) &&
                condition.apply(criteria.getTargetUrls()) &&
                condition.apply(criteria.getThumbnailId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlZorroTemptationViCriteria> copyFiltersAre(
        AlZorroTemptationViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getZipAction(), copy.getZipAction()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getTemplateId(), copy.getTemplateId()) &&
                condition.apply(criteria.getDataSourceMappingType(), copy.getDataSourceMappingType()) &&
                condition.apply(criteria.getTemplateDataMapping(), copy.getTemplateDataMapping()) &&
                condition.apply(criteria.getTargetUrls(), copy.getTargetUrls()) &&
                condition.apply(criteria.getThumbnailId(), copy.getThumbnailId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
