package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.EcmaScript;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.JohnLennon} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JohnLennonDTO implements Serializable {

    private UUID id;

    private EcmaScript provider;

    private String providerAppId;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @NotNull
    @Size(min = 2, max = 160)
    private String slug;

    private Boolean isEnabled;

    private MetaverseDTO logo;

    private OlAlmantinoMiloDTO appManager;

    private OlMasterDTO organization;

    private InitiumDTO jelloInitium;

    private InitiumDTO inhouseInitium;

    private InitiumViDTO jelloInitiumVi;

    private InitiumViDTO inhouseInitiumVi;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EcmaScript getProvider() {
        return provider;
    }

    public void setProvider(EcmaScript provider) {
        this.provider = provider;
    }

    public String getProviderAppId() {
        return providerAppId;
    }

    public void setProviderAppId(String providerAppId) {
        this.providerAppId = providerAppId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public MetaverseDTO getLogo() {
        return logo;
    }

    public void setLogo(MetaverseDTO logo) {
        this.logo = logo;
    }

    public OlAlmantinoMiloDTO getAppManager() {
        return appManager;
    }

    public void setAppManager(OlAlmantinoMiloDTO appManager) {
        this.appManager = appManager;
    }

    public OlMasterDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OlMasterDTO organization) {
        this.organization = organization;
    }

    public InitiumDTO getJelloInitium() {
        return jelloInitium;
    }

    public void setJelloInitium(InitiumDTO jelloInitium) {
        this.jelloInitium = jelloInitium;
    }

    public InitiumDTO getInhouseInitium() {
        return inhouseInitium;
    }

    public void setInhouseInitium(InitiumDTO inhouseInitium) {
        this.inhouseInitium = inhouseInitium;
    }

    public InitiumViDTO getJelloInitiumVi() {
        return jelloInitiumVi;
    }

    public void setJelloInitiumVi(InitiumViDTO jelloInitiumVi) {
        this.jelloInitiumVi = jelloInitiumVi;
    }

    public InitiumViDTO getInhouseInitiumVi() {
        return inhouseInitiumVi;
    }

    public void setInhouseInitiumVi(InitiumViDTO inhouseInitiumVi) {
        this.inhouseInitiumVi = inhouseInitiumVi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JohnLennonDTO)) {
            return false;
        }

        JohnLennonDTO johnLennonDTO = (JohnLennonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, johnLennonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JohnLennonDTO{" +
            "id='" + getId() + "'" +
            ", provider='" + getProvider() + "'" +
            ", providerAppId='" + getProviderAppId() + "'" +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", logo=" + getLogo() +
            ", appManager=" + getAppManager() +
            ", organization=" + getOrganization() +
            ", jelloInitium=" + getJelloInitium() +
            ", inhouseInitium=" + getInhouseInitium() +
            ", jelloInitiumVi=" + getJelloInitiumVi() +
            ", inhouseInitiumVi=" + getInhouseInitiumVi() +
            "}";
    }
}
