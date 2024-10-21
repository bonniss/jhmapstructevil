package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.AlcountScopy;
import ai.realworld.domain.enumeration.AlcountTypo;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlGore} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlGoreDTO implements Serializable {

    private Long id;

    @Size(min = 2, max = 256)
    private String name;

    private AlcountTypo discountType;

    private BigDecimal discountRate;

    private AlcountScopy scope;

    private AlBetonamuRelationDTO bizRelation;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlcountTypo getDiscountType() {
        return discountType;
    }

    public void setDiscountType(AlcountTypo discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public AlcountScopy getScope() {
        return scope;
    }

    public void setScope(AlcountScopy scope) {
        this.scope = scope;
    }

    public AlBetonamuRelationDTO getBizRelation() {
        return bizRelation;
    }

    public void setBizRelation(AlBetonamuRelationDTO bizRelation) {
        this.bizRelation = bizRelation;
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
        if (!(o instanceof AlGoreDTO)) {
            return false;
        }

        AlGoreDTO alGoreDTO = (AlGoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alGoreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlGoreDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", scope='" + getScope() + "'" +
            ", bizRelation=" + getBizRelation() +
            ", application=" + getApplication() +
            "}";
    }
}
