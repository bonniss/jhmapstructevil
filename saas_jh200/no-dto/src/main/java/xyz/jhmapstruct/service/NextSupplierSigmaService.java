package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierSigma;
import xyz.jhmapstruct.repository.NextSupplierSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierSigma}.
 */
@Service
@Transactional
public class NextSupplierSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierSigmaService.class);

    private final NextSupplierSigmaRepository nextSupplierSigmaRepository;

    public NextSupplierSigmaService(NextSupplierSigmaRepository nextSupplierSigmaRepository) {
        this.nextSupplierSigmaRepository = nextSupplierSigmaRepository;
    }

    /**
     * Save a nextSupplierSigma.
     *
     * @param nextSupplierSigma the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierSigma save(NextSupplierSigma nextSupplierSigma) {
        LOG.debug("Request to save NextSupplierSigma : {}", nextSupplierSigma);
        return nextSupplierSigmaRepository.save(nextSupplierSigma);
    }

    /**
     * Update a nextSupplierSigma.
     *
     * @param nextSupplierSigma the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierSigma update(NextSupplierSigma nextSupplierSigma) {
        LOG.debug("Request to update NextSupplierSigma : {}", nextSupplierSigma);
        return nextSupplierSigmaRepository.save(nextSupplierSigma);
    }

    /**
     * Partially update a nextSupplierSigma.
     *
     * @param nextSupplierSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierSigma> partialUpdate(NextSupplierSigma nextSupplierSigma) {
        LOG.debug("Request to partially update NextSupplierSigma : {}", nextSupplierSigma);

        return nextSupplierSigmaRepository
            .findById(nextSupplierSigma.getId())
            .map(existingNextSupplierSigma -> {
                if (nextSupplierSigma.getName() != null) {
                    existingNextSupplierSigma.setName(nextSupplierSigma.getName());
                }
                if (nextSupplierSigma.getContactPerson() != null) {
                    existingNextSupplierSigma.setContactPerson(nextSupplierSigma.getContactPerson());
                }
                if (nextSupplierSigma.getEmail() != null) {
                    existingNextSupplierSigma.setEmail(nextSupplierSigma.getEmail());
                }
                if (nextSupplierSigma.getPhoneNumber() != null) {
                    existingNextSupplierSigma.setPhoneNumber(nextSupplierSigma.getPhoneNumber());
                }

                return existingNextSupplierSigma;
            })
            .map(nextSupplierSigmaRepository::save);
    }

    /**
     * Get all the nextSupplierSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierSigma> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierSigmaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierSigma> findOne(Long id) {
        LOG.debug("Request to get NextSupplierSigma : {}", id);
        return nextSupplierSigmaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierSigma : {}", id);
        nextSupplierSigmaRepository.deleteById(id);
    }
}
