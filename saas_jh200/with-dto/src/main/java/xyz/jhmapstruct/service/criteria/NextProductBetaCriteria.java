package xyz.jhmapstruct.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link xyz.jhmapstruct.domain.NextProductBeta} entity. This class is used
 * in {@link xyz.jhmapstruct.web.rest.NextProductBetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /next-product-betas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NextProductBetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BigDecimalFilter price;

    private IntegerFilter stock;

    private LongFilter categoryId;

    private LongFilter tenantId;

    private LongFilter orderId;

    private LongFilter suppliersId;

    private Boolean distinct;

    public NextProductBetaCriteria() {}

    public NextProductBetaCriteria(NextProductBetaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.stock = other.optionalStock().map(IntegerFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(LongFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(LongFilter::copy).orElse(null);
        this.suppliersId = other.optionalSuppliersId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NextProductBetaCriteria copy() {
        return new NextProductBetaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public IntegerFilter getStock() {
        return stock;
    }

    public Optional<IntegerFilter> optionalStock() {
        return Optional.ofNullable(stock);
    }

    public IntegerFilter stock() {
        if (stock == null) {
            setStock(new IntegerFilter());
        }
        return stock;
    }

    public void setStock(IntegerFilter stock) {
        this.stock = stock;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public Optional<LongFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public LongFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new LongFilter());
        }
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public Optional<LongFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public LongFilter orderId() {
        if (orderId == null) {
            setOrderId(new LongFilter());
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public LongFilter getSuppliersId() {
        return suppliersId;
    }

    public Optional<LongFilter> optionalSuppliersId() {
        return Optional.ofNullable(suppliersId);
    }

    public LongFilter suppliersId() {
        if (suppliersId == null) {
            setSuppliersId(new LongFilter());
        }
        return suppliersId;
    }

    public void setSuppliersId(LongFilter suppliersId) {
        this.suppliersId = suppliersId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NextProductBetaCriteria that = (NextProductBetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(suppliersId, that.suppliersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, stock, categoryId, tenantId, orderId, suppliersId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextProductBetaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalStock().map(f -> "stock=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalSuppliersId().map(f -> "suppliersId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
