package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.repository.SupplierMiMiRepository;
import xyz.jhmapstruct.service.SupplierMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierMiMi}.
 */
@Service
@Transactional
public class SupplierMiMiServiceImpl implements SupplierMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiMiServiceImpl.class);

    private final SupplierMiMiRepository supplierMiMiRepository;

    public SupplierMiMiServiceImpl(SupplierMiMiRepository supplierMiMiRepository) {
        this.supplierMiMiRepository = supplierMiMiRepository;
    }

    @Override
    public SupplierMiMi save(SupplierMiMi supplierMiMi) {
        LOG.debug("Request to save SupplierMiMi : {}", supplierMiMi);
        return supplierMiMiRepository.save(supplierMiMi);
    }

    @Override
    public SupplierMiMi update(SupplierMiMi supplierMiMi) {
        LOG.debug("Request to update SupplierMiMi : {}", supplierMiMi);
        return supplierMiMiRepository.save(supplierMiMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<SupplierMiMi> findAll() {
        LOG.debug("Request to get all SupplierMiMis");
        return supplierMiMiRepository.findAll();
    }

    public Page<SupplierMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return supplierMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierMiMi> findOne(Long id) {
        LOG.debug("Request to get SupplierMiMi : {}", id);
        return supplierMiMiRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierMiMi : {}", id);
        supplierMiMiRepository.deleteById(id);
    }
}
