package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierVi;
import xyz.jhmapstruct.repository.NextSupplierViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierVi}.
 */
@Service
@Transactional
public class NextSupplierViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierViService.class);

    private final NextSupplierViRepository nextSupplierViRepository;

    public NextSupplierViService(NextSupplierViRepository nextSupplierViRepository) {
        this.nextSupplierViRepository = nextSupplierViRepository;
    }

    /**
     * Save a nextSupplierVi.
     *
     * @param nextSupplierVi the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierVi save(NextSupplierVi nextSupplierVi) {
        LOG.debug("Request to save NextSupplierVi : {}", nextSupplierVi);
        return nextSupplierViRepository.save(nextSupplierVi);
    }

    /**
     * Update a nextSupplierVi.
     *
     * @param nextSupplierVi the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierVi update(NextSupplierVi nextSupplierVi) {
        LOG.debug("Request to update NextSupplierVi : {}", nextSupplierVi);
        return nextSupplierViRepository.save(nextSupplierVi);
    }

    /**
     * Partially update a nextSupplierVi.
     *
     * @param nextSupplierVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierVi> partialUpdate(NextSupplierVi nextSupplierVi) {
        LOG.debug("Request to partially update NextSupplierVi : {}", nextSupplierVi);

        return nextSupplierViRepository
            .findById(nextSupplierVi.getId())
            .map(existingNextSupplierVi -> {
                if (nextSupplierVi.getName() != null) {
                    existingNextSupplierVi.setName(nextSupplierVi.getName());
                }
                if (nextSupplierVi.getContactPerson() != null) {
                    existingNextSupplierVi.setContactPerson(nextSupplierVi.getContactPerson());
                }
                if (nextSupplierVi.getEmail() != null) {
                    existingNextSupplierVi.setEmail(nextSupplierVi.getEmail());
                }
                if (nextSupplierVi.getPhoneNumber() != null) {
                    existingNextSupplierVi.setPhoneNumber(nextSupplierVi.getPhoneNumber());
                }

                return existingNextSupplierVi;
            })
            .map(nextSupplierViRepository::save);
    }

    /**
     * Get all the nextSupplierVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierVi> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierVi> findOne(Long id) {
        LOG.debug("Request to get NextSupplierVi : {}", id);
        return nextSupplierViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierVi : {}", id);
        nextSupplierViRepository.deleteById(id);
    }
}
