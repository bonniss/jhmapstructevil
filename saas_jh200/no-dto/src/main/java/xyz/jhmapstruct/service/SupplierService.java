package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Supplier;
import xyz.jhmapstruct.repository.SupplierRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Supplier}.
 */
@Service
@Transactional
public class SupplierService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierService.class);

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    /**
     * Save a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    public Supplier save(Supplier supplier) {
        LOG.debug("Request to save Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    /**
     * Update a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    public Supplier update(Supplier supplier) {
        LOG.debug("Request to update Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    /**
     * Partially update a supplier.
     *
     * @param supplier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Supplier> partialUpdate(Supplier supplier) {
        LOG.debug("Request to partially update Supplier : {}", supplier);

        return supplierRepository
            .findById(supplier.getId())
            .map(existingSupplier -> {
                if (supplier.getName() != null) {
                    existingSupplier.setName(supplier.getName());
                }
                if (supplier.getContactPerson() != null) {
                    existingSupplier.setContactPerson(supplier.getContactPerson());
                }
                if (supplier.getEmail() != null) {
                    existingSupplier.setEmail(supplier.getEmail());
                }
                if (supplier.getPhoneNumber() != null) {
                    existingSupplier.setPhoneNumber(supplier.getPhoneNumber());
                }

                return existingSupplier;
            })
            .map(supplierRepository::save);
    }

    /**
     * Get all the suppliers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Supplier> findAllWithEagerRelationships(Pageable pageable) {
        return supplierRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Supplier> findOne(Long id) {
        LOG.debug("Request to get Supplier : {}", id);
        return supplierRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}
