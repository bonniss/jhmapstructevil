package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlZorroTemptationCriteriaTest {

    @Test
    void newAlZorroTemptationCriteriaHasAllFiltersNullTest() {
        var alZorroTemptationCriteria = new AlZorroTemptationCriteria();
        assertThat(alZorroTemptationCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alZorroTemptationCriteriaFluentMethodsCreatesFiltersTest() {
        var alZorroTemptationCriteria = new AlZorroTemptationCriteria();

        setAllFilters(alZorroTemptationCriteria);

        assertThat(alZorroTemptationCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alZorroTemptationCriteriaCopyCreatesNullFilterTest() {
        var alZorroTemptationCriteria = new AlZorroTemptationCriteria();
        var copy = alZorroTemptationCriteria.copy();

        assertThat(alZorroTemptationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alZorroTemptationCriteria)
        );
    }

    @Test
    void alZorroTemptationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alZorroTemptationCriteria = new AlZorroTemptationCriteria();
        setAllFilters(alZorroTemptationCriteria);

        var copy = alZorroTemptationCriteria.copy();

        assertThat(alZorroTemptationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alZorroTemptationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alZorroTemptationCriteria = new AlZorroTemptationCriteria();

        assertThat(alZorroTemptationCriteria).hasToString("AlZorroTemptationCriteria{}");
    }

    private static void setAllFilters(AlZorroTemptationCriteria alZorroTemptationCriteria) {
        alZorroTemptationCriteria.id();
        alZorroTemptationCriteria.zipAction();
        alZorroTemptationCriteria.name();
        alZorroTemptationCriteria.templateId();
        alZorroTemptationCriteria.dataSourceMappingType();
        alZorroTemptationCriteria.templateDataMapping();
        alZorroTemptationCriteria.targetUrls();
        alZorroTemptationCriteria.thumbnailId();
        alZorroTemptationCriteria.applicationId();
        alZorroTemptationCriteria.distinct();
    }

    private static Condition<AlZorroTemptationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlZorroTemptationCriteria> copyFiltersAre(
        AlZorroTemptationCriteria copy,
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
