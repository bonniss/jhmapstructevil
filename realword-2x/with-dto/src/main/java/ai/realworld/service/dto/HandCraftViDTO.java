package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.HandCraftVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HandCraftViDTO implements Serializable {

    private Long id;

    @NotNull
    private EdSheeranViDTO agent;

    @NotNull
    private RihannaViDTO role;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EdSheeranViDTO getAgent() {
        return agent;
    }

    public void setAgent(EdSheeranViDTO agent) {
        this.agent = agent;
    }

    public RihannaViDTO getRole() {
        return role;
    }

    public void setRole(RihannaViDTO role) {
        this.role = role;
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
        if (!(o instanceof HandCraftViDTO)) {
            return false;
        }

        HandCraftViDTO handCraftViDTO = (HandCraftViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, handCraftViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HandCraftViDTO{" +
            "id=" + getId() +
            ", agent=" + getAgent() +
            ", role=" + getRole() +
            ", application=" + getApplication() +
            "}";
    }
}
