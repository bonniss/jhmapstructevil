package ai.realworld.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlPacinoVoucherVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPacinoVoucherViDTO implements Serializable {

    private UUID id;

    private String sourceTitle;

    private String sourceUrl;

    private Instant collectedDate;

    private AlPacinoDTO user;

    private AlVueVueViDTO voucher;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Instant getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(Instant collectedDate) {
        this.collectedDate = collectedDate;
    }

    public AlPacinoDTO getUser() {
        return user;
    }

    public void setUser(AlPacinoDTO user) {
        this.user = user;
    }

    public AlVueVueViDTO getVoucher() {
        return voucher;
    }

    public void setVoucher(AlVueVueViDTO voucher) {
        this.voucher = voucher;
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
        if (!(o instanceof AlPacinoVoucherViDTO)) {
            return false;
        }

        AlPacinoVoucherViDTO alPacinoVoucherViDTO = (AlPacinoVoucherViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPacinoVoucherViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPacinoVoucherViDTO{" +
            "id='" + getId() + "'" +
            ", sourceTitle='" + getSourceTitle() + "'" +
            ", sourceUrl='" + getSourceUrl() + "'" +
            ", collectedDate='" + getCollectedDate() + "'" +
            ", user=" + getUser() +
            ", voucher=" + getVoucher() +
            ", application=" + getApplication() +
            "}";
    }
}
