package ai.realworld.service;

import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.repository.AlPyuJokerViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuJokerVi}.
 */
@Service
@Transactional
public class AlPyuJokerViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuJokerViService.class);

    private final AlPyuJokerViRepository alPyuJokerViRepository;

    public AlPyuJokerViService(AlPyuJokerViRepository alPyuJokerViRepository) {
        this.alPyuJokerViRepository = alPyuJokerViRepository;
    }

    /**
     * Save a alPyuJokerVi.
     *
     * @param alPyuJokerVi the entity to save.
     * @return the persisted entity.
     */
    public AlPyuJokerVi save(AlPyuJokerVi alPyuJokerVi) {
        LOG.debug("Request to save AlPyuJokerVi : {}", alPyuJokerVi);
        return alPyuJokerViRepository.save(alPyuJokerVi);
    }

    /**
     * Update a alPyuJokerVi.
     *
     * @param alPyuJokerVi the entity to save.
     * @return the persisted entity.
     */
    public AlPyuJokerVi update(AlPyuJokerVi alPyuJokerVi) {
        LOG.debug("Request to update AlPyuJokerVi : {}", alPyuJokerVi);
        return alPyuJokerViRepository.save(alPyuJokerVi);
    }

    /**
     * Partially update a alPyuJokerVi.
     *
     * @param alPyuJokerVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuJokerVi> partialUpdate(AlPyuJokerVi alPyuJokerVi) {
        LOG.debug("Request to partially update AlPyuJokerVi : {}", alPyuJokerVi);

        return alPyuJokerViRepository
            .findById(alPyuJokerVi.getId())
            .map(existingAlPyuJokerVi -> {
                if (alPyuJokerVi.getBookingNo() != null) {
                    existingAlPyuJokerVi.setBookingNo(alPyuJokerVi.getBookingNo());
                }
                if (alPyuJokerVi.getNoteHeitiga() != null) {
                    existingAlPyuJokerVi.setNoteHeitiga(alPyuJokerVi.getNoteHeitiga());
                }
                if (alPyuJokerVi.getPeriodType() != null) {
                    existingAlPyuJokerVi.setPeriodType(alPyuJokerVi.getPeriodType());
                }
                if (alPyuJokerVi.getFromDate() != null) {
                    existingAlPyuJokerVi.setFromDate(alPyuJokerVi.getFromDate());
                }
                if (alPyuJokerVi.getToDate() != null) {
                    existingAlPyuJokerVi.setToDate(alPyuJokerVi.getToDate());
                }
                if (alPyuJokerVi.getCheckInDate() != null) {
                    existingAlPyuJokerVi.setCheckInDate(alPyuJokerVi.getCheckInDate());
                }
                if (alPyuJokerVi.getCheckOutDate() != null) {
                    existingAlPyuJokerVi.setCheckOutDate(alPyuJokerVi.getCheckOutDate());
                }
                if (alPyuJokerVi.getNumberOfAdults() != null) {
                    existingAlPyuJokerVi.setNumberOfAdults(alPyuJokerVi.getNumberOfAdults());
                }
                if (alPyuJokerVi.getNumberOfPreschoolers() != null) {
                    existingAlPyuJokerVi.setNumberOfPreschoolers(alPyuJokerVi.getNumberOfPreschoolers());
                }
                if (alPyuJokerVi.getNumberOfChildren() != null) {
                    existingAlPyuJokerVi.setNumberOfChildren(alPyuJokerVi.getNumberOfChildren());
                }
                if (alPyuJokerVi.getBookingPrice() != null) {
                    existingAlPyuJokerVi.setBookingPrice(alPyuJokerVi.getBookingPrice());
                }
                if (alPyuJokerVi.getExtraFee() != null) {
                    existingAlPyuJokerVi.setExtraFee(alPyuJokerVi.getExtraFee());
                }
                if (alPyuJokerVi.getTotalPrice() != null) {
                    existingAlPyuJokerVi.setTotalPrice(alPyuJokerVi.getTotalPrice());
                }
                if (alPyuJokerVi.getBookingStatus() != null) {
                    existingAlPyuJokerVi.setBookingStatus(alPyuJokerVi.getBookingStatus());
                }
                if (alPyuJokerVi.getHistoryRefJason() != null) {
                    existingAlPyuJokerVi.setHistoryRefJason(alPyuJokerVi.getHistoryRefJason());
                }

                return existingAlPyuJokerVi;
            })
            .map(alPyuJokerViRepository::save);
    }

    /**
     * Get all the alPyuJokerVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlPyuJokerVi> findAllWithEagerRelationships(Pageable pageable) {
        return alPyuJokerViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one alPyuJokerVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuJokerVi> findOne(UUID id) {
        LOG.debug("Request to get AlPyuJokerVi : {}", id);
        return alPyuJokerViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the alPyuJokerVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPyuJokerVi : {}", id);
        alPyuJokerViRepository.deleteById(id);
    }
}
