package ai.realworld.service;

import ai.realworld.domain.AlPounder;
import ai.realworld.repository.AlPounderRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPounder}.
 */
@Service
@Transactional
public class AlPounderService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderService.class);

    private final AlPounderRepository alPounderRepository;

    public AlPounderService(AlPounderRepository alPounderRepository) {
        this.alPounderRepository = alPounderRepository;
    }

    /**
     * Save a alPounder.
     *
     * @param alPounder the entity to save.
     * @return the persisted entity.
     */
    public AlPounder save(AlPounder alPounder) {
        LOG.debug("Request to save AlPounder : {}", alPounder);
        return alPounderRepository.save(alPounder);
    }

    /**
     * Update a alPounder.
     *
     * @param alPounder the entity to save.
     * @return the persisted entity.
     */
    public AlPounder update(AlPounder alPounder) {
        LOG.debug("Request to update AlPounder : {}", alPounder);
        return alPounderRepository.save(alPounder);
    }

    /**
     * Partially update a alPounder.
     *
     * @param alPounder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPounder> partialUpdate(AlPounder alPounder) {
        LOG.debug("Request to partially update AlPounder : {}", alPounder);

        return alPounderRepository
            .findById(alPounder.getId())
            .map(existingAlPounder -> {
                if (alPounder.getName() != null) {
                    existingAlPounder.setName(alPounder.getName());
                }
                if (alPounder.getWeight() != null) {
                    existingAlPounder.setWeight(alPounder.getWeight());
                }

                return existingAlPounder;
            })
            .map(alPounderRepository::save);
    }

    /**
     * Get one alPounder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPounder> findOne(Long id) {
        LOG.debug("Request to get AlPounder : {}", id);
        return alPounderRepository.findById(id);
    }

    /**
     * Delete the alPounder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPounder : {}", id);
        alPounderRepository.deleteById(id);
    }
}
