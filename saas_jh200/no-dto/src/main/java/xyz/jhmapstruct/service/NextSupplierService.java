package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplier;
import xyz.jhmapstruct.repository.NextSupplierRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplier}.
 */
@Service
@Transactional
public class NextSupplierService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierService.class);

    private final NextSupplierRepository nextSupplierRepository;

    public NextSupplierService(NextSupplierRepository nextSupplierRepository) {
        this.nextSupplierRepository = nextSupplierRepository;
    }

    /**
     * Save a nextSupplier.
     *
     * @param nextSupplier the entity to save.
     * @return the persisted entity.
     */
    public NextSupplier save(NextSupplier nextSupplier) {
        LOG.debug("Request to save NextSupplier : {}", nextSupplier);
        return nextSupplierRepository.save(nextSupplier);
    }

    /**
     * Update a nextSupplier.
     *
     * @param nextSupplier the entity to save.
     * @return the persisted entity.
     */
    public NextSupplier update(NextSupplier nextSupplier) {
        LOG.debug("Request to update NextSupplier : {}", nextSupplier);
        return nextSupplierRepository.save(nextSupplier);
    }

    /**
     * Partially update a nextSupplier.
     *
     * @param nextSupplier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplier> partialUpdate(NextSupplier nextSupplier) {
        LOG.debug("Request to partially update NextSupplier : {}", nextSupplier);

        return nextSupplierRepository
            .findById(nextSupplier.getId())
            .map(existingNextSupplier -> {
                if (nextSupplier.getName() != null) {
                    existingNextSupplier.setName(nextSupplier.getName());
                }
                if (nextSupplier.getContactPerson() != null) {
                    existingNextSupplier.setContactPerson(nextSupplier.getContactPerson());
                }
                if (nextSupplier.getEmail() != null) {
                    existingNextSupplier.setEmail(nextSupplier.getEmail());
                }
                if (nextSupplier.getPhoneNumber() != null) {
                    existingNextSupplier.setPhoneNumber(nextSupplier.getPhoneNumber());
                }

                return existingNextSupplier;
            })
            .map(nextSupplierRepository::save);
    }

    /**
     * Get all the nextSuppliers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplier> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplier> findOne(Long id) {
        LOG.debug("Request to get NextSupplier : {}", id);
        return nextSupplierRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplier : {}", id);
        nextSupplierRepository.deleteById(id);
    }
}
