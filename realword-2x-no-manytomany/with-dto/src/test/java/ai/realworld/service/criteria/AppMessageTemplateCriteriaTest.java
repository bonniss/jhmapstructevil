package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AppMessageTemplateCriteriaTest {

    @Test
    void newAppMessageTemplateCriteriaHasAllFiltersNullTest() {
        var appMessageTemplateCriteria = new AppMessageTemplateCriteria();
        assertThat(appMessageTemplateCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void appMessageTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var appMessageTemplateCriteria = new AppMessageTemplateCriteria();

        setAllFilters(appMessageTemplateCriteria);

        assertThat(appMessageTemplateCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void appMessageTemplateCriteriaCopyCreatesNullFilterTest() {
        var appMessageTemplateCriteria = new AppMessageTemplateCriteria();
        var copy = appMessageTemplateCriteria.copy();

        assertThat(appMessageTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(appMessageTemplateCriteria)
        );
    }

    @Test
    void appMessageTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var appMessageTemplateCriteria = new AppMessageTemplateCriteria();
        setAllFilters(appMessageTemplateCriteria);

        var copy = appMessageTemplateCriteria.copy();

        assertThat(appMessageTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(appMessageTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var appMessageTemplateCriteria = new AppMessageTemplateCriteria();

        assertThat(appMessageTemplateCriteria).hasToString("AppMessageTemplateCriteria{}");
    }

    private static void setAllFilters(AppMessageTemplateCriteria appMessageTemplateCriteria) {
        appMessageTemplateCriteria.id();
        appMessageTemplateCriteria.title();
        appMessageTemplateCriteria.topContent();
        appMessageTemplateCriteria.content();
        appMessageTemplateCriteria.bottomContent();
        appMessageTemplateCriteria.propTitleMappingJason();
        appMessageTemplateCriteria.dataSourceMappingType();
        appMessageTemplateCriteria.targetUrls();
        appMessageTemplateCriteria.thumbnailId();
        appMessageTemplateCriteria.applicationId();
        appMessageTemplateCriteria.distinct();
    }

    private static Condition<AppMessageTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getTopContent()) &&
                condition.apply(criteria.getContent()) &&
                condition.apply(criteria.getBottomContent()) &&
                condition.apply(criteria.getPropTitleMappingJason()) &&
                condition.apply(criteria.getDataSourceMappingType()) &&
                condition.apply(criteria.getTargetUrls()) &&
                condition.apply(criteria.getThumbnailId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AppMessageTemplateCriteria> copyFiltersAre(
        AppMessageTemplateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getTopContent(), copy.getTopContent()) &&
                condition.apply(criteria.getContent(), copy.getContent()) &&
                condition.apply(criteria.getBottomContent(), copy.getBottomContent()) &&
                condition.apply(criteria.getPropTitleMappingJason(), copy.getPropTitleMappingJason()) &&
                condition.apply(criteria.getDataSourceMappingType(), copy.getDataSourceMappingType()) &&
                condition.apply(criteria.getTargetUrls(), copy.getTargetUrls()) &&
                condition.apply(criteria.getThumbnailId(), copy.getThumbnailId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
