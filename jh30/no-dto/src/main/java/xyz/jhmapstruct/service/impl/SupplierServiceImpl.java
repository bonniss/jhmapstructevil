package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Supplier;
import xyz.jhmapstruct.repository.SupplierRepository;
import xyz.jhmapstruct.service.SupplierService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Supplier}.
 */
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier save(Supplier supplier) {
        LOG.debug("Request to save Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(Supplier supplier) {
        LOG.debug("Request to update Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findAll() {
        LOG.debug("Request to get all Suppliers");
        return supplierRepository.findAll();
    }

    public Page<Supplier> findAllWithEagerRelationships(Pageable pageable) {
        return supplierRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findOne(Long id) {
        LOG.debug("Request to get Supplier : {}", id);
        return supplierRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}
