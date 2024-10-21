package ai.realworld.service;

import ai.realworld.domain.AlBestToothVi;
import ai.realworld.repository.AlBestToothViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlBestToothVi}.
 */
@Service
@Transactional
public class AlBestToothViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothViService.class);

    private final AlBestToothViRepository alBestToothViRepository;

    public AlBestToothViService(AlBestToothViRepository alBestToothViRepository) {
        this.alBestToothViRepository = alBestToothViRepository;
    }

    /**
     * Save a alBestToothVi.
     *
     * @param alBestToothVi the entity to save.
     * @return the persisted entity.
     */
    public AlBestToothVi save(AlBestToothVi alBestToothVi) {
        LOG.debug("Request to save AlBestToothVi : {}", alBestToothVi);
        return alBestToothViRepository.save(alBestToothVi);
    }

    /**
     * Update a alBestToothVi.
     *
     * @param alBestToothVi the entity to save.
     * @return the persisted entity.
     */
    public AlBestToothVi update(AlBestToothVi alBestToothVi) {
        LOG.debug("Request to update AlBestToothVi : {}", alBestToothVi);
        return alBestToothViRepository.save(alBestToothVi);
    }

    /**
     * Partially update a alBestToothVi.
     *
     * @param alBestToothVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlBestToothVi> partialUpdate(AlBestToothVi alBestToothVi) {
        LOG.debug("Request to partially update AlBestToothVi : {}", alBestToothVi);

        return alBestToothViRepository
            .findById(alBestToothVi.getId())
            .map(existingAlBestToothVi -> {
                if (alBestToothVi.getName() != null) {
                    existingAlBestToothVi.setName(alBestToothVi.getName());
                }

                return existingAlBestToothVi;
            })
            .map(alBestToothViRepository::save);
    }

    /**
     * Get one alBestToothVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlBestToothVi> findOne(Long id) {
        LOG.debug("Request to get AlBestToothVi : {}", id);
        return alBestToothViRepository.findById(id);
    }

    /**
     * Delete the alBestToothVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlBestToothVi : {}", id);
        alBestToothViRepository.deleteById(id);
    }
}
