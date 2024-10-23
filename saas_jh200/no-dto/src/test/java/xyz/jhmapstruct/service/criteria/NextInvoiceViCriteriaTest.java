package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceViCriteriaTest {

    @Test
    void newNextInvoiceViCriteriaHasAllFiltersNullTest() {
        var nextInvoiceViCriteria = new NextInvoiceViCriteria();
        assertThat(nextInvoiceViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceViCriteria = new NextInvoiceViCriteria();

        setAllFilters(nextInvoiceViCriteria);

        assertThat(nextInvoiceViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceViCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceViCriteria = new NextInvoiceViCriteria();
        var copy = nextInvoiceViCriteria.copy();

        assertThat(nextInvoiceViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceViCriteria)
        );
    }

    @Test
    void nextInvoiceViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceViCriteria = new NextInvoiceViCriteria();
        setAllFilters(nextInvoiceViCriteria);

        var copy = nextInvoiceViCriteria.copy();

        assertThat(nextInvoiceViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceViCriteria = new NextInvoiceViCriteria();

        assertThat(nextInvoiceViCriteria).hasToString("NextInvoiceViCriteria{}");
    }

    private static void setAllFilters(NextInvoiceViCriteria nextInvoiceViCriteria) {
        nextInvoiceViCriteria.id();
        nextInvoiceViCriteria.invoiceNumber();
        nextInvoiceViCriteria.issueDate();
        nextInvoiceViCriteria.dueDate();
        nextInvoiceViCriteria.amount();
        nextInvoiceViCriteria.tenantId();
        nextInvoiceViCriteria.distinct();
    }

    private static Condition<NextInvoiceViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextInvoiceViCriteria> copyFiltersAre(
        NextInvoiceViCriteria copy,
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
