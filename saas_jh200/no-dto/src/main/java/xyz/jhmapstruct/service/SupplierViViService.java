package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierViVi;
import xyz.jhmapstruct.repository.SupplierViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierViVi}.
 */
@Service
@Transactional
public class SupplierViViService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViViService.class);

    private final SupplierViViRepository supplierViViRepository;

    public SupplierViViService(SupplierViViRepository supplierViViRepository) {
        this.supplierViViRepository = supplierViViRepository;
    }

    /**
     * Save a supplierViVi.
     *
     * @param supplierViVi the entity to save.
     * @return the persisted entity.
     */
    public SupplierViVi save(SupplierViVi supplierViVi) {
        LOG.debug("Request to save SupplierViVi : {}", supplierViVi);
        return supplierViViRepository.save(supplierViVi);
    }

    /**
     * Update a supplierViVi.
     *
     * @param supplierViVi the entity to save.
     * @return the persisted entity.
     */
    public SupplierViVi update(SupplierViVi supplierViVi) {
        LOG.debug("Request to update SupplierViVi : {}", supplierViVi);
        return supplierViViRepository.save(supplierViVi);
    }

    /**
     * Partially update a supplierViVi.
     *
     * @param supplierViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierViVi> partialUpdate(SupplierViVi supplierViVi) {
        LOG.debug("Request to partially update SupplierViVi : {}", supplierViVi);

        return supplierViViRepository
            .findById(supplierViVi.getId())
            .map(existingSupplierViVi -> {
                if (supplierViVi.getName() != null) {
                    existingSupplierViVi.setName(supplierViVi.getName());
                }
                if (supplierViVi.getContactPerson() != null) {
                    existingSupplierViVi.setContactPerson(supplierViVi.getContactPerson());
                }
                if (supplierViVi.getEmail() != null) {
                    existingSupplierViVi.setEmail(supplierViVi.getEmail());
                }
                if (supplierViVi.getPhoneNumber() != null) {
                    existingSupplierViVi.setPhoneNumber(supplierViVi.getPhoneNumber());
                }

                return existingSupplierViVi;
            })
            .map(supplierViViRepository::save);
    }

    /**
     * Get all the supplierViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierViVi> findAllWithEagerRelationships(Pageable pageable) {
        return supplierViViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierViVi> findOne(Long id) {
        LOG.debug("Request to get SupplierViVi : {}", id);
        return supplierViViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierViVi : {}", id);
        supplierViViRepository.deleteById(id);
    }
}
