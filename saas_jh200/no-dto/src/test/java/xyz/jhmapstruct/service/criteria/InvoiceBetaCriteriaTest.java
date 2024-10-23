package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceBetaCriteriaTest {

    @Test
    void newInvoiceBetaCriteriaHasAllFiltersNullTest() {
        var invoiceBetaCriteria = new InvoiceBetaCriteria();
        assertThat(invoiceBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceBetaCriteria = new InvoiceBetaCriteria();

        setAllFilters(invoiceBetaCriteria);

        assertThat(invoiceBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceBetaCriteriaCopyCreatesNullFilterTest() {
        var invoiceBetaCriteria = new InvoiceBetaCriteria();
        var copy = invoiceBetaCriteria.copy();

        assertThat(invoiceBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceBetaCriteria)
        );
    }

    @Test
    void invoiceBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceBetaCriteria = new InvoiceBetaCriteria();
        setAllFilters(invoiceBetaCriteria);

        var copy = invoiceBetaCriteria.copy();

        assertThat(invoiceBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceBetaCriteria = new InvoiceBetaCriteria();

        assertThat(invoiceBetaCriteria).hasToString("InvoiceBetaCriteria{}");
    }

    private static void setAllFilters(InvoiceBetaCriteria invoiceBetaCriteria) {
        invoiceBetaCriteria.id();
        invoiceBetaCriteria.invoiceNumber();
        invoiceBetaCriteria.issueDate();
        invoiceBetaCriteria.dueDate();
        invoiceBetaCriteria.amount();
        invoiceBetaCriteria.tenantId();
        invoiceBetaCriteria.distinct();
    }

    private static Condition<InvoiceBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceBetaCriteria> copyFiltersAre(InvoiceBetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
