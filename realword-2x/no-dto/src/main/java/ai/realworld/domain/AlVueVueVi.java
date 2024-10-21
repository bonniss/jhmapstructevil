package ai.realworld.domain;

import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import ai.realworld.domain.enumeration.PaoloStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlVueVueVi.
 */
@Entity
@Table(name = "al_vue_vue_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlVueVueVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Size(max = 10485760)
    @Column(name = "content_heitiga", length = 10485760)
    private String contentHeitiga;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private AlcountTypo discountType;

    @Column(name = "discount_rate", precision = 21, scale = 2)
    private BigDecimal discountRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    private AlcountScopy scope;

    @Column(name = "is_individually_used_only")
    private Boolean isIndividuallyUsedOnly;

    @Column(name = "usage_life_time_limit")
    private Integer usageLifeTimeLimit;

    @Column(name = "usage_limit_per_user")
    private Integer usageLimitPerUser;

    @Column(name = "usage_quantity")
    private Integer usageQuantity;

    @Column(name = "minimum_spend", precision = 21, scale = 2)
    private BigDecimal minimumSpend;

    @Column(name = "maximum_spend", precision = 21, scale = 2)
    private BigDecimal maximumSpend;

    @Column(name = "can_be_collected_by_user")
    private Boolean canBeCollectedByUser;

    @Column(name = "sale_price_from_date")
    private LocalDate salePriceFromDate;

    @Column(name = "sale_price_to_date")
    private LocalDate salePriceToDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status")
    private PaoloStatus publicationStatus;

    @Column(name = "published_date")
    private Instant publishedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alProPros", "alProties", "alProProVis", "alProtyVis" }, allowSetters = true)
    private Metaverse image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application", "vouchers", "customers" }, allowSetters = true)
    private AlVueVueViUsage alVueVueViUsage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "application" }, allowSetters = true)
    private Set<AlVueVueViCondition> conditions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlVueVueVi id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public AlVueVueVi code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public AlVueVueVi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentHeitiga() {
        return this.contentHeitiga;
    }

    public AlVueVueVi contentHeitiga(String contentHeitiga) {
        this.setContentHeitiga(contentHeitiga);
        return this;
    }

    public void setContentHeitiga(String contentHeitiga) {
        this.contentHeitiga = contentHeitiga;
    }

    public AlcountTypo getDiscountType() {
        return this.discountType;
    }

    public AlVueVueVi discountType(AlcountTypo discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(AlcountTypo discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountRate() {
        return this.discountRate;
    }

    public AlVueVueVi discountRate(BigDecimal discountRate) {
        this.setDiscountRate(discountRate);
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public AlcountScopy getScope() {
        return this.scope;
    }

    public AlVueVueVi scope(AlcountScopy scope) {
        this.setScope(scope);
        return this;
    }

    public void setScope(AlcountScopy scope) {
        this.scope = scope;
    }

    public Boolean getIsIndividuallyUsedOnly() {
        return this.isIndividuallyUsedOnly;
    }

    public AlVueVueVi isIndividuallyUsedOnly(Boolean isIndividuallyUsedOnly) {
        this.setIsIndividuallyUsedOnly(isIndividuallyUsedOnly);
        return this;
    }

    public void setIsIndividuallyUsedOnly(Boolean isIndividuallyUsedOnly) {
        this.isIndividuallyUsedOnly = isIndividuallyUsedOnly;
    }

    public Integer getUsageLifeTimeLimit() {
        return this.usageLifeTimeLimit;
    }

    public AlVueVueVi usageLifeTimeLimit(Integer usageLifeTimeLimit) {
        this.setUsageLifeTimeLimit(usageLifeTimeLimit);
        return this;
    }

    public void setUsageLifeTimeLimit(Integer usageLifeTimeLimit) {
        this.usageLifeTimeLimit = usageLifeTimeLimit;
    }

    public Integer getUsageLimitPerUser() {
        return this.usageLimitPerUser;
    }

    public AlVueVueVi usageLimitPerUser(Integer usageLimitPerUser) {
        this.setUsageLimitPerUser(usageLimitPerUser);
        return this;
    }

    public void setUsageLimitPerUser(Integer usageLimitPerUser) {
        this.usageLimitPerUser = usageLimitPerUser;
    }

    public Integer getUsageQuantity() {
        return this.usageQuantity;
    }

    public AlVueVueVi usageQuantity(Integer usageQuantity) {
        this.setUsageQuantity(usageQuantity);
        return this;
    }

    public void setUsageQuantity(Integer usageQuantity) {
        this.usageQuantity = usageQuantity;
    }

    public BigDecimal getMinimumSpend() {
        return this.minimumSpend;
    }

    public AlVueVueVi minimumSpend(BigDecimal minimumSpend) {
        this.setMinimumSpend(minimumSpend);
        return this;
    }

    public void setMinimumSpend(BigDecimal minimumSpend) {
        this.minimumSpend = minimumSpend;
    }

    public BigDecimal getMaximumSpend() {
        return this.maximumSpend;
    }

    public AlVueVueVi maximumSpend(BigDecimal maximumSpend) {
        this.setMaximumSpend(maximumSpend);
        return this;
    }

    public void setMaximumSpend(BigDecimal maximumSpend) {
        this.maximumSpend = maximumSpend;
    }

    public Boolean getCanBeCollectedByUser() {
        return this.canBeCollectedByUser;
    }

    public AlVueVueVi canBeCollectedByUser(Boolean canBeCollectedByUser) {
        this.setCanBeCollectedByUser(canBeCollectedByUser);
        return this;
    }

    public void setCanBeCollectedByUser(Boolean canBeCollectedByUser) {
        this.canBeCollectedByUser = canBeCollectedByUser;
    }

    public LocalDate getSalePriceFromDate() {
        return this.salePriceFromDate;
    }

    public AlVueVueVi salePriceFromDate(LocalDate salePriceFromDate) {
        this.setSalePriceFromDate(salePriceFromDate);
        return this;
    }

    public void setSalePriceFromDate(LocalDate salePriceFromDate) {
        this.salePriceFromDate = salePriceFromDate;
    }

    public LocalDate getSalePriceToDate() {
        return this.salePriceToDate;
    }

    public AlVueVueVi salePriceToDate(LocalDate salePriceToDate) {
        this.setSalePriceToDate(salePriceToDate);
        return this;
    }

    public void setSalePriceToDate(LocalDate salePriceToDate) {
        this.salePriceToDate = salePriceToDate;
    }

    public PaoloStatus getPublicationStatus() {
        return this.publicationStatus;
    }

    public AlVueVueVi publicationStatus(PaoloStatus publicationStatus) {
        this.setPublicationStatus(publicationStatus);
        return this;
    }

    public void setPublicationStatus(PaoloStatus publicationStatus) {
        this.publicationStatus = publicationStatus;
    }

    public Instant getPublishedDate() {
        return this.publishedDate;
    }

    public AlVueVueVi publishedDate(Instant publishedDate) {
        this.setPublishedDate(publishedDate);
        return this;
    }

    public void setPublishedDate(Instant publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Metaverse getImage() {
        return this.image;
    }

    public void setImage(Metaverse metaverse) {
        this.image = metaverse;
    }

    public AlVueVueVi image(Metaverse metaverse) {
        this.setImage(metaverse);
        return this;
    }

    public AlVueVueViUsage getAlVueVueViUsage() {
        return this.alVueVueViUsage;
    }

    public void setAlVueVueViUsage(AlVueVueViUsage alVueVueViUsage) {
        this.alVueVueViUsage = alVueVueViUsage;
    }

    public AlVueVueVi alVueVueViUsage(AlVueVueViUsage alVueVueViUsage) {
        this.setAlVueVueViUsage(alVueVueViUsage);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlVueVueVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    public Set<AlVueVueViCondition> getConditions() {
        return this.conditions;
    }

    public void setConditions(Set<AlVueVueViCondition> alVueVueViConditions) {
        if (this.conditions != null) {
            this.conditions.forEach(i -> i.setParent(null));
        }
        if (alVueVueViConditions != null) {
            alVueVueViConditions.forEach(i -> i.setParent(this));
        }
        this.conditions = alVueVueViConditions;
    }

    public AlVueVueVi conditions(Set<AlVueVueViCondition> alVueVueViConditions) {
        this.setConditions(alVueVueViConditions);
        return this;
    }

    public AlVueVueVi addConditions(AlVueVueViCondition alVueVueViCondition) {
        this.conditions.add(alVueVueViCondition);
        alVueVueViCondition.setParent(this);
        return this;
    }

    public AlVueVueVi removeConditions(AlVueVueViCondition alVueVueViCondition) {
        this.conditions.remove(alVueVueViCondition);
        alVueVueViCondition.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlVueVueVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlVueVueVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlVueVueVi{" +
            "id=" + getId() +
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
            "}";
    }
}
