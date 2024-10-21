package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.EcmaScript;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.OlAlmantinoMilo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OlAlmantinoMiloDTO implements Serializable {

    private UUID id;

    @NotNull
    private EcmaScript provider;

    @NotNull
    @Size(max = 1024)
    private String providerAppManagerId;

    @NotNull
    @Size(min = 2, max = 256)
    private String name;

    @Size(max = 1024)
    private String providerSecretKey;

    @Size(max = 1024)
    private String providerToken;

    @Size(max = 1024)
    private String providerRefreshToken;

    private OlMasterDTO organization;

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

    public String getProviderAppManagerId() {
        return providerAppManagerId;
    }

    public void setProviderAppManagerId(String providerAppManagerId) {
        this.providerAppManagerId = providerAppManagerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderSecretKey() {
        return providerSecretKey;
    }

    public void setProviderSecretKey(String providerSecretKey) {
        this.providerSecretKey = providerSecretKey;
    }

    public String getProviderToken() {
        return providerToken;
    }

    public void setProviderToken(String providerToken) {
        this.providerToken = providerToken;
    }

    public String getProviderRefreshToken() {
        return providerRefreshToken;
    }

    public void setProviderRefreshToken(String providerRefreshToken) {
        this.providerRefreshToken = providerRefreshToken;
    }

    public OlMasterDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OlMasterDTO organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OlAlmantinoMiloDTO)) {
            return false;
        }

        OlAlmantinoMiloDTO olAlmantinoMiloDTO = (OlAlmantinoMiloDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, olAlmantinoMiloDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OlAlmantinoMiloDTO{" +
            "id='" + getId() + "'" +
            ", provider='" + getProvider() + "'" +
            ", providerAppManagerId='" + getProviderAppManagerId() + "'" +
            ", name='" + getName() + "'" +
            ", providerSecretKey='" + getProviderSecretKey() + "'" +
            ", providerToken='" + getProviderToken() + "'" +
            ", providerRefreshToken='" + getProviderRefreshToken() + "'" +
            ", organization=" + getOrganization() +
            "}";
    }
}
