package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeBeta;
import xyz.jhmapstruct.repository.NextEmployeeBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeBeta}.
 */
@Service
@Transactional
public class NextEmployeeBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeBetaService.class);

    private final NextEmployeeBetaRepository nextEmployeeBetaRepository;

    public NextEmployeeBetaService(NextEmployeeBetaRepository nextEmployeeBetaRepository) {
        this.nextEmployeeBetaRepository = nextEmployeeBetaRepository;
    }

    /**
     * Save a nextEmployeeBeta.
     *
     * @param nextEmployeeBeta the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeBeta save(NextEmployeeBeta nextEmployeeBeta) {
        LOG.debug("Request to save NextEmployeeBeta : {}", nextEmployeeBeta);
        return nextEmployeeBetaRepository.save(nextEmployeeBeta);
    }

    /**
     * Update a nextEmployeeBeta.
     *
     * @param nextEmployeeBeta the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeBeta update(NextEmployeeBeta nextEmployeeBeta) {
        LOG.debug("Request to update NextEmployeeBeta : {}", nextEmployeeBeta);
        return nextEmployeeBetaRepository.save(nextEmployeeBeta);
    }

    /**
     * Partially update a nextEmployeeBeta.
     *
     * @param nextEmployeeBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeBeta> partialUpdate(NextEmployeeBeta nextEmployeeBeta) {
        LOG.debug("Request to partially update NextEmployeeBeta : {}", nextEmployeeBeta);

        return nextEmployeeBetaRepository
            .findById(nextEmployeeBeta.getId())
            .map(existingNextEmployeeBeta -> {
                if (nextEmployeeBeta.getFirstName() != null) {
                    existingNextEmployeeBeta.setFirstName(nextEmployeeBeta.getFirstName());
                }
                if (nextEmployeeBeta.getLastName() != null) {
                    existingNextEmployeeBeta.setLastName(nextEmployeeBeta.getLastName());
                }
                if (nextEmployeeBeta.getEmail() != null) {
                    existingNextEmployeeBeta.setEmail(nextEmployeeBeta.getEmail());
                }
                if (nextEmployeeBeta.getHireDate() != null) {
                    existingNextEmployeeBeta.setHireDate(nextEmployeeBeta.getHireDate());
                }
                if (nextEmployeeBeta.getPosition() != null) {
                    existingNextEmployeeBeta.setPosition(nextEmployeeBeta.getPosition());
                }

                return existingNextEmployeeBeta;
            })
            .map(nextEmployeeBetaRepository::save);
    }

    /**
     * Get one nextEmployeeBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeBeta> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeBeta : {}", id);
        return nextEmployeeBetaRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeBeta : {}", id);
        nextEmployeeBetaRepository.deleteById(id);
    }
}
