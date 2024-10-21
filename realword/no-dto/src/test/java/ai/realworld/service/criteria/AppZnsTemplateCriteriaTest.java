package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AppZnsTemplateCriteriaTest {

    @Test
    void newAppZnsTemplateCriteriaHasAllFiltersNullTest() {
        var appZnsTemplateCriteria = new AppZnsTemplateCriteria();
        assertThat(appZnsTemplateCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void appZnsTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var appZnsTemplateCriteria = new AppZnsTemplateCriteria();

        setAllFilters(appZnsTemplateCriteria);

        assertThat(appZnsTemplateCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void appZnsTemplateCriteriaCopyCreatesNullFilterTest() {
        var appZnsTemplateCriteria = new AppZnsTemplateCriteria();
        var copy = appZnsTemplateCriteria.copy();

        assertThat(appZnsTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(appZnsTemplateCriteria)
        );
    }

    @Test
    void appZnsTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var appZnsTemplateCriteria = new AppZnsTemplateCriteria();
        setAllFilters(appZnsTemplateCriteria);

        var copy = appZnsTemplateCriteria.copy();

        assertThat(appZnsTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(appZnsTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var appZnsTemplateCriteria = new AppZnsTemplateCriteria();

        assertThat(appZnsTemplateCriteria).hasToString("AppZnsTemplateCriteria{}");
    }

    private static void setAllFilters(AppZnsTemplateCriteria appZnsTemplateCriteria) {
        appZnsTemplateCriteria.id();
        appZnsTemplateCriteria.znsAction();
        appZnsTemplateCriteria.name();
        appZnsTemplateCriteria.templateId();
        appZnsTemplateCriteria.dataSourceMappingType();
        appZnsTemplateCriteria.templateDataMapping();
        appZnsTemplateCriteria.targetUrls();
        appZnsTemplateCriteria.thumbnailId();
        appZnsTemplateCriteria.applicationId();
        appZnsTemplateCriteria.distinct();
    }

    private static Condition<AppZnsTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getZnsAction()) &&
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

    private static Condition<AppZnsTemplateCriteria> copyFiltersAre(
        AppZnsTemplateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getZnsAction(), copy.getZnsAction()) &&
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
