package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierViVi;
import xyz.jhmapstruct.repository.SupplierViViRepository;
import xyz.jhmapstruct.service.SupplierViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierViVi}.
 */
@Service
@Transactional
public class SupplierViViServiceImpl implements SupplierViViService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViViServiceImpl.class);

    private final SupplierViViRepository supplierViViRepository;

    public SupplierViViServiceImpl(SupplierViViRepository supplierViViRepository) {
        this.supplierViViRepository = supplierViViRepository;
    }

    @Override
    public SupplierViVi save(SupplierViVi supplierViVi) {
        LOG.debug("Request to save SupplierViVi : {}", supplierViVi);
        return supplierViViRepository.save(supplierViVi);
    }

    @Override
    public SupplierViVi update(SupplierViVi supplierViVi) {
        LOG.debug("Request to update SupplierViVi : {}", supplierViVi);
        return supplierViViRepository.save(supplierViVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<SupplierViVi> findAll() {
        LOG.debug("Request to get all SupplierViVis");
        return supplierViViRepository.findAll();
    }

    public Page<SupplierViVi> findAllWithEagerRelationships(Pageable pageable) {
        return supplierViViRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierViVi> findOne(Long id) {
        LOG.debug("Request to get SupplierViVi : {}", id);
        return supplierViViRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierViVi : {}", id);
        supplierViViRepository.deleteById(id);
    }
}
