package ai.realworld.service.criteria;

import ai.realworld.domain.enumeration.EcmaScript;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link ai.realworld.domain.OlAlmantinoMilo} entity. This class is used
 * in {@link ai.realworld.web.rest.OlAlmantinoMiloResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ol-almantino-milos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OlAlmantinoMiloCriteria implements Serializable, Criteria {

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

    private StringFilter providerAppManagerId;

    private StringFilter name;

    private StringFilter providerSecretKey;

    private StringFilter providerToken;

    private StringFilter providerRefreshToken;

    private UUIDFilter organizationId;

    private UUIDFilter applicationsId;

    private Boolean distinct;

    public OlAlmantinoMiloCriteria() {}

    public OlAlmantinoMiloCriteria(OlAlmantinoMiloCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.provider = other.optionalProvider().map(EcmaScriptFilter::copy).orElse(null);
        this.providerAppManagerId = other.optionalProviderAppManagerId().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.providerSecretKey = other.optionalProviderSecretKey().map(StringFilter::copy).orElse(null);
        this.providerToken = other.optionalProviderToken().map(StringFilter::copy).orElse(null);
        this.providerRefreshToken = other.optionalProviderRefreshToken().map(StringFilter::copy).orElse(null);
        this.organizationId = other.optionalOrganizationId().map(UUIDFilter::copy).orElse(null);
        this.applicationsId = other.optionalApplicationsId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OlAlmantinoMiloCriteria copy() {
        return new OlAlmantinoMiloCriteria(this);
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

    public StringFilter getProviderAppManagerId() {
        return providerAppManagerId;
    }

    public Optional<StringFilter> optionalProviderAppManagerId() {
        return Optional.ofNullable(providerAppManagerId);
    }

    public StringFilter providerAppManagerId() {
        if (providerAppManagerId == null) {
            setProviderAppManagerId(new StringFilter());
        }
        return providerAppManagerId;
    }

    public void setProviderAppManagerId(StringFilter providerAppManagerId) {
        this.providerAppManagerId = providerAppManagerId;
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

    public StringFilter getProviderSecretKey() {
        return providerSecretKey;
    }

    public Optional<StringFilter> optionalProviderSecretKey() {
        return Optional.ofNullable(providerSecretKey);
    }

    public StringFilter providerSecretKey() {
        if (providerSecretKey == null) {
            setProviderSecretKey(new StringFilter());
        }
        return providerSecretKey;
    }

    public void setProviderSecretKey(StringFilter providerSecretKey) {
        this.providerSecretKey = providerSecretKey;
    }

    public StringFilter getProviderToken() {
        return providerToken;
    }

    public Optional<StringFilter> optionalProviderToken() {
        return Optional.ofNullable(providerToken);
    }

    public StringFilter providerToken() {
        if (providerToken == null) {
            setProviderToken(new StringFilter());
        }
        return providerToken;
    }

    public void setProviderToken(StringFilter providerToken) {
        this.providerToken = providerToken;
    }

    public StringFilter getProviderRefreshToken() {
        return providerRefreshToken;
    }

    public Optional<StringFilter> optionalProviderRefreshToken() {
        return Optional.ofNullable(providerRefreshToken);
    }

    public StringFilter providerRefreshToken() {
        if (providerRefreshToken == null) {
            setProviderRefreshToken(new StringFilter());
        }
        return providerRefreshToken;
    }

    public void setProviderRefreshToken(StringFilter providerRefreshToken) {
        this.providerRefreshToken = providerRefreshToken;
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
        final OlAlmantinoMiloCriteria that = (OlAlmantinoMiloCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(provider, that.provider) &&
            Objects.equals(providerAppManagerId, that.providerAppManagerId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(providerSecretKey, that.providerSecretKey) &&
            Objects.equals(providerToken, that.providerToken) &&
            Objects.equals(providerRefreshToken, that.providerRefreshToken) &&
            Objects.equals(organizationId, that.organizationId) &&
            Objects.equals(applicationsId, that.applicationsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            provider,
            providerAppManagerId,
            name,
            providerSecretKey,
            providerToken,
            providerRefreshToken,
            organizationId,
            applicationsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OlAlmantinoMiloCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProvider().map(f -> "provider=" + f + ", ").orElse("") +
            optionalProviderAppManagerId().map(f -> "providerAppManagerId=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalProviderSecretKey().map(f -> "providerSecretKey=" + f + ", ").orElse("") +
            optionalProviderToken().map(f -> "providerToken=" + f + ", ").orElse("") +
            optionalProviderRefreshToken().map(f -> "providerRefreshToken=" + f + ", ").orElse("") +
            optionalOrganizationId().map(f -> "organizationId=" + f + ", ").orElse("") +
            optionalApplicationsId().map(f -> "applicationsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
