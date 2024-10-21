package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AllMassageThaiCriteriaTest {

    @Test
    void newAllMassageThaiCriteriaHasAllFiltersNullTest() {
        var allMassageThaiCriteria = new AllMassageThaiCriteria();
        assertThat(allMassageThaiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void allMassageThaiCriteriaFluentMethodsCreatesFiltersTest() {
        var allMassageThaiCriteria = new AllMassageThaiCriteria();

        setAllFilters(allMassageThaiCriteria);

        assertThat(allMassageThaiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void allMassageThaiCriteriaCopyCreatesNullFilterTest() {
        var allMassageThaiCriteria = new AllMassageThaiCriteria();
        var copy = allMassageThaiCriteria.copy();

        assertThat(allMassageThaiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(allMassageThaiCriteria)
        );
    }

    @Test
    void allMassageThaiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var allMassageThaiCriteria = new AllMassageThaiCriteria();
        setAllFilters(allMassageThaiCriteria);

        var copy = allMassageThaiCriteria.copy();

        assertThat(allMassageThaiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(allMassageThaiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var allMassageThaiCriteria = new AllMassageThaiCriteria();

        assertThat(allMassageThaiCriteria).hasToString("AllMassageThaiCriteria{}");
    }

    private static void setAllFilters(AllMassageThaiCriteria allMassageThaiCriteria) {
        allMassageThaiCriteria.id();
        allMassageThaiCriteria.title();
        allMassageThaiCriteria.topContent();
        allMassageThaiCriteria.content();
        allMassageThaiCriteria.bottomContent();
        allMassageThaiCriteria.propTitleMappingJason();
        allMassageThaiCriteria.dataSourceMappingType();
        allMassageThaiCriteria.targetUrls();
        allMassageThaiCriteria.thumbnailId();
        allMassageThaiCriteria.applicationId();
        allMassageThaiCriteria.distinct();
    }

    private static Condition<AllMassageThaiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AllMassageThaiCriteria> copyFiltersAre(
        AllMassageThaiCriteria copy,
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
