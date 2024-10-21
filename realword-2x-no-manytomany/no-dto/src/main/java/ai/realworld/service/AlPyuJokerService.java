package ai.realworld.service;

import ai.realworld.domain.AlPyuJoker;
import ai.realworld.repository.AlPyuJokerRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuJoker}.
 */
@Service
@Transactional
public class AlPyuJokerService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuJokerService.class);

    private final AlPyuJokerRepository alPyuJokerRepository;

    public AlPyuJokerService(AlPyuJokerRepository alPyuJokerRepository) {
        this.alPyuJokerRepository = alPyuJokerRepository;
    }

    /**
     * Save a alPyuJoker.
     *
     * @param alPyuJoker the entity to save.
     * @return the persisted entity.
     */
    public AlPyuJoker save(AlPyuJoker alPyuJoker) {
        LOG.debug("Request to save AlPyuJoker : {}", alPyuJoker);
        return alPyuJokerRepository.save(alPyuJoker);
    }

    /**
     * Update a alPyuJoker.
     *
     * @param alPyuJoker the entity to save.
     * @return the persisted entity.
     */
    public AlPyuJoker update(AlPyuJoker alPyuJoker) {
        LOG.debug("Request to update AlPyuJoker : {}", alPyuJoker);
        return alPyuJokerRepository.save(alPyuJoker);
    }

    /**
     * Partially update a alPyuJoker.
     *
     * @param alPyuJoker the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuJoker> partialUpdate(AlPyuJoker alPyuJoker) {
        LOG.debug("Request to partially update AlPyuJoker : {}", alPyuJoker);

        return alPyuJokerRepository
            .findById(alPyuJoker.getId())
            .map(existingAlPyuJoker -> {
                if (alPyuJoker.getBookingNo() != null) {
                    existingAlPyuJoker.setBookingNo(alPyuJoker.getBookingNo());
                }
                if (alPyuJoker.getNoteHeitiga() != null) {
                    existingAlPyuJoker.setNoteHeitiga(alPyuJoker.getNoteHeitiga());
                }
                if (alPyuJoker.getPeriodType() != null) {
                    existingAlPyuJoker.setPeriodType(alPyuJoker.getPeriodType());
                }
                if (alPyuJoker.getFromDate() != null) {
                    existingAlPyuJoker.setFromDate(alPyuJoker.getFromDate());
                }
                if (alPyuJoker.getToDate() != null) {
                    existingAlPyuJoker.setToDate(alPyuJoker.getToDate());
                }
                if (alPyuJoker.getCheckInDate() != null) {
                    existingAlPyuJoker.setCheckInDate(alPyuJoker.getCheckInDate());
                }
                if (alPyuJoker.getCheckOutDate() != null) {
                    existingAlPyuJoker.setCheckOutDate(alPyuJoker.getCheckOutDate());
                }
                if (alPyuJoker.getNumberOfAdults() != null) {
                    existingAlPyuJoker.setNumberOfAdults(alPyuJoker.getNumberOfAdults());
                }
                if (alPyuJoker.getNumberOfPreschoolers() != null) {
                    existingAlPyuJoker.setNumberOfPreschoolers(alPyuJoker.getNumberOfPreschoolers());
                }
                if (alPyuJoker.getNumberOfChildren() != null) {
                    existingAlPyuJoker.setNumberOfChildren(alPyuJoker.getNumberOfChildren());
                }
                if (alPyuJoker.getBookingPrice() != null) {
                    existingAlPyuJoker.setBookingPrice(alPyuJoker.getBookingPrice());
                }
                if (alPyuJoker.getExtraFee() != null) {
                    existingAlPyuJoker.setExtraFee(alPyuJoker.getExtraFee());
                }
                if (alPyuJoker.getTotalPrice() != null) {
                    existingAlPyuJoker.setTotalPrice(alPyuJoker.getTotalPrice());
                }
                if (alPyuJoker.getBookingStatus() != null) {
                    existingAlPyuJoker.setBookingStatus(alPyuJoker.getBookingStatus());
                }
                if (alPyuJoker.getHistoryRefJason() != null) {
                    existingAlPyuJoker.setHistoryRefJason(alPyuJoker.getHistoryRefJason());
                }

                return existingAlPyuJoker;
            })
            .map(alPyuJokerRepository::save);
    }

    /**
     * Get one alPyuJoker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuJoker> findOne(UUID id) {
        LOG.debug("Request to get AlPyuJoker : {}", id);
        return alPyuJokerRepository.findById(id);
    }

    /**
     * Delete the alPyuJoker by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPyuJoker : {}", id);
        alPyuJokerRepository.deleteById(id);
    }
}
