package ai.realworld.service;

import ai.realworld.domain.AlPyuDjibrilVi;
import ai.realworld.repository.AlPyuDjibrilViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuDjibrilVi}.
 */
@Service
@Transactional
public class AlPyuDjibrilViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilViService.class);

    private final AlPyuDjibrilViRepository alPyuDjibrilViRepository;

    public AlPyuDjibrilViService(AlPyuDjibrilViRepository alPyuDjibrilViRepository) {
        this.alPyuDjibrilViRepository = alPyuDjibrilViRepository;
    }

    /**
     * Save a alPyuDjibrilVi.
     *
     * @param alPyuDjibrilVi the entity to save.
     * @return the persisted entity.
     */
    public AlPyuDjibrilVi save(AlPyuDjibrilVi alPyuDjibrilVi) {
        LOG.debug("Request to save AlPyuDjibrilVi : {}", alPyuDjibrilVi);
        return alPyuDjibrilViRepository.save(alPyuDjibrilVi);
    }

    /**
     * Update a alPyuDjibrilVi.
     *
     * @param alPyuDjibrilVi the entity to save.
     * @return the persisted entity.
     */
    public AlPyuDjibrilVi update(AlPyuDjibrilVi alPyuDjibrilVi) {
        LOG.debug("Request to update AlPyuDjibrilVi : {}", alPyuDjibrilVi);
        return alPyuDjibrilViRepository.save(alPyuDjibrilVi);
    }

    /**
     * Partially update a alPyuDjibrilVi.
     *
     * @param alPyuDjibrilVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuDjibrilVi> partialUpdate(AlPyuDjibrilVi alPyuDjibrilVi) {
        LOG.debug("Request to partially update AlPyuDjibrilVi : {}", alPyuDjibrilVi);

        return alPyuDjibrilViRepository
            .findById(alPyuDjibrilVi.getId())
            .map(existingAlPyuDjibrilVi -> {
                if (alPyuDjibrilVi.getRateType() != null) {
                    existingAlPyuDjibrilVi.setRateType(alPyuDjibrilVi.getRateType());
                }
                if (alPyuDjibrilVi.getRate() != null) {
                    existingAlPyuDjibrilVi.setRate(alPyuDjibrilVi.getRate());
                }
                if (alPyuDjibrilVi.getIsEnabled() != null) {
                    existingAlPyuDjibrilVi.setIsEnabled(alPyuDjibrilVi.getIsEnabled());
                }

                return existingAlPyuDjibrilVi;
            })
            .map(alPyuDjibrilViRepository::save);
    }

    /**
     * Get one alPyuDjibrilVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuDjibrilVi> findOne(Long id) {
        LOG.debug("Request to get AlPyuDjibrilVi : {}", id);
        return alPyuDjibrilViRepository.findById(id);
    }

    /**
     * Delete the alPyuDjibrilVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPyuDjibrilVi : {}", id);
        alPyuDjibrilViRepository.deleteById(id);
    }
}
