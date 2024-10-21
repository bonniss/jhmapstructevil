package ai.realworld.service;

import ai.realworld.domain.AlGore;
import ai.realworld.repository.AlGoreRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGore}.
 */
@Service
@Transactional
public class AlGoreService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreService.class);

    private final AlGoreRepository alGoreRepository;

    public AlGoreService(AlGoreRepository alGoreRepository) {
        this.alGoreRepository = alGoreRepository;
    }

    /**
     * Save a alGore.
     *
     * @param alGore the entity to save.
     * @return the persisted entity.
     */
    public AlGore save(AlGore alGore) {
        LOG.debug("Request to save AlGore : {}", alGore);
        return alGoreRepository.save(alGore);
    }

    /**
     * Update a alGore.
     *
     * @param alGore the entity to save.
     * @return the persisted entity.
     */
    public AlGore update(AlGore alGore) {
        LOG.debug("Request to update AlGore : {}", alGore);
        return alGoreRepository.save(alGore);
    }

    /**
     * Partially update a alGore.
     *
     * @param alGore the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGore> partialUpdate(AlGore alGore) {
        LOG.debug("Request to partially update AlGore : {}", alGore);

        return alGoreRepository
            .findById(alGore.getId())
            .map(existingAlGore -> {
                if (alGore.getName() != null) {
                    existingAlGore.setName(alGore.getName());
                }
                if (alGore.getDiscountType() != null) {
                    existingAlGore.setDiscountType(alGore.getDiscountType());
                }
                if (alGore.getDiscountRate() != null) {
                    existingAlGore.setDiscountRate(alGore.getDiscountRate());
                }
                if (alGore.getScope() != null) {
                    existingAlGore.setScope(alGore.getScope());
                }

                return existingAlGore;
            })
            .map(alGoreRepository::save);
    }

    /**
     * Get one alGore by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGore> findOne(Long id) {
        LOG.debug("Request to get AlGore : {}", id);
        return alGoreRepository.findById(id);
    }

    /**
     * Delete the alGore by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlGore : {}", id);
        alGoreRepository.deleteById(id);
    }
}
