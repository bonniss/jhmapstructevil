package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.SupplierTheta;
import xyz.jhmapstruct.repository.SupplierThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.SupplierTheta}.
 */
@Service
@Transactional
public class SupplierThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierThetaService.class);

    private final SupplierThetaRepository supplierThetaRepository;

    public SupplierThetaService(SupplierThetaRepository supplierThetaRepository) {
        this.supplierThetaRepository = supplierThetaRepository;
    }

    /**
     * Save a supplierTheta.
     *
     * @param supplierTheta the entity to save.
     * @return the persisted entity.
     */
    public SupplierTheta save(SupplierTheta supplierTheta) {
        LOG.debug("Request to save SupplierTheta : {}", supplierTheta);
        return supplierThetaRepository.save(supplierTheta);
    }

    /**
     * Update a supplierTheta.
     *
     * @param supplierTheta the entity to save.
     * @return the persisted entity.
     */
    public SupplierTheta update(SupplierTheta supplierTheta) {
        LOG.debug("Request to update SupplierTheta : {}", supplierTheta);
        return supplierThetaRepository.save(supplierTheta);
    }

    /**
     * Partially update a supplierTheta.
     *
     * @param supplierTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SupplierTheta> partialUpdate(SupplierTheta supplierTheta) {
        LOG.debug("Request to partially update SupplierTheta : {}", supplierTheta);

        return supplierThetaRepository
            .findById(supplierTheta.getId())
            .map(existingSupplierTheta -> {
                if (supplierTheta.getName() != null) {
                    existingSupplierTheta.setName(supplierTheta.getName());
                }
                if (supplierTheta.getContactPerson() != null) {
                    existingSupplierTheta.setContactPerson(supplierTheta.getContactPerson());
                }
                if (supplierTheta.getEmail() != null) {
                    existingSupplierTheta.setEmail(supplierTheta.getEmail());
                }
                if (supplierTheta.getPhoneNumber() != null) {
                    existingSupplierTheta.setPhoneNumber(supplierTheta.getPhoneNumber());
                }

                return existingSupplierTheta;
            })
            .map(supplierThetaRepository::save);
    }

    /**
     * Get all the supplierThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SupplierTheta> findAllWithEagerRelationships(Pageable pageable) {
        return supplierThetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one supplierTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SupplierTheta> findOne(Long id) {
        LOG.debug("Request to get SupplierTheta : {}", id);
        return supplierThetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the supplierTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SupplierTheta : {}", id);
        supplierThetaRepository.deleteById(id);
    }
}
