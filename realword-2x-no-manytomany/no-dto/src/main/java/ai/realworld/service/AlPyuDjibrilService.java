package ai.realworld.service;

import ai.realworld.domain.AlPyuDjibril;
import ai.realworld.repository.AlPyuDjibrilRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuDjibril}.
 */
@Service
@Transactional
public class AlPyuDjibrilService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilService.class);

    private final AlPyuDjibrilRepository alPyuDjibrilRepository;

    public AlPyuDjibrilService(AlPyuDjibrilRepository alPyuDjibrilRepository) {
        this.alPyuDjibrilRepository = alPyuDjibrilRepository;
    }

    /**
     * Save a alPyuDjibril.
     *
     * @param alPyuDjibril the entity to save.
     * @return the persisted entity.
     */
    public AlPyuDjibril save(AlPyuDjibril alPyuDjibril) {
        LOG.debug("Request to save AlPyuDjibril : {}", alPyuDjibril);
        return alPyuDjibrilRepository.save(alPyuDjibril);
    }

    /**
     * Update a alPyuDjibril.
     *
     * @param alPyuDjibril the entity to save.
     * @return the persisted entity.
     */
    public AlPyuDjibril update(AlPyuDjibril alPyuDjibril) {
        LOG.debug("Request to update AlPyuDjibril : {}", alPyuDjibril);
        return alPyuDjibrilRepository.save(alPyuDjibril);
    }

    /**
     * Partially update a alPyuDjibril.
     *
     * @param alPyuDjibril the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuDjibril> partialUpdate(AlPyuDjibril alPyuDjibril) {
        LOG.debug("Request to partially update AlPyuDjibril : {}", alPyuDjibril);

        return alPyuDjibrilRepository
            .findById(alPyuDjibril.getId())
            .map(existingAlPyuDjibril -> {
                if (alPyuDjibril.getRateType() != null) {
                    existingAlPyuDjibril.setRateType(alPyuDjibril.getRateType());
                }
                if (alPyuDjibril.getRate() != null) {
                    existingAlPyuDjibril.setRate(alPyuDjibril.getRate());
                }
                if (alPyuDjibril.getIsEnabled() != null) {
                    existingAlPyuDjibril.setIsEnabled(alPyuDjibril.getIsEnabled());
                }

                return existingAlPyuDjibril;
            })
            .map(alPyuDjibrilRepository::save);
    }

    /**
     * Get one alPyuDjibril by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuDjibril> findOne(Long id) {
        LOG.debug("Request to get AlPyuDjibril : {}", id);
        return alPyuDjibrilRepository.findById(id);
    }

    /**
     * Delete the alPyuDjibril by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPyuDjibril : {}", id);
        alPyuDjibrilRepository.deleteById(id);
    }
}
