package ai.realworld.service;

import ai.realworld.domain.SicilyUmetoVi;
import ai.realworld.repository.SicilyUmetoViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.SicilyUmetoVi}.
 */
@Service
@Transactional
public class SicilyUmetoViService {

    private static final Logger LOG = LoggerFactory.getLogger(SicilyUmetoViService.class);

    private final SicilyUmetoViRepository sicilyUmetoViRepository;

    public SicilyUmetoViService(SicilyUmetoViRepository sicilyUmetoViRepository) {
        this.sicilyUmetoViRepository = sicilyUmetoViRepository;
    }

    /**
     * Save a sicilyUmetoVi.
     *
     * @param sicilyUmetoVi the entity to save.
     * @return the persisted entity.
     */
    public SicilyUmetoVi save(SicilyUmetoVi sicilyUmetoVi) {
        LOG.debug("Request to save SicilyUmetoVi : {}", sicilyUmetoVi);
        return sicilyUmetoViRepository.save(sicilyUmetoVi);
    }

    /**
     * Update a sicilyUmetoVi.
     *
     * @param sicilyUmetoVi the entity to save.
     * @return the persisted entity.
     */
    public SicilyUmetoVi update(SicilyUmetoVi sicilyUmetoVi) {
        LOG.debug("Request to update SicilyUmetoVi : {}", sicilyUmetoVi);
        return sicilyUmetoViRepository.save(sicilyUmetoVi);
    }

    /**
     * Partially update a sicilyUmetoVi.
     *
     * @param sicilyUmetoVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SicilyUmetoVi> partialUpdate(SicilyUmetoVi sicilyUmetoVi) {
        LOG.debug("Request to partially update SicilyUmetoVi : {}", sicilyUmetoVi);

        return sicilyUmetoViRepository
            .findById(sicilyUmetoVi.getId())
            .map(existingSicilyUmetoVi -> {
                if (sicilyUmetoVi.getType() != null) {
                    existingSicilyUmetoVi.setType(sicilyUmetoVi.getType());
                }
                if (sicilyUmetoVi.getContent() != null) {
                    existingSicilyUmetoVi.setContent(sicilyUmetoVi.getContent());
                }

                return existingSicilyUmetoVi;
            })
            .map(sicilyUmetoViRepository::save);
    }

    /**
     * Get one sicilyUmetoVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SicilyUmetoVi> findOne(Long id) {
        LOG.debug("Request to get SicilyUmetoVi : {}", id);
        return sicilyUmetoViRepository.findById(id);
    }

    /**
     * Delete the sicilyUmetoVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SicilyUmetoVi : {}", id);
        sicilyUmetoViRepository.deleteById(id);
    }
}
