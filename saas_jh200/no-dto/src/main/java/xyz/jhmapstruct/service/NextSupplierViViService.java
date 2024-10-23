package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierViVi;
import xyz.jhmapstruct.repository.NextSupplierViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierViVi}.
 */
@Service
@Transactional
public class NextSupplierViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierViViService.class);

    private final NextSupplierViViRepository nextSupplierViViRepository;

    public NextSupplierViViService(NextSupplierViViRepository nextSupplierViViRepository) {
        this.nextSupplierViViRepository = nextSupplierViViRepository;
    }

    /**
     * Save a nextSupplierViVi.
     *
     * @param nextSupplierViVi the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierViVi save(NextSupplierViVi nextSupplierViVi) {
        LOG.debug("Request to save NextSupplierViVi : {}", nextSupplierViVi);
        return nextSupplierViViRepository.save(nextSupplierViVi);
    }

    /**
     * Update a nextSupplierViVi.
     *
     * @param nextSupplierViVi the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierViVi update(NextSupplierViVi nextSupplierViVi) {
        LOG.debug("Request to update NextSupplierViVi : {}", nextSupplierViVi);
        return nextSupplierViViRepository.save(nextSupplierViVi);
    }

    /**
     * Partially update a nextSupplierViVi.
     *
     * @param nextSupplierViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierViVi> partialUpdate(NextSupplierViVi nextSupplierViVi) {
        LOG.debug("Request to partially update NextSupplierViVi : {}", nextSupplierViVi);

        return nextSupplierViViRepository
            .findById(nextSupplierViVi.getId())
            .map(existingNextSupplierViVi -> {
                if (nextSupplierViVi.getName() != null) {
                    existingNextSupplierViVi.setName(nextSupplierViVi.getName());
                }
                if (nextSupplierViVi.getContactPerson() != null) {
                    existingNextSupplierViVi.setContactPerson(nextSupplierViVi.getContactPerson());
                }
                if (nextSupplierViVi.getEmail() != null) {
                    existingNextSupplierViVi.setEmail(nextSupplierViVi.getEmail());
                }
                if (nextSupplierViVi.getPhoneNumber() != null) {
                    existingNextSupplierViVi.setPhoneNumber(nextSupplierViVi.getPhoneNumber());
                }

                return existingNextSupplierViVi;
            })
            .map(nextSupplierViViRepository::save);
    }

    /**
     * Get all the nextSupplierViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierViVi> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierViViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierViVi> findOne(Long id) {
        LOG.debug("Request to get NextSupplierViVi : {}", id);
        return nextSupplierViViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierViVi : {}", id);
        nextSupplierViViRepository.deleteById(id);
    }
}
