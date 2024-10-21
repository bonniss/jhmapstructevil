package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.EeriePointSource;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ai.realworld.domain.AlPacinoPointHistoryVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoPointHistoryViDTO implements Serializable {

    private Long id;

    private EeriePointSource source;

    private String associatedId;

    private Integer pointAmount;

    private AlPacinoDTO customer;

    private JohnLennonDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EeriePointSource getSource() {
        return source;
    }

    public void setSource(EeriePointSource source) {
        this.source = source;
    }

    public String getAssociatedId() {
        return associatedId;
    }

    public void setAssociatedId(String associatedId) {
        this.associatedId = associatedId;
    }

    public Integer getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(Integer pointAmount) {
        this.pointAmount = pointAmount;
    }

    public AlPacinoDTO getCustomer() {
        return customer;
    }

    public void setCustomer(AlPacinoDTO customer) {
        this.customer = customer;
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
        if (!(o instanceof AlPacinoPointHistoryViDTO)) {
            return false;
        }

        AlPacinoPointHistoryViDTO alPacinoPointHistoryViDTO = (AlPacinoPointHistoryViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPacinoPointHistoryViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoPointHistoryViDTO{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", associatedId='" + getAssociatedId() + "'" +
            ", pointAmount=" + getPointAmount() +
            ", customer=" + getCustomer() +
            ", application=" + getApplication() +
            "}";
    }
}
