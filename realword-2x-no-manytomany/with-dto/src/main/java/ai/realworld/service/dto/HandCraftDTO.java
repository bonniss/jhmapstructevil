package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.HandCraft} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HandCraftDTO implements Serializable {

    private Long id;

    @NotNull
    private EdSheeranDTO agent;

    @NotNull
    private RihannaDTO role;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EdSheeranDTO getAgent() {
        return agent;
    }

    public void setAgent(EdSheeranDTO agent) {
        this.agent = agent;
    }

    public RihannaDTO getRole() {
        return role;
    }

    public void setRole(RihannaDTO role) {
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
        if (!(o instanceof HandCraftDTO)) {
            return false;
        }

        HandCraftDTO handCraftDTO = (HandCraftDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, handCraftDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HandCraftDTO{" +
            "id=" + getId() +
            ", agent=" + getAgent() +
            ", role=" + getRole() +
            ", application=" + getApplication() +
            "}";
    }
}
