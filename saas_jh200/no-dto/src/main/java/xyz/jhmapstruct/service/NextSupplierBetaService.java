package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierBeta;
import xyz.jhmapstruct.repository.NextSupplierBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierBeta}.
 */
@Service
@Transactional
public class NextSupplierBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierBetaService.class);

    private final NextSupplierBetaRepository nextSupplierBetaRepository;

    public NextSupplierBetaService(NextSupplierBetaRepository nextSupplierBetaRepository) {
        this.nextSupplierBetaRepository = nextSupplierBetaRepository;
    }

    /**
     * Save a nextSupplierBeta.
     *
     * @param nextSupplierBeta the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierBeta save(NextSupplierBeta nextSupplierBeta) {
        LOG.debug("Request to save NextSupplierBeta : {}", nextSupplierBeta);
        return nextSupplierBetaRepository.save(nextSupplierBeta);
    }

    /**
     * Update a nextSupplierBeta.
     *
     * @param nextSupplierBeta the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierBeta update(NextSupplierBeta nextSupplierBeta) {
        LOG.debug("Request to update NextSupplierBeta : {}", nextSupplierBeta);
        return nextSupplierBetaRepository.save(nextSupplierBeta);
    }

    /**
     * Partially update a nextSupplierBeta.
     *
     * @param nextSupplierBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierBeta> partialUpdate(NextSupplierBeta nextSupplierBeta) {
        LOG.debug("Request to partially update NextSupplierBeta : {}", nextSupplierBeta);

        return nextSupplierBetaRepository
            .findById(nextSupplierBeta.getId())
            .map(existingNextSupplierBeta -> {
                if (nextSupplierBeta.getName() != null) {
                    existingNextSupplierBeta.setName(nextSupplierBeta.getName());
                }
                if (nextSupplierBeta.getContactPerson() != null) {
                    existingNextSupplierBeta.setContactPerson(nextSupplierBeta.getContactPerson());
                }
                if (nextSupplierBeta.getEmail() != null) {
                    existingNextSupplierBeta.setEmail(nextSupplierBeta.getEmail());
                }
                if (nextSupplierBeta.getPhoneNumber() != null) {
                    existingNextSupplierBeta.setPhoneNumber(nextSupplierBeta.getPhoneNumber());
                }

                return existingNextSupplierBeta;
            })
            .map(nextSupplierBetaRepository::save);
    }

    /**
     * Get all the nextSupplierBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierBeta> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierBetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierBeta> findOne(Long id) {
        LOG.debug("Request to get NextSupplierBeta : {}", id);
        return nextSupplierBetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierBeta : {}", id);
        nextSupplierBetaRepository.deleteById(id);
    }
}
