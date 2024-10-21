package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.repository.SupplierViRepository;
import xyz.jhmapstruct.service.SupplierViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierVi}.
 */
@Service
@Transactional
public class SupplierViServiceImpl implements SupplierViService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViServiceImpl.class);

    private final SupplierViRepository supplierViRepository;

    public SupplierViServiceImpl(SupplierViRepository supplierViRepository) {
        this.supplierViRepository = supplierViRepository;
    }

    @Override
    public SupplierVi save(SupplierVi supplierVi) {
        LOG.debug("Request to save SupplierVi : {}", supplierVi);
        return supplierViRepository.save(supplierVi);
    }

    @Override
    public SupplierVi update(SupplierVi supplierVi) {
        LOG.debug("Request to update SupplierVi : {}", supplierVi);
        return supplierViRepository.save(supplierVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<SupplierVi> findAll() {
        LOG.debug("Request to get all SupplierVis");
        return supplierViRepository.findAll();
    }

    public Page<SupplierVi> findAllWithEagerRelationships(Pageable pageable) {
        return supplierViRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierVi> findOne(Long id) {
        LOG.debug("Request to get SupplierVi : {}", id);
        return supplierViRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierVi : {}", id);
        supplierViRepository.deleteById(id);
    }
}
