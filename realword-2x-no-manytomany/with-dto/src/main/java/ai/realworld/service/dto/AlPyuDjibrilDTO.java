package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.BenedictRiottaType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlPyuDjibril} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPyuDjibrilDTO implements Serializable {

    private Long id;

    private BenedictRiottaType rateType;

    private BigDecimal rate;

    private Boolean isEnabled;

    private AlProtyDTO property;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BenedictRiottaType getRateType() {
        return rateType;
    }

    public void setRateType(BenedictRiottaType rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public AlProtyDTO getProperty() {
        return property;
    }

    public void setProperty(AlProtyDTO property) {
        this.property = property;
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
        if (!(o instanceof AlPyuDjibrilDTO)) {
            return false;
        }

        AlPyuDjibrilDTO alPyuDjibrilDTO = (AlPyuDjibrilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPyuDjibrilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPyuDjibrilDTO{" +
            "id=" + getId() +
            ", rateType='" + getRateType() + "'" +
            ", rate=" + getRate() +
            ", isEnabled='" + getIsEnabled() + "'" +
            ", property=" + getProperty() +
            ", application=" + getApplication() +
            "}";
    }
}
