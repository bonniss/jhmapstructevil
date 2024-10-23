package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierBeta;
import xyz.jhmapstruct.repository.SupplierBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierBeta}.
 */
@Service
@Transactional
public class SupplierBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierBetaService.class);

    private final SupplierBetaRepository supplierBetaRepository;

    public SupplierBetaService(SupplierBetaRepository supplierBetaRepository) {
        this.supplierBetaRepository = supplierBetaRepository;
    }

    /**
     * Save a supplierBeta.
     *
     * @param supplierBeta the entity to save.
     * @return the persisted entity.
     */
    public SupplierBeta save(SupplierBeta supplierBeta) {
        LOG.debug("Request to save SupplierBeta : {}", supplierBeta);
        return supplierBetaRepository.save(supplierBeta);
    }

    /**
     * Update a supplierBeta.
     *
     * @param supplierBeta the entity to save.
     * @return the persisted entity.
     */
    public SupplierBeta update(SupplierBeta supplierBeta) {
        LOG.debug("Request to update SupplierBeta : {}", supplierBeta);
        return supplierBetaRepository.save(supplierBeta);
    }

    /**
     * Partially update a supplierBeta.
     *
     * @param supplierBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierBeta> partialUpdate(SupplierBeta supplierBeta) {
        LOG.debug("Request to partially update SupplierBeta : {}", supplierBeta);

        return supplierBetaRepository
            .findById(supplierBeta.getId())
            .map(existingSupplierBeta -> {
                if (supplierBeta.getName() != null) {
                    existingSupplierBeta.setName(supplierBeta.getName());
                }
                if (supplierBeta.getContactPerson() != null) {
                    existingSupplierBeta.setContactPerson(supplierBeta.getContactPerson());
                }
                if (supplierBeta.getEmail() != null) {
                    existingSupplierBeta.setEmail(supplierBeta.getEmail());
                }
                if (supplierBeta.getPhoneNumber() != null) {
                    existingSupplierBeta.setPhoneNumber(supplierBeta.getPhoneNumber());
                }

                return existingSupplierBeta;
            })
            .map(supplierBetaRepository::save);
    }

    /**
     * Get all the supplierBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierBeta> findAllWithEagerRelationships(Pageable pageable) {
        return supplierBetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierBeta> findOne(Long id) {
        LOG.debug("Request to get SupplierBeta : {}", id);
        return supplierBetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierBeta : {}", id);
        supplierBetaRepository.deleteById(id);
    }
}
