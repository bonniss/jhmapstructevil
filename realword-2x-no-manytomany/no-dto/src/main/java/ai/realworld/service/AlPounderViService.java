package ai.realworld.service;

import ai.realworld.domain.AlPounderVi;
import ai.realworld.repository.AlPounderViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPounderVi}.
 */
@Service
@Transactional
public class AlPounderViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderViService.class);

    private final AlPounderViRepository alPounderViRepository;

    public AlPounderViService(AlPounderViRepository alPounderViRepository) {
        this.alPounderViRepository = alPounderViRepository;
    }

    /**
     * Save a alPounderVi.
     *
     * @param alPounderVi the entity to save.
     * @return the persisted entity.
     */
    public AlPounderVi save(AlPounderVi alPounderVi) {
        LOG.debug("Request to save AlPounderVi : {}", alPounderVi);
        return alPounderViRepository.save(alPounderVi);
    }

    /**
     * Update a alPounderVi.
     *
     * @param alPounderVi the entity to save.
     * @return the persisted entity.
     */
    public AlPounderVi update(AlPounderVi alPounderVi) {
        LOG.debug("Request to update AlPounderVi : {}", alPounderVi);
        return alPounderViRepository.save(alPounderVi);
    }

    /**
     * Partially update a alPounderVi.
     *
     * @param alPounderVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPounderVi> partialUpdate(AlPounderVi alPounderVi) {
        LOG.debug("Request to partially update AlPounderVi : {}", alPounderVi);

        return alPounderViRepository
            .findById(alPounderVi.getId())
            .map(existingAlPounderVi -> {
                if (alPounderVi.getName() != null) {
                    existingAlPounderVi.setName(alPounderVi.getName());
                }
                if (alPounderVi.getWeight() != null) {
                    existingAlPounderVi.setWeight(alPounderVi.getWeight());
                }

                return existingAlPounderVi;
            })
            .map(alPounderViRepository::save);
    }

    /**
     * Get one alPounderVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPounderVi> findOne(Long id) {
        LOG.debug("Request to get AlPounderVi : {}", id);
        return alPounderViRepository.findById(id);
    }

    /**
     * Delete the alPounderVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPounderVi : {}", id);
        alPounderViRepository.deleteById(id);
    }
}
