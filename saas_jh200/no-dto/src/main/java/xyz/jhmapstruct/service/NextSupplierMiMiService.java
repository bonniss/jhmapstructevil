package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierMiMi;
import xyz.jhmapstruct.repository.NextSupplierMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierMiMi}.
 */
@Service
@Transactional
public class NextSupplierMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierMiMiService.class);

    private final NextSupplierMiMiRepository nextSupplierMiMiRepository;

    public NextSupplierMiMiService(NextSupplierMiMiRepository nextSupplierMiMiRepository) {
        this.nextSupplierMiMiRepository = nextSupplierMiMiRepository;
    }

    /**
     * Save a nextSupplierMiMi.
     *
     * @param nextSupplierMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierMiMi save(NextSupplierMiMi nextSupplierMiMi) {
        LOG.debug("Request to save NextSupplierMiMi : {}", nextSupplierMiMi);
        return nextSupplierMiMiRepository.save(nextSupplierMiMi);
    }

    /**
     * Update a nextSupplierMiMi.
     *
     * @param nextSupplierMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierMiMi update(NextSupplierMiMi nextSupplierMiMi) {
        LOG.debug("Request to update NextSupplierMiMi : {}", nextSupplierMiMi);
        return nextSupplierMiMiRepository.save(nextSupplierMiMi);
    }

    /**
     * Partially update a nextSupplierMiMi.
     *
     * @param nextSupplierMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierMiMi> partialUpdate(NextSupplierMiMi nextSupplierMiMi) {
        LOG.debug("Request to partially update NextSupplierMiMi : {}", nextSupplierMiMi);

        return nextSupplierMiMiRepository
            .findById(nextSupplierMiMi.getId())
            .map(existingNextSupplierMiMi -> {
                if (nextSupplierMiMi.getName() != null) {
                    existingNextSupplierMiMi.setName(nextSupplierMiMi.getName());
                }
                if (nextSupplierMiMi.getContactPerson() != null) {
                    existingNextSupplierMiMi.setContactPerson(nextSupplierMiMi.getContactPerson());
                }
                if (nextSupplierMiMi.getEmail() != null) {
                    existingNextSupplierMiMi.setEmail(nextSupplierMiMi.getEmail());
                }
                if (nextSupplierMiMi.getPhoneNumber() != null) {
                    existingNextSupplierMiMi.setPhoneNumber(nextSupplierMiMi.getPhoneNumber());
                }

                return existingNextSupplierMiMi;
            })
            .map(nextSupplierMiMiRepository::save);
    }

    /**
     * Get all the nextSupplierMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierMiMi> findOne(Long id) {
        LOG.debug("Request to get NextSupplierMiMi : {}", id);
        return nextSupplierMiMiRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierMiMi : {}", id);
        nextSupplierMiMiRepository.deleteById(id);
    }
}
