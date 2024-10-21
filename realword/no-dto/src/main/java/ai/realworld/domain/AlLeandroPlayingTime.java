package ai.realworld.domain;

import ai.realworld.domain.enumeration.MaBooDragonBall;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlLeandroPlayingTime.
 */
@Entity
@Table(name = "al_leandro_playing_time")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlLeandroPlayingTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MaBooDragonBall status;

    @Column(name = "won_date")
    private Instant wonDate;

    @Column(name = "sent_award_to_player_at")
    private Instant sentAwardToPlayerAt;

    @Column(name = "sent_award_to_player_by")
    private String sentAwardToPlayerBy;

    @Column(name = "player_received_the_award_at")
    private Instant playerReceivedTheAwardAt;

    @Column(name = "play_source_time")
    private String playSourceTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "programBackground", "wheelBackground", "application", "awards" }, allowSetters = true)
    private AlLeandro maggi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "application", "membershipTier", "alVueVueUsage" }, allowSetters = true)
    private AlPacino user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "image", "maggi", "application" }, allowSetters = true)
    private AlDesire award;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logo", "appManager", "organization", "jelloInitium", "inhouseInitium" }, allowSetters = true)
    private JohnLennon application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AlLeandroPlayingTime id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MaBooDragonBall getStatus() {
        return this.status;
    }

    public AlLeandroPlayingTime status(MaBooDragonBall status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(MaBooDragonBall status) {
        this.status = status;
    }

    public Instant getWonDate() {
        return this.wonDate;
    }

    public AlLeandroPlayingTime wonDate(Instant wonDate) {
        this.setWonDate(wonDate);
        return this;
    }

    public void setWonDate(Instant wonDate) {
        this.wonDate = wonDate;
    }

    public Instant getSentAwardToPlayerAt() {
        return this.sentAwardToPlayerAt;
    }

    public AlLeandroPlayingTime sentAwardToPlayerAt(Instant sentAwardToPlayerAt) {
        this.setSentAwardToPlayerAt(sentAwardToPlayerAt);
        return this;
    }

    public void setSentAwardToPlayerAt(Instant sentAwardToPlayerAt) {
        this.sentAwardToPlayerAt = sentAwardToPlayerAt;
    }

    public String getSentAwardToPlayerBy() {
        return this.sentAwardToPlayerBy;
    }

    public AlLeandroPlayingTime sentAwardToPlayerBy(String sentAwardToPlayerBy) {
        this.setSentAwardToPlayerBy(sentAwardToPlayerBy);
        return this;
    }

    public void setSentAwardToPlayerBy(String sentAwardToPlayerBy) {
        this.sentAwardToPlayerBy = sentAwardToPlayerBy;
    }

    public Instant getPlayerReceivedTheAwardAt() {
        return this.playerReceivedTheAwardAt;
    }

    public AlLeandroPlayingTime playerReceivedTheAwardAt(Instant playerReceivedTheAwardAt) {
        this.setPlayerReceivedTheAwardAt(playerReceivedTheAwardAt);
        return this;
    }

    public void setPlayerReceivedTheAwardAt(Instant playerReceivedTheAwardAt) {
        this.playerReceivedTheAwardAt = playerReceivedTheAwardAt;
    }

    public String getPlaySourceTime() {
        return this.playSourceTime;
    }

    public AlLeandroPlayingTime playSourceTime(String playSourceTime) {
        this.setPlaySourceTime(playSourceTime);
        return this;
    }

    public void setPlaySourceTime(String playSourceTime) {
        this.playSourceTime = playSourceTime;
    }

    public AlLeandro getMaggi() {
        return this.maggi;
    }

    public void setMaggi(AlLeandro alLeandro) {
        this.maggi = alLeandro;
    }

    public AlLeandroPlayingTime maggi(AlLeandro alLeandro) {
        this.setMaggi(alLeandro);
        return this;
    }

    public AlPacino getUser() {
        return this.user;
    }

    public void setUser(AlPacino alPacino) {
        this.user = alPacino;
    }

    public AlLeandroPlayingTime user(AlPacino alPacino) {
        this.setUser(alPacino);
        return this;
    }

    public AlDesire getAward() {
        return this.award;
    }

    public void setAward(AlDesire alDesire) {
        this.award = alDesire;
    }

    public AlLeandroPlayingTime award(AlDesire alDesire) {
        this.setAward(alDesire);
        return this;
    }

    public JohnLennon getApplication() {
        return this.application;
    }

    public void setApplication(JohnLennon johnLennon) {
        this.application = johnLennon;
    }

    public AlLeandroPlayingTime application(JohnLennon johnLennon) {
        this.setApplication(johnLennon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlLeandroPlayingTime)) {
            return false;
        }
        return getId() != null && getId().equals(((AlLeandroPlayingTime) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlLeandroPlayingTime{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", wonDate='" + getWonDate() + "'" +
            ", sentAwardToPlayerAt='" + getSentAwardToPlayerAt() + "'" +
            ", sentAwardToPlayerBy='" + getSentAwardToPlayerBy() + "'" +
            ", playerReceivedTheAwardAt='" + getPlayerReceivedTheAwardAt() + "'" +
            ", playSourceTime='" + getPlaySourceTime() + "'" +
            "}";
    }
}
