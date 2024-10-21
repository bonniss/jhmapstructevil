package ai.realworld.service;

import ai.realworld.domain.AlPedroTax;
import ai.realworld.repository.AlPedroTaxRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPedroTax}.
 */
@Service
@Transactional
public class AlPedroTaxService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxService.class);

    private final AlPedroTaxRepository alPedroTaxRepository;

    public AlPedroTaxService(AlPedroTaxRepository alPedroTaxRepository) {
        this.alPedroTaxRepository = alPedroTaxRepository;
    }

    /**
     * Save a alPedroTax.
     *
     * @param alPedroTax the entity to save.
     * @return the persisted entity.
     */
    public AlPedroTax save(AlPedroTax alPedroTax) {
        LOG.debug("Request to save AlPedroTax : {}", alPedroTax);
        return alPedroTaxRepository.save(alPedroTax);
    }

    /**
     * Update a alPedroTax.
     *
     * @param alPedroTax the entity to save.
     * @return the persisted entity.
     */
    public AlPedroTax update(AlPedroTax alPedroTax) {
        LOG.debug("Request to update AlPedroTax : {}", alPedroTax);
        return alPedroTaxRepository.save(alPedroTax);
    }

    /**
     * Partially update a alPedroTax.
     *
     * @param alPedroTax the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPedroTax> partialUpdate(AlPedroTax alPedroTax) {
        LOG.debug("Request to partially update AlPedroTax : {}", alPedroTax);

        return alPedroTaxRepository
            .findById(alPedroTax.getId())
            .map(existingAlPedroTax -> {
                if (alPedroTax.getName() != null) {
                    existingAlPedroTax.setName(alPedroTax.getName());
                }
                if (alPedroTax.getDescription() != null) {
                    existingAlPedroTax.setDescription(alPedroTax.getDescription());
                }
                if (alPedroTax.getWeight() != null) {
                    existingAlPedroTax.setWeight(alPedroTax.getWeight());
                }
                if (alPedroTax.getPropertyType() != null) {
                    existingAlPedroTax.setPropertyType(alPedroTax.getPropertyType());
                }

                return existingAlPedroTax;
            })
            .map(alPedroTaxRepository::save);
    }

    /**
     * Get one alPedroTax by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPedroTax> findOne(Long id) {
        LOG.debug("Request to get AlPedroTax : {}", id);
        return alPedroTaxRepository.findById(id);
    }

    /**
     * Delete the alPedroTax by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPedroTax : {}", id);
        alPedroTaxRepository.deleteById(id);
    }
}
