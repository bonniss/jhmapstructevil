package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.repository.SupplierMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierMiMi}.
 */
@Service
@Transactional
public class SupplierMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiMiService.class);

    private final SupplierMiMiRepository supplierMiMiRepository;

    public SupplierMiMiService(SupplierMiMiRepository supplierMiMiRepository) {
        this.supplierMiMiRepository = supplierMiMiRepository;
    }

    /**
     * Save a supplierMiMi.
     *
     * @param supplierMiMi the entity to save.
     * @return the persisted entity.
     */
    public SupplierMiMi save(SupplierMiMi supplierMiMi) {
        LOG.debug("Request to save SupplierMiMi : {}", supplierMiMi);
        return supplierMiMiRepository.save(supplierMiMi);
    }

    /**
     * Update a supplierMiMi.
     *
     * @param supplierMiMi the entity to save.
     * @return the persisted entity.
     */
    public SupplierMiMi update(SupplierMiMi supplierMiMi) {
        LOG.debug("Request to update SupplierMiMi : {}", supplierMiMi);
        return supplierMiMiRepository.save(supplierMiMi);
    }

    /**
     * Partially update a supplierMiMi.
     *
     * @param supplierMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierMiMi> partialUpdate(SupplierMiMi supplierMiMi) {
        LOG.debug("Request to partially update SupplierMiMi : {}", supplierMiMi);

        return supplierMiMiRepository
            .findById(supplierMiMi.getId())
            .map(existingSupplierMiMi -> {
                if (supplierMiMi.getName() != null) {
                    existingSupplierMiMi.setName(supplierMiMi.getName());
                }
                if (supplierMiMi.getContactPerson() != null) {
                    existingSupplierMiMi.setContactPerson(supplierMiMi.getContactPerson());
                }
                if (supplierMiMi.getEmail() != null) {
                    existingSupplierMiMi.setEmail(supplierMiMi.getEmail());
                }
                if (supplierMiMi.getPhoneNumber() != null) {
                    existingSupplierMiMi.setPhoneNumber(supplierMiMi.getPhoneNumber());
                }

                return existingSupplierMiMi;
            })
            .map(supplierMiMiRepository::save);
    }

    /**
     * Get all the supplierMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return supplierMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierMiMi> findOne(Long id) {
        LOG.debug("Request to get SupplierMiMi : {}", id);
        return supplierMiMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierMiMi : {}", id);
        supplierMiMiRepository.deleteById(id);
    }
}
