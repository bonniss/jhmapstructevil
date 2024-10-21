package ai.realworld.service;

import ai.realworld.domain.SicilyUmeto;
import ai.realworld.repository.SicilyUmetoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.SicilyUmeto}.
 */
@Service
@Transactional
public class SicilyUmetoService {

    private static final Logger LOG = LoggerFactory.getLogger(SicilyUmetoService.class);

    private final SicilyUmetoRepository sicilyUmetoRepository;

    public SicilyUmetoService(SicilyUmetoRepository sicilyUmetoRepository) {
        this.sicilyUmetoRepository = sicilyUmetoRepository;
    }

    /**
     * Save a sicilyUmeto.
     *
     * @param sicilyUmeto the entity to save.
     * @return the persisted entity.
     */
    public SicilyUmeto save(SicilyUmeto sicilyUmeto) {
        LOG.debug("Request to save SicilyUmeto : {}", sicilyUmeto);
        return sicilyUmetoRepository.save(sicilyUmeto);
    }

    /**
     * Update a sicilyUmeto.
     *
     * @param sicilyUmeto the entity to save.
     * @return the persisted entity.
     */
    public SicilyUmeto update(SicilyUmeto sicilyUmeto) {
        LOG.debug("Request to update SicilyUmeto : {}", sicilyUmeto);
        return sicilyUmetoRepository.save(sicilyUmeto);
    }

    /**
     * Partially update a sicilyUmeto.
     *
     * @param sicilyUmeto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SicilyUmeto> partialUpdate(SicilyUmeto sicilyUmeto) {
        LOG.debug("Request to partially update SicilyUmeto : {}", sicilyUmeto);

        return sicilyUmetoRepository
            .findById(sicilyUmeto.getId())
            .map(existingSicilyUmeto -> {
                if (sicilyUmeto.getType() != null) {
                    existingSicilyUmeto.setType(sicilyUmeto.getType());
                }
                if (sicilyUmeto.getContent() != null) {
                    existingSicilyUmeto.setContent(sicilyUmeto.getContent());
                }

                return existingSicilyUmeto;
            })
            .map(sicilyUmetoRepository::save);
    }

    /**
     * Get one sicilyUmeto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SicilyUmeto> findOne(Long id) {
        LOG.debug("Request to get SicilyUmeto : {}", id);
        return sicilyUmetoRepository.findById(id);
    }

    /**
     * Delete the sicilyUmeto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SicilyUmeto : {}", id);
        sicilyUmetoRepository.deleteById(id);
    }
}
