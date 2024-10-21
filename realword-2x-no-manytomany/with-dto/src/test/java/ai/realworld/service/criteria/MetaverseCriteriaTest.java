package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetaverseCriteriaTest {

    @Test
    void newMetaverseCriteriaHasAllFiltersNullTest() {
        var metaverseCriteria = new MetaverseCriteria();
        assertThat(metaverseCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void metaverseCriteriaFluentMethodsCreatesFiltersTest() {
        var metaverseCriteria = new MetaverseCriteria();

        setAllFilters(metaverseCriteria);

        assertThat(metaverseCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void metaverseCriteriaCopyCreatesNullFilterTest() {
        var metaverseCriteria = new MetaverseCriteria();
        var copy = metaverseCriteria.copy();

        assertThat(metaverseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(metaverseCriteria)
        );
    }

    @Test
    void metaverseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metaverseCriteria = new MetaverseCriteria();
        setAllFilters(metaverseCriteria);

        var copy = metaverseCriteria.copy();

        assertThat(metaverseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(metaverseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metaverseCriteria = new MetaverseCriteria();

        assertThat(metaverseCriteria).hasToString("MetaverseCriteria{}");
    }

    private static void setAllFilters(MetaverseCriteria metaverseCriteria) {
        metaverseCriteria.id();
        metaverseCriteria.filename();
        metaverseCriteria.contentType();
        metaverseCriteria.fileExt();
        metaverseCriteria.fileSize();
        metaverseCriteria.fileUrl();
        metaverseCriteria.thumbnailUrl();
        metaverseCriteria.blurhash();
        metaverseCriteria.objectName();
        metaverseCriteria.objectMetaJason();
        metaverseCriteria.urlLifespanInSeconds();
        metaverseCriteria.urlExpiredDate();
        metaverseCriteria.autoRenewUrl();
        metaverseCriteria.isEnabled();
        metaverseCriteria.distinct();
    }

    private static Condition<MetaverseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFilename()) &&
                condition.apply(criteria.getContentType()) &&
                condition.apply(criteria.getFileExt()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getFileUrl()) &&
                condition.apply(criteria.getThumbnailUrl()) &&
                condition.apply(criteria.getBlurhash()) &&
                condition.apply(criteria.getObjectName()) &&
                condition.apply(criteria.getObjectMetaJason()) &&
                condition.apply(criteria.getUrlLifespanInSeconds()) &&
                condition.apply(criteria.getUrlExpiredDate()) &&
                condition.apply(criteria.getAutoRenewUrl()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MetaverseCriteria> copyFiltersAre(MetaverseCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFilename(), copy.getFilename()) &&
                condition.apply(criteria.getContentType(), copy.getContentType()) &&
                condition.apply(criteria.getFileExt(), copy.getFileExt()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getFileUrl(), copy.getFileUrl()) &&
                condition.apply(criteria.getThumbnailUrl(), copy.getThumbnailUrl()) &&
                condition.apply(criteria.getBlurhash(), copy.getBlurhash()) &&
                condition.apply(criteria.getObjectName(), copy.getObjectName()) &&
                condition.apply(criteria.getObjectMetaJason(), copy.getObjectMetaJason()) &&
                condition.apply(criteria.getUrlLifespanInSeconds(), copy.getUrlLifespanInSeconds()) &&
                condition.apply(criteria.getUrlExpiredDate(), copy.getUrlExpiredDate()) &&
                condition.apply(criteria.getAutoRenewUrl(), copy.getAutoRenewUrl()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
