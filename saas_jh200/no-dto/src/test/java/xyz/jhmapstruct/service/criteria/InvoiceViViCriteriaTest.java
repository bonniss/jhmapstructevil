package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceViViCriteriaTest {

    @Test
    void newInvoiceViViCriteriaHasAllFiltersNullTest() {
        var invoiceViViCriteria = new InvoiceViViCriteria();
        assertThat(invoiceViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void invoiceViViCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceViViCriteria = new InvoiceViViCriteria();

        setAllFilters(invoiceViViCriteria);

        assertThat(invoiceViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void invoiceViViCriteriaCopyCreatesNullFilterTest() {
        var invoiceViViCriteria = new InvoiceViViCriteria();
        var copy = invoiceViViCriteria.copy();

        assertThat(invoiceViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceViViCriteria)
        );
    }

    @Test
    void invoiceViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceViViCriteria = new InvoiceViViCriteria();
        setAllFilters(invoiceViViCriteria);

        var copy = invoiceViViCriteria.copy();

        assertThat(invoiceViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(invoiceViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceViViCriteria = new InvoiceViViCriteria();

        assertThat(invoiceViViCriteria).hasToString("InvoiceViViCriteria{}");
    }

    private static void setAllFilters(InvoiceViViCriteria invoiceViViCriteria) {
        invoiceViViCriteria.id();
        invoiceViViCriteria.invoiceNumber();
        invoiceViViCriteria.issueDate();
        invoiceViViCriteria.dueDate();
        invoiceViViCriteria.amount();
        invoiceViViCriteria.tenantId();
        invoiceViViCriteria.distinct();
    }

    private static Condition<InvoiceViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<InvoiceViViCriteria> copyFiltersAre(InvoiceViViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
