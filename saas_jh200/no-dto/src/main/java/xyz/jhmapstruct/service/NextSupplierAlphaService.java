package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierAlpha;
import xyz.jhmapstruct.repository.NextSupplierAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierAlpha}.
 */
@Service
@Transactional
public class NextSupplierAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierAlphaService.class);

    private final NextSupplierAlphaRepository nextSupplierAlphaRepository;

    public NextSupplierAlphaService(NextSupplierAlphaRepository nextSupplierAlphaRepository) {
        this.nextSupplierAlphaRepository = nextSupplierAlphaRepository;
    }

    /**
     * Save a nextSupplierAlpha.
     *
     * @param nextSupplierAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierAlpha save(NextSupplierAlpha nextSupplierAlpha) {
        LOG.debug("Request to save NextSupplierAlpha : {}", nextSupplierAlpha);
        return nextSupplierAlphaRepository.save(nextSupplierAlpha);
    }

    /**
     * Update a nextSupplierAlpha.
     *
     * @param nextSupplierAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierAlpha update(NextSupplierAlpha nextSupplierAlpha) {
        LOG.debug("Request to update NextSupplierAlpha : {}", nextSupplierAlpha);
        return nextSupplierAlphaRepository.save(nextSupplierAlpha);
    }

    /**
     * Partially update a nextSupplierAlpha.
     *
     * @param nextSupplierAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierAlpha> partialUpdate(NextSupplierAlpha nextSupplierAlpha) {
        LOG.debug("Request to partially update NextSupplierAlpha : {}", nextSupplierAlpha);

        return nextSupplierAlphaRepository
            .findById(nextSupplierAlpha.getId())
            .map(existingNextSupplierAlpha -> {
                if (nextSupplierAlpha.getName() != null) {
                    existingNextSupplierAlpha.setName(nextSupplierAlpha.getName());
                }
                if (nextSupplierAlpha.getContactPerson() != null) {
                    existingNextSupplierAlpha.setContactPerson(nextSupplierAlpha.getContactPerson());
                }
                if (nextSupplierAlpha.getEmail() != null) {
                    existingNextSupplierAlpha.setEmail(nextSupplierAlpha.getEmail());
                }
                if (nextSupplierAlpha.getPhoneNumber() != null) {
                    existingNextSupplierAlpha.setPhoneNumber(nextSupplierAlpha.getPhoneNumber());
                }

                return existingNextSupplierAlpha;
            })
            .map(nextSupplierAlphaRepository::save);
    }

    /**
     * Get all the nextSupplierAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierAlphaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierAlpha> findOne(Long id) {
        LOG.debug("Request to get NextSupplierAlpha : {}", id);
        return nextSupplierAlphaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierAlpha : {}", id);
        nextSupplierAlphaRepository.deleteById(id);
    }
}
