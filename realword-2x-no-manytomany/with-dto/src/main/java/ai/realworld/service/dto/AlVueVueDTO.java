package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import ai.realworld.domain.enumeration.PaoloStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlVueVue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueDTO implements Serializable {

    private UUID id;

    private String code;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 10485760)
    private String contentHeitiga;

    private AlcountTypo discountType;

    private BigDecimal discountRate;

    private AlcountScopy scope;

    private Boolean isIndividuallyUsedOnly;

    private Integer usageLifeTimeLimit;

    private Integer usageLimitPerUser;

    private Integer usageQuantity;

    private BigDecimal minimumSpend;

    private BigDecimal maximumSpend;

    private Boolean canBeCollectedByUser;

    private LocalDate salePriceFromDate;

    private LocalDate salePriceToDate;

    private PaoloStatus publicationStatus;

    private Instant publishedDate;

    private MetaverseDTO image;

    private AlVueVueUsageDTO alVueVueUsage;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentHeitiga() {
        return contentHeitiga;
    }

    public void setContentHeitiga(String contentHeitiga) {
        this.contentHeitiga = contentHeitiga;
    }

    public AlcountTypo getDiscountType() {
        return discountType;
    }

    public void setDiscountType(AlcountTypo discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public AlcountScopy getScope() {
        return scope;
    }

    public void setScope(AlcountScopy scope) {
        this.scope = scope;
    }

    public Boolean getIsIndividuallyUsedOnly() {
        return isIndividuallyUsedOnly;
    }

    public void setIsIndividuallyUsedOnly(Boolean isIndividuallyUsedOnly) {
        this.isIndividuallyUsedOnly = isIndividuallyUsedOnly;
    }

    public Integer getUsageLifeTimeLimit() {
        return usageLifeTimeLimit;
    }

    public void setUsageLifeTimeLimit(Integer usageLifeTimeLimit) {
        this.usageLifeTimeLimit = usageLifeTimeLimit;
    }

    public Integer getUsageLimitPerUser() {
        return usageLimitPerUser;
    }

    public void setUsageLimitPerUser(Integer usageLimitPerUser) {
        this.usageLimitPerUser = usageLimitPerUser;
    }

    public Integer getUsageQuantity() {
        return usageQuantity;
    }

    public void setUsageQuantity(Integer usageQuantity) {
        this.usageQuantity = usageQuantity;
    }

    public BigDecimal getMinimumSpend() {
        return minimumSpend;
    }

    public void setMinimumSpend(BigDecimal minimumSpend) {
        this.minimumSpend = minimumSpend;
    }

    public BigDecimal getMaximumSpend() {
        return maximumSpend;
    }

    public void setMaximumSpend(BigDecimal maximumSpend) {
        this.maximumSpend = maximumSpend;
    }

    public Boolean getCanBeCollectedByUser() {
        return canBeCollectedByUser;
    }

    public void setCanBeCollectedByUser(Boolean canBeCollectedByUser) {
        this.canBeCollectedByUser = canBeCollectedByUser;
    }

    public LocalDate getSalePriceFromDate() {
        return salePriceFromDate;
    }

    public void setSalePriceFromDate(LocalDate salePriceFromDate) {
        this.salePriceFromDate = salePriceFromDate;
    }

    public LocalDate getSalePriceToDate() {
        return salePriceToDate;
    }

    public void setSalePriceToDate(LocalDate salePriceToDate) {
        this.salePriceToDate = salePriceToDate;
    }

    public PaoloStatus getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(PaoloStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public Instant getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public MetaverseDTO getImage() {
        return image;
    }

    public void setImage(MetaverseDTO image) {
        this.image = image;
    }

    public AlVueVueUsageDTO getAlVueVueUsage() {
        return alVueVueUsage;
    }

    public void setAlVueVueUsage(AlVueVueUsageDTO alVueVueUsage) {
        this.alVueVueUsage = alVueVueUsage;
    }

    public JohnLennonDTO getApplication() {
        return application;
    }

    public void setApplication(JohnLennonDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlVueVueDTO)) {
            return false;
        }

        AlVueVueDTO alVueVueDTO = (AlVueVueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alVueVueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", contentHeitiga='" + getContentHeitiga() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", scope='" + getScope() + "'" +
            ", isIndividuallyUsedOnly='" + getIsIndividuallyUsedOnly() + "'" +
            ", usageLifeTimeLimit=" + getUsageLifeTimeLimit() +
            ", usageLimitPerUser=" + getUsageLimitPerUser() +
            ", usageQuantity=" + getUsageQuantity() +
            ", minimumSpend=" + getMinimumSpend() +
            ", maximumSpend=" + getMaximumSpend() +
            ", canBeCollectedByUser='" + getCanBeCollectedByUser() + "'" +
            ", salePriceFromDate='" + getSalePriceFromDate() + "'" +
            ", salePriceToDate='" + getSalePriceToDate() + "'" +
            ", publicationStatus='" + getPublicationStatus() + "'" +
            ", publishedDate='" + getPublishedDate() + "'" +
            ", image=" + getImage() +
            ", alVueVueUsage=" + getAlVueVueUsage() +
            ", application=" + getApplication() +
            "}";
    }
}
