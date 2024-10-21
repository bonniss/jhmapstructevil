package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlAlexTypeVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlAlexTypeViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-alex-type-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlAlexTypeViCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter description;

    private BooleanFilter canDoRetail;

    private BooleanFilter isOrgDivision;

    private StringFilter configJason;

    private IntegerFilter treeDepth;

    private UUIDFilter applicationId;

    private LongFilter asSupplierId;

    private LongFilter asCustomerId;

    private UUIDFilter agenciesId;

    private Boolean distinct;

    public AlAlexTypeViCriteria() {}

    public AlAlexTypeViCriteria(AlAlexTypeViCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.canDoRetail = other.optionalCanDoRetail().map(BooleanFilter::copy).orElse(null);
        this.isOrgDivision = other.optionalIsOrgDivision().map(BooleanFilter::copy).orElse(null);
        this.configJason = other.optionalConfigJason().map(StringFilter::copy).orElse(null);
        this.treeDepth = other.optionalTreeDepth().map(IntegerFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.asSupplierId = other.optionalAsSupplierId().map(LongFilter::copy).orElse(null);
        this.asCustomerId = other.optionalAsCustomerId().map(LongFilter::copy).orElse(null);
        this.agenciesId = other.optionalAgenciesId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlAlexTypeViCriteria copy() {
        return new AlAlexTypeViCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getCanDoRetail() {
        return canDoRetail;
    }

    public Optional<BooleanFilter> optionalCanDoRetail() {
        return Optional.ofNullable(canDoRetail);
    }

    public BooleanFilter canDoRetail() {
        if (canDoRetail == null) {
            setCanDoRetail(new BooleanFilter());
        }
        return canDoRetail;
    }

    public void setCanDoRetail(BooleanFilter canDoRetail) {
        this.canDoRetail = canDoRetail;
    }

    public BooleanFilter getIsOrgDivision() {
        return isOrgDivision;
    }

    public Optional<BooleanFilter> optionalIsOrgDivision() {
        return Optional.ofNullable(isOrgDivision);
    }

    public BooleanFilter isOrgDivision() {
        if (isOrgDivision == null) {
            setIsOrgDivision(new BooleanFilter());
        }
        return isOrgDivision;
    }

    public void setIsOrgDivision(BooleanFilter isOrgDivision) {
        this.isOrgDivision = isOrgDivision;
    }

    public StringFilter getConfigJason() {
        return configJason;
    }

    public Optional<StringFilter> optionalConfigJason() {
        return Optional.ofNullable(configJason);
    }

    public StringFilter configJason() {
        if (configJason == null) {
            setConfigJason(new StringFilter());
        }
        return configJason;
    }

    public void setConfigJason(StringFilter configJason) {
        this.configJason = configJason;
    }

    public IntegerFilter getTreeDepth() {
        return treeDepth;
    }

    public Optional<IntegerFilter> optionalTreeDepth() {
        return Optional.ofNullable(treeDepth);
    }

    public IntegerFilter treeDepth() {
        if (treeDepth == null) {
            setTreeDepth(new IntegerFilter());
        }
        return treeDepth;
    }

    public void setTreeDepth(IntegerFilter treeDepth) {
        this.treeDepth = treeDepth;
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

    public LongFilter getAsSupplierId() {
        return asSupplierId;
    }

    public Optional<LongFilter> optionalAsSupplierId() {
        return Optional.ofNullable(asSupplierId);
    }

    public LongFilter asSupplierId() {
        if (asSupplierId == null) {
            setAsSupplierId(new LongFilter());
        }
        return asSupplierId;
    }

    public void setAsSupplierId(LongFilter asSupplierId) {
        this.asSupplierId = asSupplierId;
    }

    public LongFilter getAsCustomerId() {
        return asCustomerId;
    }

    public Optional<LongFilter> optionalAsCustomerId() {
        return Optional.ofNullable(asCustomerId);
    }

    public LongFilter asCustomerId() {
        if (asCustomerId == null) {
            setAsCustomerId(new LongFilter());
        }
        return asCustomerId;
    }

    public void setAsCustomerId(LongFilter asCustomerId) {
        this.asCustomerId = asCustomerId;
    }

    public UUIDFilter getAgenciesId() {
        return agenciesId;
    }

    public Optional<UUIDFilter> optionalAgenciesId() {
        return Optional.ofNullable(agenciesId);
    }

    public UUIDFilter agenciesId() {
        if (agenciesId == null) {
            setAgenciesId(new UUIDFilter());
        }
        return agenciesId;
    }

    public void setAgenciesId(UUIDFilter agenciesId) {
        this.agenciesId = agenciesId;
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
        final AlAlexTypeViCriteria that = (AlAlexTypeViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(canDoRetail, that.canDoRetail) &&
            Objects.equals(isOrgDivision, that.isOrgDivision) &&
            Objects.equals(configJason, that.configJason) &&
            Objects.equals(treeDepth, that.treeDepth) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(asSupplierId, that.asSupplierId) &&
            Objects.equals(asCustomerId, that.asCustomerId) &&
            Objects.equals(agenciesId, that.agenciesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            canDoRetail,
            isOrgDivision,
            configJason,
            treeDepth,
            applicationId,
            asSupplierId,
            asCustomerId,
            agenciesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlAlexTypeViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCanDoRetail().map(f -> "canDoRetail=" + f + ", ").orElse("") +
            optionalIsOrgDivision().map(f -> "isOrgDivision=" + f + ", ").orElse("") +
            optionalConfigJason().map(f -> "configJason=" + f + ", ").orElse("") +
            optionalTreeDepth().map(f -> "treeDepth=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalAsSupplierId().map(f -> "asSupplierId=" + f + ", ").orElse("") +
            optionalAsCustomerId().map(f -> "asCustomerId=" + f + ", ").orElse("") +
            optionalAgenciesId().map(f -> "agenciesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
