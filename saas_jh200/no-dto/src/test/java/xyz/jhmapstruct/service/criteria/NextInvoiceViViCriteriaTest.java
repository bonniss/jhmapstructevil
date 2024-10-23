package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceViViCriteriaTest {

    @Test
    void newNextInvoiceViViCriteriaHasAllFiltersNullTest() {
        var nextInvoiceViViCriteria = new NextInvoiceViViCriteria();
        assertThat(nextInvoiceViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceViViCriteria = new NextInvoiceViViCriteria();

        setAllFilters(nextInvoiceViViCriteria);

        assertThat(nextInvoiceViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceViViCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceViViCriteria = new NextInvoiceViViCriteria();
        var copy = nextInvoiceViViCriteria.copy();

        assertThat(nextInvoiceViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceViViCriteria)
        );
    }

    @Test
    void nextInvoiceViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceViViCriteria = new NextInvoiceViViCriteria();
        setAllFilters(nextInvoiceViViCriteria);

        var copy = nextInvoiceViViCriteria.copy();

        assertThat(nextInvoiceViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceViViCriteria = new NextInvoiceViViCriteria();

        assertThat(nextInvoiceViViCriteria).hasToString("NextInvoiceViViCriteria{}");
    }

    private static void setAllFilters(NextInvoiceViViCriteria nextInvoiceViViCriteria) {
        nextInvoiceViViCriteria.id();
        nextInvoiceViViCriteria.invoiceNumber();
        nextInvoiceViViCriteria.issueDate();
        nextInvoiceViViCriteria.dueDate();
        nextInvoiceViViCriteria.amount();
        nextInvoiceViViCriteria.tenantId();
        nextInvoiceViViCriteria.distinct();
    }

    private static Condition<NextInvoiceViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getInvoiceNumber()) &&
                condition.apply(criteria.getIssueDate()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextInvoiceViViCriteria> copyFiltersAre(
        NextInvoiceViViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getInvoiceNumber(), copy.getInvoiceNumber()) &&
                condition.apply(criteria.getIssueDate(), copy.getIssueDate()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
