package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierMi;
import xyz.jhmapstruct.repository.NextSupplierMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierMi}.
 */
@Service
@Transactional
public class NextSupplierMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierMiService.class);

    private final NextSupplierMiRepository nextSupplierMiRepository;

    public NextSupplierMiService(NextSupplierMiRepository nextSupplierMiRepository) {
        this.nextSupplierMiRepository = nextSupplierMiRepository;
    }

    /**
     * Save a nextSupplierMi.
     *
     * @param nextSupplierMi the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierMi save(NextSupplierMi nextSupplierMi) {
        LOG.debug("Request to save NextSupplierMi : {}", nextSupplierMi);
        return nextSupplierMiRepository.save(nextSupplierMi);
    }

    /**
     * Update a nextSupplierMi.
     *
     * @param nextSupplierMi the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierMi update(NextSupplierMi nextSupplierMi) {
        LOG.debug("Request to update NextSupplierMi : {}", nextSupplierMi);
        return nextSupplierMiRepository.save(nextSupplierMi);
    }

    /**
     * Partially update a nextSupplierMi.
     *
     * @param nextSupplierMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierMi> partialUpdate(NextSupplierMi nextSupplierMi) {
        LOG.debug("Request to partially update NextSupplierMi : {}", nextSupplierMi);

        return nextSupplierMiRepository
            .findById(nextSupplierMi.getId())
            .map(existingNextSupplierMi -> {
                if (nextSupplierMi.getName() != null) {
                    existingNextSupplierMi.setName(nextSupplierMi.getName());
                }
                if (nextSupplierMi.getContactPerson() != null) {
                    existingNextSupplierMi.setContactPerson(nextSupplierMi.getContactPerson());
                }
                if (nextSupplierMi.getEmail() != null) {
                    existingNextSupplierMi.setEmail(nextSupplierMi.getEmail());
                }
                if (nextSupplierMi.getPhoneNumber() != null) {
                    existingNextSupplierMi.setPhoneNumber(nextSupplierMi.getPhoneNumber());
                }

                return existingNextSupplierMi;
            })
            .map(nextSupplierMiRepository::save);
    }

    /**
     * Get all the nextSupplierMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierMi> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierMi> findOne(Long id) {
        LOG.debug("Request to get NextSupplierMi : {}", id);
        return nextSupplierMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierMi : {}", id);
        nextSupplierMiRepository.deleteById(id);
    }
}
