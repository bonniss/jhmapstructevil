package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceBetaCriteriaTest {

    @Test
    void newNextInvoiceBetaCriteriaHasAllFiltersNullTest() {
        var nextInvoiceBetaCriteria = new NextInvoiceBetaCriteria();
        assertThat(nextInvoiceBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceBetaCriteria = new NextInvoiceBetaCriteria();

        setAllFilters(nextInvoiceBetaCriteria);

        assertThat(nextInvoiceBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceBetaCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceBetaCriteria = new NextInvoiceBetaCriteria();
        var copy = nextInvoiceBetaCriteria.copy();

        assertThat(nextInvoiceBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceBetaCriteria)
        );
    }

    @Test
    void nextInvoiceBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceBetaCriteria = new NextInvoiceBetaCriteria();
        setAllFilters(nextInvoiceBetaCriteria);

        var copy = nextInvoiceBetaCriteria.copy();

        assertThat(nextInvoiceBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceBetaCriteria = new NextInvoiceBetaCriteria();

        assertThat(nextInvoiceBetaCriteria).hasToString("NextInvoiceBetaCriteria{}");
    }

    private static void setAllFilters(NextInvoiceBetaCriteria nextInvoiceBetaCriteria) {
        nextInvoiceBetaCriteria.id();
        nextInvoiceBetaCriteria.invoiceNumber();
        nextInvoiceBetaCriteria.issueDate();
        nextInvoiceBetaCriteria.dueDate();
        nextInvoiceBetaCriteria.amount();
        nextInvoiceBetaCriteria.tenantId();
        nextInvoiceBetaCriteria.distinct();
    }

    private static Condition<NextInvoiceBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextInvoiceBetaCriteria> copyFiltersAre(
        NextInvoiceBetaCriteria copy,
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
