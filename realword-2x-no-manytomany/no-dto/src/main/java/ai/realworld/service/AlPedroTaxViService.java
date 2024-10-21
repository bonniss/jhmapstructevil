package ai.realworld.service;

import ai.realworld.domain.AlPedroTaxVi;
import ai.realworld.repository.AlPedroTaxViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPedroTaxVi}.
 */
@Service
@Transactional
public class AlPedroTaxViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxViService.class);

    private final AlPedroTaxViRepository alPedroTaxViRepository;

    public AlPedroTaxViService(AlPedroTaxViRepository alPedroTaxViRepository) {
        this.alPedroTaxViRepository = alPedroTaxViRepository;
    }

    /**
     * Save a alPedroTaxVi.
     *
     * @param alPedroTaxVi the entity to save.
     * @return the persisted entity.
     */
    public AlPedroTaxVi save(AlPedroTaxVi alPedroTaxVi) {
        LOG.debug("Request to save AlPedroTaxVi : {}", alPedroTaxVi);
        return alPedroTaxViRepository.save(alPedroTaxVi);
    }

    /**
     * Update a alPedroTaxVi.
     *
     * @param alPedroTaxVi the entity to save.
     * @return the persisted entity.
     */
    public AlPedroTaxVi update(AlPedroTaxVi alPedroTaxVi) {
        LOG.debug("Request to update AlPedroTaxVi : {}", alPedroTaxVi);
        return alPedroTaxViRepository.save(alPedroTaxVi);
    }

    /**
     * Partially update a alPedroTaxVi.
     *
     * @param alPedroTaxVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPedroTaxVi> partialUpdate(AlPedroTaxVi alPedroTaxVi) {
        LOG.debug("Request to partially update AlPedroTaxVi : {}", alPedroTaxVi);

        return alPedroTaxViRepository
            .findById(alPedroTaxVi.getId())
            .map(existingAlPedroTaxVi -> {
                if (alPedroTaxVi.getName() != null) {
                    existingAlPedroTaxVi.setName(alPedroTaxVi.getName());
                }
                if (alPedroTaxVi.getDescription() != null) {
                    existingAlPedroTaxVi.setDescription(alPedroTaxVi.getDescription());
                }
                if (alPedroTaxVi.getWeight() != null) {
                    existingAlPedroTaxVi.setWeight(alPedroTaxVi.getWeight());
                }
                if (alPedroTaxVi.getPropertyType() != null) {
                    existingAlPedroTaxVi.setPropertyType(alPedroTaxVi.getPropertyType());
                }

                return existingAlPedroTaxVi;
            })
            .map(alPedroTaxViRepository::save);
    }

    /**
     * Get one alPedroTaxVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPedroTaxVi> findOne(Long id) {
        LOG.debug("Request to get AlPedroTaxVi : {}", id);
        return alPedroTaxViRepository.findById(id);
    }

    /**
     * Delete the alPedroTaxVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPedroTaxVi : {}", id);
        alPedroTaxViRepository.deleteById(id);
    }
}
