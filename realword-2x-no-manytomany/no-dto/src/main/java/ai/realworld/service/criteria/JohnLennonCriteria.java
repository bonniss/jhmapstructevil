package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.EcmaScript;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.JohnLennon} entity. This class is used
 * in {@link ai.realworld.web.rest.JohnLennonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /john-lennons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JohnLennonCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EcmaScript
     */
    public static class EcmaScriptFilter extends Filter<EcmaScript> {

        public EcmaScriptFilter() {}

        public EcmaScriptFilter(EcmaScriptFilter filter) {
            super(filter);
        }

        @Override
        public EcmaScriptFilter copy() {
            return new EcmaScriptFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private EcmaScriptFilter provider;

    private StringFilter providerAppId;

    private StringFilter name;

    private StringFilter slug;

    private BooleanFilter isEnabled;

    private LongFilter logoId;

    private UUIDFilter appManagerId;

    private UUIDFilter organizationId;

    private LongFilter jelloInitiumId;

    private LongFilter inhouseInitiumId;

    private LongFilter jelloInitiumViId;

    private LongFilter inhouseInitiumViId;

    private Boolean distinct;

    public JohnLennonCriteria() {}

    public JohnLennonCriteria(JohnLennonCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.provider = other.optionalProvider().map(EcmaScriptFilter::copy).orElse(null);
        this.providerAppId = other.optionalProviderAppId().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.slug = other.optionalSlug().map(StringFilter::copy).orElse(null);
        this.isEnabled = other.optionalIsEnabled().map(BooleanFilter::copy).orElse(null);
        this.logoId = other.optionalLogoId().map(LongFilter::copy).orElse(null);
        this.appManagerId = other.optionalAppManagerId().map(UUIDFilter::copy).orElse(null);
        this.organizationId = other.optionalOrganizationId().map(UUIDFilter::copy).orElse(null);
        this.jelloInitiumId = other.optionalJelloInitiumId().map(LongFilter::copy).orElse(null);
        this.inhouseInitiumId = other.optionalInhouseInitiumId().map(LongFilter::copy).orElse(null);
        this.jelloInitiumViId = other.optionalJelloInitiumViId().map(LongFilter::copy).orElse(null);
        this.inhouseInitiumViId = other.optionalInhouseInitiumViId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public JohnLennonCriteria copy() {
        return new JohnLennonCriteria(this);
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

    public EcmaScriptFilter getProvider() {
        return provider;
    }

    public Optional<EcmaScriptFilter> optionalProvider() {
        return Optional.ofNullable(provider);
    }

    public EcmaScriptFilter provider() {
        if (provider == null) {
            setProvider(new EcmaScriptFilter());
        }
        return provider;
    }

    public void setProvider(EcmaScriptFilter provider) {
        this.provider = provider;
    }

    public StringFilter getProviderAppId() {
        return providerAppId;
    }

    public Optional<StringFilter> optionalProviderAppId() {
        return Optional.ofNullable(providerAppId);
    }

    public StringFilter providerAppId() {
        if (providerAppId == null) {
            setProviderAppId(new StringFilter());
        }
        return providerAppId;
    }

    public void setProviderAppId(StringFilter providerAppId) {
        this.providerAppId = providerAppId;
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

    public UUIDFilter getAppManagerId() {
        return appManagerId;
    }

    public Optional<UUIDFilter> optionalAppManagerId() {
        return Optional.ofNullable(appManagerId);
    }

    public UUIDFilter appManagerId() {
        if (appManagerId == null) {
            setAppManagerId(new UUIDFilter());
        }
        return appManagerId;
    }

    public void setAppManagerId(UUIDFilter appManagerId) {
        this.appManagerId = appManagerId;
    }

    public UUIDFilter getOrganizationId() {
        return organizationId;
    }

    public Optional<UUIDFilter> optionalOrganizationId() {
        return Optional.ofNullable(organizationId);
    }

    public UUIDFilter organizationId() {
        if (organizationId == null) {
            setOrganizationId(new UUIDFilter());
        }
        return organizationId;
    }

    public void setOrganizationId(UUIDFilter organizationId) {
        this.organizationId = organizationId;
    }

    public LongFilter getJelloInitiumId() {
        return jelloInitiumId;
    }

    public Optional<LongFilter> optionalJelloInitiumId() {
        return Optional.ofNullable(jelloInitiumId);
    }

    public LongFilter jelloInitiumId() {
        if (jelloInitiumId == null) {
            setJelloInitiumId(new LongFilter());
        }
        return jelloInitiumId;
    }

    public void setJelloInitiumId(LongFilter jelloInitiumId) {
        this.jelloInitiumId = jelloInitiumId;
    }

    public LongFilter getInhouseInitiumId() {
        return inhouseInitiumId;
    }

    public Optional<LongFilter> optionalInhouseInitiumId() {
        return Optional.ofNullable(inhouseInitiumId);
    }

    public LongFilter inhouseInitiumId() {
        if (inhouseInitiumId == null) {
            setInhouseInitiumId(new LongFilter());
        }
        return inhouseInitiumId;
    }

    public void setInhouseInitiumId(LongFilter inhouseInitiumId) {
        this.inhouseInitiumId = inhouseInitiumId;
    }

    public LongFilter getJelloInitiumViId() {
        return jelloInitiumViId;
    }

    public Optional<LongFilter> optionalJelloInitiumViId() {
        return Optional.ofNullable(jelloInitiumViId);
    }

    public LongFilter jelloInitiumViId() {
        if (jelloInitiumViId == null) {
            setJelloInitiumViId(new LongFilter());
        }
        return jelloInitiumViId;
    }

    public void setJelloInitiumViId(LongFilter jelloInitiumViId) {
        this.jelloInitiumViId = jelloInitiumViId;
    }

    public LongFilter getInhouseInitiumViId() {
        return inhouseInitiumViId;
    }

    public Optional<LongFilter> optionalInhouseInitiumViId() {
        return Optional.ofNullable(inhouseInitiumViId);
    }

    public LongFilter inhouseInitiumViId() {
        if (inhouseInitiumViId == null) {
            setInhouseInitiumViId(new LongFilter());
        }
        return inhouseInitiumViId;
    }

    public void setInhouseInitiumViId(LongFilter inhouseInitiumViId) {
        this.inhouseInitiumViId = inhouseInitiumViId;
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
        final JohnLennonCriteria that = (JohnLennonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(provider, that.provider) &&
            Objects.equals(providerAppId, that.providerAppId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(isEnabled, that.isEnabled) &&
            Objects.equals(logoId, that.logoId) &&
            Objects.equals(appManagerId, that.appManagerId) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(jelloInitiumId, that.jelloInitiumId) &&
            Objects.equals(inhouseInitiumId, that.inhouseInitiumId) &&
            Objects.equals(jelloInitiumViId, that.jelloInitiumViId) &&
            Objects.equals(inhouseInitiumViId, that.inhouseInitiumViId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            provider,
            providerAppId,
            name,
            slug,
            isEnabled,
            logoId,
            appManagerId,
            organizationId,
            jelloInitiumId,
            inhouseInitiumId,
            jelloInitiumViId,
            inhouseInitiumViId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JohnLennonCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProvider().map(f -> "provider=" + f + ", ").orElse("") +
            optionalProviderAppId().map(f -> "providerAppId=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalSlug().map(f -> "slug=" + f + ", ").orElse("") +
            optionalIsEnabled().map(f -> "isEnabled=" + f + ", ").orElse("") +
            optionalLogoId().map(f -> "logoId=" + f + ", ").orElse("") +
            optionalAppManagerId().map(f -> "appManagerId=" + f + ", ").orElse("") +
            optionalOrganizationId().map(f -> "organizationId=" + f + ", ").orElse("") +
            optionalJelloInitiumId().map(f -> "jelloInitiumId=" + f + ", ").orElse("") +
            optionalInhouseInitiumId().map(f -> "inhouseInitiumId=" + f + ", ").orElse("") +
            optionalJelloInitiumViId().map(f -> "jelloInitiumViId=" + f + ", ").orElse("") +
            optionalInhouseInitiumViId().map(f -> "inhouseInitiumViId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
