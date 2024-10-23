package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.repository.SupplierViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierVi}.
 */
@Service
@Transactional
public class SupplierViService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViService.class);

    private final SupplierViRepository supplierViRepository;

    public SupplierViService(SupplierViRepository supplierViRepository) {
        this.supplierViRepository = supplierViRepository;
    }

    /**
     * Save a supplierVi.
     *
     * @param supplierVi the entity to save.
     * @return the persisted entity.
     */
    public SupplierVi save(SupplierVi supplierVi) {
        LOG.debug("Request to save SupplierVi : {}", supplierVi);
        return supplierViRepository.save(supplierVi);
    }

    /**
     * Update a supplierVi.
     *
     * @param supplierVi the entity to save.
     * @return the persisted entity.
     */
    public SupplierVi update(SupplierVi supplierVi) {
        LOG.debug("Request to update SupplierVi : {}", supplierVi);
        return supplierViRepository.save(supplierVi);
    }

    /**
     * Partially update a supplierVi.
     *
     * @param supplierVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierVi> partialUpdate(SupplierVi supplierVi) {
        LOG.debug("Request to partially update SupplierVi : {}", supplierVi);

        return supplierViRepository
            .findById(supplierVi.getId())
            .map(existingSupplierVi -> {
                if (supplierVi.getName() != null) {
                    existingSupplierVi.setName(supplierVi.getName());
                }
                if (supplierVi.getContactPerson() != null) {
                    existingSupplierVi.setContactPerson(supplierVi.getContactPerson());
                }
                if (supplierVi.getEmail() != null) {
                    existingSupplierVi.setEmail(supplierVi.getEmail());
                }
                if (supplierVi.getPhoneNumber() != null) {
                    existingSupplierVi.setPhoneNumber(supplierVi.getPhoneNumber());
                }

                return existingSupplierVi;
            })
            .map(supplierViRepository::save);
    }

    /**
     * Get all the supplierVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierVi> findAllWithEagerRelationships(Pageable pageable) {
        return supplierViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierVi> findOne(Long id) {
        LOG.debug("Request to get SupplierVi : {}", id);
        return supplierViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierVi : {}", id);
        supplierViRepository.deleteById(id);
    }
}
