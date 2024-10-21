package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.repository.SupplierMiRepository;
import xyz.jhmapstruct.service.SupplierMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierMi}.
 */
@Service
@Transactional
public class SupplierMiServiceImpl implements SupplierMiService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiServiceImpl.class);

    private final SupplierMiRepository supplierMiRepository;

    public SupplierMiServiceImpl(SupplierMiRepository supplierMiRepository) {
        this.supplierMiRepository = supplierMiRepository;
    }

    @Override
    public SupplierMi save(SupplierMi supplierMi) {
        LOG.debug("Request to save SupplierMi : {}", supplierMi);
        return supplierMiRepository.save(supplierMi);
    }

    @Override
    public SupplierMi update(SupplierMi supplierMi) {
        LOG.debug("Request to update SupplierMi : {}", supplierMi);
        return supplierMiRepository.save(supplierMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<SupplierMi> findAll() {
        LOG.debug("Request to get all SupplierMis");
        return supplierMiRepository.findAll();
    }

    public Page<SupplierMi> findAllWithEagerRelationships(Pageable pageable) {
        return supplierMiRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierMi> findOne(Long id) {
        LOG.debug("Request to get SupplierMi : {}", id);
        return supplierMiRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierMi : {}", id);
        supplierMiRepository.deleteById(id);
    }
}
