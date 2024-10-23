package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierSigma;
import xyz.jhmapstruct.repository.SupplierSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierSigma}.
 */
@Service
@Transactional
public class SupplierSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierSigmaService.class);

    private final SupplierSigmaRepository supplierSigmaRepository;

    public SupplierSigmaService(SupplierSigmaRepository supplierSigmaRepository) {
        this.supplierSigmaRepository = supplierSigmaRepository;
    }

    /**
     * Save a supplierSigma.
     *
     * @param supplierSigma the entity to save.
     * @return the persisted entity.
     */
    public SupplierSigma save(SupplierSigma supplierSigma) {
        LOG.debug("Request to save SupplierSigma : {}", supplierSigma);
        return supplierSigmaRepository.save(supplierSigma);
    }

    /**
     * Update a supplierSigma.
     *
     * @param supplierSigma the entity to save.
     * @return the persisted entity.
     */
    public SupplierSigma update(SupplierSigma supplierSigma) {
        LOG.debug("Request to update SupplierSigma : {}", supplierSigma);
        return supplierSigmaRepository.save(supplierSigma);
    }

    /**
     * Partially update a supplierSigma.
     *
     * @param supplierSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierSigma> partialUpdate(SupplierSigma supplierSigma) {
        LOG.debug("Request to partially update SupplierSigma : {}", supplierSigma);

        return supplierSigmaRepository
            .findById(supplierSigma.getId())
            .map(existingSupplierSigma -> {
                if (supplierSigma.getName() != null) {
                    existingSupplierSigma.setName(supplierSigma.getName());
                }
                if (supplierSigma.getContactPerson() != null) {
                    existingSupplierSigma.setContactPerson(supplierSigma.getContactPerson());
                }
                if (supplierSigma.getEmail() != null) {
                    existingSupplierSigma.setEmail(supplierSigma.getEmail());
                }
                if (supplierSigma.getPhoneNumber() != null) {
                    existingSupplierSigma.setPhoneNumber(supplierSigma.getPhoneNumber());
                }

                return existingSupplierSigma;
            })
            .map(supplierSigmaRepository::save);
    }

    /**
     * Get all the supplierSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierSigma> findAllWithEagerRelationships(Pageable pageable) {
        return supplierSigmaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierSigma> findOne(Long id) {
        LOG.debug("Request to get SupplierSigma : {}", id);
        return supplierSigmaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierSigma : {}", id);
        supplierSigmaRepository.deleteById(id);
    }
}
