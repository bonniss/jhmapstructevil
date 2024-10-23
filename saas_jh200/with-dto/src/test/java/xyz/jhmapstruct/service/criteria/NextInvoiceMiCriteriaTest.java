package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceMiCriteriaTest {

    @Test
    void newNextInvoiceMiCriteriaHasAllFiltersNullTest() {
        var nextInvoiceMiCriteria = new NextInvoiceMiCriteria();
        assertThat(nextInvoiceMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceMiCriteria = new NextInvoiceMiCriteria();

        setAllFilters(nextInvoiceMiCriteria);

        assertThat(nextInvoiceMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceMiCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceMiCriteria = new NextInvoiceMiCriteria();
        var copy = nextInvoiceMiCriteria.copy();

        assertThat(nextInvoiceMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceMiCriteria)
        );
    }

    @Test
    void nextInvoiceMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceMiCriteria = new NextInvoiceMiCriteria();
        setAllFilters(nextInvoiceMiCriteria);

        var copy = nextInvoiceMiCriteria.copy();

        assertThat(nextInvoiceMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceMiCriteria = new NextInvoiceMiCriteria();

        assertThat(nextInvoiceMiCriteria).hasToString("NextInvoiceMiCriteria{}");
    }

    private static void setAllFilters(NextInvoiceMiCriteria nextInvoiceMiCriteria) {
        nextInvoiceMiCriteria.id();
        nextInvoiceMiCriteria.invoiceNumber();
        nextInvoiceMiCriteria.issueDate();
        nextInvoiceMiCriteria.dueDate();
        nextInvoiceMiCriteria.amount();
        nextInvoiceMiCriteria.tenantId();
        nextInvoiceMiCriteria.distinct();
    }

    private static Condition<NextInvoiceMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextInvoiceMiCriteria> copyFiltersAre(
        NextInvoiceMiCriteria copy,
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
