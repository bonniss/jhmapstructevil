package ai.realworld.domain;

import ai.realworld.domain.enumeration.BenedictType;
import ai.realworld.domain.enumeration.PaulBargainStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlPyuJokerVi.
 */
@Entity
@Table(name = "al_pyu_joker_vi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPyuJokerVi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "booking_no", unique = true)
    private String bookingNo;

    @Size(max = 65535)
    @Column(name = "note_heitiga", length = 65535)
    private String noteHeitiga;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type")
    private BenedictType periodType;

    @Column(name = "from_date")
    private Instant fromDate;

    @Column(name = "to_date")
    private Instant toDate;

    @Column(name = "check_in_date")
    private Instant checkInDate;

    @Column(name = "check_out_date")
    private Instant checkOutDate;

    @Min(value = 1)
    @Column(name = "number_of_adults")
    private Integer numberOfAdults;

    @Min(value = 0)
    @Column(name = "number_of_preschoolers")
    private Integer numberOfPreschoolers;

    @Min(value = 0)
    @Column(name = "number_of_children")
    private Integer numberOfChildren;

    @Column(name = "booking_price", precision = 21, scale = 2)
    private BigDecimal bookingPrice;

    @Column(name = "extra_fee", precision = 21, scale = 2)
    private BigDecimal extraFee;

    @Column(name = "total_price", precision = 21, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private PaulBargainStatus bookingStatus;

    @Size(max = 10485760)
    @Column(name = "history_ref_jason", length = 10485760)
    private String historyRefJason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "application", "membershipTier", "alVueVueUsage", "membershipTierVi", "alVueVueViUsage" },
        allowSetters = true
    )
    private AlPacino customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "agency", "avatar", "internalUser", "appUser", "application", "agentRoles" }, allowSetters = true)
    private EdSheeranVi personInCharge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium", "jelloInitiumVi", "inhouseInitiumVi" },
        allowSetters = true
    )
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlPyuJokerVi id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBookingNo() {
        return this.bookingNo;
    }

    public AlPyuJokerVi bookingNo(String bookingNo) {
        this.setBookingNo(bookingNo);
        return this;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getNoteHeitiga() {
        return this.noteHeitiga;
    }

    public AlPyuJokerVi noteHeitiga(String noteHeitiga) {
        this.setNoteHeitiga(noteHeitiga);
        return this;
    }

    public void setNoteHeitiga(String noteHeitiga) {
        this.noteHeitiga = noteHeitiga;
    }

    public BenedictType getPeriodType() {
        return this.periodType;
    }

    public AlPyuJokerVi periodType(BenedictType periodType) {
        this.setPeriodType(periodType);
        return this;
    }

    public void setPeriodType(BenedictType periodType) {
        this.periodType = periodType;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public AlPyuJokerVi fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return this.toDate;
    }

    public AlPyuJokerVi toDate(Instant toDate) {
        this.setToDate(toDate);
        return this;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public Instant getCheckInDate() {
        return this.checkInDate;
    }

    public AlPyuJokerVi checkInDate(Instant checkInDate) {
        this.setCheckInDate(checkInDate);
        return this;
    }

    public void setCheckInDate(Instant checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Instant getCheckOutDate() {
        return this.checkOutDate;
    }

    public AlPyuJokerVi checkOutDate(Instant checkOutDate) {
        this.setCheckOutDate(checkOutDate);
        return this;
    }

    public void setCheckOutDate(Instant checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getNumberOfAdults() {
        return this.numberOfAdults;
    }

    public AlPyuJokerVi numberOfAdults(Integer numberOfAdults) {
        this.setNumberOfAdults(numberOfAdults);
        return this;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public Integer getNumberOfPreschoolers() {
        return this.numberOfPreschoolers;
    }

    public AlPyuJokerVi numberOfPreschoolers(Integer numberOfPreschoolers) {
        this.setNumberOfPreschoolers(numberOfPreschoolers);
        return this;
    }

    public void setNumberOfPreschoolers(Integer numberOfPreschoolers) {
        this.numberOfPreschoolers = numberOfPreschoolers;
    }

    public Integer getNumberOfChildren() {
        return this.numberOfChildren;
    }

    public AlPyuJokerVi numberOfChildren(Integer numberOfChildren) {
        this.setNumberOfChildren(numberOfChildren);
        return this;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public BigDecimal getBookingPrice() {
        return this.bookingPrice;
    }

    public AlPyuJokerVi bookingPrice(BigDecimal bookingPrice) {
        this.setBookingPrice(bookingPrice);
        return this;
    }

    public void setBookingPrice(BigDecimal bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public BigDecimal getExtraFee() {
        return this.extraFee;
    }

    public AlPyuJokerVi extraFee(BigDecimal extraFee) {
        this.setExtraFee(extraFee);
        return this;
    }

    public void setExtraFee(BigDecimal extraFee) {
        this.extraFee = extraFee;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public AlPyuJokerVi totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaulBargainStatus getBookingStatus() {
        return this.bookingStatus;
    }

    public AlPyuJokerVi bookingStatus(PaulBargainStatus bookingStatus) {
        this.setBookingStatus(bookingStatus);
        return this;
    }

    public void setBookingStatus(PaulBargainStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getHistoryRefJason() {
        return this.historyRefJason;
    }

    public AlPyuJokerVi historyRefJason(String historyRefJason) {
        this.setHistoryRefJason(historyRefJason);
        return this;
    }

    public void setHistoryRefJason(String historyRefJason) {
        this.historyRefJason = historyRefJason;
    }

    public AlPacino getCustomer() {
        return this.customer;
    }

    public void setCustomer(AlPacino alPacino) {
        this.customer = alPacino;
    }

    public AlPyuJokerVi customer(AlPacino alPacino) {
        this.setCustomer(alPacino);
        return this;
    }

    public EdSheeranVi getPersonInCharge() {
        return this.personInCharge;
    }

    public void setPersonInCharge(EdSheeranVi edSheeranVi) {
        this.personInCharge = edSheeranVi;
    }

    public AlPyuJokerVi personInCharge(EdSheeranVi edSheeranVi) {
        this.setPersonInCharge(edSheeranVi);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlPyuJokerVi application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlPyuJokerVi)) {
            return false;
        }
        return getId() != null && getId().equals(((AlPyuJokerVi) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPyuJokerVi{" +
            "id=" + getId() +
            ", bookingNo='" + getBookingNo() + "'" +
            ", noteHeitiga='" + getNoteHeitiga() + "'" +
            ", periodType='" + getPeriodType() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", checkInDate='" + getCheckInDate() + "'" +
            ", checkOutDate='" + getCheckOutDate() + "'" +
            ", numberOfAdults=" + getNumberOfAdults() +
            ", numberOfPreschoolers=" + getNumberOfPreschoolers() +
            ", numberOfChildren=" + getNumberOfChildren() +
            ", bookingPrice=" + getBookingPrice() +
            ", extraFee=" + getExtraFee() +
            ", totalPrice=" + getTotalPrice() +
            ", bookingStatus='" + getBookingStatus() + "'" +
            ", historyRefJason='" + getHistoryRefJason() + "'" +
            "}";
    }
}
