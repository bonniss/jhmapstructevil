package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import ai.realworld.domain.enumeration.PaoloStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlVueVue} entity. This class is used
 * in {@link ai.realworld.web.rest.AlVueVueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-vue-vues?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AlcountTypo
     */
    public static class AlcountTypoFilter extends Filter<AlcountTypo> {

        public AlcountTypoFilter() {}

        public AlcountTypoFilter(AlcountTypoFilter filter) {
            super(filter);
        }

        @Override
        public AlcountTypoFilter copy() {
            return new AlcountTypoFilter(this);
        }
    }

    /**
     * Class for filtering AlcountScopy
     */
    public static class AlcountScopyFilter extends Filter<AlcountScopy> {

        public AlcountScopyFilter() {}

        public AlcountScopyFilter(AlcountScopyFilter filter) {
            super(filter);
        }

        @Override
        public AlcountScopyFilter copy() {
            return new AlcountScopyFilter(this);
        }
    }

    /**
     * Class for filtering PaoloStatus
     */
    public static class PaoloStatusFilter extends Filter<PaoloStatus> {

        public PaoloStatusFilter() {}

        public PaoloStatusFilter(PaoloStatusFilter filter) {
            super(filter);
        }

        @Override
        public PaoloStatusFilter copy() {
            return new PaoloStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter contentHeitiga;

    private AlcountTypoFilter discountType;

    private BigDecimalFilter discountRate;

    private AlcountScopyFilter scope;

    private BooleanFilter isIndividuallyUsedOnly;

    private IntegerFilter usageLifeTimeLimit;

    private IntegerFilter usageLimitPerUser;

    private IntegerFilter usageQuantity;

    private BigDecimalFilter minimumSpend;

    private BigDecimalFilter maximumSpend;

    private BooleanFilter canBeCollectedByUser;

    private LocalDateFilter salePriceFromDate;

    private LocalDateFilter salePriceToDate;

    private PaoloStatusFilter publicationStatus;

    private InstantFilter publishedDate;

    private LongFilter imageId;

    private UUIDFilter alVueVueUsageId;

    private UUIDFilter applicationId;

    private LongFilter conditionsId;

    private Boolean distinct;

    public AlVueVueCriteria() {}

    public AlVueVueCriteria(AlVueVueCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.contentHeitiga = other.optionalContentHeitiga().map(StringFilter::copy).orElse(null);
        this.discountType = other.optionalDiscountType().map(AlcountTypoFilter::copy).orElse(null);
        this.discountRate = other.optionalDiscountRate().map(BigDecimalFilter::copy).orElse(null);
        this.scope = other.optionalScope().map(AlcountScopyFilter::copy).orElse(null);
        this.isIndividuallyUsedOnly = other.optionalIsIndividuallyUsedOnly().map(BooleanFilter::copy).orElse(null);
        this.usageLifeTimeLimit = other.optionalUsageLifeTimeLimit().map(IntegerFilter::copy).orElse(null);
        this.usageLimitPerUser = other.optionalUsageLimitPerUser().map(IntegerFilter::copy).orElse(null);
        this.usageQuantity = other.optionalUsageQuantity().map(IntegerFilter::copy).orElse(null);
        this.minimumSpend = other.optionalMinimumSpend().map(BigDecimalFilter::copy).orElse(null);
        this.maximumSpend = other.optionalMaximumSpend().map(BigDecimalFilter::copy).orElse(null);
        this.canBeCollectedByUser = other.optionalCanBeCollectedByUser().map(BooleanFilter::copy).orElse(null);
        this.salePriceFromDate = other.optionalSalePriceFromDate().map(LocalDateFilter::copy).orElse(null);
        this.salePriceToDate = other.optionalSalePriceToDate().map(LocalDateFilter::copy).orElse(null);
        this.publicationStatus = other.optionalPublicationStatus().map(PaoloStatusFilter::copy).orElse(null);
        this.publishedDate = other.optionalPublishedDate().map(InstantFilter::copy).orElse(null);
        this.imageId = other.optionalImageId().map(LongFilter::copy).orElse(null);
        this.alVueVueUsageId = other.optionalAlVueVueUsageId().map(UUIDFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.conditionsId = other.optionalConditionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlVueVueCriteria copy() {
        return new AlVueVueCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public StringFilter getContentHeitiga() {
        return contentHeitiga;
    }

    public Optional<StringFilter> optionalContentHeitiga() {
        return Optional.ofNullable(contentHeitiga);
    }

    public StringFilter contentHeitiga() {
        if (contentHeitiga == null) {
            setContentHeitiga(new StringFilter());
        }
        return contentHeitiga;
    }

    public void setContentHeitiga(StringFilter contentHeitiga) {
        this.contentHeitiga = contentHeitiga;
    }

    public AlcountTypoFilter getDiscountType() {
        return discountType;
    }

    public Optional<AlcountTypoFilter> optionalDiscountType() {
        return Optional.ofNullable(discountType);
    }

    public AlcountTypoFilter discountType() {
        if (discountType == null) {
            setDiscountType(new AlcountTypoFilter());
        }
        return discountType;
    }

    public void setDiscountType(AlcountTypoFilter discountType) {
        this.discountType = discountType;
    }

    public BigDecimalFilter getDiscountRate() {
        return discountRate;
    }

    public Optional<BigDecimalFilter> optionalDiscountRate() {
        return Optional.ofNullable(discountRate);
    }

    public BigDecimalFilter discountRate() {
        if (discountRate == null) {
            setDiscountRate(new BigDecimalFilter());
        }
        return discountRate;
    }

    public void setDiscountRate(BigDecimalFilter discountRate) {
        this.discountRate = discountRate;
    }

    public AlcountScopyFilter getScope() {
        return scope;
    }

    public Optional<AlcountScopyFilter> optionalScope() {
        return Optional.ofNullable(scope);
    }

    public AlcountScopyFilter scope() {
        if (scope == null) {
            setScope(new AlcountScopyFilter());
        }
        return scope;
    }

    public void setScope(AlcountScopyFilter scope) {
        this.scope = scope;
    }

    public BooleanFilter getIsIndividuallyUsedOnly() {
        return isIndividuallyUsedOnly;
    }

    public Optional<BooleanFilter> optionalIsIndividuallyUsedOnly() {
        return Optional.ofNullable(isIndividuallyUsedOnly);
    }

    public BooleanFilter isIndividuallyUsedOnly() {
        if (isIndividuallyUsedOnly == null) {
            setIsIndividuallyUsedOnly(new BooleanFilter());
        }
        return isIndividuallyUsedOnly;
    }

    public void setIsIndividuallyUsedOnly(BooleanFilter isIndividuallyUsedOnly) {
        this.isIndividuallyUsedOnly = isIndividuallyUsedOnly;
    }

    public IntegerFilter getUsageLifeTimeLimit() {
        return usageLifeTimeLimit;
    }

    public Optional<IntegerFilter> optionalUsageLifeTimeLimit() {
        return Optional.ofNullable(usageLifeTimeLimit);
    }

    public IntegerFilter usageLifeTimeLimit() {
        if (usageLifeTimeLimit == null) {
            setUsageLifeTimeLimit(new IntegerFilter());
        }
        return usageLifeTimeLimit;
    }

    public void setUsageLifeTimeLimit(IntegerFilter usageLifeTimeLimit) {
        this.usageLifeTimeLimit = usageLifeTimeLimit;
    }

    public IntegerFilter getUsageLimitPerUser() {
        return usageLimitPerUser;
    }

    public Optional<IntegerFilter> optionalUsageLimitPerUser() {
        return Optional.ofNullable(usageLimitPerUser);
    }

    public IntegerFilter usageLimitPerUser() {
        if (usageLimitPerUser == null) {
            setUsageLimitPerUser(new IntegerFilter());
        }
        return usageLimitPerUser;
    }

    public void setUsageLimitPerUser(IntegerFilter usageLimitPerUser) {
        this.usageLimitPerUser = usageLimitPerUser;
    }

    public IntegerFilter getUsageQuantity() {
        return usageQuantity;
    }

    public Optional<IntegerFilter> optionalUsageQuantity() {
        return Optional.ofNullable(usageQuantity);
    }

    public IntegerFilter usageQuantity() {
        if (usageQuantity == null) {
            setUsageQuantity(new IntegerFilter());
        }
        return usageQuantity;
    }

    public void setUsageQuantity(IntegerFilter usageQuantity) {
        this.usageQuantity = usageQuantity;
    }

    public BigDecimalFilter getMinimumSpend() {
        return minimumSpend;
    }

    public Optional<BigDecimalFilter> optionalMinimumSpend() {
        return Optional.ofNullable(minimumSpend);
    }

    public BigDecimalFilter minimumSpend() {
        if (minimumSpend == null) {
            setMinimumSpend(new BigDecimalFilter());
        }
        return minimumSpend;
    }

    public void setMinimumSpend(BigDecimalFilter minimumSpend) {
        this.minimumSpend = minimumSpend;
    }

    public BigDecimalFilter getMaximumSpend() {
        return maximumSpend;
    }

    public Optional<BigDecimalFilter> optionalMaximumSpend() {
        return Optional.ofNullable(maximumSpend);
    }

    public BigDecimalFilter maximumSpend() {
        if (maximumSpend == null) {
            setMaximumSpend(new BigDecimalFilter());
        }
        return maximumSpend;
    }

    public void setMaximumSpend(BigDecimalFilter maximumSpend) {
        this.maximumSpend = maximumSpend;
    }

    public BooleanFilter getCanBeCollectedByUser() {
        return canBeCollectedByUser;
    }

    public Optional<BooleanFilter> optionalCanBeCollectedByUser() {
        return Optional.ofNullable(canBeCollectedByUser);
    }

    public BooleanFilter canBeCollectedByUser() {
        if (canBeCollectedByUser == null) {
            setCanBeCollectedByUser(new BooleanFilter());
        }
        return canBeCollectedByUser;
    }

    public void setCanBeCollectedByUser(BooleanFilter canBeCollectedByUser) {
        this.canBeCollectedByUser = canBeCollectedByUser;
    }

    public LocalDateFilter getSalePriceFromDate() {
        return salePriceFromDate;
    }

    public Optional<LocalDateFilter> optionalSalePriceFromDate() {
        return Optional.ofNullable(salePriceFromDate);
    }

    public LocalDateFilter salePriceFromDate() {
        if (salePriceFromDate == null) {
            setSalePriceFromDate(new LocalDateFilter());
        }
        return salePriceFromDate;
    }

    public void setSalePriceFromDate(LocalDateFilter salePriceFromDate) {
        this.salePriceFromDate = salePriceFromDate;
    }

    public LocalDateFilter getSalePriceToDate() {
        return salePriceToDate;
    }

    public Optional<LocalDateFilter> optionalSalePriceToDate() {
        return Optional.ofNullable(salePriceToDate);
    }

    public LocalDateFilter salePriceToDate() {
        if (salePriceToDate == null) {
            setSalePriceToDate(new LocalDateFilter());
        }
        return salePriceToDate;
    }

    public void setSalePriceToDate(LocalDateFilter salePriceToDate) {
        this.salePriceToDate = salePriceToDate;
    }

    public PaoloStatusFilter getPublicationStatus() {
        return publicationStatus;
    }

    public Optional<PaoloStatusFilter> optionalPublicationStatus() {
        return Optional.ofNullable(publicationStatus);
    }

    public PaoloStatusFilter publicationStatus() {
        if (publicationStatus == null) {
            setPublicationStatus(new PaoloStatusFilter());
        }
        return publicationStatus;
    }

    public void setPublicationStatus(PaoloStatusFilter publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public InstantFilter getPublishedDate() {
        return publishedDate;
    }

    public Optional<InstantFilter> optionalPublishedDate() {
        return Optional.ofNullable(publishedDate);
    }

    public InstantFilter publishedDate() {
        if (publishedDate == null) {
            setPublishedDate(new InstantFilter());
        }
        return publishedDate;
    }

    public void setPublishedDate(InstantFilter publishedDate) {
        this.publishedDate = publishedDate;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public Optional<LongFilter> optionalImageId() {
        return Optional.ofNullable(imageId);
    }

    public LongFilter imageId() {
        if (imageId == null) {
            setImageId(new LongFilter());
        }
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    public UUIDFilter getAlVueVueUsageId() {
        return alVueVueUsageId;
    }

    public Optional<UUIDFilter> optionalAlVueVueUsageId() {
        return Optional.ofNullable(alVueVueUsageId);
    }

    public UUIDFilter alVueVueUsageId() {
        if (alVueVueUsageId == null) {
            setAlVueVueUsageId(new UUIDFilter());
        }
        return alVueVueUsageId;
    }

    public void setAlVueVueUsageId(UUIDFilter alVueVueUsageId) {
        this.alVueVueUsageId = alVueVueUsageId;
    }

    public UUIDFilter getApplicationId() {
        return applicationId;
    }

    public Optional<UUIDFilter> optionalApplicationId() {
        return Optional.ofNullable(applicationId);
    }

    public UUIDFilter applicationId() {
        if (applicationId == null) {
            setApplicationId(new UUIDFilter());
        }
        return applicationId;
    }

    public void setApplicationId(UUIDFilter applicationId) {
        this.applicationId = applicationId;
    }

    public LongFilter getConditionsId() {
        return conditionsId;
    }

    public Optional<LongFilter> optionalConditionsId() {
        return Optional.ofNullable(conditionsId);
    }

    public LongFilter conditionsId() {
        if (conditionsId == null) {
            setConditionsId(new LongFilter());
        }
        return conditionsId;
    }

    public void setConditionsId(LongFilter conditionsId) {
        this.conditionsId = conditionsId;
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
        final AlVueVueCriteria that = (AlVueVueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(contentHeitiga, that.contentHeitiga) &&
            Objects.equals(discountType, that.discountType) &&
            Objects.equals(discountRate, that.discountRate) &&
            Objects.equals(scope, that.scope) &&
            Objects.equals(isIndividuallyUsedOnly, that.isIndividuallyUsedOnly) &&
            Objects.equals(usageLifeTimeLimit, that.usageLifeTimeLimit) &&
            Objects.equals(usageLimitPerUser, that.usageLimitPerUser) &&
            Objects.equals(usageQuantity, that.usageQuantity) &&
            Objects.equals(minimumSpend, that.minimumSpend) &&
            Objects.equals(maximumSpend, that.maximumSpend) &&
            Objects.equals(canBeCollectedByUser, that.canBeCollectedByUser) &&
            Objects.equals(salePriceFromDate, that.salePriceFromDate) &&
            Objects.equals(salePriceToDate, that.salePriceToDate) &&
            Objects.equals(publicationStatus, that.publicationStatus) &&
            Objects.equals(publishedDate, that.publishedDate) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(alVueVueUsageId, that.alVueVueUsageId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(conditionsId, that.conditionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            contentHeitiga,
            discountType,
            discountRate,
            scope,
            isIndividuallyUsedOnly,
            usageLifeTimeLimit,
            usageLimitPerUser,
            usageQuantity,
            minimumSpend,
            maximumSpend,
            canBeCollectedByUser,
            salePriceFromDate,
            salePriceToDate,
            publicationStatus,
            publishedDate,
            imageId,
            alVueVueUsageId,
            applicationId,
            conditionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalContentHeitiga().map(f -> "contentHeitiga=" + f + ", ").orElse("") +
            optionalDiscountType().map(f -> "discountType=" + f + ", ").orElse("") +
            optionalDiscountRate().map(f -> "discountRate=" + f + ", ").orElse("") +
            optionalScope().map(f -> "scope=" + f + ", ").orElse("") +
            optionalIsIndividuallyUsedOnly().map(f -> "isIndividuallyUsedOnly=" + f + ", ").orElse("") +
            optionalUsageLifeTimeLimit().map(f -> "usageLifeTimeLimit=" + f + ", ").orElse("") +
            optionalUsageLimitPerUser().map(f -> "usageLimitPerUser=" + f + ", ").orElse("") +
            optionalUsageQuantity().map(f -> "usageQuantity=" + f + ", ").orElse("") +
            optionalMinimumSpend().map(f -> "minimumSpend=" + f + ", ").orElse("") +
            optionalMaximumSpend().map(f -> "maximumSpend=" + f + ", ").orElse("") +
            optionalCanBeCollectedByUser().map(f -> "canBeCollectedByUser=" + f + ", ").orElse("") +
            optionalSalePriceFromDate().map(f -> "salePriceFromDate=" + f + ", ").orElse("") +
            optionalSalePriceToDate().map(f -> "salePriceToDate=" + f + ", ").orElse("") +
            optionalPublicationStatus().map(f -> "publicationStatus=" + f + ", ").orElse("") +
            optionalPublishedDate().map(f -> "publishedDate=" + f + ", ").orElse("") +
            optionalImageId().map(f -> "imageId=" + f + ", ").orElse("") +
            optionalAlVueVueUsageId().map(f -> "alVueVueUsageId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalConditionsId().map(f -> "conditionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
