package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceCriteriaTest {

    @Test
    void newNextInvoiceCriteriaHasAllFiltersNullTest() {
        var nextInvoiceCriteria = new NextInvoiceCriteria();
        assertThat(nextInvoiceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceCriteria = new NextInvoiceCriteria();

        setAllFilters(nextInvoiceCriteria);

        assertThat(nextInvoiceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceCriteria = new NextInvoiceCriteria();
        var copy = nextInvoiceCriteria.copy();

        assertThat(nextInvoiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceCriteria)
        );
    }

    @Test
    void nextInvoiceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceCriteria = new NextInvoiceCriteria();
        setAllFilters(nextInvoiceCriteria);

        var copy = nextInvoiceCriteria.copy();

        assertThat(nextInvoiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceCriteria = new NextInvoiceCriteria();

        assertThat(nextInvoiceCriteria).hasToString("NextInvoiceCriteria{}");
    }

    private static void setAllFilters(NextInvoiceCriteria nextInvoiceCriteria) {
        nextInvoiceCriteria.id();
        nextInvoiceCriteria.invoiceNumber();
        nextInvoiceCriteria.issueDate();
        nextInvoiceCriteria.dueDate();
        nextInvoiceCriteria.amount();
        nextInvoiceCriteria.tenantId();
        nextInvoiceCriteria.distinct();
    }

    private static Condition<NextInvoiceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextInvoiceCriteria> copyFiltersAre(NextInvoiceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
