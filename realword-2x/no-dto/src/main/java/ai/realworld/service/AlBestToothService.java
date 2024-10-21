package ai.realworld.service;

import ai.realworld.domain.AlBestTooth;
import ai.realworld.repository.AlBestToothRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlBestTooth}.
 */
@Service
@Transactional
public class AlBestToothService {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothService.class);

    private final AlBestToothRepository alBestToothRepository;

    public AlBestToothService(AlBestToothRepository alBestToothRepository) {
        this.alBestToothRepository = alBestToothRepository;
    }

    /**
     * Save a alBestTooth.
     *
     * @param alBestTooth the entity to save.
     * @return the persisted entity.
     */
    public AlBestTooth save(AlBestTooth alBestTooth) {
        LOG.debug("Request to save AlBestTooth : {}", alBestTooth);
        return alBestToothRepository.save(alBestTooth);
    }

    /**
     * Update a alBestTooth.
     *
     * @param alBestTooth the entity to save.
     * @return the persisted entity.
     */
    public AlBestTooth update(AlBestTooth alBestTooth) {
        LOG.debug("Request to update AlBestTooth : {}", alBestTooth);
        return alBestToothRepository.save(alBestTooth);
    }

    /**
     * Partially update a alBestTooth.
     *
     * @param alBestTooth the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlBestTooth> partialUpdate(AlBestTooth alBestTooth) {
        LOG.debug("Request to partially update AlBestTooth : {}", alBestTooth);

        return alBestToothRepository
            .findById(alBestTooth.getId())
            .map(existingAlBestTooth -> {
                if (alBestTooth.getName() != null) {
                    existingAlBestTooth.setName(alBestTooth.getName());
                }

                return existingAlBestTooth;
            })
            .map(alBestToothRepository::save);
    }

    /**
     * Get one alBestTooth by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlBestTooth> findOne(Long id) {
        LOG.debug("Request to get AlBestTooth : {}", id);
        return alBestToothRepository.findById(id);
    }

    /**
     * Delete the alBestTooth by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlBestTooth : {}", id);
        alBestToothRepository.deleteById(id);
    }
}
