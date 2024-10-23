package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.repository.SupplierMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierMi}.
 */
@Service
@Transactional
public class SupplierMiService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiService.class);

    private final SupplierMiRepository supplierMiRepository;

    public SupplierMiService(SupplierMiRepository supplierMiRepository) {
        this.supplierMiRepository = supplierMiRepository;
    }

    /**
     * Save a supplierMi.
     *
     * @param supplierMi the entity to save.
     * @return the persisted entity.
     */
    public SupplierMi save(SupplierMi supplierMi) {
        LOG.debug("Request to save SupplierMi : {}", supplierMi);
        return supplierMiRepository.save(supplierMi);
    }

    /**
     * Update a supplierMi.
     *
     * @param supplierMi the entity to save.
     * @return the persisted entity.
     */
    public SupplierMi update(SupplierMi supplierMi) {
        LOG.debug("Request to update SupplierMi : {}", supplierMi);
        return supplierMiRepository.save(supplierMi);
    }

    /**
     * Partially update a supplierMi.
     *
     * @param supplierMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierMi> partialUpdate(SupplierMi supplierMi) {
        LOG.debug("Request to partially update SupplierMi : {}", supplierMi);

        return supplierMiRepository
            .findById(supplierMi.getId())
            .map(existingSupplierMi -> {
                if (supplierMi.getName() != null) {
                    existingSupplierMi.setName(supplierMi.getName());
                }
                if (supplierMi.getContactPerson() != null) {
                    existingSupplierMi.setContactPerson(supplierMi.getContactPerson());
                }
                if (supplierMi.getEmail() != null) {
                    existingSupplierMi.setEmail(supplierMi.getEmail());
                }
                if (supplierMi.getPhoneNumber() != null) {
                    existingSupplierMi.setPhoneNumber(supplierMi.getPhoneNumber());
                }

                return existingSupplierMi;
            })
            .map(supplierMiRepository::save);
    }

    /**
     * Get all the supplierMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierMi> findAllWithEagerRelationships(Pageable pageable) {
        return supplierMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierMi> findOne(Long id) {
        LOG.debug("Request to get SupplierMi : {}", id);
        return supplierMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierMi : {}", id);
        supplierMiRepository.deleteById(id);
    }
}
