package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.OlBakeryType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.OlMaster} entity. This class is used
 * in {@link ai.realworld.web.rest.OlMasterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ol-masters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OlMasterCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OlBakeryType
     */
    public static class OlBakeryTypeFilter extends Filter<OlBakeryType> {

        public OlBakeryTypeFilter() {}

        public OlBakeryTypeFilter(OlBakeryTypeFilter filter) {
            super(filter);
        }

        @Override
        public OlBakeryTypeFilter copy() {
            return new OlBakeryTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter slug;

    private StringFilter descriptionHeitiga;

    private OlBakeryTypeFilter businessType;

    private StringFilter email;

    private StringFilter hotline;

    private StringFilter taxCode;

    private StringFilter contactsJason;

    private StringFilter extensionJason;

    private BooleanFilter isEnabled;

    private LongFilter addressId;

    private UUIDFilter applicationsId;

    private Boolean distinct;

    public OlMasterCriteria() {}

    public OlMasterCriteria(OlMasterCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.slug = other.optionalSlug().map(StringFilter::copy).orElse(null);
        this.descriptionHeitiga = other.optionalDescriptionHeitiga().map(StringFilter::copy).orElse(null);
        this.businessType = other.optionalBusinessType().map(OlBakeryTypeFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.hotline = other.optionalHotline().map(StringFilter::copy).orElse(null);
        this.taxCode = other.optionalTaxCode().map(StringFilter::copy).orElse(null);
        this.contactsJason = other.optionalContactsJason().map(StringFilter::copy).orElse(null);
        this.extensionJason = other.optionalExtensionJason().map(StringFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.addressId = other.optionalAddressId().map(LongFilter::copy).orElse(null);
        this.applicationsId = other.optionalApplicationsId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OlMasterCriteria copy() {
        return new OlMasterCriteria(this);
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

    public StringFilter getSlug() {
        return slug;
    }

    public Optional<StringFilter> optionalSlug() {
        return Optional.ofNullable(slug);
    }

    public StringFilter slug() {
        if (slug == null) {
            setSlug(new StringFilter());
        }
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getDescriptionHeitiga() {
        return descriptionHeitiga;
    }

    public Optional<StringFilter> optionalDescriptionHeitiga() {
        return Optional.ofNullable(descriptionHeitiga);
    }

    public StringFilter descriptionHeitiga() {
        if (descriptionHeitiga == null) {
            setDescriptionHeitiga(new StringFilter());
        }
        return descriptionHeitiga;
    }

    public void setDescriptionHeitiga(StringFilter descriptionHeitiga) {
        this.descriptionHeitiga = descriptionHeitiga;
    }

    public OlBakeryTypeFilter getBusinessType() {
        return businessType;
    }

    public Optional<OlBakeryTypeFilter> optionalBusinessType() {
        return Optional.ofNullable(businessType);
    }

    public OlBakeryTypeFilter businessType() {
        if (businessType == null) {
            setBusinessType(new OlBakeryTypeFilter());
        }
        return businessType;
    }

    public void setBusinessType(OlBakeryTypeFilter businessType) {
        this.businessType = businessType;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public UUIDFilter getApplicationsId() {
        return applicationsId;
    }

    public Optional<UUIDFilter> optionalApplicationsId() {
        return Optional.ofNullable(applicationsId);
    }

    public UUIDFilter applicationsId() {
        if (applicationsId == null) {
            setApplicationsId(new UUIDFilter());
        }
        return applicationsId;
    }

    public void setApplicationsId(UUIDFilter applicationsId) {
        this.applicationsId = applicationsId;
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
        final OlMasterCriteria that = (OlMasterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(descriptionHeitiga, that.descriptionHeitiga) &&
            Objects.equals(businessType, that.businessType) &&
            Objects.equals(email, that.email) &&
            Objects.equals(hotline, that.hotline) &&
            Objects.equals(taxCode, that.taxCode) &&
            Objects.equals(contactsJason, that.contactsJason) &&
            Objects.equals(extensionJason, that.extensionJason) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(applicationsId, that.applicationsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            slug,
            descriptionHeitiga,
            businessType,
            email,
            hotline,
            taxCode,
            contactsJason,
            extensionJason,
            isEnabled,
            addressId,
            applicationsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OlMasterCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalSlug().map(f -> "slug=" + f + ", ").orElse("") +
            optionalDescriptionHeitiga().map(f -> "descriptionHeitiga=" + f + ", ").orElse("") +
            optionalBusinessType().map(f -> "businessType=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalHotline().map(f -> "hotline=" + f + ", ").orElse("") +
            optionalTaxCode().map(f -> "taxCode=" + f + ", ").orElse("") +
            optionalContactsJason().map(f -> "contactsJason=" + f + ", ").orElse("") +
            optionalExtensionJason().map(f -> "extensionJason=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalAddressId().map(f -> "addressId=" + f + ", ").orElse("") +
            optionalApplicationsId().map(f -> "applicationsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
