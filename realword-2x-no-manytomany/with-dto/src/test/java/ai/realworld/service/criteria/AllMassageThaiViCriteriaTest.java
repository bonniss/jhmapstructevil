package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AllMassageThaiViCriteriaTest {

    @Test
    void newAllMassageThaiViCriteriaHasAllFiltersNullTest() {
        var allMassageThaiViCriteria = new AllMassageThaiViCriteria();
        assertThat(allMassageThaiViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void allMassageThaiViCriteriaFluentMethodsCreatesFiltersTest() {
        var allMassageThaiViCriteria = new AllMassageThaiViCriteria();

        setAllFilters(allMassageThaiViCriteria);

        assertThat(allMassageThaiViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void allMassageThaiViCriteriaCopyCreatesNullFilterTest() {
        var allMassageThaiViCriteria = new AllMassageThaiViCriteria();
        var copy = allMassageThaiViCriteria.copy();

        assertThat(allMassageThaiViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(allMassageThaiViCriteria)
        );
    }

    @Test
    void allMassageThaiViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var allMassageThaiViCriteria = new AllMassageThaiViCriteria();
        setAllFilters(allMassageThaiViCriteria);

        var copy = allMassageThaiViCriteria.copy();

        assertThat(allMassageThaiViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(allMassageThaiViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var allMassageThaiViCriteria = new AllMassageThaiViCriteria();

        assertThat(allMassageThaiViCriteria).hasToString("AllMassageThaiViCriteria{}");
    }

    private static void setAllFilters(AllMassageThaiViCriteria allMassageThaiViCriteria) {
        allMassageThaiViCriteria.id();
        allMassageThaiViCriteria.title();
        allMassageThaiViCriteria.topContent();
        allMassageThaiViCriteria.content();
        allMassageThaiViCriteria.bottomContent();
        allMassageThaiViCriteria.propTitleMappingJason();
        allMassageThaiViCriteria.dataSourceMappingType();
        allMassageThaiViCriteria.targetUrls();
        allMassageThaiViCriteria.thumbnailId();
        allMassageThaiViCriteria.applicationId();
        allMassageThaiViCriteria.distinct();
    }

    private static Condition<AllMassageThaiViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AllMassageThaiViCriteria> copyFiltersAre(
        AllMassageThaiViCriteria copy,
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
