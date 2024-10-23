package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierGamma;
import xyz.jhmapstruct.repository.SupplierGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierGamma}.
 */
@Service
@Transactional
public class SupplierGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierGammaService.class);

    private final SupplierGammaRepository supplierGammaRepository;

    public SupplierGammaService(SupplierGammaRepository supplierGammaRepository) {
        this.supplierGammaRepository = supplierGammaRepository;
    }

    /**
     * Save a supplierGamma.
     *
     * @param supplierGamma the entity to save.
     * @return the persisted entity.
     */
    public SupplierGamma save(SupplierGamma supplierGamma) {
        LOG.debug("Request to save SupplierGamma : {}", supplierGamma);
        return supplierGammaRepository.save(supplierGamma);
    }

    /**
     * Update a supplierGamma.
     *
     * @param supplierGamma the entity to save.
     * @return the persisted entity.
     */
    public SupplierGamma update(SupplierGamma supplierGamma) {
        LOG.debug("Request to update SupplierGamma : {}", supplierGamma);
        return supplierGammaRepository.save(supplierGamma);
    }

    /**
     * Partially update a supplierGamma.
     *
     * @param supplierGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierGamma> partialUpdate(SupplierGamma supplierGamma) {
        LOG.debug("Request to partially update SupplierGamma : {}", supplierGamma);

        return supplierGammaRepository
            .findById(supplierGamma.getId())
            .map(existingSupplierGamma -> {
                if (supplierGamma.getName() != null) {
                    existingSupplierGamma.setName(supplierGamma.getName());
                }
                if (supplierGamma.getContactPerson() != null) {
                    existingSupplierGamma.setContactPerson(supplierGamma.getContactPerson());
                }
                if (supplierGamma.getEmail() != null) {
                    existingSupplierGamma.setEmail(supplierGamma.getEmail());
                }
                if (supplierGamma.getPhoneNumber() != null) {
                    existingSupplierGamma.setPhoneNumber(supplierGamma.getPhoneNumber());
                }

                return existingSupplierGamma;
            })
            .map(supplierGammaRepository::save);
    }

    /**
     * Get all the supplierGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierGamma> findAllWithEagerRelationships(Pageable pageable) {
        return supplierGammaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierGamma> findOne(Long id) {
        LOG.debug("Request to get SupplierGamma : {}", id);
        return supplierGammaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierGamma : {}", id);
        supplierGammaRepository.deleteById(id);
    }
}
