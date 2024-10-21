package ai.realworld.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.AlAppleVi} entity. This class is used
 * in {@link ai.realworld.web.rest.AlAppleViResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /al-apple-vis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlAppleViCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter hotline;

    private StringFilter taxCode;

    private StringFilter contactsJason;

    private StringFilter extensionJason;

    private BooleanFilter isEnabled;

    private LongFilter addressId;

    private UUIDFilter agencyTypeId;

    private LongFilter logoId;

    private UUIDFilter applicationId;

    private LongFilter agentsId;

    private Boolean distinct;

    public AlAppleViCriteria() {}

    public AlAppleViCriteria(AlAppleViCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.hotline = other.optionalHotline().map(StringFilter::copy).orElse(null);
        this.taxCode = other.optionalTaxCode().map(StringFilter::copy).orElse(null);
        this.contactsJason = other.optionalContactsJason().map(StringFilter::copy).orElse(null);
        this.extensionJason = other.optionalExtensionJason().map(StringFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.addressId = other.optionalAddressId().map(LongFilter::copy).orElse(null);
        this.agencyTypeId = other.optionalAgencyTypeId().map(UUIDFilter::copy).orElse(null);
        this.logoId = other.optionalLogoId().map(LongFilter::copy).orElse(null);
        this.applicationId = other.optionalApplicationId().map(UUIDFilter::copy).orElse(null);
        this.agentsId = other.optionalAgentsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlAppleViCriteria copy() {
        return new AlAppleViCriteria(this);
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

    public StringFilter getHotline() {
        return hotline;
    }

    public Optional<StringFilter> optionalHotline() {
        return Optional.ofNullable(hotline);
    }

    public StringFilter hotline() {
        if (hotline == null) {
            setHotline(new StringFilter());
        }
        return hotline;
    }

    public void setHotline(StringFilter hotline) {
        this.hotline = hotline;
    }

    public StringFilter getTaxCode() {
        return taxCode;
    }

    public Optional<StringFilter> optionalTaxCode() {
        return Optional.ofNullable(taxCode);
    }

    public StringFilter taxCode() {
        if (taxCode == null) {
            setTaxCode(new StringFilter());
        }
        return taxCode;
    }

    public void setTaxCode(StringFilter taxCode) {
        this.taxCode = taxCode;
    }

    public StringFilter getContactsJason() {
        return contactsJason;
    }

    public Optional<StringFilter> optionalContactsJason() {
        return Optional.ofNullable(contactsJason);
    }

    public StringFilter contactsJason() {
        if (contactsJason == null) {
            setContactsJason(new StringFilter());
        }
        return contactsJason;
    }

    public void setContactsJason(StringFilter contactsJason) {
        this.contactsJason = contactsJason;
    }

    public StringFilter getExtensionJason() {
        return extensionJason;
    }

    public Optional<StringFilter> optionalExtensionJason() {
        return Optional.ofNullable(extensionJason);
    }

    public StringFilter extensionJason() {
        if (extensionJason == null) {
            setExtensionJason(new StringFilter());
        }
        return extensionJason;
    }

    public void setExtensionJason(StringFilter extensionJason) {
        this.extensionJason = extensionJason;
    }

    public BooleanFilter getIsEnabled() {
        return isEnabled;
    }

    public Optional<BooleanFilter> optionalIsEnabled() {
        return Optional.ofNullable(isEnabled);
    }

    public BooleanFilter isEnabled() {
        if (isEnabled == null) {
            setIsEnabled(new BooleanFilter());
        }
        return isEnabled;
    }

    public void setIsEnabled(BooleanFilter isEnabled) {
        this.isEnabled = isEnabled;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public Optional<LongFilter> optionalAddressId() {
        return Optional.ofNullable(addressId);
    }

    public LongFilter addressId() {
        if (addressId == null) {
            setAddressId(new LongFilter());
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public UUIDFilter getAgencyTypeId() {
        return agencyTypeId;
    }

    public Optional<UUIDFilter> optionalAgencyTypeId() {
        return Optional.ofNullable(agencyTypeId);
    }

    public UUIDFilter agencyTypeId() {
        if (agencyTypeId == null) {
            setAgencyTypeId(new UUIDFilter());
        }
        return agencyTypeId;
    }

    public void setAgencyTypeId(UUIDFilter agencyTypeId) {
        this.agencyTypeId = agencyTypeId;
    }

    public LongFilter getLogoId() {
        return logoId;
    }

    public Optional<LongFilter> optionalLogoId() {
        return Optional.ofNullable(logoId);
    }

    public LongFilter logoId() {
        if (logoId == null) {
            setLogoId(new LongFilter());
        }
        return logoId;
    }

    public void setLogoId(LongFilter logoId) {
        this.logoId = logoId;
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

    public LongFilter getAgentsId() {
        return agentsId;
    }

    public Optional<LongFilter> optionalAgentsId() {
        return Optional.ofNullable(agentsId);
    }

    public LongFilter agentsId() {
        if (agentsId == null) {
            setAgentsId(new LongFilter());
        }
        return agentsId;
    }

    public void setAgentsId(LongFilter agentsId) {
        this.agentsId = agentsId;
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
        final AlAppleViCriteria that = (AlAppleViCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(hotline, that.hotline) &&
            Objects.equals(taxCode, that.taxCode) &&
            Objects.equals(contactsJason, that.contactsJason) &&
            Objects.equals(extensionJason, that.extensionJason) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(agencyTypeId, that.agencyTypeId) &&
            Objects.equals(logoId, that.logoId) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(agentsId, that.agentsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            hotline,
            taxCode,
            contactsJason,
            extensionJason,
            isEnabled,
            addressId,
            agencyTypeId,
            logoId,
            applicationId,
            agentsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlAppleViCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalHotline().map(f -> "hotline=" + f + ", ").orElse("") +
            optionalTaxCode().map(f -> "taxCode=" + f + ", ").orElse("") +
            optionalContactsJason().map(f -> "contactsJason=" + f + ", ").orElse("") +
            optionalExtensionJason().map(f -> "extensionJason=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalAddressId().map(f -> "addressId=" + f + ", ").orElse("") +
            optionalAgencyTypeId().map(f -> "agencyTypeId=" + f + ", ").orElse("") +
            optionalLogoId().map(f -> "logoId=" + f + ", ").orElse("") +
            optionalApplicationId().map(f -> "applicationId=" + f + ", ").orElse("") +
            optionalAgentsId().map(f -> "agentsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
