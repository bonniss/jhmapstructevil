package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextInvoiceAlphaCriteriaTest {

    @Test
    void newNextInvoiceAlphaCriteriaHasAllFiltersNullTest() {
        var nextInvoiceAlphaCriteria = new NextInvoiceAlphaCriteria();
        assertThat(nextInvoiceAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextInvoiceAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextInvoiceAlphaCriteria = new NextInvoiceAlphaCriteria();

        setAllFilters(nextInvoiceAlphaCriteria);

        assertThat(nextInvoiceAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextInvoiceAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextInvoiceAlphaCriteria = new NextInvoiceAlphaCriteria();
        var copy = nextInvoiceAlphaCriteria.copy();

        assertThat(nextInvoiceAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceAlphaCriteria)
        );
    }

    @Test
    void nextInvoiceAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextInvoiceAlphaCriteria = new NextInvoiceAlphaCriteria();
        setAllFilters(nextInvoiceAlphaCriteria);

        var copy = nextInvoiceAlphaCriteria.copy();

        assertThat(nextInvoiceAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextInvoiceAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextInvoiceAlphaCriteria = new NextInvoiceAlphaCriteria();

        assertThat(nextInvoiceAlphaCriteria).hasToString("NextInvoiceAlphaCriteria{}");
    }

    private static void setAllFilters(NextInvoiceAlphaCriteria nextInvoiceAlphaCriteria) {
        nextInvoiceAlphaCriteria.id();
        nextInvoiceAlphaCriteria.invoiceNumber();
        nextInvoiceAlphaCriteria.issueDate();
        nextInvoiceAlphaCriteria.dueDate();
        nextInvoiceAlphaCriteria.amount();
        nextInvoiceAlphaCriteria.tenantId();
        nextInvoiceAlphaCriteria.distinct();
    }

    private static Condition<NextInvoiceAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextInvoiceAlphaCriteria> copyFiltersAre(
        NextInvoiceAlphaCriteria copy,
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
