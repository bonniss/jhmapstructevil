package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextSupplierTheta;
import xyz.jhmapstruct.repository.NextSupplierThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextSupplierTheta}.
 */
@Service
@Transactional
public class NextSupplierThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierThetaService.class);

    private final NextSupplierThetaRepository nextSupplierThetaRepository;

    public NextSupplierThetaService(NextSupplierThetaRepository nextSupplierThetaRepository) {
        this.nextSupplierThetaRepository = nextSupplierThetaRepository;
    }

    /**
     * Save a nextSupplierTheta.
     *
     * @param nextSupplierTheta the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierTheta save(NextSupplierTheta nextSupplierTheta) {
        LOG.debug("Request to save NextSupplierTheta : {}", nextSupplierTheta);
        return nextSupplierThetaRepository.save(nextSupplierTheta);
    }

    /**
     * Update a nextSupplierTheta.
     *
     * @param nextSupplierTheta the entity to save.
     * @return the persisted entity.
     */
    public NextSupplierTheta update(NextSupplierTheta nextSupplierTheta) {
        LOG.debug("Request to update NextSupplierTheta : {}", nextSupplierTheta);
        return nextSupplierThetaRepository.save(nextSupplierTheta);
    }

    /**
     * Partially update a nextSupplierTheta.
     *
     * @param nextSupplierTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextSupplierTheta> partialUpdate(NextSupplierTheta nextSupplierTheta) {
        LOG.debug("Request to partially update NextSupplierTheta : {}", nextSupplierTheta);

        return nextSupplierThetaRepository
            .findById(nextSupplierTheta.getId())
            .map(existingNextSupplierTheta -> {
                if (nextSupplierTheta.getName() != null) {
                    existingNextSupplierTheta.setName(nextSupplierTheta.getName());
                }
                if (nextSupplierTheta.getContactPerson() != null) {
                    existingNextSupplierTheta.setContactPerson(nextSupplierTheta.getContactPerson());
                }
                if (nextSupplierTheta.getEmail() != null) {
                    existingNextSupplierTheta.setEmail(nextSupplierTheta.getEmail());
                }
                if (nextSupplierTheta.getPhoneNumber() != null) {
                    existingNextSupplierTheta.setPhoneNumber(nextSupplierTheta.getPhoneNumber());
                }

                return existingNextSupplierTheta;
            })
            .map(nextSupplierThetaRepository::save);
    }

    /**
     * Get all the nextSupplierThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextSupplierTheta> findAllWithEagerRelationships(Pageable pageable) {
        return nextSupplierThetaRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one nextSupplierTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextSupplierTheta> findOne(Long id) {
        LOG.debug("Request to get NextSupplierTheta : {}", id);
        return nextSupplierThetaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the nextSupplierTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextSupplierTheta : {}", id);
        nextSupplierThetaRepository.deleteById(id);
    }
}
