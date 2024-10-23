package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceViCriteriaTest {

    @Test
    void newInvoiceViCriteriaHasAllFiltersNullTest() {
        var invoiceViCriteria = new InvoiceViCriteria();
        assertThat(invoiceViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceViCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceViCriteria = new InvoiceViCriteria();

        setAllFilters(invoiceViCriteria);

        assertThat(invoiceViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceViCriteriaCopyCreatesNullFilterTest() {
        var invoiceViCriteria = new InvoiceViCriteria();
        var copy = invoiceViCriteria.copy();

        assertThat(invoiceViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceViCriteria)
        );
    }

    @Test
    void invoiceViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceViCriteria = new InvoiceViCriteria();
        setAllFilters(invoiceViCriteria);

        var copy = invoiceViCriteria.copy();

        assertThat(invoiceViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceViCriteria = new InvoiceViCriteria();

        assertThat(invoiceViCriteria).hasToString("InvoiceViCriteria{}");
    }

    private static void setAllFilters(InvoiceViCriteria invoiceViCriteria) {
        invoiceViCriteria.id();
        invoiceViCriteria.invoiceNumber();
        invoiceViCriteria.issueDate();
        invoiceViCriteria.dueDate();
        invoiceViCriteria.amount();
        invoiceViCriteria.tenantId();
        invoiceViCriteria.distinct();
    }

    private static Condition<InvoiceViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceViCriteria> copyFiltersAre(InvoiceViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
