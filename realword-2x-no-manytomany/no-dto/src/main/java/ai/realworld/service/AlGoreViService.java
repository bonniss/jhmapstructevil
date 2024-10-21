package ai.realworld.service;

import ai.realworld.domain.AlGoreVi;
import ai.realworld.repository.AlGoreViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGoreVi}.
 */
@Service
@Transactional
public class AlGoreViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreViService.class);

    private final AlGoreViRepository alGoreViRepository;

    public AlGoreViService(AlGoreViRepository alGoreViRepository) {
        this.alGoreViRepository = alGoreViRepository;
    }

    /**
     * Save a alGoreVi.
     *
     * @param alGoreVi the entity to save.
     * @return the persisted entity.
     */
    public AlGoreVi save(AlGoreVi alGoreVi) {
        LOG.debug("Request to save AlGoreVi : {}", alGoreVi);
        return alGoreViRepository.save(alGoreVi);
    }

    /**
     * Update a alGoreVi.
     *
     * @param alGoreVi the entity to save.
     * @return the persisted entity.
     */
    public AlGoreVi update(AlGoreVi alGoreVi) {
        LOG.debug("Request to update AlGoreVi : {}", alGoreVi);
        return alGoreViRepository.save(alGoreVi);
    }

    /**
     * Partially update a alGoreVi.
     *
     * @param alGoreVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoreVi> partialUpdate(AlGoreVi alGoreVi) {
        LOG.debug("Request to partially update AlGoreVi : {}", alGoreVi);

        return alGoreViRepository
            .findById(alGoreVi.getId())
            .map(existingAlGoreVi -> {
                if (alGoreVi.getName() != null) {
                    existingAlGoreVi.setName(alGoreVi.getName());
                }
                if (alGoreVi.getDiscountType() != null) {
                    existingAlGoreVi.setDiscountType(alGoreVi.getDiscountType());
                }
                if (alGoreVi.getDiscountRate() != null) {
                    existingAlGoreVi.setDiscountRate(alGoreVi.getDiscountRate());
                }
                if (alGoreVi.getScope() != null) {
                    existingAlGoreVi.setScope(alGoreVi.getScope());
                }

                return existingAlGoreVi;
            })
            .map(alGoreViRepository::save);
    }

    /**
     * Get one alGoreVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoreVi> findOne(Long id) {
        LOG.debug("Request to get AlGoreVi : {}", id);
        return alGoreViRepository.findById(id);
    }

    /**
     * Delete the alGoreVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlGoreVi : {}", id);
        alGoreViRepository.deleteById(id);
    }
}
