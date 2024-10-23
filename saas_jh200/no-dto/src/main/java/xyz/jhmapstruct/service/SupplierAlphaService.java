package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierAlpha;
import xyz.jhmapstruct.repository.SupplierAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierAlpha}.
 */
@Service
@Transactional
public class SupplierAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierAlphaService.class);

    private final SupplierAlphaRepository supplierAlphaRepository;

    public SupplierAlphaService(SupplierAlphaRepository supplierAlphaRepository) {
        this.supplierAlphaRepository = supplierAlphaRepository;
    }

    /**
     * Save a supplierAlpha.
     *
     * @param supplierAlpha the entity to save.
     * @return the persisted entity.
     */
    public SupplierAlpha save(SupplierAlpha supplierAlpha) {
        LOG.debug("Request to save SupplierAlpha : {}", supplierAlpha);
        return supplierAlphaRepository.save(supplierAlpha);
    }

    /**
     * Update a supplierAlpha.
     *
     * @param supplierAlpha the entity to save.
     * @return the persisted entity.
     */
    public SupplierAlpha update(SupplierAlpha supplierAlpha) {
        LOG.debug("Request to update SupplierAlpha : {}", supplierAlpha);
        return supplierAlphaRepository.save(supplierAlpha);
    }

    /**
     * Partially update a supplierAlpha.
     *
     * @param supplierAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierAlpha> partialUpdate(SupplierAlpha supplierAlpha) {
        LOG.debug("Request to partially update SupplierAlpha : {}", supplierAlpha);

        return supplierAlphaRepository
            .findById(supplierAlpha.getId())
            .map(existingSupplierAlpha -> {
                if (supplierAlpha.getName() != null) {
                    existingSupplierAlpha.setName(supplierAlpha.getName());
                }
                if (supplierAlpha.getContactPerson() != null) {
                    existingSupplierAlpha.setContactPerson(supplierAlpha.getContactPerson());
                }
                if (supplierAlpha.getEmail() != null) {
                    existingSupplierAlpha.setEmail(supplierAlpha.getEmail());
                }
                if (supplierAlpha.getPhoneNumber() != null) {
                    existingSupplierAlpha.setPhoneNumber(supplierAlpha.getPhoneNumber());
                }

                return existingSupplierAlpha;
            })
            .map(supplierAlphaRepository::save);
    }

    /**
     * Get all the supplierAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return supplierAlphaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierAlpha> findOne(Long id) {
        LOG.debug("Request to get SupplierAlpha : {}", id);
        return supplierAlphaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierAlpha : {}", id);
        supplierAlphaRepository.deleteById(id);
    }
}
