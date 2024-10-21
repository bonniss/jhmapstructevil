package ai.realworld.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlInquiryVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlInquiryViDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 256)
    private String title;

    @Size(max = 65535)
    private String body;

    @NotNull
    @Size(min = 2, max = 256)
    private String sender;

    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    private String phone;

    @Size(max = 10485760)
    private String contentJason;

    private AlPacinoDTO customer;

    private AlAppleViDTO agency;

    private EdSheeranViDTO personInCharge;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContentJason() {
        return contentJason;
    }

    public void setContentJason(String contentJason) {
        this.contentJason = contentJason;
    }

    public AlPacinoDTO getCustomer() {
        return customer;
    }

    public void setCustomer(AlPacinoDTO customer) {
        this.customer = customer;
    }

    public AlAppleViDTO getAgency() {
        return agency;
    }

    public void setAgency(AlAppleViDTO agency) {
        this.agency = agency;
    }

    public EdSheeranViDTO getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(EdSheeranViDTO personInCharge) {
        this.personInCharge = personInCharge;
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
        if (!(o instanceof AlInquiryViDTO)) {
            return false;
        }

        AlInquiryViDTO alInquiryViDTO = (AlInquiryViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alInquiryViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlInquiryViDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", body='" + getBody() + "'" +
            ", sender='" + getSender() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", contentJason='" + getContentJason() + "'" +
            ", customer=" + getCustomer() +
            ", agency=" + getAgency() +
            ", personInCharge=" + getPersonInCharge() +
            ", application=" + getApplication() +
            "}";
    }
}
