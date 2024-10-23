package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierGamma;
import xyz.jhmapstruct.repository.NextSupplierGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierGamma}.
 */
@Service
@Transactional
public class NextSupplierGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierGammaService.class);

    private final NextSupplierGammaRepository nextSupplierGammaRepository;

    public NextSupplierGammaService(NextSupplierGammaRepository nextSupplierGammaRepository) {
        this.nextSupplierGammaRepository = nextSupplierGammaRepository;
    }

    /**
     * Save a nextSupplierGamma.
     *
     * @param nextSupplierGamma the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierGamma save(NextSupplierGamma nextSupplierGamma) {
        LOG.debug("Request to save NextSupplierGamma : {}", nextSupplierGamma);
        return nextSupplierGammaRepository.save(nextSupplierGamma);
    }

    /**
     * Update a nextSupplierGamma.
     *
     * @param nextSupplierGamma the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierGamma update(NextSupplierGamma nextSupplierGamma) {
        LOG.debug("Request to update NextSupplierGamma : {}", nextSupplierGamma);
        return nextSupplierGammaRepository.save(nextSupplierGamma);
    }

    /**
     * Partially update a nextSupplierGamma.
     *
     * @param nextSupplierGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierGamma> partialUpdate(NextSupplierGamma nextSupplierGamma) {
        LOG.debug("Request to partially update NextSupplierGamma : {}", nextSupplierGamma);

        return nextSupplierGammaRepository
            .findById(nextSupplierGamma.getId())
            .map(existingNextSupplierGamma -> {
                if (nextSupplierGamma.getName() != null) {
                    existingNextSupplierGamma.setName(nextSupplierGamma.getName());
                }
                if (nextSupplierGamma.getContactPerson() != null) {
                    existingNextSupplierGamma.setContactPerson(nextSupplierGamma.getContactPerson());
                }
                if (nextSupplierGamma.getEmail() != null) {
                    existingNextSupplierGamma.setEmail(nextSupplierGamma.getEmail());
                }
                if (nextSupplierGamma.getPhoneNumber() != null) {
                    existingNextSupplierGamma.setPhoneNumber(nextSupplierGamma.getPhoneNumber());
                }

                return existingNextSupplierGamma;
            })
            .map(nextSupplierGammaRepository::save);
    }

    /**
     * Get all the nextSupplierGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierGamma> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierGammaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierGamma> findOne(Long id) {
        LOG.debug("Request to get NextSupplierGamma : {}", id);
        return nextSupplierGammaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierGamma : {}", id);
        nextSupplierGammaRepository.deleteById(id);
    }
}
