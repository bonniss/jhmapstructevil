package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceMiCriteriaTest {

    @Test
    void newInvoiceMiCriteriaHasAllFiltersNullTest() {
        var invoiceMiCriteria = new InvoiceMiCriteria();
        assertThat(invoiceMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceMiCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceMiCriteria = new InvoiceMiCriteria();

        setAllFilters(invoiceMiCriteria);

        assertThat(invoiceMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceMiCriteriaCopyCreatesNullFilterTest() {
        var invoiceMiCriteria = new InvoiceMiCriteria();
        var copy = invoiceMiCriteria.copy();

        assertThat(invoiceMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceMiCriteria)
        );
    }

    @Test
    void invoiceMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceMiCriteria = new InvoiceMiCriteria();
        setAllFilters(invoiceMiCriteria);

        var copy = invoiceMiCriteria.copy();

        assertThat(invoiceMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceMiCriteria = new InvoiceMiCriteria();

        assertThat(invoiceMiCriteria).hasToString("InvoiceMiCriteria{}");
    }

    private static void setAllFilters(InvoiceMiCriteria invoiceMiCriteria) {
        invoiceMiCriteria.id();
        invoiceMiCriteria.invoiceNumber();
        invoiceMiCriteria.issueDate();
        invoiceMiCriteria.dueDate();
        invoiceMiCriteria.amount();
        invoiceMiCriteria.tenantId();
        invoiceMiCriteria.distinct();
    }

    private static Condition<InvoiceMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceMiCriteria> copyFiltersAre(InvoiceMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
