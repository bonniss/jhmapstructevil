package ai.realworld.service.dto;

import ai.realworld.domain.enumeration.BenedictType;
import ai.realworld.domain.enumeration.PaulBargainStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ai.realworld.domain.AlPyuJokerVi} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlPyuJokerViDTO implements Serializable {

    private UUID id;

    private String bookingNo;

    @Size(max = 65535)
    private String noteHeitiga;

    private BenedictType periodType;

    private Instant fromDate;

    private Instant toDate;

    private Instant checkInDate;

    private Instant checkOutDate;

    @Min(value = 1)
    private Integer numberOfAdults;

    @Min(value = 0)
    private Integer numberOfPreschoolers;

    @Min(value = 0)
    private Integer numberOfChildren;

    private BigDecimal bookingPrice;

    private BigDecimal extraFee;

    private BigDecimal totalPrice;

    private PaulBargainStatus bookingStatus;

    @Size(max = 10485760)
    private String historyRefJason;

    private AlPacinoDTO customer;

    private EdSheeranViDTO personInCharge;

    private JohnLennonDTO application;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getNoteHeitiga() {
        return noteHeitiga;
    }

    public void setNoteHeitiga(String noteHeitiga) {
        this.noteHeitiga = noteHeitiga;
    }

    public BenedictType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(BenedictType periodType) {
        this.periodType = periodType;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public Instant getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Instant checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Instant getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Instant checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public Integer getNumberOfPreschoolers() {
        return numberOfPreschoolers;
    }

    public void setNumberOfPreschoolers(Integer numberOfPreschoolers) {
        this.numberOfPreschoolers = numberOfPreschoolers;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public BigDecimal getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(BigDecimal bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public BigDecimal getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(BigDecimal extraFee) {
        this.extraFee = extraFee;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaulBargainStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(PaulBargainStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getHistoryRefJason() {
        return historyRefJason;
    }

    public void setHistoryRefJason(String historyRefJason) {
        this.historyRefJason = historyRefJason;
    }

    public AlPacinoDTO getCustomer() {
        return customer;
    }

    public void setCustomer(AlPacinoDTO customer) {
        this.customer = customer;
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
        if (!(o instanceof AlPyuJokerViDTO)) {
            return false;
        }

        AlPyuJokerViDTO alPyuJokerViDTO = (AlPyuJokerViDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alPyuJokerViDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlPyuJokerViDTO{" +
            "id='" + getId() + "'" +
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
            ", customer=" + getCustomer() +
            ", personInCharge=" + getPersonInCharge() +
            ", application=" + getApplication() +
            "}";
    }
}
